package com.zero.box.sample;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.util.Log;
import android.util.Pair;

import com.zero.support.box.plugin.BoxRuntime;

import java.util.Map;

public class BoxInitializer {
    public static void init(String name, Context callerContext, ClassLoader caller, PackageInfo packageInfo, Context context, Pair<Object, Class> target, Map<String, Object> extra) {
        BoxRuntime.init(name, callerContext, caller, packageInfo, context, target, extra);
        BoxRuntime.getCallerInvocation().addInvocationTarget("test",new Pair<>(new ITest(){
            @Override
            public String hello(String test) {
                return "reply: "+test;
            }
        } ,ITest.class));

        ITestCaller caller1 = BoxRuntime.getCallerService("test2",ITestCaller.class);
        Log.e("box", "init: "+caller1.caller("xxx") );
    }
}
