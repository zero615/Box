package com.zero.box;

import android.app.Activity;
import android.content.Context;

import com.zero.support.app.ButtonActivity;
import com.zero.support.box.plugin.BoxRuntime;


public class TestActivity extends ButtonActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        //重写该方法后，就可以正常使用 R 文件
        super.attachBaseContext(BoxRuntime.getCallerInvocation().createBoxContextForActivity(newBase,this));
    }
}
