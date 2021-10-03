package com.zero.support.box.invoke;

import android.util.Pair;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.WeakHashMap;


public class LocalInvocation {
    private static final WeakHashMap<Object, Object> invocations = new WeakHashMap<>();

    public static <T> T asInvocation(Pair<Object, Class> target, Class<?> cls) {
        return asInvocation(target.first, target.second, cls);
    }

    public static <T> T asInvocation(Object target, Class<?> targetCls, Class<?> cls) {
        if (target == null) {
            return null;
        }
        synchronized (invocations) {
            Object o = findInvocationByTarget(target);
            if (o == null) {
                ReflectInvocationHandler handler =  new ReflectInvocationHandler( targetCls, cls);
                o = Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls},handler);
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
