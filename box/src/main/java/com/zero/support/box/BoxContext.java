package com.zero.support.box;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.Display;

public class BoxContext extends ContextWrapper {

    private Resources.Theme mTheme;
    private Resources mResource;
    private AssetManager mAssetManager;
    private int mThemeId;
    private PackageInfo mPackageInfo;
    private ClassLoader mClassLoader;

    public BoxContext(ClassLoader loader, PackageInfo packageInfo, Context base, Resources resources, int theme) {
        super(base);
        mPackageInfo = packageInfo;
        mThemeId = theme;
        mResource = resources;
        mClassLoader = loader;
        mAssetManager = resources.getAssets();
    }

    @Override
    public ClassLoader getClassLoader() {
        return mClassLoader;
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
        return new BoxContext(mClassLoader,mPackageInfo, context, mResource, mThemeId);
    }

    @Override
    public Context createDisplayContext(Display display) {
        Context context = super.createDisplayContext(display);
        return new BoxContext(mClassLoader,mPackageInfo, context, mResource, mThemeId);
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