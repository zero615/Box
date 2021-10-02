package com.zero.support.box.invoke;

import android.util.Pair;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.WeakHashMap;


public class LocalInvocation {
    private static final Map<Object, Object> invokes = new WeakHashMap<>();

    public static <T> T asInvocation(Pair<Object, Class> target, Class<?> cls) {
        return asInvocation(target.first, target.second, cls);
    }

    public static <T> T asInvocation(Object target, Class<?> targetCls, Class<?> cls) {
        if (target == null) {
            return null;
        }
        synchronized (invokes) {
            Object o = queryKey(target);
            if (o == null) {
                o = Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, new ReflectInvoke( targetCls, cls));
                invokes.put(o, target);
            }
            return (T) o;
        }
    }

    public static <T> T peekInvocation(Object target) {
        if (target == null) {
            return null;
        }
        synchronized (invokes) {
            Object o = queryKey(target);
            return (T) o;
        }
    }


    public static Object getTarget(Object invocation) {
        synchronized (invokes) {
            return invokes.get(invocation);
        }
    }

    private static Object queryKey(Object target) {
        Object key = null;
        for (Map.Entry<Object, Object> entry : invokes.entrySet()) {
            if (entry.getValue() == target) {
                key = entry.getKey();
                break;
            }
        }
        return key;
    }

    public static void removeInvocation(Object target) {
        synchronized (invokes) {
            Object key = queryKey(target);
            if (key != null) {
                invokes.remove(target);
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
