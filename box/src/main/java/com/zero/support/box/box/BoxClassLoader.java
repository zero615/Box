package com.zero.support.box.box;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexClassLoader;


public class BoxClassLoader extends DexClassLoader {
    private BaseDexClassLoader host;
    private Method findClass;

    public BoxClassLoader(String dexPath, String librarySearchPath, BaseDexClassLoader parent) {
        super(dexPath, new File(dexPath).getParent(), librarySearchPath, parent);
        host = (BaseDexClassLoader) BoxClassLoader.class.getClassLoader();
        try {
            findClass = host.getClass().getMethod("findClass", String.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> result = null;
        try {
            result = super.loadClass(name, resolve);
        } catch (Throwable e) {

        }
        if (result == null) {
            try {
                result = (Class<?>) findClass.invoke(name, resolve);
            } catch (Throwable e) {
                throw new ClassNotFoundException(name);
            }
        }
        return result;
    }
}
