package com.zero.support.box.plugin;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;

import java.lang.reflect.InvocationHandler;
import java.util.Map;


@SuppressWarnings("all")
public class BoxRuntime {
    private static String boxName;
    private static ClassLoader caller;
    private static Context callerContext;
    private static PackageInfo packageInfo;
    private static Context context;
    private static Map<String, Object> extras;
    private static InvocationHandler callerHandler;

    public static void init(String name, Context callerContext, ClassLoader caller, PackageInfo packageInfo, Context context, InvocationHandler handler, Map<String, Object> extra) {
        BoxRuntime.boxName = name;
        BoxRuntime.caller = caller;
        BoxRuntime.callerContext = callerContext;
        BoxRuntime.packageInfo = packageInfo;
        BoxRuntime.callerHandler = handler;
        BoxRuntime.extras = extra;
    }

    public String getBoxName() {
        return boxName;
    }

    public InvocationHandler registerInvocationHandler(String name, InvocationHandler handler) {
        name = "@BoxHandler:" + name;
        synchronized (extras) {
            return (InvocationHandler) extras.put(name, handler);
        }
    }

    public InvocationHandler unregisterInvocationHandler(String name) {
        name = "@BoxHandler:" + name;
        synchronized (extras) {
            return (InvocationHandler) extras.remove(name);
        }
    }

    public InvocationHandler getCallerInvocationHandler(String name) {
        name = "@CallerHandler:" + name;
        synchronized (extras) {
            return (InvocationHandler) extras.remove(name);
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

    public static Context getContext() {
        return context;
    }

    public static boolean isPlugin() {
        return caller != BoxRuntime.class.getClassLoader();
    }

    public static Context createBoxContext(Context base, int theme) {
        return (Context) callCallerMethod("_createBoxContext", base, theme);
    }

    public static Context createBoxContextForActivity(Activity activity, Context base) {
        return (Context) callCallerMethod("_createBoxContextForActivity", activity, base);
    }

    public static Object callCallerMethod(String name, Object... args) {
        try {
            return (InvocationHandler) callerHandler.invoke(name, null, args);
        } catch (Throwable throwable) {
            //ignore
        }
        return null;
    }

}
