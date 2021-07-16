package com.zero.support.box.box;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import dalvik.system.DexClassLoader;

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

    public static ClassLoader loadToParent(Application context, String path, String lib) {
        ClassLoader loader = context.getClassLoader();
        ClassLoader target = load(context, loader.getParent(), path, lib);
        try {
            parentField.set(loader, target);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return target;
    }

    public static ClassLoader load(Application context, String path, String lib) {
        return load(context, context.getClassLoader(), path, lib);
    }

    public static ClassLoader load(Application context, ClassLoader parent, String path, String lib) {
        PackageInfo packageInfo = context.getPackageManager().getPackageArchiveInfo(path, PackageManager.GET_META_DATA | PackageManager.GET_ACTIVITIES);
        packageInfo.applicationInfo.sourceDir = path;
        packageInfo.applicationInfo.publicSourceDir = path;
        DexClassLoader classLoader = new DexClassLoader(path,  new File(path).getParent(),lib, parent);
        String runtimeClass = packageInfo.packageName + NAME_RUNTIME;
        try {
            Class<?> cls = classLoader.loadClass(runtimeClass);
            BoxRuntime runtime = new BoxRuntime(cls, packageInfo);
            runtime.init( context, context.getClassLoader(), packageInfo, Collections.emptyMap());
            Box box = new Box(packageInfo, classLoader, runtime);
            boxes.put(packageInfo.packageName, box);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return classLoader;
    }
}
