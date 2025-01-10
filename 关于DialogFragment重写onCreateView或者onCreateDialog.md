# DialogFragment 的 onCreateView 返回的 View 是怎么设置给 Dialog 的。

一句话概括，就是 监听生命周期变化，当 Fragment 调用了 onCreateView 方法之后，初始化并发送生命周期事件。

DialogFragment 在 onAttach 的时候，观察生命周期，收到事件的时候，把 onCreateView 方法 返回的View 设置给Dialog ( mDialog.setContentView(view);)。


DialogFragment 的 监听器，观察声明周期

```java

private Observer<LifecycleOwner> mObserver = new Observer<LifecycleOwner>() {
    @SuppressLint("SyntheticAccessor")
    @Override
    public void onChanged(LifecycleOwner lifecycleOwner) {
        if (lifecycleOwner != null && mShowsDialog) {
            //注释1处，这里内部会调用 getView 方法，返回 onCreateView 方法 创建的View
            View view = requireView();
            if (view.getParent() != null) {
                throw new IllegalStateException(
                        "DialogFragment can not be attached to a container view");
            }
            if (mDialog != null) {
                if (FragmentManager.isLoggingEnabled(Log.DEBUG)) {
                    Log.d(TAG, "DialogFragment " + this + " setting the content view on "
                            + mDialog);
                }
                mDialog.setContentView(view);
            }
        }
    }
};

```

注释1处，这里内部会调用 getView 方法，返回 onCreateView 方法 创建的View。 这里要注意，会判断 lifecycleOwner 不为null 才 执行。

```java

@MainThread
@Override
public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    //注释2处，添加监听
    getViewLifecycleOwnerLiveData().observeForever(mObserver);
    if (!mShownByMe) {
        // If not explicitly shown through our API, take this as an
        // indication that the dialog is no longer dismissed.
        mDismissed = false;
    }
}
```
注释2处，添加监听。

Fragment 的 performCreateView 方法。

```java
void performCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        mChildFragmentManager.noteStateNotSaved();
        mPerformedCreateView = true;
        mViewLifecycleOwner = new FragmentViewLifecycleOwner(this, getViewModelStore(),
                () -> {
                    // Perform the restore as soon as the FragmentViewLifecycleOwner
                    // becomes initialized, to ensure it is always available
                    mViewLifecycleOwner.performRestore(mSavedViewRegistryState);
                    mSavedViewRegistryState = null;
                });
        //注释1处，这里内部会调用 onCreateView 方法，创建View
        mView = onCreateView(inflater, container, savedInstanceState);
        if (mView != null) {
            //注释化 mViewLifecycleOwner
            // Initialize the view lifecycle
            mViewLifecycleOwner.initialize();
            // Tell the fragment's new view about it before we tell anyone listening
            // to mViewLifecycleOwnerLiveData and before onViewCreated, so that calls to
            // ViewTree get() methods return something meaningful
            if (FragmentManager.isLoggingEnabled(Log.DEBUG)) {
                Log.d(FragmentManager.TAG, "Setting ViewLifecycleOwner on View " + mView
                        + " for Fragment " + this);
            }
            ViewTreeLifecycleOwner.set(mView, mViewLifecycleOwner);
            ViewTreeViewModelStoreOwner.set(mView, mViewLifecycleOwner);
            ViewTreeSavedStateRegistryOwner.set(mView, mViewLifecycleOwner);
            //注释2处，这里才为 mViewLifecycleOwnerLiveData 的 LifecycleOwner 赋值为 mViewLifecycleOwner
            // Then inform any Observers of the new LifecycleOwner
            mViewLifecycleOwnerLiveData.setValue(mViewLifecycleOwner);
        } else {
            if (mViewLifecycleOwner.isInitialized()) {
                throw new IllegalStateException("Called getViewLifecycleOwner() but "
                        + "onCreateView() returned null");
            }
            mViewLifecycleOwner = null;
        }
    }

```

注释1处，这里内部会调用 onCreateView 方法，创建View。

注释2处，这里才为 mViewLifecycleOwnerLiveData 的 LifecycleOwner 赋值为 mViewLifecycleOwner。


大概原理就是这样，DialogFragment 的onAttach方法添加监听，在某个生命周期变化的时候，给 Dialog setContentView： onCreateView 方法创建的View。