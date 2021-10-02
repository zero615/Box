package com.zero.support.box;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Box {
    private PackageInfo packageInfo;
    private BoxRuntime runtime;
    private final Map<String, Object> extras = new HashMap<>();
    public ClassLoader classLoader;
    public AssetManager assetManager;
    private Context context;
    private String boxName;


    public Box(String boxName, Context callerContext, PackageInfo packageInfo, ClassLoader classLoader) {
        this.boxName = boxName;
        this.packageInfo = packageInfo;
        if (classLoader == null || classLoader == Box.class.getClassLoader()) {
            this.classLoader = Box.class.getClassLoader();
            this.context = callerContext;
            this.assetManager = callerContext.getAssets();
        } else {
            this.classLoader = classLoader;
            createAssetManager();
            this.context = createBoxContext(callerContext, getDefaultTheme());
        }
        this.runtime = new BoxRuntime(this);
        runtime.init(boxName, callerContext, callerContext.getClassLoader(), context, classLoader, packageInfo, extras);
    }

    public String getBoxName() {
        return boxName;
    }

    public Context getContext() {
        return context;
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


    public int getTheme(Activity activity) {
        PackageInfo info = getPackageInfo();
        String name = activity.getClass().getName();
        ActivityInfo[] activities = info.activities;
        int theme = info.applicationInfo.theme;
        if (activities != null) {
            for (ActivityInfo activityInfo : activities) {
                String target;
                if (activityInfo.targetActivity != null) {
                    target = activityInfo.targetActivity;
                } else {
                    target = activityInfo.name;
                }
                if (TextUtils.equals(activityInfo.name, name)) {
                    if (activityInfo.theme != 0) {
                        theme = activityInfo.theme;
                    }
                    break;
                }
            }
        }
        return theme;
    }

    public int getDefaultTheme() {
        return getPackageInfo().applicationInfo.theme;
    }

    public Context createBoxContext(Context base, int theme) {
        if (theme == 0) {
            theme = getDefaultTheme();
        }
        return new BoxContext(classLoader, packageInfo, base, newResources(base), theme);
    }

    public Context createBoxContextForActivity(Activity activity, Context base) {
        int theme = getTheme(activity);
        return createBoxContext(base, theme);
    }

    private Resources newResources(Context context) {
        Resources resource = context.getResources();
        return new BoxResources(classLoader, packageInfo.packageName, assetManager, resource);
    }

    public InvocationHandler getCallerInvokeHandler(String name) {
        String realName = "@CallerHandler:" + name;
        synchronized (extras) {
            return (InvocationHandler) extras.get(realName);
        }
    }

    public void registerCallerInvokeHandler(String name, InvocationHandler handler) {
        String realName = "@CallerHandler:" + name;
        synchronized (extras) {
            extras.put(realName, handler);
        }
    }

    public void unregisterCallerInvokeHandler(String name) {
        String realName = "@CallerHandler:" + name;
        synchronized (extras) {
            extras.remove(realName);
        }
    }

    public InvocationHandler getBoxInvokeHandler(String name) {
        synchronized (extras) {
            String realName = "@BoxHandler:" + name;
            return (InvocationHandler) extras.get(realName);
        }
    }
}
