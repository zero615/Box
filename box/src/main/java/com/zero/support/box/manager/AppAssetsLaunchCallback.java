package com.zero.support.box.manager;

import android.content.Context;

import com.zero.support.box.Box;
import com.zero.support.box.BoxManager;
import com.zero.support.box.Sdk;
import com.zero.support.box.util.FileUtils;

import java.io.File;
import java.io.IOException;

public class AppAssetsLaunchCallback implements LauncherCallback {

    private String assetsDir;
    private String extensionName;

    public AppAssetsLaunchCallback(String assetsDir, String extensionName) {
        this.assetsDir = assetsDir;
        this.extensionName = extensionName;
    }

    @Override
    public boolean onInstallLauncher(Context context, Launcher launcher) {
        if (launcher.getInstallToken() != new File(context.getPackageCodePath()).lastModified()) {
            installFromAsset(context, launcher);
        }
        return launcher.getCurrentPath() != null;
    }

    @Override
    public Box onLoadLauncher(Context context, Launcher launcher, ClassLoader parent) {
        String next = launcher.getCurrentPath();
        File target = new File(next, "base.apk");
        File libRoot = new File(next, "lib");
        File lib = new File(libRoot, Sdk.getAbiName(libRoot));
        return BoxManager.load(context, parent, target, lib);
    }

    private void installFromAsset(Context context, Launcher launcher) {
        launcher.enableNext();
        //onInstallSdk
        File next = launcher.getAvailableNext();
        File target = new File(next, "base.apk");
        try {
            FileUtils.extractAsset(context, assetsDir + launcher.getName() + "." + extensionName, target);
            FileUtils.extractFile(target, "lib", next);
            launcher.switchToNext(next.getCanonicalPath(), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
