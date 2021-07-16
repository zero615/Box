package com.zero.support.box;

import android.content.Context;

import com.zero.support.box.SdkInstaller;
import com.zero.support.box.manager.Launcher;
import com.zero.support.box.util.FileUtils;

import java.io.File;
import java.io.IOException;

public class AssetsInstaller extends SdkInstaller {
    private String assets;
    private String suffix;

    public AssetsInstaller(String assets, String suffix) {
        this.assets = assets;
        this.suffix = suffix;
    }

    @Override
    public void onInstallSdk(Context context, String name, Launcher launcher) {
        if (launcher.getNextPath() != null) {
            launcher.switchToNext(launcher.getNextPath());
            return;
        }
        File next = launcher.getAvailableNext();
        File target = new File(next, "base.apk");
        try {
            FileUtils.extractAsset(context, assets + name + suffix, target);
            FileUtils.extractFile(target, "lib", new File(target.getParentFile(), "lib"));
            launcher.switchToNext(next.getCanonicalPath());
            launcher.removePrevious();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean canInstall(Context context, String name, Launcher launcher) {
        return super.canInstall(context, name, launcher);
    }
}
