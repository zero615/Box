package com.zero.support.box.manager;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.zero.support.box.util.FileUtils;
import com.zero.support.box.util.Preferences;

import java.io.File;

/**
 * 基于token的目录管理器，只维护两个目录
 */
public class Launcher {
    private static final String KEY_TOKEN = "token";
    private static final String KEY_NEXT_TOKEN = "token";
    private static final String KEY_INSTALL_TOKEN = "install_token";
    private static final String KEY_CURRENT = "current";
    private static final String KEY_PREVIOUS = "previous";
    private static final String KEY_NEXT = "next";
    private final Preferences preferences;
    private final String name;
    private final File root;


    public Launcher(File root, String name) {
        this(new File(root, name + ".json"), root, name);
    }

    public String getName() {
        return name;
    }

    public Launcher(File launcher, File root, String name) {
        this.name = name;
        this.root = root;
        preferences = new Preferences(launcher, false);
    }

    public File getAvailableNext() {
        if (getNextPath() != null) {
            return null;
        }
        String currentPath = getCurrentPath();
        if (currentPath == null) {
            return new File(root, name + "-1");
        }
        File current = new File(currentPath);
        if (TextUtils.equals(name + "-1", current.getName())) {
            return new File(root, name + "-2");
        } else {
            return new File(root, name + "-1");
        }
    }


    public String getNextPath() {
        return preferences.getString(KEY_NEXT, null);
    }

    @SuppressLint("ApplySharedPref")
    public void installNext(String next, long token) {
        preferences.edit().putString(KEY_NEXT, next).putLong(KEY_NEXT_TOKEN, token).commit();
    }


    public String getCurrentPath() {
        return preferences.getString(KEY_CURRENT, null);
    }


    public String getPreviousPath() {
        return preferences.getString(KEY_PREVIOUS, null);
    }

    public boolean isNeedInstall() {
        return getCurrentPath() == null;
    }

    public boolean enableNext() {
        long token = preferences.getLong(KEY_NEXT_TOKEN, 0L);
        if (token != 0) {
            return switchToNext(getNextPath(), token);
        }
        return false;
    }

    @SuppressLint("ApplySharedPref")
    public boolean switchToNext(String next, long token) {
        SharedPreferences.Editor editor = preferences.edit();
        if (getInstallToken() == 0) {
            editor.putLong(KEY_INSTALL_TOKEN, token);
        }
        editor.putLong(KEY_TOKEN, token);
        boolean result = editor.putString(KEY_PREVIOUS, getCurrentPath())
                .putString(KEY_CURRENT, next)
                .putString(KEY_NEXT, null)
                .putLong(KEY_NEXT_TOKEN, 0).commit();
        if (result) {
            removePrevious();
        }
        return true;
    }

    public void removePrevious() {
        String previous = getPreviousPath();
        if (previous != null) {
            File scratch = new File(previous);
            if (FileUtils.deleteQuietly(scratch)) {
                if (scratch.exists()) {
                    Log.e("launcher", "removePrevious: fail");
                }
            }
        }
        preferences.putString(KEY_PREVIOUS, null);
    }

    public boolean isInstall() {
        return getCurrentPath() == null;
    }

    public boolean isFirstInstall() {
        return getInstallToken() == getToken();
    }

    public long getInstallToken() {

        return preferences.getLong("install_token", 0);
    }

    public long getToken() {
        return preferences.getLong("token", 0);
    }

}
