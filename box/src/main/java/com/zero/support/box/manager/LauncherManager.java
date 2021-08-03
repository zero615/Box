package com.zero.support.box.manager;

import android.app.Application;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LauncherManager {
    private final Map<File, Map<String, Launcher>> launchers = new HashMap<>();
    private final Application app;
    private static LauncherManager instance;
    private final LauncherCallback launcherCallback;
    private final File root;

    public static LauncherManager getDefault() {
        return instance;
    }

    public LauncherManager(Application app, File root, LauncherCallback callback) {
        this.app = app;
        this.launcherCallback = callback;
        this.root = root;
    }

    public LauncherCallback getLauncherCallback() {
        return launcherCallback;
    }

    public File getRoot() {
        return root;
    }

    public static void initialize(Application app, File root, LauncherCallback callback) {
        instance = new LauncherManager(app, root, callback);
    }

    public Launcher getLauncher(File root, String name) {
        synchronized (launchers) {
            Map<String, Launcher> map = launchers.get(root);
            if (map == null) {
                map = new HashMap<>();
                launchers.put(root, map);
            }
            Launcher launcher = map.get(name);
            if (launcher == null) {
                launcher = new Launcher(root, name);
                map.put(name, launcher);
            }
            return launcher;
        }
    }

    public boolean install(String name) {
        return install(root, name);
    }

    public boolean install(File root, String name) {
        return launcherCallback.onInstallLauncher(app, getLauncher(root, name));
    }

    public Launcher getLauncher(String name) {
        return getLauncher(root, name);
    }
}

