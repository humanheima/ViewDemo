# Android Theme 日夜间模式的原理

AppCompatDelegateImpl 的 applyDayNight 方法。

```java
@Override
public boolean applyDayNight() {
    return applyApplicationSpecificConfig(true);
}
```

```java
private boolean applyApplicationSpecificConfig(final boolean allowRecreation) {
    return applyApplicationSpecificConfig(allowRecreation,
            /* isLocalesApplicationRequired */ true);
}

private boolean applyApplicationSpecificConfig(final boolean allowRecreation,
                                               final boolean isLocalesApplicationRequired) {
    if (mDestroyed) {
        if (DEBUG) {
            Log.d(TAG, "applyApplicationSpecificConfig. Skipping because host is "
                    + "destroyed");
        }
        // If we're destroyed, ignore the call
        return false;
    }

    @NightMode final int nightMode = calculateNightMode();
    @ApplyableNightMode final int modeToApply = mapNightMode(mContext, nightMode);

    LocaleListCompat localesToBeApplied = null;
    if (Build.VERSION.SDK_INT < 33) {
        localesToBeApplied = calculateApplicationLocales(mContext);
    }

    if (!isLocalesApplicationRequired && localesToBeApplied != null) {
        // Reaching here would mean that the requested locales has already been applied and
        // no modification is required. Hence, localesToBeApplied is kept same as the current
        // configuration locales.
        localesToBeApplied =
                getConfigurationLocales(mContext.getResources()
                        .getConfiguration());
    }

    //注释1处，应用配置
    final boolean applied = updateAppConfiguration(modeToApply, localesToBeApplied,
            allowRecreation);

    //...

    return applied;
}
```

```java
private boolean updateAppConfiguration(int nightMode, @Nullable LocaleListCompat
            locales, final boolean allowRecreation) {
        boolean handled = false;

        final Configuration overrideConfig =
                createOverrideAppConfiguration(mContext, nightMode, locales, null, false);

        final int activityHandlingConfigChange = getActivityHandlesConfigChangesFlags(mContext);
        final Configuration currentConfiguration = mEffectiveConfiguration == null
                ? mContext.getResources().getConfiguration() : mEffectiveConfiguration;
        final int currentNightMode = currentConfiguration.uiMode
                & Configuration.UI_MODE_NIGHT_MASK;
        final int newNightMode = overrideConfig.uiMode & Configuration.UI_MODE_NIGHT_MASK;

        final LocaleListCompat currentLocales = getConfigurationLocales(currentConfiguration);
        final LocaleListCompat newLocales;
        if (locales == null) {
            newLocales = null;
        } else {
            newLocales = getConfigurationLocales(overrideConfig);
        }

        // Bitmask representing if there is a change in nightMode or Locales, mapped by bits
        // ActivityInfo.CONFIG_UI_MODE and ActivityInfo.CONFIG_LOCALE respectively.
        int configChanges = 0;
        if (currentNightMode != newNightMode) {
            configChanges |= ActivityInfo.CONFIG_UI_MODE;
        }
        if (newLocales != null && !currentLocales.equals(newLocales)) {
            configChanges |= ActivityInfo.CONFIG_LOCALE;
            if (Build.VERSION.SDK_INT >= 17) {
                configChanges |= ActivityInfo.CONFIG_LAYOUT_DIRECTION;
            }
        }

        if (DEBUG) {
            Log.d(TAG, String.format(
                    "updateAppConfiguration [allowRecreation:%s, "
                            + "currentNightMode:%s, newNightMode:%s, currentLocales:%s, "
                            + "newLocales:%s, activityHandlingNightModeChanges:%s, "
                            + "activityHandlingLocalesChanges:%s, "
                            + "activityHandlingLayoutDirectionChanges:%s, "
                            + "baseContextAttached:%s, "
                            + "created:%s, canReturnDifferentContext:%s, host:%s]",
                    allowRecreation, currentNightMode, newNightMode,
                    currentLocales,
                    newLocales,
                    ((activityHandlingConfigChange & ActivityInfo.CONFIG_UI_MODE) != 0),
                    ((activityHandlingConfigChange & ActivityInfo.CONFIG_LOCALE) != 0),
                    ((activityHandlingConfigChange & ActivityInfo.CONFIG_LAYOUT_DIRECTION) != 0),
                    mBaseContextAttached, mCreated,
                    sCanReturnDifferentContext, mHost));
        }
        if ((~activityHandlingConfigChange & configChanges) != 0
                && allowRecreation
                && mBaseContextAttached
                && (sCanReturnDifferentContext || mCreated)
                && mHost instanceof Activity
                && !((Activity) mHost).isChild()) {
            // If we're an attached, standalone Activity, we can recreate() to apply using the
            // attachBaseContext() + createConfigurationContext() code path.
            // Else, we need to use updateConfiguration() before we're 'created' (below)
            if (DEBUG) {
                Log.d(TAG, "updateAppConfiguration attempting to recreate Activity: "
                        + mHost);
            }
            //注释1处，重建Activity
            ActivityCompat.recreate((Activity) mHost);
            handled = true;
        } else if (DEBUG) {
            Log.d(TAG, "updateAppConfiguration not recreating Activity: " + mHost);
        }

        if (!handled && (configChanges != 0)) {
            // Else we need to use the updateConfiguration path
            if (DEBUG) {
                Log.d(TAG, "updateAppConfiguration. Updating resources config on host: "
                        + mHost);
            }
            // If all the configurations that need to be altered are handled by the activity,
            // only then callOnConfigChange is set to true.
            //注释2处，更新resource
            updateResourcesConfiguration(newNightMode, newLocales,
                    /* callOnConfigChange = */(configChanges & activityHandlingConfigChange)
                            == configChanges, null);

            handled = true;
        }

        if (DEBUG && !handled) {
            Log.d(TAG,
                    "updateAppConfiguration. Skipping. nightMode: " + nightMode + " and "
                            + "locales: " +  locales + " for host:" + mHost);
        }

        if (handled && mHost instanceof AppCompatActivity) {
            if ((configChanges & ActivityInfo.CONFIG_UI_MODE) != 0) {
                ((AppCompatActivity) mHost).onNightModeChanged(nightMode);
            }
            if ((configChanges & ActivityInfo.CONFIG_LOCALE) != 0) {
                ((AppCompatActivity) mHost).onLocalesChanged(locales);
            }
        }

        if (handled && newLocales != null) {
            // LocaleListCompat's default locales are updated here using the configuration
            // locales to keep default locales in sync with application locales and also to cover
            // the case where framework re-adjusts input locales by bringing forward the most
            // suitable locale.
            setDefaultLocalesForLocaleList(getConfigurationLocales(
                    mContext.getResources().getConfiguration()));
        }
        return handled;
    }
```

