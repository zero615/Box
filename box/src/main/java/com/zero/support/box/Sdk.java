package com.zero.support.box;

import android.app.ActivityThread;
import android.app.Application;
import android.os.Build;
import android.os.Process;

import com.zero.support.box.manager.Launcher;
import com.zero.support.box.manager.LauncherCallback;
import com.zero.support.box.manager.LauncherManager;

import java.io.File;
import java.lang.reflect.Method;

import static android.os.Build.VERSION.SDK_INT;

public class Sdk {
    private static Application app;
    private static String processName;

    public static void initialize(Application app, File root, LauncherCallback callback) {
        Sdk.app = app;
        Sdk.processName = getCurrentProcessName();
        LauncherManager.initialize(app, root, callback);
    }

    private static String getCurrentProcessName() {
        if (SDK_INT >= Build.VERSION_CODES.P) {
            return Application.getProcessName();
        } else {
            return ActivityThread.currentProcessName();
        }
    }

    public static String currentProcessName() {
        return processName;
    }

    public static Application getApplication() {
        return app;
    }

    @SuppressWarnings("all")
    public static boolean is64bit() {
        if (SDK_INT < 21) {
            return false;
        } else if (SDK_INT < 23) {
            try {
                Class<?> cls = Class.forName("dalvik.system.VMRuntime");
                Method method = cls.getDeclaredMethod("is64Bit");
                method.setAccessible(true);
                return (boolean) method.invoke(null);
            } catch (Throwable e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return Process.is64Bit();
        }
    }

    public static boolean isInstall(String name) {
        return LauncherManager.getDefault().getLauncher(name).getCurrentPath() != null;
    }

    public static ClassLoader load(String name, ClassLoader parent) {
        Launcher launcher = LauncherManager.getDefault().getLauncher(name);
        String path = launcher.getCurrentPath();
        if (path == null) {
            return null;
        }
        return BoxManager.load(app, parent, new File(path, "base.apk").getPath(), new File(path, getAbiName()).getPath());
    }

    public static String getAbiName() {
        return new File(app.getApplicationInfo().nativeLibraryDir).getName();
    }
}
