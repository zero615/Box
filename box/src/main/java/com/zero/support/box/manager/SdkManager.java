package com.zero.support.box.manager;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Process;
import android.text.TextUtils;

import com.zero.support.box.SdkConfig;
import com.zero.support.box.SdkInstaller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SdkManager {
    private final String processName;
    private PackageInfo packageInfo;
    private final File root;
    private final Map<String, Launcher> launchers = new HashMap<>();
    private final Application app;
    private boolean main;
    private static SdkManager INSTANCE;
    public String installProcessName;
    private SdkConfig config;
    private SdkInstaller sdkCallback;

    public SdkInstaller getSdkCallback() {
        return sdkCallback;
    }

    public File getRoot() {
        return root;
    }

    public void setInstallProcessName(String installProcessName) {
        this.installProcessName = installProcessName;
    }

    public static SdkManager initialize(SdkConfig config) {
        INSTANCE = new SdkManager(config);
        return INSTANCE;
    }

    public PackageInfo getPackageInfo() {
        return packageInfo;
    }

    public static SdkManager getInstance() {
        return INSTANCE;
    }

    public Application getApplication() {
        return app;
    }

    public SdkManager(SdkConfig config) {
        this.app = config.getApplication();
        this.config = config;
        this.processName = getProcessName(app, Process.myPid());
        try {
            this.packageInfo = app.getPackageManager().getPackageInfo(app.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        this.root = config.getRoot();
        this.main = TextUtils.equals(processName, packageInfo.applicationInfo.processName);
    }


    public boolean isMainProcess() {
        return main;
    }

    public String getProcessName() {
        return processName;
    }

    public Launcher getLauncher(String name) {
        return getLauncher(root, name);
    }

    public Launcher getLauncher(File root, String name) {
        synchronized (launchers) {
            Launcher launcher = launchers.get(name);
            if (launcher == null) {
                launcher = new Launcher(this, root, name);
                launchers.put(name, launcher);
                if (sdkCallback.canInstall(app, name, launcher)) {
                    sdkCallback.onInstallSdk(app, name, launcher);
                }
            }
            return launcher;
        }
    }


    public static String getProcessName(Context context, int pid) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list = am.getRunningAppProcesses();
        if (list == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo processInfo : list) {
            if (pid == processInfo.pid) {
                return processInfo.processName;
            }
        }
        return null;
    }

    public long getInstallTime() {
        return new File(config.getHostPackageInfo().applicationInfo.sourceDir).lastModified();
    }
}
