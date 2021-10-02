package com.zero.support.box;


import com.zero.support.box.invoke.BoxName;
import com.zero.support.box.invoke.InvokeTarget;
import com.zero.support.box.invoke.ReflectInvoke;

import java.util.HashMap;
import java.util.Map;

public class LocalInvoke {
    private final Map<Object, Object> invokes = new HashMap<>();
    private Box box;
    public LocalInvoke(Box box) {
    }
    public  <T> T asInvoke(InvokeTarget target, Class<?> cls) {
        if (target == null || target.object == null) {
            return null;
        }
        synchronized (invokes) {
            Object o = invokes.get(target.object);
            if (o == null) {
                o = new ReflectInvoke(target, cls);
                invokes.put(target.object, o);
            }
            return (T) o;
        }
    }

    public <T> T getBoxLocalInvoke(Class<?> cls) {
        return asInvoke(box.getBoxInvocationTarget(getInvokeName(cls)), cls);
    }

    public void registerLocalInvoke(Object object, Class<?> cls) {
        box.registerInvocationTarget(getInvokeName(cls), new InvokeTarget(object, cls));
    }

    public void unregisterLocalInvoke(Class<?> cls) {
        box.unregisterInvocationTarget(getInvokeName(cls));
    }

    public  String getInvokeName(Class<?> cls) {
        BoxName boxName = cls.getAnnotation(BoxName.class);
        if (boxName != null) {
            return boxName.value();
        }
        return cls.getName();
    }

    public static InvokeTarget asTarget(Object object, Class<?> cls) {
        return new InvokeTarget(object, cls);
    }
}
