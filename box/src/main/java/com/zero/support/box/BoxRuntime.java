package com.zero.support.box;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;

import java.lang.reflect.Method;
import java.util.Map;

public class BoxRuntime {
    private Method getAssetManager;
    private Method init;
    private Method getContext;

    public BoxRuntime(Class<?> box) {
        try {
            init = box.getDeclaredMethod("init", Context.class, ClassLoader.class, PackageInfo.class, Map.class);
            getAssetManager = box.getDeclaredMethod("getAssetManager");
            getContext = box.getDeclaredMethod("getContext");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    final void init(android.content.Context context, ClassLoader caller, PackageInfo info, Map<String, Object> extras) {
        try {
            init.invoke(null, context, caller, info, extras);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }


    public final AssetManager getAssetManager() {
        try {
            return (AssetManager) getAssetManager.invoke(null);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public final Context getContext() {
        try {
            return (Context) getContext.invoke(null);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

}