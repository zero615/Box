package com.zero.support.box;

import android.content.pm.PackageInfo;

public class Box {
    private String packageName;
    private String path;
    public BoxClassLoader classLoader;

    public Box(PackageInfo packageInfo, BoxClassLoader classLoader, BoxRuntime runtime) {

    }
}
