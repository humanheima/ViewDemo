### Android 资源加载流程


### 获取颜色


getResource().getColor()


Resources 的 getColor 方法。



```java
public int getColor(@ColorRes int id, @Nullable Theme theme) throws NotFoundException {
	final TypedValue value = obtainTempTypedValue();
	try {
		final ResourcesImpl impl = mResourcesImpl;
		impl.getValue(id, value, true);
		if (value.type >= TypedValue.TYPE_FIRST_iNT
		                    && value.type <= TypedValue.TYPE_LAST_iNT) {
			//注释1处，获取颜色
			return value.data;
		} else if (value.type != TypedValue.TYPE_STRING) {
			throw new NotFoundException("Resource ID #0x" + Integer.toHexString(id)
			                        + " type #0x" + Integer.toHexString(value.type) + " is not valid");
		}
		//注释2处，如果获取的颜色是 ColorList，这种情况发生在我们指定的 color 是一个 selector 文件
		final ColorStateList csl = impl.loadColorStateList(this, value, id, theme);
		//返回 selector 默认状态下的颜色
		return csl.getDefaultColor();
	}
	finally {
		releaseTempTypedValue(value);
	}
}
```

注释1处，获取颜色，正常我们直接使用 `R.color.colorPrimary` 会走到这里。

```kotlin
 val color = resources.getColor(R.color.colorPrimary)

```


注释2处，如果获取的颜色是 ColorList，这种情况发生在我们指定的 color 是一个 selector 文件。 `src/main/res/color/color_list.xml`


```xml
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">

    <item android:color="#FF4081"/>
    <item android:color="@color/colorPrimary" android:state_focused="true"/>
    <item android:color="@color/colorPrimaryDark" android:state_pressed="true"/>
</selector>

```

这个时候，就会走注释2处，调用 ResourcesImpl 的 loadColorStateList 方法。


```java
ColorStateList loadColorStateList(Resources wrapper, TypedValue value, int id,
            Resources.Theme theme)
            throws NotFoundException {
	if (TRACE_FOR_PRELOAD) {
		// Log only framework resources
		if ((id >>> 24) == 0x1) {
			final String name = getResourceName(id);
			if (name != null) android.util.Log.d("PreloadColorStateList", name);
		}
	}
	final long key = (((long) value.assetCookie) << 32) | value.data;
	// Handle inline color definitions.
	// 注释1处，如果是 #ff3344 这样的颜色会走这里
	if (value.type >= TypedValue.TYPE_FIRST_COLOR_iNT
	                && value.type <= TypedValue.TYPE_LAST_COLOR_iNT) {
		return getColorStateListFromint(value, key);
	}
	// 注释2处，  selector 文件 走这里
	ComplexColor complexColor = loadComplexColorFromName(wrapper, theme, value, id);
	if (complexColor != null && complexColor instanceof ColorStateList) {
		return (ColorStateList) complexColor;
	}
	throw new NotFoundException(
	                "Can't find ColorStateList from drawable resource ID #0x"
	                        + Integer.toHexString(id));
}
```
 
 注释2处，  selector 文件 走这里  loadComplexColorFromName

```java
private ComplexColor loadComplexColorFromName(Resources wrapper, Resources.Theme theme,
            TypedValue value, int id) {
	final long key = (((long) value.assetCookie) << 32) | value.data;
	//注释1处，先从缓存里获取
	final ConfigurationBoundResourceCache<ComplexColor> cache = mComplexColorCache;
	ComplexColor complexColor = cache.getInstance(key, wrapper, theme);
	if (complexColor != null) {
		return complexColor;
	}
	//注释2处，从预加载的缓存中获取
	final android.content.res.ConstantState<ComplexColor> factory =
	                sPreloadedComplexColors.get(key);

	if (factory != null) {
		complexColor = factory.newInstance(wrapper, theme);
	}
	//注释3处，调用 loadComplexColorForCookie 从xml文件中创建
	if (complexColor == null) {
		complexColor = loadComplexColorForCookie(wrapper, value, id, theme);
	}
	if (complexColor != null) {
		complexColor.setBaseChangingConfigurations(value.changingConfigurations);
		if (mPreloading) {
			if (verifyPreloadConfig(complexColor.getChangingConfigurations(),
			                        0, value.resourceId, "color")) {
				sPreloadedComplexColors.put(key, complexColor.getConstantState());
			}
		} else {
			//获取的颜色加入缓存。
			cache.put(key, theme, complexColor.getConstantState());
		}
	}
	return complexColor;
}

```

