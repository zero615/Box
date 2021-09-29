package com.zero.support.box.manager;

import android.content.Context;

import com.zero.support.box.Box;

public interface LauncherCallback {
    boolean onInstallLauncher(Context context, Launcher launcher);

    Box onLoadLauncher(Context context, Launcher launcher, ClassLoader parent);

}
