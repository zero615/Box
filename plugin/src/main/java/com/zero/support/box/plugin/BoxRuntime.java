package com.zero.support.box.plugin;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.util.Pair;

import com.zero.support.box.plugin.invoke.IInvocation;
import com.zero.support.box.plugin.invoke.LocalInvocation;
import com.zero.support.box.plugin.invoke.MethodInvoke;

import java.lang.reflect.Method;
import java.util.Map;


@SuppressWarnings("all")
public class BoxRuntime {
    private static String boxName;
    private static ClassLoader caller;
    private static Context callerContext;
    private static PackageInfo packageInfo;
    private static Context context;
    private static Map<String, Object> extras;
    private static IInvocation callerInvocation;
    private static BoxInvocation invocation;

    public static Object[] init(String name, Context callerContext, ClassLoader caller, PackageInfo packageInfo, Context context, Object target, Class<?> cls, Map<String, Object[]> methods, Map<String, Object> extra) {
        BoxRuntime.boxName = name;
        BoxRuntime.caller = caller;
        BoxRuntime.callerContext = callerContext;
        BoxRuntime.packageInfo = packageInfo;
        BoxRuntime.invocation = new BoxInvocation();
        BoxRuntime.callerInvocation = LocalInvocation.asInvocation(target, cls, methods, IInvocation.class);
        LocalInvocation.setTargetInvocation(BoxRuntime.callerInvocation);
        BoxRuntime.extras = extra;
        Object[] objects = new Object[3];
        objects[0] = invocation;
        objects[1] = IInvocation.class;
        objects[2] = MethodInvoke.createMethod(IInvocation.class,false);
        return objects;
    }

    public static String getBoxName() {
        return boxName;
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

    public static IInvocation getCallerInvocation() {
        return callerInvocation;
    }

    public static <T> T getCallerService(String name, Class<T> cls) {
        return LocalInvocation.asInvocation(invocation.getInvocationTarget(name), cls);
    }

    public static BoxInvocation getInvocation() {
        return invocation;
    }
}
