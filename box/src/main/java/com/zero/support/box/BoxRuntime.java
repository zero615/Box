package com.zero.support.box;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

public class BoxRuntime implements InvocationHandler {
    private final Box box;

    public BoxRuntime(Box box) {
        this.box = box;
    }

    final void init(String boxName, Context callerContext, ClassLoader caller, Context context, ClassLoader loader, PackageInfo info, Map<String, Object> extras) {

        try {
            Class<?> initializer = Class.forName(boxName + ".BoxInitializer", true, loader);
            Method init = initializer.getDeclaredMethod("init", String.class, Context.class, ClassLoader.class, PackageInfo.class, Context.class, InvocationHandler.class, Map.class);
            init.invoke(null, boxName, callerContext, caller, info, context, this, extras);
        } catch (Throwable e) {
            e.printStackTrace();
            Log.e("BoxRuntime", "initializer: failed for " + boxName);
        }
        try {
            Class<?> boxPlugin = Class.forName(boxName + ".BoxPlugin", true, loader);
            Method onPluginLoaded = boxPlugin.getDeclaredMethod("onPluginLoaded");
            onPluginLoaded.invoke(null);
        } catch (Throwable e) {
            Log.e("BoxRuntime", "tryInitBoxPlugin: failed for " + boxName);
        }

    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String name = String.valueOf(proxy);
        switch (name) {
            case "_createBoxContextForActivity":
                return box.createBoxContextForActivity((Activity) args[0], (Context) args[1]);
            case "_createBoxContext":
                return box.createBoxContext((Context) args[0], (int) args[1]);
        }
        return null;
    }
}