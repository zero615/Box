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

@SuppressWarnings("all")
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

    public static File getRoot() {
        return LauncherManager.getDefault().getRoot();
    }

    public static String currentProcessName() {
        return processName;
    }

    public static Application getApplication() {
        return app;
    }


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

    public static boolean install(String name) {
        return LauncherManager.getDefault().install(name);
    }

    public static boolean install(File root, String name) {
        return LauncherManager.getDefault().install(root, name);
    }

    public static ClassLoader load(String name, ClassLoader parent) {
        install(name);
        Launcher launcher = LauncherManager.getDefault().getLauncher(name);
        return LauncherManager.getDefault().getLauncherCallback().onLoadLauncher(app, launcher, parent);
    }

    public static ClassLoader load(File root, String name, ClassLoader parent) {
        install(root, name);
        Launcher launcher = LauncherManager.getDefault().getLauncher(root, name);
        return LauncherManager.getDefault().getLauncherCallback().onLoadLauncher(app, launcher, parent);
    }

    public static void becomToParent(ClassLoader target, ClassLoader loader) {
        BoxManager.becomToParent(target, loader);
    }

    public static String getAbiName(File libRoot) {
        String abis[];
        if (SDK_INT < 21) {
            return Build.CPU_ABI;
        }
        if (is64bit()) {
            for (String abi : Build.SUPPORTED_64_BIT_ABIS) {
                if (new File(libRoot, abi).exists()) {
                    return abi;
                }
            }
            return Build.SUPPORTED_64_BIT_ABIS[0];
        } else {
            for (String abi : Build.SUPPORTED_32_BIT_ABIS) {
                if (new File(libRoot, abi).exists()) {
                    return abi;
                }
            }
            return Build.SUPPORTED_32_BIT_ABIS[0];
        }
    }

    public static String getAbiName() {
        String abis[];
        if (SDK_INT < 21) {
            return Build.CPU_ABI;
        }
        if (is64bit()) {
            return Build.SUPPORTED_64_BIT_ABIS[0];
        } else {
            return Build.SUPPORTED_32_BIT_ABIS[0];
        }
    }
}
