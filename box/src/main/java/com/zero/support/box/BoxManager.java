package com.zero.support.box;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import dalvik.system.DexClassLoader;

@SuppressWarnings("all")
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

    public static Box getBox(String packageName) {
        synchronized (boxes) {
            return boxes.get(packageName);
        }
    }


    public static void becomToParent(ClassLoader target, ClassLoader loader) {
        try {
            if (target == null || loader == null) {
                return;
            }
            parentField.set(target, loader);
        } catch (Throwable e) {
            e.printStackTrace();
        }
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
            packageInfo.applicationInfo.nativeLibraryDir = lib.getCanonicalPath();
            DexClassLoader classLoader = new DexClassLoader(path, apk.getParentFile().getCanonicalPath(), lib.getCanonicalPath(), parent);
            Box box = new Box(packageInfo.packageName,context,packageInfo, classLoader);
            synchronized (boxes) {
                boxes.put(packageInfo.packageName, box);
            }
            return box;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
}
