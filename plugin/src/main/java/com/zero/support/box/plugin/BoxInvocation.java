package com.zero.support.box.plugin;

import android.app.Activity;
import android.content.Context;

import com.zero.support.box.plugin.invoke.IInvocation;
import com.zero.support.box.plugin.invoke.LocalInvocation;
import com.zero.support.box.plugin.invoke.MethodInvoke;
import com.zero.support.box.plugin.invoke.TargetHolder;

import java.util.HashMap;
import java.util.Map;

public class BoxInvocation implements IInvocation {

    private final Map<Class<?>, Map<String, Object[]>> methods = new HashMap<>();

    @Override
    public void addInvocationTarget(String name, Object target, Class<?> targetCls) {
        BoxRuntime.getCallerInvocation().addInvocationTarget(name, target, targetCls);
    }

    public Object[] getInvocationTarget(String name) {
        return BoxRuntime.getCallerInvocation().getInvocationTarget(name);
    }

    @Override
    public void removeInvocationTarget(String name) {
        BoxRuntime.getCallerInvocation().removeInvocationTarget(name);
    }


    @Override
    public Map<String, Object[]> getInvocationMethods(Class<?> cls) {
        synchronized (methods) {
            Map<String, Object[]> map = methods.get(cls);
            if (map == null) {
                map = MethodInvoke.createMethod(cls, false);
                methods.put(cls, map);
            }
            return map;
        }
    }


    @Override
    public Context createBoxContext(Context base, int theme) {
        return BoxRuntime.getCallerInvocation().createBoxContext(base, theme);
    }

    @Override
    public Context createBoxContextForActivity(Context context, Activity activity) {
        return BoxRuntime.getCallerInvocation().createBoxContextForActivity(context, activity);
    }

}
