package com.zero.support.box.plugin.component;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.IBinder;

import com.zero.support.box.plugin.invoke.IInvocationService;

public class InvocationService implements IInvocationService {
    private Service service;

    @Override
    public Context attach(Service service, Context newBase) {
        this.service = service;
        return newBase;
    }

    public final Service requireService() {
        return service;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return 0;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

    }

    @Override
    public void onLowMemory() {

    }

    @Override
    public void onRebind(Intent intent) {

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

    }

    @Override
    public void onTrimMemory(int level) {

    }

    @Override
    public void onDestroy() {

    }

    public void stopSelf() {
        service.stopSelf();
    }

    public void stopSelf(int startId) {
        service.stopSelf(startId);
    }

    public final void startForeground(int id, Notification notification) {
        service.startForeground(id, notification);
    }


    public final void startForeground(int id, Notification notification, int foregroundServiceType) {
        if (Build.VERSION.SDK_INT >= 29) {
            service.startForeground(id, notification, foregroundServiceType);
        }
    }
}
