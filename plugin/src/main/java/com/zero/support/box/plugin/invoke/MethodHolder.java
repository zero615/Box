package com.zero.support.box.plugin.invoke;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

class MethodHolder {
    public String name;
    public Method method;
    public Type[] types;
    public Class<?>[] params;

    public Type returnType;
    public Class<?> returnCls;

    public MethodHolder(Method method) {
        this.method = method;
        this.types = method.getGenericParameterTypes();
        this.returnType = method.getGenericReturnType();
        BoxName nameAno = method.getAnnotation(BoxName.class);
        if (nameAno != null) {
            this.name = nameAno.value();
        } else {
            this.name = method.getName();
        }
        this.params = method.getParameterTypes();
        this.returnCls = method.getReturnType();
    }
}