注释3处，调用 loadComplexColorForCookie 从xml文件中创建

```java
private ComplexColor loadComplexColorForCookie(Resources wrapper, TypedValue value, int id,
            Resources.Theme theme) {
	if (value.string == null) {
		throw new UnsupportedOperationException(
		                    "Can't convert to ComplexColor: type=0x" + value.type);
	}
	//在这个例子中是 res/color/color_list.xml
	final String file = value.string.toString();
	if (TRACE_FOR_MISS_PRELOAD) {
		// Log only framework resources
		if ((id >>> 24) == 0x1) {
			final String name = getResourceName(id);
			if (name != null) {
				Log.d(TAG, "Loading framework ComplexColor #" + Integer.toHexString(id)
				                            + ": " + name + " at " + file);
			}
		}
	}
	if (DEBUG_LOAD) {
		Log.v(TAG, "Loading ComplexColor for cookie " + value.assetCookie + ": " + file);
	}
	ComplexColor complexColor = null;
	Trace.traceBegin(Trace.TRACE_TAG_RESOURCES, file);
	//从 xml 文件中获取颜色
	if (file.endsWith(".xml")) {
		try {
			final XmlResourceParser parser = loadXmlResourceParser(
			                        file, id, value.assetCookie, "ComplexColor");
			final AttributeSet attrs = Xml.asAttributeSet(parser);
			int type;
			while ((type = parser.next()) != XmlPullParser.START_TAG
			                        && type != XmlPullParser.END_DOCUMENT) {
				// Seek parser to start tag.
			}
			if (type != XmlPullParser.START_TAG) {
				throw new XmlPullParserException("No start tag found");
			}
			final String name = parser.getName();
			if (name.equals("gradient")) {
				//渐变色
				complexColor = GradientColor.createFromXmlInner(wrapper, parser, attrs, theme);
			} else if (name.equals("selector")) {
				//selector
				complexColor = ColorStateList.createFromXmlInner(wrapper, parser, attrs, theme);
			}
			parser.close();
		}
		catch (Exception e) {
			Trace.traceEnd(Trace.TRACE_TAG_RESOURCES);
			final NotFoundException rnf = new NotFoundException(
			                        "File " + file + " from ComplexColor resource ID #0x"
			                                + Integer.toHexString(id));
			rnf.initCause(e);
			throw rnf;
		}
	} 
	//...
	Trace.traceEnd(Trace.TRACE_TAG_RESOURCES);
	return complexColor;
}
```




### 获取 Drawable

getResource().getDrawable()

Resources 的 getDrawableForDensity 方法。

```java
public Drawable getDrawableForDensity(@DrawableRes int id, int density, @Nullable Theme theme) {
	final TypedValue value = obtainTempTypedValue();
	try {
		final ResourcesImpl impl = mResourcesImpl;
		//注释1处
		impl.getValueForDensity(id, density, value, true);
		//注释2处，
		return impl.loadDrawable(this, value, id, density, theme);
	}
	finally {
		releaseTempTypedValue(value);
	}
}
```

ResourcesImpl 的 getValueForDensity 方法。

