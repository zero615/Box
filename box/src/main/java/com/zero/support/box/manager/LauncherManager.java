package com.zero.support.box.manager;

import android.app.Application;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LauncherManager {
    private final Map<String, Launcher> launchers = new HashMap<>();
    private final Application app;
    private static LauncherManager instance;
    private final LauncherCallback callback;
    private final File root;

    public static LauncherManager getDefault() {
        return instance;
    }

    public LauncherManager(Application app, File root, LauncherCallback callback) {
        this.app = app;
        this.callback = callback;
        this.root = root;
    }

    public static void initialize(Application app, File root, LauncherCallback callback) {
        instance = new LauncherManager(app, root, callback);
    }

    public Launcher getLauncher(File root, String name) {
        synchronized (launchers) {
            Launcher launcher = launchers.get(name);
            if (launcher == null) {
                launcher = new Launcher(root, name);
                launchers.put(name, launcher);
                callback.onBindLauncher(app, launcher);
            }
            return launcher;
        }
    }

    public Launcher getLauncher(String name) {
        return getLauncher(root, name);
    }
}

