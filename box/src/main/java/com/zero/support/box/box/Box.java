package com.zero.support.box.box;

import android.content.pm.PackageInfo;

import dalvik.system.DexClassLoader;

public class Box {
    private String packageName;
    private String path;
    public DexClassLoader classLoader;

    public Box(PackageInfo packageInfo, DexClassLoader classLoader, BoxRuntime runtime) {

    }
}
