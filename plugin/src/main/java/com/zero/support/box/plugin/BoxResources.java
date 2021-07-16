package com.zero.support.box.plugin;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.TypedValue;

import androidx.annotation.RequiresApi;

public class BoxResources extends Resources {
    private String mPackageName;
    private Resources mResource;
    public BoxResources(String packageName, AssetManager manager, Resources resource) {
        super(manager, resource.getDisplayMetrics(), resource.getConfiguration());
        mPackageName = packageName;
        mResource = resource;
    }
    /**
     * 重定向包名
     */
    @Override
    public int getIdentifier(String name, String defType, String defPackage) {
        int resId = super.getIdentifier(name, defType, mPackageName);
        if (resId > 0) {
            return resId;
        }
        return super.getIdentifier(name, defType, defPackage);
    }
    @Override
    public CharSequence getText(int id) throws NotFoundException {
        try {
            return super.getText(id);
        } catch (Exception e) {
            return mResource.getText(id);
        }
    }
    @Override
    public CharSequence getText(int id, CharSequence def) {
        try {
            return super.getText(id, def);
        } catch (Exception e) {
            return mResource.getText(id, def);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public int getColor(int id, Theme theme) throws NotFoundException {
        try {
            return super.getColor(id, theme);
        } catch (Exception e) {
            return mResource.getColor(id, theme);
        }
    }
    @Override
    public int getColor(int id) throws NotFoundException {
        try {
            return super.getColor(id);
        } catch (Exception e) {
            return mResource.getColor(id);
        }
    }
    @Override
    public CharSequence getQuantityText(int id, int quantity) throws NotFoundException {
        try {
            return super.getQuantityText(id, quantity);
        } catch (Exception e) {
            return mResource.getQuantityText(id, quantity);
        }
    }
    @Override
    public CharSequence[] getTextArray(int id) throws NotFoundException {
        try {
            return super.getTextArray(id);
        } catch (Exception e) {
            return mResource.getTextArray(id);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public ColorStateList getColorStateList(int id, Theme theme) throws NotFoundException {
        try {
            return super.getColorStateList(id, theme);
        } catch (Exception e) {
            return mResource.getColorStateList(id, theme);
        }
    }
    @Override
    public ColorStateList getColorStateList(int id) throws NotFoundException {
        try {
            return super.getColorStateList(id);
        } catch (Exception e) {
            return mResource.getColorStateList(id);
        }
    }
    @Override
    public String getString(int id) throws NotFoundException {
        try {
            return super.getString(id);
        } catch (Exception e) {
            return mResource.getString(id);
        }
    }
    @Override
    public String getString(int id, Object... formatArgs) throws NotFoundException {
        try {
            return super.getString(id, formatArgs);
        } catch (Exception e) {
            return mResource.getString(id, formatArgs);
        }
    }
    @Override
    public float getDimension(int id) throws NotFoundException {
        try {
            return super.getDimension(id);
        } catch (Exception e) {
            return mResource.getDimension(id);
        }
    }
    @Override
    public int getDimensionPixelOffset(int id) throws NotFoundException {
        try {
            return super.getDimensionPixelOffset(id);
        } catch (Exception e) {
            return mResource.getDimensionPixelOffset(id);
        }
    }
    @Override
    public int getDimensionPixelSize(int id) throws NotFoundException {
        try {
            return super.getDimensionPixelSize(id);
        } catch (Exception e) {
            return mResource.getDimensionPixelSize(id);
        }
    }
    @Override
    public void getValueForDensity(int id, int density, TypedValue outValue, boolean resolveRefs) throws NotFoundException {
        try {
            super.getValueForDensity(id, density, outValue, resolveRefs);
        } catch (Exception e) {
            mResource.getValueForDensity(id, density, outValue, resolveRefs);
        }
    }
    @Override
    public Drawable getDrawable(int id) throws NotFoundException {
        try {
            return super.getDrawable(id);
        } catch (Exception e) {
            return mResource.getDrawable(id);
        }
    }
    @Override
    public Drawable getDrawableForDensity(int id, int density) throws NotFoundException {
        try {
            return super.getDrawableForDensity(id, density);
        } catch (Exception e) {
            return mResource.getDrawableForDensity(id, density);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public Drawable getDrawable(int id, Theme theme) throws NotFoundException {
        try {
            return super.getDrawable(id, theme);
        } catch (Exception e) {
            return mResource.getDrawable(id, theme);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public Drawable getDrawableForDensity(int id, int density, Theme theme) {
        try {
            return super.getDrawableForDensity(id, density, theme);
        } catch (Exception e) {
            return mResource.getDrawableForDensity(id, density, theme);
        }
    }
}