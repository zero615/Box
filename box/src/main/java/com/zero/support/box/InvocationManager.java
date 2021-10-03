package com.zero.support.box;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.text.TextUtils;

import com.zero.support.box.plugin.invoke.IInvocation;
import com.zero.support.box.plugin.invoke.LocalInvocation;
import com.zero.support.box.plugin.invoke.MethodInvoke;
import com.zero.support.box.plugin.invoke.TargetHolder;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("all")
public class InvocationManager implements IInvocation {

    private Box box;

    public InvocationManager(Box box) {
        this.box = box;
    }


    private final Map<String, TargetHolder> invokes = new HashMap<>();
    private final Map<Class<?>, Map<String, Object[]>> methods = new HashMap<>();

    @Override
    public void addInvocationTarget(String name, Object target, Class<?> targetCls) {
        synchronized (invokes) {
            TargetHolder pair = invokes.put(name, new TargetHolder(target, targetCls));
            if (pair != null) {
                LocalInvocation.removeInvocation(pair);
            }
        }
    }

    public TargetHolder getInvocationTarget(String name) {
        synchronized (invokes) {
            return invokes.get(name);
        }
    }

    @Override
    public void removeInvocationTarget(String name) {
        synchronized (invokes) {
            TargetHolder pair = invokes.remove(name);
            if (pair != null) {
                LocalInvocation.removeInvocation(pair);
            }
        }
    }


    @Override
    public Map<String, Object[]> getInvocationMethods(Class<?> cls) {
        synchronized (methods) {
            Map<String, Object[]> map = methods.get(cls);
            if (map == null) {
                map = MethodInvoke.createMethod(cls,false);
                methods.put(cls, map);
            }
            return map;
        }
    }


    public int getTheme(Activity activity) {
        PackageInfo info = box.getPackageInfo();
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
        return box.getPackageInfo().applicationInfo.theme;
    }

    private Resources newResources(Context context) {
        Resources resource = context.getResources();
        return new BoxResources(box.getClassLoader(), box.getPackageInfo().packageName, box.getAssetManager(), resource);
    }

    @Override
    public Context createBoxContext(Context base, int theme) {
        if (theme == 0) {
            theme = getDefaultTheme();
        }
        return new BoxContext(box.getClassLoader(), box.getPackageInfo(), base, newResources(base), theme);
    }

    @Override
    public Context createBoxContextForActivity(Context context, Activity activity) {
        int theme = getTheme(activity);
        return createBoxContext(context, theme);
    }


}
