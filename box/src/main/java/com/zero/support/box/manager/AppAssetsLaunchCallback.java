package com.zero.support.box.manager;

import android.content.Context;

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
    public void onBindLauncher(Context context, Launcher launcher) {
        if (launcher.getInstallToken() != new File(context.getPackageCodePath()).lastModified()) {
            installFromAsset(context, launcher);
        }
    }

    private void installFromAsset(Context context, Launcher launcher) {
        launcher.enableNext();
        //onInstallSdk
        File next = launcher.getAvailableNext();
        File target = new File(next, "base.apk");
        try {
            FileUtils.extractAsset(context, new File(assetsDir).getCanonicalPath() + launcher.getName() + "." + extensionName, target);
            FileUtils.extractFile(target, "lib", new File(target.getParentFile(), "lib"));
            launcher.switchToNext(next.getCanonicalPath(), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
