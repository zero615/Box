package com.zero.box;

import android.app.Application;
import android.content.Context;
import android.util.Pair;

import com.zero.support.box.Box;
import com.zero.support.box.Sdk;
import com.zero.support.box.manager.AppAssetsLaunchCallback;

import java.io.File;


public class App extends Application {
    public static Box box;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Sdk.initialize(this, new File(base.getCacheDir(), "test"), new AppAssetsLaunchCallback("", "apk"));

        box = Sdk.load("", getClassLoader().getParent(), true);
        box.getBoxInvocation().addInvocationTarget("test2", new ITestCaller() {
            @Override
            public String caller(String test) {
                return "caller:" + test;
            }
        }, ITestCaller.class);
        box.prepare();
    }


    public void onPluginLoaded(Context context) {
        //当作为插件加载时， 插件会创建一个临时Application 对象， 并调用该方法
//        BoxRuntime.init("","","","");
        Application app;
    }
}
