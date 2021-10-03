package com.zero.support.box.plugin.invoke;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;

public interface IInvocationService {

    @BoxName("attach")
    Context attach(Service service, Context newBase);

    @BoxName("onCreate")
    void onCreate();

    @BoxName("onBind")
    IBinder onBind(Intent intent);

    @BoxName("onUnbind")
    boolean onUnbind(Intent intent);

    @BoxName("onStartCommand")
    int onStartCommand(Intent intent, int flags, int startId);

    @BoxName("onRebind")
    void onRebind(Intent intent);

    @BoxName("onTaskRemoved")
    void onTaskRemoved(Intent rootIntent);

    @BoxName("onDestroy")
    void onDestroy();


    @BoxName("onTrimMemory")
    public void onTrimMemory(int level);


    @BoxName("onLowMemory")
    public void onLowMemory();


    @BoxName("onConfigurationChanged")
    public void onConfigurationChanged(Configuration newConfig);

}
