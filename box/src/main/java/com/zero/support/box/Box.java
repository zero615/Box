package com.zero.support.box;

import android.content.pm.PackageInfo;

import java.util.HashMap;
import java.util.Map;

import dalvik.system.DexClassLoader;

public class Box {
    private PackageInfo packageInfo;
    private BoxRuntime runtime;
    private final Map<String, Object> extras = new HashMap<>();
    public ClassLoader classLoader;

    public Box(PackageInfo packageInfo, DexClassLoader classLoader) {
        this.packageInfo = packageInfo;
        this.classLoader = classLoader;
    }

    void attachBoxRuntime(BoxRuntime runtime) {
        this.runtime = runtime;
    }

    public Object putExtra(String key, Object o) {
        return extras.put(key, o);
    }

    public Object removeExtra(String key) {
        return extras.remove(key);
    }

    public void putAllExtra(Map<String, Object> map) {
        extras.putAll(map);
    }

    public void clearExtra() {
        extras.clear();
    }

    Map<String,Object> getExtras(){
        return extras;
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
