package com.zero.support.box;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

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

    public static Box loadToParent(Context context, File path, File lib, ClassLoader loader) {
        Box target = load(context, loader.getParent(), path, lib);
        try {
            parentField.set(loader, target);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return target;
    }

    public static Box load(Context context, File path, File lib) {
        return load(context, context.getClassLoader(), path, lib);
    }

    public static Box load(Context context, ClassLoader parent, File apk, File lib) {
        try {
            String path = apk.getCanonicalPath();
            PackageInfo packageInfo = context.getPackageManager().getPackageArchiveInfo(path, PackageManager.GET_META_DATA | PackageManager.GET_ACTIVITIES);
            packageInfo.applicationInfo.sourceDir = path;
            packageInfo.applicationInfo.publicSourceDir = path;
            DexClassLoader classLoader = new DexClassLoader(path, apk.getParentFile().getCanonicalPath(), lib.getCanonicalPath(), parent);
            String runtimeClass = packageInfo.packageName + NAME_RUNTIME;
            Class<?> cls = classLoader.loadClass(runtimeClass);
            BoxRuntime runtime = new BoxRuntime(cls);
            runtime.init(context, context.getClassLoader(), packageInfo, Collections.emptyMap());
            Box box = new Box(packageInfo, classLoader, runtime);
            boxes.put(packageInfo.packageName, box);
            return box;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
}
