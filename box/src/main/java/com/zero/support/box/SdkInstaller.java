package com.zero.support.box;

import android.content.Context;

import com.zero.support.box.manager.Launcher;

public class SdkInstaller {

    public boolean canInstall(Context context, String name, Launcher launcher) {
        return launcher.isNeedInstall();
    }

    public void onInstallSdk(Context context, String name, Launcher launcher) {

    }
}