```java
void getValueForDensity(@AnyRes int id, int density, TypedValue outValue,
            Boolean resolveRefs) throws NotFoundException {
	//注释1处，AssetManager 的 getResourceValue 方法。
	Boolean found = mAssets.getResourceValue(id, density, outValue, resolveRefs);
	if (found) {
		return;
	}
	throw new NotFoundException("Resource ID #0x" + Integer.toHexString(id));
}
```


```java

final Boolean getResourceValue(@AnyRes int resId, int densityDpi, @NonNull TypedValue outValue,
            Boolean resolveRefs) {
	synchronized (this) {
		//注释1处，调用 loadResourceValue 方法。 这是一个 native 方法。
		final int block = loadResourceValue(resId, (short) densityDpi, outValue, resolveRefs);
		if (block < 0) {
			return false;
		}
		// Convert the changing configurations flags populated by native code.
		outValue.changingConfigurations = ActivityInfo.activityInfoConfigNativeToJava(
		                    outValue.changingConfigurations);
		if (outValue.type == TypedValue.TYPE_STRING) {
			outValue.string = mStringBlocks[block].get(outValue.data);
		}
		return true;
	}
}

```

Resources 的 getDrawableForDensity 方法的注释2处，调用 ResourcesImpl 的 loadDrawable 方法。


