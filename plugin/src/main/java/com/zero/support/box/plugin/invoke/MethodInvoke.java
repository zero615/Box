package com.zero.support.box.plugin.invoke;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MethodInvoke {
//    public String boxName;
//    public Method method;
//    public Class<?>[] params;
//    public Class<?> returnCls;


    public static String getBoxName(Object[] args) {
        return (String) args[0];
    }

    public static Method getMethod(Object[] args) {
        return (Method) args[1];
    }


    public static Class<?>[] getParameterTypes(Object[] args) {
        return (Class<?>[]) args[2];
    }

    public static Class<?> getReturnType(Object[] args) {
        return (Class<?>) args[3];
    }

    public static Object[] createMethod(Method method) {
        Object[] objects = new Object[4];
        BoxName boxName = method.getAnnotation(BoxName.class);
        if (boxName != null) {
            objects[0] = boxName.value();
        } else {
            objects[0] = method.getName();
        }

        objects[1] = method;
        objects[2] = method.getParameterTypes();
        objects[3] = method.getReturnType();
        return objects;
    }

    public static Map<String, Object[]> createMethod(Class<?> cls, boolean proxy) {
        Map<String, Object[]> map = new HashMap<>();
        Method[] methodArray = cls.getDeclaredMethods();
        for (Method m : methodArray) {
            Object[] objects = MethodInvoke.createMethod(m);
            if (proxy) {
                map.put(MethodInvoke.getMethod(objects).getName(), objects);
            } else {
                map.put(MethodInvoke.getBoxName(objects), objects);
            }
        }
        return map;
    }

}
