package com.zero.support.box.invoke;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ReflectInvoke implements InvocationHandler {
    ClassHolder targetHolder;
    ClassHolder localHolder;


    public ReflectInvoke(Class<?> targetCls, Class<?> local) {
        targetHolder = new ClassHolder(targetCls, false);
        localHolder = new ClassHolder(local, true);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(this, args);
        }
        MethodHolder localMethod = localHolder.getMethod(method.getName());
        MethodHolder targetHolder = this.targetHolder.getMethod(localMethod.name);
        Class<?> cls;
        for (int i = 0; i < targetHolder.params.length; i++) {
            cls = targetHolder.params[i];
            if (cls.isInterface() && !isCurrent(cls)) {
                args[i] = LocalInvocation.asInvocation(args[i], localMethod.params[i], cls);
            }
        }

        return targetHolder.method.invoke(LocalInvocation.getTarget(proxy), args);
    }


    private boolean isCurrent(Class<?> cls) {
        ClassLoader loader = getClass().getClassLoader();
        if (loader == null) {
            return false;
        }
        do {
            if (loader == cls.getClassLoader()) {
                return true;
            }
        } while ((loader = loader.getParent()) != null);

        return false;
    }


}