```java
Drawable loadDrawable(@NonNull Resources wrapper, @NonNull TypedValue value, int id,
            int density, @Nullable Resources.Theme theme)
            throws NotFoundException {
	// If the drawable's XML lives in our current density qualifier,
	// it's okay to use a scaled version from the cache. Otherwise, we
	// need to actually load the drawable from XML.
    //是否使用缓存，传入的 density 是 0 ，useCache 为true
	final Boolean useCache = density == 0 || value.density == mMetrics.densityDpi;
	// Pretend the requested density is actually the display density. If
	// the drawable returned is not the requested density, then force it
	// to be scaled later by dividing its density by the ratio of
	// requested density to actual device density. Drawables that have
	// undefined density or no density don't need to be handled here.
	if (density > 0 && value.density > 0 && value.density != TypedValue.DENSITY_NONE) {
		if (value.density == density) {
			value.density = mMetrics.densityDpi;
		} else {
			value.density = (value.density * mMetrics.densityDpi) / density;
		}
	}
	try {
		//...
		//标记是否是 ColorDrawable 
		final Boolean isColorDrawable;
		//该主题下的绘制资源缓存
		final DrawableCache caches;
		final long key;
		//注释1处，条件满足， 是 ColorDrawable ，我的理解 ColorDrawable 就是我们直接给 View 的 Background 设置一个颜色。 例如 android:background="@color/colorPrimary"
		if (value.type >= TypedValue.TYPE_FIRST_COLOR_iNT
		                    && value.type <= TypedValue.TYPE_LAST_COLOR_iNT) {

			isColorDrawable = true;
			caches = mColorDrawableCache;
			key = value.data;
		} else {
			isColorDrawable = false;
			caches = mDrawableCache;
			key = (((long) value.assetCookie) << 32) | value.data;
		}
		// First, check whether we have a cached version of this drawable
		// that was inflated against the specified theme. Skip the cache if
		// we're currently preloading or we're not using the cache.
		if (!mPreloading && useCache) {//条件为true
			final Drawable cachedDrawable = caches.getInstance(key, wrapper, theme);
			if (cachedDrawable != null) {
				//注释2处，从缓存中获取的不为null，直接返回。
				cachedDrawable.setChangingConfigurations(value.changingConfigurations);
				return cachedDrawable;
			}
		}
		// Next, check preloaded drawables. Preloaded drawables may contain
		// unresolved theme attributes.
		//注释3处，从预加载的资源中获取，只有 zygote 进程才会预加载一些资源，这些资源是共享的。系统中的每个应用都可以使用。
		final Drawable.ConstantState cs;
		if (isColorDrawable) {
			cs = sPreloadedColorDrawables.get(key);
		} else {
			cs = sPreloadedDrawables[mConfiguration.getLayoutDirection()].get(key);
		}
		Drawable dr;
		Boolean needsNewDrawableAfterCache = false;
		if (cs != null) {
			if (TRACE_FOR_DETAILED_PRELOAD) {
				// Log only framework resources
				if (((id >>> 24) == 0x1) && (android.os.Process.myUid() != 0)) {
					final String name = getResourceName(id);
					//命中的预加载的资源
					if (name != null) {
						Log.d(TAG_PRELOAD, "Hit preloaded FW drawable #"
						                                    + Integer.toHexString(id) + " " + name);
					}
				}
			}
			//注释4处，从预加载的资源中获取Drawable
			dr = cs.newDrawable(wrapper);
		} else if (isColorDrawable) {
			//注释5处，创建新的 ColorDrawable
			dr = new ColorDrawable(value.data);
		} else {
			//注释6处，创建新的 Drawable 从 XML文件或者资源流中加载 drawable
			dr = loadDrawableForCookie(wrapper, value, id, density);
		}
		// DrawableContainer' constant state has drawables instances. In order to leave the
		// constant state intact in the cache, we need to create a new DrawableContainer after
		// added to cache.
		if (dr instanceof DrawableContainer)  {
			needsNewDrawableAfterCache = true;
		}
		// Determine if the drawable has unresolved theme attributes. If it
		// does, we'll need to apply a theme and store it in a theme-specific
		// cache.
		//应用主题
		final Boolean canApplyTheme = dr != null && dr.canApplyTheme();
		if (canApplyTheme && theme != null) {
			dr = dr.mutate();
			dr.applyTheme(theme);
			dr.clearMutated();
		}
		// 如果我们能够获取一个 drawable，把它加入合适的缓存：预加载的缓存，没有主题的缓存，null主题的缓存，特定主题的缓存。
		if (dr != null) {
			dr.setChangingConfigurations(value.changingConfigurations);
			if (useCache) {
				//加入缓存
				cacheDrawable(value, isColorDrawable, caches, theme, canApplyTheme, key, dr);
				if (needsNewDrawableAfterCache) {
					Drawable.ConstantState state = dr.getConstantState();
					if (state != null) {
						dr = state.newDrawable(wrapper);
					}
				}
			}
		}
		return dr;
	}
	catch (Exception e) {
		String name;
		try {
			name = getResourceName(id);
		}
		catch (NotFoundException e2) {
			name = "(missing name)";
		}
		// The target drawable might fail to load for any number of
		// reasons, but we always want to include the resource name.
		// Since the client already expects this method to throw a
		// NotFoundException, just throw one of those.
		final NotFoundException nfe = new NotFoundException("Drawable " + name
		                    + " with resource ID #0x" + Integer.toHexString(id), e);
		nfe.setStackTrace(new StackTraceElement[0]);
		throw nfe;
	}
}
```


注释1处，条件满足， 是 ColorDrawable ，我的理解 ColorDrawable 就是我们直接给 View 的 Background 设置一个颜色。 例如 android:background="@color/colorPrimary"。

注释2处，从缓存中获取的不为null，直接返回。

注释3处，从预加载的资源中获取，只有 zygote 进程才会预加载一些资源，这些资源是共享的。系统中的每个应用都可以使用。

注释4处，从预加载的资源中获取Drawable。

注释5处，创建新的 ColorDrawable、

注释6处，创建新的 Drawable 从 XML文件或者 资源流中加载 drawable 。

