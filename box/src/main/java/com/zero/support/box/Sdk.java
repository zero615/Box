package com.zero.support.box;

import android.app.Application;
import android.os.Process;

import com.zero.support.box.manager.Launcher;
import com.zero.support.box.manager.SdkManager;

import java.io.File;
import java.lang.reflect.Method;

import static android.os.Build.VERSION.SDK_INT;

public class Sdk {
    private static Application app;

    public static void initialize(SdkConfig config) {
        Sdk.app = config.getApplication();
        SdkManager.initialize(config);
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
        return SdkManager.getInstance().getLauncher(name).getCurrentPath() != null;
    }

    public static void load(String name, ClassLoader parent) {
        Launcher launcher = SdkManager.getInstance().getLauncher(name);
        String path = launcher.getCurrentPath();
        BoxManager.load(app, parent, new File(path, "base.apk").getPath(), new File(path, getAbiName()).getPath());
    }

    public static String getAbiName() {
        return new File(app.getApplicationInfo().nativeLibraryDir).getName();
    }
}
