package com.zero.support.box;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.zero.support.box.manager.AppAssetsLaunchCallback;
import com.zero.support.box.manager.LauncherCallback;

import java.io.File;

public class SimpleSdkConfig extends SdkConfig {
    private PackageInfo packageInfo;
    private File root;
    private Application app;
    private AppAssetsLaunchCallback callback;


    /**
     * @param app
     * @param root   sdk 本地目录
     * @param assets sdk assets 目录
     * @param suffix sdk suffix sdk扩展名
     */
    public SimpleSdkConfig(Application app, File root, String assets, String suffix) {
        this.app = app;
        this.root = root;
        callback = new AppAssetsLaunchCallback(assets,suffix);
        try {
            this.packageInfo = app.getPackageManager().getPackageInfo(app.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Application getApplication() {
        return app;
    }

    @Override
    public File getRoot() {
        return root;
    }

    @Override
    public PackageInfo getHostPackageInfo() {
        return packageInfo;
    }

    @Override
    public LauncherCallback getLaunchCallback() {
        return callback;
    }

}