注释1处，重建Activity。这里注意，如果Activity的生命周期还没达到 ON_STARTED 状态的话，就会直接返回，不会重新onCreate。
ActivityThread 的 handleRelaunchActivityLocally 方法中，做了返回处理。

```java
public void handleRelaunchActivityLocally(IBinder token) {
    final ActivityClientRecord r = mActivities.get(token);
    if (r == null) {
        Log.w(TAG, "Activity to relaunch no longer exists");
        return;
    }

    final int prevState = r.getLifecycleState();

    if (prevState < ON_START || prevState > ON_STOP) {
        //注释1处，如果Activity的生命周期还没达到 ON_STARTED 状态，或者已经到达 ON_STOP 状态的话，就会直接返回。
        Log.w(TAG, "Activity state must be in [ON_START..ON_STOP] in order to be relaunched,"
                + "current state is " + prevState);
        return;
    }

    ActivityClient.getInstance().activityLocalRelaunch(r.token);
    // Initialize a relaunch request.
    final MergedConfiguration mergedConfiguration = new MergedConfiguration(
            r.createdConfig != null
                    ? r.createdConfig : mConfigurationController.getConfiguration(),
            r.overrideConfig);
    final ActivityRelaunchItem activityRelaunchItem = ActivityRelaunchItem.obtain(
            null /* pendingResults */, null /* pendingIntents */, 0 /* configChanges */,
            mergedConfiguration, r.mPreserveWindow);
    // Make sure to match the existing lifecycle state in the end of the transaction.
    final ActivityLifecycleItem lifecycleRequest =
            TransactionExecutorHelper.getLifecycleRequestForCurrentState(r);
    // Schedule the transaction.
    final ClientTransaction transaction = ClientTransaction.obtain(this.mAppThread, r.token);
    transaction.addCallback(activityRelaunchItem);
    transaction.setLifecycleStateRequest(lifecycleRequest);
    executeTransaction(transaction);
}
```



注释2处，更新resource 。我认为最关键的点就是  ResourcesImpl#updateConfiguration 方法。

