package com.zero.support.box.plugin.invoke;

import android.util.Log;

import com.zero.support.box.plugin.BoxInvocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

public class ReflectInvocationHandler implements InvocationHandler {

    Map<String, Object[]> targetMethods;
    Map<String, Object[]> localMethods;


    public ReflectInvocationHandler(Map<String, Object[]> targetMethods, Class<?> local) {
        this.targetMethods = targetMethods;
        this.localMethods = MethodInvoke.createMethod(local, true);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getDeclaringClass() == Object.class) {
            if (method.getName().equals("equals")) {
                return args[0] == proxy;
            }
            return method.invoke(this, args);
        }


        Object[] locals = localMethods.get(method.getName());
        if (locals == null) {
            Log.e("box", "invoke: why");
            return null;
        }
        Object[] targets = this.targetMethods.get(MethodInvoke.getBoxName(locals));
        if (targets == null) {
            Log.e("box", "invoke: why");
            return null;
        }
        Class<?> cls;
        Class<?>[] params = MethodInvoke.getParameterTypes(targets);
        Class<?>[] localParams = MethodInvoke.getParameterTypes(locals);
        for (int i = 0; i < params.length; i++) {
            cls = params[i];
            if (cls.isInterface() && !isCurrent(cls)) {
                args[i] = LocalInvocation.asInvocation(args[i], localParams[i], null, cls);
            }
        }
        Object o = MethodInvoke.getMethod(targets).invoke(LocalInvocation.getTarget(proxy), args);

        Class<?> returnType = MethodInvoke.getReturnType(targets);
        if (!isCurrent(returnType)) {
            o = LocalInvocation.asInvocation(o, returnType, null, MethodInvoke.getReturnType(locals));
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
