package com.zero.support.box;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.util.Log;

import com.zero.support.box.plugin.invoke.IInvocation;
import com.zero.support.box.plugin.invoke.LocalInvocation;
import com.zero.support.box.plugin.invoke.MethodInvoke;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Box {
    private final PackageInfo packageInfo;
    private final Map<String, Object> extras = new HashMap<>();
    private final ClassLoader classLoader;
    private AssetManager assetManager;
    private final Context context;
    private String boxName;
    private final boolean host;
    private final InvocationManager invocation;
    private IInvocation boxInvocation;
    private volatile boolean prepared;

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
    }

    public void setBoxName(String boxName) {
        this.boxName = boxName;
    }

    public boolean isPrepared() {
        return prepared;
    }

    public void prepare() {
        if (prepared) {
            return;
        }
        prepared = true;
        Object[] objects = init(boxName, Sdk.getApplication(), Sdk.getApplication().getClassLoader(), context, classLoader, packageInfo, invocation, extras);
        if (objects != null) {
            boxInvocation = LocalInvocation.asInvocation((Object) objects[0], (Class<?>) objects[1], (Map<String, Object[]>) objects[2], IInvocation.class);
            LocalInvocation.setTargetInvocation(boxInvocation);
        }
    }

    public IInvocation getBoxInvocation() {
        return boxInvocation;
    }

    final Object[] init(String boxName, Context callerContext, ClassLoader caller, Context context, ClassLoader loader, PackageInfo info, InvocationManager invocation, Map<String, Object> extras) {
        try {
            Class<?> initializer = Class.forName(boxName + ".BoxInitializer", true, loader);
            Method init = initializer.getDeclaredMethod("init", String.class, Context.class, ClassLoader.class, PackageInfo.class, Context.class, Object.class, Class.class, Map.class, Map.class);
            return (Object[]) init.invoke(null, boxName, callerContext, caller, info, context, invocation, IInvocation.class, MethodInvoke.createMethod(IInvocation.class, false), extras);
        } catch (Throwable e) {
            e.printStackTrace();
            Log.e("BoxRuntime", "initializer: failed for " + boxName);
        }
        return null;
    }


    public <T> T getService(String name, Class<T> cls) {
        return LocalInvocation.asInvocation(invocation.getInvocationTarget(name), cls);
    }

    public void addService(String name, Object object, Class<?> cls) {
        invocation.addInvocationTarget(name, object, cls);
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

    public InvocationManager getInvocation() {
        return invocation;
    }
}
