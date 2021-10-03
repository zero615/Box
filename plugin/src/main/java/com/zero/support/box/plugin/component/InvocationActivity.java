package com.zero.support.box.plugin.component;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import com.zero.support.box.plugin.invoke.IInvocationActivity;

public class InvocationActivity implements IInvocationActivity {
    private Activity activity;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

    }

    @Override
    public void onLowMemory() {

    }

    @Override
    public void onTrimMemory(int level) {

    }

    @Override
    public Context attach(Activity activity, Context newBase) {
        this.activity = activity;
        return newBase;
    }

    public final Activity requireActivity() {
        return activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onNewIntent(Intent intent) {

    }

    @Override
    public void onAttachedToWindow() {

    }

    @Override
    public void onDetachedFromWindow() {

    }

    public void finish() {
        activity.finish();
    }

    public void startActivity(Intent intent) {
        activity.startActivity(intent);
    }

    public void startActivity(Intent intent, Bundle options) {
        activity.startActivity(intent, options);
    }

    public Intent getIntent() {
        return activity.getIntent();
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode, null);
    }

    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        activity.startActivityForResult(intent, requestCode, options);
    }
}
