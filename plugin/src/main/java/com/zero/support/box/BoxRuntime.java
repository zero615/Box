package com.zero.support.box;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;

import androidx.annotation.Keep;

import java.lang.reflect.Method;
import java.util.Map;

@Keep
@SuppressWarnings("all")
public class BoxRuntime {
    private static PackageInfo packageInfo;
    private static ClassLoader caller;
    private static Context callerContext;
    private static Map<String, Object> extras;
    private static AssetManager assetManager;
    private static Context context;
    private static IBoxPlugin plugin;

    @Keep
    public static void init(Context callerContext, ClassLoader caller, PackageInfo packageInfo, Map<String, Object> extra) {
        BoxRuntime.caller = caller;
        BoxRuntime.callerContext = callerContext;
        BoxRuntime.packageInfo = packageInfo;
        BoxRuntime.extras = extra;
        if (caller != BoxRuntime.class.getClassLoader()) {
            ensureInit();
            context = BoxContext.newBoxContext(callerContext, packageInfo.applicationInfo.theme);
            tryInitBoxPlugin();
        } else {
            context = callerContext;
        }
    }

    private static void tryInitBoxPlugin() {
        context = BoxContext.newBoxContext(callerContext, packageInfo.applicationInfo.theme);
        String name = packageInfo.applicationInfo.name;
        try {
            Class<?> cls = Class.forName(name);
            if (IBoxPlugin.class.isAssignableFrom(cls)) {
                plugin = (IBoxPlugin) cls.newInstance();
                plugin.onPluginLoaded(context);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    public static Context getCallerContext() {
        return callerContext;
    }

    public static ClassLoader getCaller() {
        return caller;
    }

    public static PackageInfo getPackageInfo() {
        return packageInfo;
    }

    public static Map<String, Object> getExtras() {
        return extras;
    }

    public static Object getExtra(String key) {
        return extras.get(key);
    }

    @Keep
    public static Context getContext() {
        return context;
    }

    @Keep
    public static AssetManager getAssetManager() {
        return assetManager;
    }

    public static boolean isPlugin() {
        return caller != BoxRuntime.class.getClassLoader();
    }

    private static void ensureInit() {
        if (assetManager != null) {
            return;
        }
        try {
            String path = packageInfo.applicationInfo.sourceDir;
            if (path != null) {
                assetManager = AssetManager.class.newInstance();
                Method method = AssetManager.class.getMethod("addAssetPath", String.class);
                method.invoke(assetManager, path);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static String getPackageName() {
        return packageInfo.packageName;
    }

}
