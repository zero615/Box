package com.zero.support.box;

import android.content.pm.PackageInfo;

import dalvik.system.DexClassLoader;

public class Box {
    private PackageInfo packageInfo;
    private BoxRuntime runtime;
    public ClassLoader classLoader;

    public Box(PackageInfo packageInfo, DexClassLoader classLoader, BoxRuntime runtime) {
        this.packageInfo = packageInfo;
        this.classLoader = classLoader;
        this.runtime = runtime;
    }

    public PackageInfo getPackageInfo() {
        return packageInfo;
    }

    public BoxRuntime getRuntime() {
        return runtime;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }
}
