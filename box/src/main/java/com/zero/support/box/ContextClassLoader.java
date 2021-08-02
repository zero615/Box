package com.zero.support.box;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ContextClassLoader extends ClassLoader {
    private List<BoxClassLoader> loaders = new CopyOnWriteArrayList<>();
    private static ContextClassLoader INSTANCE;

    public ContextClassLoader(ClassLoader parent) {
        super(parent);
    }

    public static synchronized ContextClassLoader install() {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        ClassLoader loader = ContextClassLoader.class.getClassLoader();
        INSTANCE = new ContextClassLoader(loader.getParent());

        try {
            Field field = ClassLoader.class.getField("parent");
            field.setAccessible(true);
            field.set(loader, INSTANCE);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return INSTANCE;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        for (BoxClassLoader loader : loaders) {
            try {
                return loader.loadClass(name, resolve);
            } catch (Throwable e) {
            }
        }
        return super.loadClass(name, resolve);
    }
}
