package com.zero.support.box.manager;

import android.app.Application;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LauncherManager {
    private final Map<String, Launcher> launchers = new HashMap<>();
    private Application app;
    private static LauncherManager instance;
    private LauncherCallback callback;

    public LauncherManager(Application app, LauncherCallback callback) {
        this.app = app;
        this.callback = callback;
    }

    public static void initialize(Application app, LauncherCallback callback) {
        instance = new LauncherManager(app, callback);
    }

    public Launcher getLauncher(File root, String name) {
        synchronized (launchers) {
            Launcher launcher = launchers.get(name);
            if (launcher == null) {
                launcher = new Launcher(root,name);
                launchers.put(name, launcher);
                callback.onBindLauncher(app, launcher);
            }
            return launcher;
        }
    }
}

