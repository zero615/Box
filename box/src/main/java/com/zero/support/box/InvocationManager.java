package com.zero.support.box;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Pair;

import com.zero.support.box.invoke.IInvocation;
import com.zero.support.box.invoke.LocalInvocation;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("all")
public class InvocationManager implements IInvocation {
    private static Map<String, Pair<Object, Class>> invokes = new HashMap<>();

    private Box box;

    public InvocationManager(Box box) {
        this.box = box;
    }

    @Override
    public void addInvocationTarget(String name, Pair<Object, Class> target) {
        synchronized (invokes) {
            Pair<Object, Class> pair = invokes.put(name, target);
            if (pair != null) {
                LocalInvocation.removeInvocation(pair);
            }
        }
    }

    @Override
    public void removeInvocationTarget(String name) {
        synchronized (invokes) {
            Pair<Object, Class> pair = invokes.remove(name);
            if (pair != null) {
                LocalInvocation.removeInvocation(pair);
            }
        }
    }

    @Override
    public Pair<Object, Class> getInvocationTarget(String name) {
        synchronized (invokes) {
            return invokes.get(name);
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
