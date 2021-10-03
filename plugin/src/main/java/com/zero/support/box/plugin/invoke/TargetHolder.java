package com.zero.support.box.plugin.invoke;

public class TargetHolder {
    public Object object;
    public Class<?> cls;

    public TargetHolder(Object object, Class<?> cls) {
        this.object = object;
        this.cls = cls;
    }
}