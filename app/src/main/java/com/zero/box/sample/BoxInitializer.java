package com.zero.box.sample;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.util.Log;
import android.util.Pair;

import com.zero.support.box.plugin.BoxRuntime;

import java.util.Map;

public class BoxInitializer {
    public static Object[] init(String name, Context callerContext, ClassLoader caller, PackageInfo packageInfo, Context context,  Object target, Class<?> cls, Map<String, Object[]> methods, Map<String, Object> extra) {
       Object[] objects =  BoxRuntime.init(name, callerContext, caller, packageInfo, context, target,cls,methods, extra);
        BoxRuntime.getCallerInvocation().addInvocationTarget("test",new ITest(){
            @Override
            public String hello(String test) {
                return "reply: "+test;
            }} ,ITest.class);

        ITestCaller caller1 = BoxRuntime.getCallerService("test2",ITestCaller.class);
        Log.e("box", "init: "+caller1.caller("xxx") );
        return objects;
    }
}
