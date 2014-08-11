package com.delectable.mobile.ui;

public interface LifecycleProvider {
    public void registerLifecycleListener(LifecycleListener listener);

    public void unregister(LifecycleListener listener);

    public boolean isVisible();
}
