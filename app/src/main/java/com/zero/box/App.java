package com.zero.box;

import android.app.Application;
import android.content.Context;



public class App extends Application{

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }


    public void onPluginLoaded(Context context) {
        //当作为插件加载时， 插件会创建一个临时Application 对象， 并调用该方法
//        BoxRuntime.init("","","","");
        Application app;
    }
}
