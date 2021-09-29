package com.zero.support.box;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.Display;

public class BoxContext extends ContextWrapper {

    private Resources.Theme mTheme;
    private Resources mResource;
    private AssetManager mAssetManager;
    private int mThemeId;

    public BoxContext(Context base, Resources resources, int theme) {
        super(base);
        mThemeId = theme;
        mResource = resources;
        mAssetManager = resources.getAssets();
    }


    public static PackageInfo getPackageInfo() {
        return BoxRuntime.getPackageInfo();
    }

    public static AssetManager getAssetManager() {
        return BoxRuntime.getAssetManager();
    }

    public static Context newBoxContext(Activity activity, Context base) {
        return newBoxContext(base, getTheme(activity));
    }

    public static Context newBoxContext(Context context, int theme) {
        if (theme == 0) {
            theme = getDefaultTheme();
        }
        return new BoxContext(context, newResources(context), theme);
    }

    private static Resources newResources(Context context) {
        Resources resource = context.getResources();
        return new BoxResources(BoxRuntime.getPackageName(), BoxRuntime.getAssetManager(), resource);
    }

    private static int getTheme(Activity activity) {
        PackageInfo info = BoxRuntime.getPackageInfo();
        String name = activity.getClass().getName();
        ActivityInfo[] activities = info.activities;
        int theme = info.applicationInfo.theme;
        if (activities != null) {
            for (ActivityInfo activityInfo : activities) {
                if (TextUtils.equals(activityInfo.name, name)) {
                    if (activityInfo.theme != 0) {
                        theme = activityInfo.theme;
                    }
                    break;
                }
            }
        }
        return theme;
    }

    private static int getDefaultTheme() {
        return BoxRuntime.getPackageInfo().applicationInfo.theme;
    }


    @Override
    public Resources.Theme getTheme() {
        if (mTheme == null) {
            mTheme = mResource.newTheme();
            mTheme.applyStyle(mThemeId, true);
        }
        return mTheme;
    }

    @Override
    public Context createConfigurationContext(Configuration overrideConfiguration) {
        Context context = super.createConfigurationContext(overrideConfiguration);
        return new BoxContext(context, mResource, mThemeId);
    }

    @Override
    public Context createDisplayContext(Display display) {
        Context context = super.createDisplayContext(display);
        return new BoxContext(context, mResource, mThemeId);
    }

    @Override
    public Resources getResources() {
        return mResource;
    }

    @Override
    public AssetManager getAssets() {
        return mAssetManager;
    }

    @Override
    public Object getSystemService(String name) {
        return super.getSystemService(name);
    }
}