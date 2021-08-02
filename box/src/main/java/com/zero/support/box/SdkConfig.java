package com.zero.support.box;

import android.app.Application;
import android.content.pm.PackageInfo;

import com.zero.support.box.manager.LauncherCallback;

import java.io.File;

public abstract class SdkConfig {


    public SdkConfig() {

    }

    public abstract Application getApplication();

    public abstract File getRoot();

    public abstract PackageInfo getHostPackageInfo();

    public abstract LauncherCallback getLaunchCallback();
}
