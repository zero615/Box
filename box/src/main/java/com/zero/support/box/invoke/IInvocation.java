package com.zero.support.box.invoke;

import android.app.Activity;
import android.content.Context;
import android.util.Pair;

public interface IInvocation {
    @BoxName("addInvocationTarget")
    void addInvocationTarget(String name, Pair<Object, Class> target) ;

    @BoxName("removeInvocationTarget")
    void removeInvocationTarget(String name);

    @BoxName("getInvocationTarget")
    Pair<Object,Class> getInvocationTarget(String name);


    @BoxName("createBoxContext")
    Context createBoxContext(Context context, int theme);

    @BoxName("createBoxContextForActivity")
    Context createBoxContextForActivity(Context context, Activity activity);
}
