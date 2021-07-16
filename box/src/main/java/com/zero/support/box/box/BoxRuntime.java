package com.zero.support.box.box;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;

import java.lang.reflect.Method;
import java.util.Map;

public class BoxRuntime {
    private Method getPackageInfo;
    private Method getCallerContext;
    private Method getCaller;
    private Method getExtra;
    private Method init;

    public BoxRuntime(Class<?> box, PackageInfo packageInfo) {
        try {
            getPackageInfo = box.getDeclaredMethod("getPackageInfo");
            getCallerContext = box.getDeclaredMethod("getCallerContext");
            getCaller = box.getDeclaredMethod("getCaller");
            getExtra = box.getDeclaredMethod("getExtra", String.class);
            init = box.getDeclaredMethod("init", Application.class, ClassLoader.class, PackageInfo.class, Map.class);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    final void init(Application context, ClassLoader caller, PackageInfo info, Map extras) {
        try {
            init.invoke(null, context, caller, info, extras);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public final String getPackageInfo() {
        try {
            return (String) getPackageInfo.invoke(null);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public final Context getCallerContext() {
        try {
            return (Context) getCallerContext.invoke(null);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public final ClassLoader getCaller() {
        try {
            return (ClassLoader) getCaller.invoke(null);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public final Object getExtra(String key) {
        try {
            return getExtra.invoke(null, key);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}