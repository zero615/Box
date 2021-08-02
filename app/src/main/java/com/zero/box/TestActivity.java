package com.zero.box;

import android.app.Activity;
import android.content.Context;

import com.zero.box.sample.plugin.BoxContext;

public class TestActivity extends Activity {
    @Override
    protected void attachBaseContext(Context newBase) {
        //重写该方法后，就可以正常使用 R 文件
        super.attachBaseContext(BoxContext.newBoxContext(this,newBase));
    }
}
