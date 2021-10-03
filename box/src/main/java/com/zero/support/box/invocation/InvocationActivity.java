package com.zero.support.box.invocation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zero.support.box.plugin.invoke.IInvocationActivity;


public abstract class InvocationActivity extends Activity {
    private IInvocationActivity activity;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        activity = onCreateInvocationActivity();
        activity.attach(this, newBase);
    }

    public abstract IInvocationActivity onCreateInvocationActivity();

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity.onCreate(savedInstanceState);
    }


    @Override
    protected void onStart() {
        super.onStart();
        activity.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        activity.onRestart();
    }


    @Override
    public void onBackPressed() {
        if (!activity.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        activity.onSaveInstanceState(outState);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        activity.onDetachedFromWindow();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        activity.onAttachedToWindow();
    }

    @Override
    protected void onStop() {
        super.onStop();
        activity.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        activity.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        activity.onNewIntent(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        activity.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        activity.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        activity.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        activity.onTrimMemory(level);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        activity.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        activity.onDestroy();
    }
}
