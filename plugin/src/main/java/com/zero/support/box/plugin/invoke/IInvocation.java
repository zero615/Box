package com.zero.support.box.plugin.invoke;

import android.app.Activity;
import android.content.Context;
import android.util.Pair;

import java.util.Map;

public interface IInvocation {
    @BoxName("addInvocationTarget")
    void addInvocationTarget(String name, Object target, Class<?> cls);

    @BoxName("removeInvocationTarget")
    void removeInvocationTarget(String name);

    @BoxName("getInvocationTarget")
    Object[] getInvocationTarget(String name);

    @BoxName("getInvocationMethods")
    Map<String, Object[]> getInvocationMethods(Class<?> cls);

    @BoxName("createBoxContext")
    Context createBoxContext(Context context, int theme);

    @BoxName("createBoxContextForActivity")
    Context createBoxContextForActivity(Context context, Activity activity);
}
