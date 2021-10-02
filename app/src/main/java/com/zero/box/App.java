package com.zero.box;

import android.app.Application;
import android.content.Context;

import com.zero.support.box.Sdk;
import com.zero.support.box.manager.AppAssetsLaunchCallback;

import java.io.File;


public class App extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Sdk.initialize(this,new File(base.getCacheDir(),"test"),new AppAssetsLaunchCallback("","apk"));

        Sdk.load("",getClassLoader().getParent(),true);
    }


    public void onPluginLoaded(Context context) {
        //当作为插件加载时， 插件会创建一个临时Application 对象， 并调用该方法
//        BoxRuntime.init("","","","");
        Application app;
    }
}
