package com.zero.box.sample;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.util.Pair;

import com.zero.support.box.plugin.BoxRuntime;

import java.util.Map;

public class BoxInitializer {
    public static void init(String name, Context callerContext, ClassLoader caller, PackageInfo packageInfo, Context context, Pair<Object, Class> target, Map<String, Object> extra) {
        BoxRuntime.init(name, callerContext, caller, packageInfo, context, target, extra);
//        BoxRuntime.getCallerInvocation().registerInvocationTarget("", );

    }
}
