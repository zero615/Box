package com.zero.support.box.plugin.invoke;

import android.app.Activity;
import android.content.Context;
import android.util.Pair;

public interface IInvocation {
    @BoxName("registerInvocationTarget")
    public void registerInvocationTarget(String name, Pair<Object, Class> target) ;

    @BoxName("unregisterInvocationTarget")
    public void unregisterInvocationTarget(String name);

    @BoxName("getInvocationTarget")
    Pair<Object,Class> getInvocationTarget(String name);


    @BoxName("createBoxContext")
    Context createBoxContext(Context context, int theme);

    @BoxName("createBoxContextForActivity")
    Context createBoxContextForActivity(Context context, Activity activity);
}
