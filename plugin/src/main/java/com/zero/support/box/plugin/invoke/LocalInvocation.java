package com.zero.support.box.plugin.invoke;

import android.util.Log;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.WeakHashMap;


public class LocalInvocation {
    private static final WeakHashMap<Object, Object> invocations = new WeakHashMap<>();
    private static IInvocation targetInvocation;

    public static void setTargetInvocation(IInvocation invocation) {
        LocalInvocation.targetInvocation = invocation;
    }

    public static <T> T asInvocation(TargetHolder holder, Class<?> cls) {
        return asInvocation(holder.object, holder.cls, null, cls);
    }

    public static <T> T asInvocation(Object target, Class<?> targetCls, Map<String, Object[]> targetMethod, Class<?> cls) {
        if (target == null) {
            return null;
        }
        if (targetCls == cls) {
            return (T) target;
        }
        if (targetMethod == null) {
            targetMethod = targetInvocation.getInvocationMethods(targetCls);
            Log.e("xgf", "asInvocation: "+targetMethod );
        }
        synchronized (invocations) {
            Object o = findInvocationByTarget(target);
            if (o == null) {
                ReflectInvocationHandler handler = new ReflectInvocationHandler(targetMethod, cls);
                o = Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, handler);
                invocations.put(o, target);
            }
            return (T) o;
        }
    }

    public static <T> T peekInvocation(Object target) {
        if (target == null) {
            return null;
        }
        synchronized (invocations) {
            Object o = findInvocationByTarget(target);
            return (T) o;
        }
    }


    public static Object getTarget(Object invocation) {
        synchronized (invocations) {
            return invocations.get(invocation);
        }
    }

    private static Object findInvocationByTarget(Object target) {
        Object key = null;
        for (Map.Entry<Object, Object> entry : invocations.entrySet()) {
            if (entry.getValue() == target) {
                key = entry.getKey();
                break;
            }
        }
        return key;
    }

    public static void removeInvocation(Object target) {
        synchronized (invocations) {
            Object key = findInvocationByTarget(target);
            if (key != null) {
                invocations.remove(target);
            }
        }
    }

    public static String getInvokeName(Class<?> cls) {
        BoxName boxName = cls.getAnnotation(BoxName.class);
        if (boxName != null) {
            return boxName.value();
        }
        return cls.getName();
    }

}
