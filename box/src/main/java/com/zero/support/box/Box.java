package com.zero.support.box;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import com.zero.support.box.invoke.IInvocation;

import com.zero.support.box.invoke.LocalInvocation;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Box {
    private PackageInfo packageInfo;
    private final Map<String, Object> extras = new HashMap<>();
    private ClassLoader classLoader;
    private AssetManager assetManager;
    private Context context;
    private String boxName;
    private boolean host;
    private IInvocation invocation;

    public Box(String boxName, Context callerContext, PackageInfo packageInfo, ClassLoader classLoader, boolean host) {
        this.boxName = boxName;
        this.packageInfo = packageInfo;
        this.classLoader = classLoader;
        this.host = host;
        this.invocation = new InvocationManager(this);
        if (host) {
            this.context = callerContext;
            this.assetManager = callerContext.getAssets();
        } else {
            createAssetManager();
            this.context = invocation.createBoxContext(callerContext, 0);
        }

        init(boxName, callerContext, callerContext.getClassLoader(), context, classLoader, packageInfo, invocation, extras);
    }

    final void init(String boxName, Context callerContext, ClassLoader caller, Context context, ClassLoader loader, PackageInfo info, IInvocation invocation, Map<String, Object> extras) {
        try {
            Class<?> initializer = Class.forName(boxName + ".BoxInitializer", true, loader);
            Method init = initializer.getDeclaredMethod("init", String.class, Context.class, ClassLoader.class, PackageInfo.class, Context.class, Pair.class, Map.class);
            init.invoke(null, boxName, callerContext, caller, info, context, new Pair(invocation, IInvocation.class), extras);
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

    public <T> T getBoxService(String name, Class<?> cls) {
        Pair<Object, Class> pair = invocation.getInvocationTarget(name);
        return LocalInvocation.asInvocation(pair, cls);
    }

    public String getBoxName() {
        return boxName;
    }

    public Context getContext() {
        return context;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    private void createAssetManager() {
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


    public Object putExtra(String key, Object o) {
        synchronized (extras) {
            return extras.put(key, o);
        }
    }

    public Object removeExtra(String key) {
        synchronized (extras) {
            return extras.remove(key);
        }
    }

    public void putAllExtra(Map<String, Object> map) {
        synchronized (extras) {
            extras.putAll(map);
        }

    }

    public void clearExtra() {
        synchronized (extras) {
            extras.clear();
        }
    }

    Map<String, Object> getExtras() {
        return extras;
    }

    public PackageInfo getPackageInfo() {
        return packageInfo;
    }


    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public boolean isPlugin() {
        return !host;
    }

}