```java

/**
 * 从 XML文件或者 资源流中加载 drawable
 *
 * @return Drawable, or null if Drawable cannot be decoded.
 */
@Nullable
private Drawable loadDrawableForCookie(@NonNull Resources wrapper, @NonNull TypedValue value,
            int id, int density) {
	if (value.string == null) {
		throw new NotFoundException("Resource "" + getResourceName(id) + "" ("
		                    + Integer.toHexString(id) + ") is not a Drawable (color or path): " + value);
	}
	//file 类似 res/drawable-xxhdpi-v4/avatar.png
	final String file = value.string.toString();
	if (TRACE_FOR_MISS_PRELOAD) {
		// Log only framework resources
		if ((id >>> 24) == 0x1) {
			final String name = getResourceName(id);
			if (name != null) {
				Log.d(TAG, "Loading framework drawable #" + Integer.toHexString(id)
				                            + ": " + name + " at " + file);
			}
		}
	}
	// For preload tracing.
	long startTime = 0;
	int startBitmapCount = 0;
	long startBitmapSize = 0;
	int startDrawableCount = 0;
	if (TRACE_FOR_DETAILED_PRELOAD) {
		startTime = System.nanoTime();
		startBitmapCount = Bitmap.sPreloadTracingNumInstantiatedBitmaps;
		startBitmapSize = Bitmap.sPreloadTracingTotalBitmapsSize;
		startDrawableCount = sPreloadTracingNumLoadedDrawables;
	}
	if (DEBUG_LOAD) {
		Log.v(TAG, "Loading drawable for cookie " + value.assetCookie + ": " + file);
	}
	final Drawable dr;
	Trace.traceBegin(Trace.TRACE_TAG_RESOURCES, file);
	LookupStack stack = mLookupStack.get();
	try {
		// Perform a linear search to check if we have already referenced this resource before.
		if (stack.contains(id)) {
			throw new Exception("Recursive reference in drawable");
		}
		stack.push(id);
		try {
			//注释1处，从 xml 文件加载
			if (file.endsWith(".xml")) {
				final XmlResourceParser rp = loadXmlResourceParser(
				                            file, id, value.assetCookie, "drawable");
				dr = Drawable.createFromXmlForDensity(wrapper, rp, density, null);
				rp.close();
			} else {
				//注释2处，加载 .png，.jpg 等
				final InputStream is = mAssets.openNonAsset(
				                            value.assetCookie, file, AssetManager.ACCESS_STREAMING);
				AssetInputStream ais = (AssetInputStream) is;
				dr = decodeImageDrawable(ais, wrapper, value);
			}
		}
		finally {
			stack.pop();
		}
	}
	catch (Exception | StackOverflowError e) {
		Trace.traceEnd(Trace.TRACE_TAG_RESOURCES);
		final NotFoundException rnf = new NotFoundException(
		                    "File " + file + " from drawable resource ID #0x" + Integer.toHexString(id));
		rnf.initCause(e);
		throw rnf;
	}
	Trace.traceEnd(Trace.TRACE_TAG_RESOURCES);
	if (TRACE_FOR_DETAILED_PRELOAD) {
		if (((id >>> 24) == 0x1)) {
			final String name = getResourceName(id);
			if (name != null) {
				final long time = System.nanoTime() - startTime;
				final int loadedBitmapCount =
				                            Bitmap.sPreloadTracingNumInstantiatedBitmaps - startBitmapCount;
				final long loadedBitmapSize =
				                            Bitmap.sPreloadTracingTotalBitmapsSize - startBitmapSize;
				final int loadedDrawables =
				                            sPreloadTracingNumLoadedDrawables - startDrawableCount;
				sPreloadTracingNumLoadedDrawables++;
				final Boolean isRoot = (android.os.Process.myUid() == 0);
				Log.d(TAG_PRELOAD,
				                            (isRoot ? "Preloaded FW drawable #"
				                                    : "Loaded non-preloaded FW drawable #")
				                            + Integer.toHexString(id)
				                            + " " + name
				                            + " " + file
				                            + " " + dr.getClass().getCanonicalName()
				                            + " #nested_drawables= " + loadedDrawables
				                            + " #bitmaps= " + loadedBitmapCount
				                            + " total_bitmap_size= " + loadedBitmapSize
				                            + " in[us] " + (time / 1000));
			}
		}
	}
	return dr;
}
```