```java
public void updateConfiguration(Configuration config, DisplayMetrics metrics,
                                    CompatibilityInfo compat) {
        Trace.traceBegin(Trace.TRACE_TAG_RESOURCES, "ResourcesImpl#updateConfiguration");
        try {
            synchronized (mAccessLock) {
                if (DEBUG_CONFIG) {
                    Slog.i(TAG, "**** Updating config of " + this + ": old config is "
                            + mConfiguration + " old compat is "
                            + mDisplayAdjustments.getCompatibilityInfo());
                    Slog.i(TAG, "**** Updating config of " + this + ": new config is "
                            + config + " new compat is " + compat);
                }
                if (compat != null) {
                    mDisplayAdjustments.setCompatibilityInfo(compat);
                }
                if (metrics != null) {
                    mMetrics.setTo(metrics);
                }
                // NOTE: We should re-arrange this code to create a Display
                // with the CompatibilityInfo that is used everywhere we deal
                // with the display in relation to this app, rather than
                // doing the conversion here.  This impl should be okay because
                // we make sure to return a compatible display in the places
                // where there are public APIs to retrieve the display...  but
                // it would be cleaner and more maintainable to just be
                // consistently dealing with a compatible display everywhere in
                // the framework.
                mDisplayAdjustments.getCompatibilityInfo().applyToDisplayMetrics(mMetrics);

                final @Config int configChanges = calcConfigChanges(config);

                // If even after the update there are no Locales set, grab the default locales.
                LocaleList locales = mConfiguration.getLocales();
                if (locales.isEmpty()) {
                    locales = LocaleList.getDefault();
                    mConfiguration.setLocales(locales);
                }

                if ((configChanges & ActivityInfo.CONFIG_LOCALE) != 0) {
                    if (locales.size() > 1) {
                        // The LocaleList has changed. We must query the AssetManager's available
                        // Locales and figure out the best matching Locale in the new LocaleList.
                        String[] availableLocales = mAssets.getNonSystemLocales();
                        if (LocaleList.isPseudoLocalesOnly(availableLocales)) {
                            // No app defined locales, so grab the system locales.
                            availableLocales = mAssets.getLocales();
                            if (LocaleList.isPseudoLocalesOnly(availableLocales)) {
                                availableLocales = null;
                            }
                        }

                        if (availableLocales != null) {
                            final Locale bestLocale = locales.getFirstMatchWithEnglishSupported(
                                    availableLocales);
                            if (bestLocale != null && bestLocale != locales.get(0)) {
                                mConfiguration.setLocales(new LocaleList(bestLocale, locales));
                            }
                        }
                    }
                }

                if (mConfiguration.densityDpi != Configuration.DENSITY_DPI_UNDEFINED) {
                    mMetrics.densityDpi = mConfiguration.densityDpi;
                    mMetrics.density =
                            mConfiguration.densityDpi * DisplayMetrics.DENSITY_DEFAULT_SCALE;
                }

                // Protect against an unset fontScale.
                mMetrics.scaledDensity = mMetrics.density *
                        (mConfiguration.fontScale != 0 ? mConfiguration.fontScale : 1.0f);
                mMetrics.fontScaleConverter =
                        FontScaleConverterFactory.forScale(mConfiguration.fontScale);

                final int width, height;
                if (mMetrics.widthPixels >= mMetrics.heightPixels) {
                    width = mMetrics.widthPixels;
                    height = mMetrics.heightPixels;
                } else {
                    //noinspection SuspiciousNameCombination
                    width = mMetrics.heightPixels;
                    //noinspection SuspiciousNameCombination
                    height = mMetrics.widthPixels;
                }

                final int keyboardHidden;
                if (mConfiguration.keyboardHidden == Configuration.KEYBOARDHIDDEN_NO
                        && mConfiguration.hardKeyboardHidden
                        == Configuration.HARDKEYBOARDHIDDEN_YES) {
                    keyboardHidden = Configuration.KEYBOARDHIDDEN_SOFT;
                } else {
                    keyboardHidden = mConfiguration.keyboardHidden;
                }

                //注释1处，mAssets
                mAssets.setConfiguration(mConfiguration.mcc, mConfiguration.mnc,
                        adjustLanguageTag(mConfiguration.getLocales().get(0).toLanguageTag()),
                        mConfiguration.orientation,
                        mConfiguration.touchscreen,
                        mConfiguration.densityDpi, mConfiguration.keyboard,
                        keyboardHidden, mConfiguration.navigation, width, height,
                        mConfiguration.smallestScreenWidthDp,
                        mConfiguration.screenWidthDp, mConfiguration.screenHeightDp,
                        mConfiguration.screenLayout, mConfiguration.uiMode,
                        mConfiguration.colorMode, mConfiguration.getGrammaticalGender(),
                        Build.VERSION.RESOURCES_SDK_INT);

                if (DEBUG_CONFIG) {
                    Slog.i(TAG, "**** Updating config of " + this + ": final config is "
                            + mConfiguration + " final compat is "
                            + mDisplayAdjustments.getCompatibilityInfo());
                }
                // 更新 mDrawableCache
                mDrawableCache.onConfigurationChange(configChanges);
                // 更新 mColorDrawableCache
                mColorDrawableCache.onConfigurationChange(configChanges);
                // 更新 mComplexColorCache
                mComplexColorCache.onConfigurationChange(configChanges);
                
                mAnimatorCache.onConfigurationChange(configChanges);
                mStateListAnimatorCache.onConfigurationChange(configChanges);

                flushLayoutCache();
            }
            synchronized (sSync) {
                if (mPluralRule != null) {
                    mPluralRule = PluralRules.forLocale(mConfiguration.getLocales().get(0));
                }
            }
        } finally {
            Trace.traceEnd(Trace.TRACE_TAG_RESOURCES);
        }
    }
```