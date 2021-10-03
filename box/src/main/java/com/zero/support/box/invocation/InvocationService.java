package com.zero.support.box.invocation;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.zero.support.box.plugin.invoke.IInvocationService;


public abstract class InvocationService extends Service {
    private IInvocationService service;

    @Override
    protected void attachBaseContext(Context newBase) {
        service = onCreateInvocationService();
        newBase = service.attach(this, newBase);
        super.attachBaseContext(newBase);
    }

    public abstract IInvocationService onCreateInvocationService();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return service.onBind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return service.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        service.onTrimMemory(level);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        service.onLowMemory();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        service.onConfigurationChanged(newConfig);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        service.onTaskRemoved(rootIntent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        service.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return service.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        service.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        service.onDestroy();
    }



}
