package com.zero.support.box.manager;

import android.content.Context;

public interface LauncherCallback {
    boolean onInstallLauncher(Context context, Launcher launcher);

    ClassLoader onLoadLauncher(Context context, Launcher launcher, ClassLoader parent);

}
