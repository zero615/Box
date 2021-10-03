package com.zero.support.box.plugin.invoke;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import com.zero.support.box.plugin.invoke.BoxName;

public interface IInvocationActivity {


    @BoxName("attach")
    Context attach(Activity activity, Context newBase);

    @BoxName("onCreate")
    void onCreate(Bundle savedInstanceState);


    @BoxName("onStart")
    void onStart();

    @BoxName("onResume")
    void onResume();

    @BoxName("onPause")
    void onPause();

    @BoxName("onStop")
    void onStop();

    @BoxName("onRestart")
    void onRestart();

    @BoxName("onSaveInstanceState")
    void onSaveInstanceState(Bundle outState);

    @BoxName("onRestoreInstanceState")
    void onRestoreInstanceState(Bundle savedInstanceState);

    @BoxName("onBackPressed")
    void onBackPressed();

    @BoxName("onActivityResult")
    void onActivityResult(int requestCode, int resultCode, Intent data);

    @BoxName("onDestroy")
    void onDestroy();

    @BoxName("onNewIntent")
    void onNewIntent(Intent intent);


    @BoxName("onAttachedToWindow")
    void onAttachedToWindow();


    @BoxName("onDetachedFromWindow")
    void onDetachedFromWindow();


    @BoxName("onTrimMemory")
    public void onTrimMemory(int level);


    @BoxName("onLowMemory")
    public void onLowMemory();


    @BoxName("onConfigurationChanged")
    public void onConfigurationChanged(Configuration newConfig);
}
