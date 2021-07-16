package com.zero.support.box;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import dalvik.system.BaseDexClassLoader;

public class BoxManager {
    public static final String NAME_RUNTIME = ".BoxRuntime";
    private static final Map<String, Box> boxes = new HashMap<>();
    private static Field parentField;

    static {
        try {
            parentField = ClassLoader.class.getDeclaredField("parent");
            parentField.setAccessible(true);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static ClassLoader loadToParent(Context context, String path, String lib) {
        ClassLoader loader = context.getClassLoader();
        ClassLoader target = load(context, loader.getParent(), path, lib);
        try {
            parentField.set(loader, target);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return target;
    }

    public static ClassLoader load(Context context, String path, String lib) {
        return load(context, context.getClassLoader(), path, lib);
    }

    public static ClassLoader load(Context context, ClassLoader parent, String path, String lib) {
        PackageInfo packageInfo = context.getPackageManager().getPackageArchiveInfo(path, PackageManager.GET_META_DATA | PackageManager.GET_ACTIVITIES);
        packageInfo.applicationInfo.sourceDir = path;
        packageInfo.applicationInfo.publicSourceDir = path;
        BoxClassLoader classLoader = new BoxClassLoader(path, lib, (BaseDexClassLoader) parent);
        String runtimeClass = packageInfo.packageName + NAME_RUNTIME;
        try {
            Class<?> cls = classLoader.findClass(runtimeClass);
            BoxRuntime runtime = new BoxRuntime(cls);
            runtime.init(context, context.getClassLoader(), packageInfo, Collections.emptyMap());
            Box box = new Box(packageInfo, classLoader, runtime);
            boxes.put(packageInfo.packageName, box);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return classLoader;
    }
}
