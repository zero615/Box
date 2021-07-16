package com.zero.support.box;

import android.app.Application;
import android.content.pm.PackageInfo;

import java.io.File;

public abstract class SdkConfig {


    public SdkConfig() {

    }

    public abstract Application getApplication();

    public abstract File getRoot();

    public abstract PackageInfo getHostPackageInfo();

    public abstract SdkInstaller getSdkInstaller();
}
