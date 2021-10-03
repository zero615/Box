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

    private final Map<String, TargetHolder> invokes = new HashMap<>();
    private final Map<Class<?>,Map<String,Object[]>> methods = new HashMap<>();

    public static final BoxInvocation INSTANCE = new BoxInvocation();
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
        synchronized (methods){
           Map<String,Object[]> map =  methods.get(cls);
           if (map==null){
               map = MethodInvoke.createMethod(cls,false);
               methods.put(cls,map);
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
