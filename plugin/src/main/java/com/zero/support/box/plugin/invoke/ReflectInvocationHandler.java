package com.zero.support.box.plugin.invoke;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ReflectInvocationHandler implements InvocationHandler {
    ClassHolder targetHolder;
    ClassHolder localHolder;


    public ReflectInvocationHandler(Class<?> targetCls, Class<?> local) {
        targetHolder = new ClassHolder(targetCls, false);
        localHolder = new ClassHolder(local, true);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getDeclaringClass() == Object.class) {
            if (method.getName().equals("equals")) {
                return args[0] == proxy;
            }
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
        Object o = targetHolder.method.invoke(LocalInvocation.getTarget(proxy), args);

        if (!isCurrent(targetHolder.returnCls)) {
            o = LocalInvocation.asInvocation(o, targetHolder.returnCls, localMethod.returnCls);
        }
        return o;
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