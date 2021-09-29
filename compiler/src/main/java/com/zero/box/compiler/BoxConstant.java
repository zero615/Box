package com.zero.box.compiler;

public class BoxConstant {
    public static final String HOLDER = "com.zero.support.box";

    public static final String BOX_PLUGIN = "package com.zero.support.box;\n" +
            "\n" +
            "import android.content.Context;\n" +
            "\n" +
            "public interface IBoxPlugin {\n" +
            "    void onPluginLoaded(Context context);\n" +
            "}\n";
    public static final String BOX_RESOURCE = "package com.zero.support.box;\n" +
            "import android.content.res.AssetManager;\n" +
            "import android.content.res.ColorStateList;\n" +
            "import android.content.res.Resources;\n" +
            "import android.graphics.drawable.Drawable;\n" +
            "import android.os.Build;\n" +
            "import android.util.TypedValue;\n" +
            "\n" +
            "import androidx.annotation.RequiresApi;\n" +
            "\n" +
            "public class BoxResources extends Resources {\n" +
            "    private String mPackageName;\n" +
            "    private Resources mResource;\n" +
            "    public BoxResources(String packageName, AssetManager manager, Resources resource) {\n" +
            "        super(manager, resource.getDisplayMetrics(), resource.getConfiguration());\n" +
            "        mPackageName = packageName;\n" +
            "        mResource = resource;\n" +
            "    }\n" +
            "    /**\n" +
            "     * 重定向包名\n" +
            "     */\n" +
            "    @Override\n" +
            "    public int getIdentifier(String name, String defType, String defPackage) {\n" +
            "        int resId = super.getIdentifier(name, defType, mPackageName);\n" +
            "        if (resId > 0) {\n" +
            "            return resId;\n" +
            "        }\n" +
            "        return super.getIdentifier(name, defType, defPackage);\n" +
            "    }\n" +
            "    @Override\n" +
            "    public CharSequence getText(int id) throws NotFoundException {\n" +
            "        try {\n" +
            "            return super.getText(id);\n" +
            "        } catch (Exception e) {\n" +
            "            return mResource.getText(id);\n" +
            "        }\n" +
            "    }\n" +
            "    @Override\n" +
            "    public CharSequence getText(int id, CharSequence def) {\n" +
            "        try {\n" +
            "            return super.getText(id, def);\n" +
            "        } catch (Exception e) {\n" +
            "            return mResource.getText(id, def);\n" +
            "        }\n" +
            "    }\n" +
            "    @RequiresApi(api = Build.VERSION_CODES.M)\n" +
            "    @Override\n" +
            "    public int getColor(int id, Theme theme) throws NotFoundException {\n" +
            "        try {\n" +
            "            return super.getColor(id, theme);\n" +
            "        } catch (Exception e) {\n" +
            "            return mResource.getColor(id, theme);\n" +
            "        }\n" +
            "    }\n" +
            "    @Override\n" +
            "    public int getColor(int id) throws NotFoundException {\n" +
            "        try {\n" +
            "            return super.getColor(id);\n" +
            "        } catch (Exception e) {\n" +
            "            return mResource.getColor(id);\n" +
            "        }\n" +
            "    }\n" +
            "    @Override\n" +
            "    public CharSequence getQuantityText(int id, int quantity) throws NotFoundException {\n" +
            "        try {\n" +
            "            return super.getQuantityText(id, quantity);\n" +
            "        } catch (Exception e) {\n" +
            "            return mResource.getQuantityText(id, quantity);\n" +
            "        }\n" +
            "    }\n" +
            "    @Override\n" +
            "    public CharSequence[] getTextArray(int id) throws NotFoundException {\n" +
            "        try {\n" +
            "            return super.getTextArray(id);\n" +
            "        } catch (Exception e) {\n" +
            "            return mResource.getTextArray(id);\n" +
            "        }\n" +
            "    }\n" +
            "    @RequiresApi(api = Build.VERSION_CODES.M)\n" +
            "    @Override\n" +
            "    public ColorStateList getColorStateList(int id, Theme theme) throws NotFoundException {\n" +
            "        try {\n" +
            "            return super.getColorStateList(id, theme);\n" +
            "        } catch (Exception e) {\n" +
            "            return mResource.getColorStateList(id, theme);\n" +
            "        }\n" +
            "    }\n" +
            "    @Override\n" +
            "    public ColorStateList getColorStateList(int id) throws NotFoundException {\n" +
            "        try {\n" +
            "            return super.getColorStateList(id);\n" +
            "        } catch (Exception e) {\n" +
            "            return mResource.getColorStateList(id);\n" +
            "        }\n" +
            "    }\n" +
            "    @Override\n" +
            "    public String getString(int id) throws NotFoundException {\n" +
            "        try {\n" +
            "            return super.getString(id);\n" +
            "        } catch (Exception e) {\n" +
            "            return mResource.getString(id);\n" +
            "        }\n" +
            "    }\n" +
            "    @Override\n" +
            "    public String getString(int id, Object... formatArgs) throws NotFoundException {\n" +
            "        try {\n" +
            "            return super.getString(id, formatArgs);\n" +
            "        } catch (Exception e) {\n" +
            "            return mResource.getString(id, formatArgs);\n" +
            "        }\n" +
            "    }\n" +
            "    @Override\n" +
            "    public float getDimension(int id) throws NotFoundException {\n" +
            "        try {\n" +
            "            return super.getDimension(id);\n" +
            "        } catch (Exception e) {\n" +
            "            return mResource.getDimension(id);\n" +
            "        }\n" +
            "    }\n" +
            "    @Override\n" +
            "    public int getDimensionPixelOffset(int id) throws NotFoundException {\n" +
            "        try {\n" +
            "            return super.getDimensionPixelOffset(id);\n" +
            "        } catch (Exception e) {\n" +
            "            return mResource.getDimensionPixelOffset(id);\n" +
            "        }\n" +
            "    }\n" +
            "    @Override\n" +
            "    public int getDimensionPixelSize(int id) throws NotFoundException {\n" +
            "        try {\n" +
            "            return super.getDimensionPixelSize(id);\n" +
            "        } catch (Exception e) {\n" +
            "            return mResource.getDimensionPixelSize(id);\n" +
            "        }\n" +
            "    }\n" +
            "    @Override\n" +
            "    public void getValueForDensity(int id, int density, TypedValue outValue, boolean resolveRefs) throws NotFoundException {\n" +
            "        try {\n" +
            "            super.getValueForDensity(id, density, outValue, resolveRefs);\n" +
            "        } catch (Exception e) {\n" +
            "            mResource.getValueForDensity(id, density, outValue, resolveRefs);\n" +
            "        }\n" +
            "    }\n" +
            "    @Override\n" +
            "    public Drawable getDrawable(int id) throws NotFoundException {\n" +
            "        try {\n" +
            "            return super.getDrawable(id);\n" +
            "        } catch (Exception e) {\n" +
            "            return mResource.getDrawable(id);\n" +
            "        }\n" +
            "    }\n" +
            "    @Override\n" +
            "    public Drawable getDrawableForDensity(int id, int density) throws NotFoundException {\n" +
            "        try {\n" +
            "            return super.getDrawableForDensity(id, density);\n" +
            "        } catch (Exception e) {\n" +
            "            return mResource.getDrawableForDensity(id, density);\n" +
            "        }\n" +
            "    }\n" +
            "    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)\n" +
            "    @Override\n" +
            "    public Drawable getDrawable(int id, Theme theme) throws NotFoundException {\n" +
            "        try {\n" +
            "            return super.getDrawable(id, theme);\n" +
            "        } catch (Exception e) {\n" +
            "            return mResource.getDrawable(id, theme);\n" +
            "        }\n" +
            "    }\n" +
            "    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)\n" +
            "    @Override\n" +
            "    public Drawable getDrawableForDensity(int id, int density, Theme theme) {\n" +
            "        try {\n" +
            "            return super.getDrawableForDensity(id, density, theme);\n" +
            "        } catch (Exception e) {\n" +
            "            return mResource.getDrawableForDensity(id, density, theme);\n" +
            "        }\n" +
            "    }\n" +
            "}";

    public static final String BOX_CONTEXT = "package com.zero.support.box;\n" +
            "\n" +
            "import android.app.Activity;\n" +
            "import android.content.Context;\n" +
            "import android.content.ContextWrapper;\n" +
            "import android.content.pm.ActivityInfo;\n" +
            "import android.content.pm.PackageInfo;\n" +
            "import android.content.res.AssetManager;\n" +
            "import android.content.res.Configuration;\n" +
            "import android.content.res.Resources;\n" +
            "import android.text.TextUtils;\n" +
            "import android.view.Display;\n" +
            "\n" +
            "public class BoxContext extends ContextWrapper {\n" +
            "\n" +
            "    private Resources.Theme mTheme;\n" +
            "    private Resources mResource;\n" +
            "    private AssetManager mAssetManager;\n" +
            "    private int mThemeId;\n" +
            "\n" +
            "    public BoxContext(Context base, Resources resources, int theme) {\n" +
            "        super(base);\n" +
            "        mThemeId = theme;\n" +
            "        mResource = resources;\n" +
            "        mAssetManager = resources.getAssets();\n" +
            "    }\n" +
            "\n" +
            "\n" +
            "    public static PackageInfo getPackageInfo() {\n" +
            "        return BoxRuntime.getPackageInfo();\n" +
            "    }\n" +
            "\n" +
            "    public static AssetManager getAssetManager() {\n" +
            "        return BoxRuntime.getAssetManager();\n" +
            "    }\n" +
            "\n" +
            "    public static Context newBoxContext(Activity activity, Context base) {\n" +
            "        return newBoxContext(base, getTheme(activity));\n" +
            "    }\n" +
            "\n" +
            "    public static Context newBoxContext(Context context, int theme) {\n" +
            "        if (theme == 0) {\n" +
            "            theme = getDefaultTheme();\n" +
            "        }\n" +
            "        return new BoxContext(context, newResources(context), theme);\n" +
            "    }\n" +
            "\n" +
            "    private static Resources newResources(Context context) {\n" +
            "        Resources resource = context.getResources();\n" +
            "        return new BoxResources(BoxRuntime.getPackageName(), BoxRuntime.getAssetManager(), resource);\n" +
            "    }\n" +
            "\n" +
            "    private static int getTheme(Activity activity) {\n" +
            "        PackageInfo info = BoxRuntime.getPackageInfo();\n" +
            "        String name = activity.getClass().getName();\n" +
            "        ActivityInfo[] activities = info.activities;\n" +
            "        int theme = info.applicationInfo.theme;\n" +
            "        if (activities != null) {\n" +
            "            for (ActivityInfo activityInfo : activities) {\n" +
            "                if (TextUtils.equals(activityInfo.name, name)) {\n" +
            "                    if (activityInfo.theme != 0) {\n" +
            "                        theme = activityInfo.theme;\n" +
            "                    }\n" +
            "                    break;\n" +
            "                }\n" +
            "            }\n" +
            "        }\n" +
            "        return theme;\n" +
            "    }\n" +
            "\n" +
            "    private static int getDefaultTheme() {\n" +
            "        return BoxRuntime.getPackageInfo().applicationInfo.theme;\n" +
            "    }\n" +
            "\n" +
            "\n" +
            "    @Override\n" +
            "    public Resources.Theme getTheme() {\n" +
            "        if (mTheme == null) {\n" +
            "            mTheme = mResource.newTheme();\n" +
            "            mTheme.applyStyle(mThemeId, true);\n" +
            "        }\n" +
            "        return mTheme;\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public Context createConfigurationContext(Configuration overrideConfiguration) {\n" +
            "        Context context = super.createConfigurationContext(overrideConfiguration);\n" +
            "        return new BoxContext(context, mResource, mThemeId);\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public Context createDisplayContext(Display display) {\n" +
            "        Context context = super.createDisplayContext(display);\n" +
            "        return new BoxContext(context, mResource, mThemeId);\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public Resources getResources() {\n" +
            "        return mResource;\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public AssetManager getAssets() {\n" +
            "        return mAssetManager;\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public Object getSystemService(String name) {\n" +
            "        return super.getSystemService(name);\n" +
            "    }\n" +
            "}";

    public static final String BOX_RUNTIME = "package com.zero.support.box;\n" +
            "\n" +
            "import android.content.Context;\n" +
            "import android.content.pm.PackageInfo;\n" +
            "import android.content.res.AssetManager;\n" +
            "\n" +
            "import androidx.annotation.Keep;\n" +
            "\n" +
            "import java.lang.reflect.Method;\n" +
            "import java.util.Map;\n" +
            "\n" +
            "@Keep\n" +
            "@SuppressWarnings(\"all\")\n" +
            "public class BoxRuntime {\n" +
            "    private static PackageInfo packageInfo;\n" +
            "    private static ClassLoader caller;\n" +
            "    private static Context callerContext;\n" +
            "    private static Map<String, Object> extras;\n" +
            "    private static AssetManager assetManager;\n" +
            "    private static Context context;\n" +
            "    private static IBoxPlugin plugin;\n" +
            "\n" +
            "    @Keep\n" +
            "    public static void init(Context callerContext, ClassLoader caller, PackageInfo packageInfo, Map<String, Object> extra) {\n" +
            "        BoxRuntime.caller = caller;\n" +
            "        BoxRuntime.callerContext = callerContext;\n" +
            "        BoxRuntime.packageInfo = packageInfo;\n" +
            "        BoxRuntime.extras = extra;\n" +
            "        if (caller != BoxRuntime.class.getClassLoader()) {\n" +
            "            context = BoxContext.newBoxContext(callerContext, packageInfo.applicationInfo.theme);\n" +
            "            tryInitBoxPlugin();\n" +
            "            ensureInit();\n" +
            "        } else {\n" +
            "            context = callerContext;\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    private static void tryInitBoxPlugin() {\n" +
            "        context = BoxContext.newBoxContext(callerContext, packageInfo.applicationInfo.theme);\n" +
            "        String name = packageInfo.applicationInfo.name;\n" +
            "        try {\n" +
            "            Class<?> cls = Class.forName(name);\n" +
            "            if (IBoxPlugin.class.isAssignableFrom(cls)) {\n" +
            "                plugin = (IBoxPlugin) cls.newInstance();\n" +
            "                plugin.onPluginLoaded(context);\n" +
            "            }\n" +
            "        } catch (Throwable e) {\n" +
            "            e.printStackTrace();\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "\n" +
            "    public static Context getCallerContext() {\n" +
            "        return callerContext;\n" +
            "    }\n" +
            "\n" +
            "    public static ClassLoader getCaller() {\n" +
            "        return caller;\n" +
            "    }\n" +
            "\n" +
            "    public static PackageInfo getPackageInfo() {\n" +
            "        return packageInfo;\n" +
            "    }\n" +
            "\n" +
            "    public static Map<String, Object> getExtras() {\n" +
            "        return extras;\n" +
            "    }\n" +
            "\n" +
            "    public static Object getExtra(String key) {\n" +
            "        return extras.get(key);\n" +
            "    }\n" +
            "\n" +
            "    public static Context getContext() {\n" +
            "        return context;\n" +
            "    }\n" +
            "\n" +
            "    public static AssetManager getAssetManager() {\n" +
            "        return assetManager;\n" +
            "    }\n" +
            "\n" +
            "    public static boolean isPlugin() {\n" +
            "        return caller != BoxRuntime.class.getClassLoader();\n" +
            "    }\n" +
            "\n" +
            "    private static void ensureInit() {\n" +
            "        if (assetManager != null) {\n" +
            "            return;\n" +
            "        }\n" +
            "        try {\n" +
            "            String path = packageInfo.applicationInfo.sourceDir;\n" +
            "            if (path != null) {\n" +
            "                assetManager = AssetManager.class.newInstance();\n" +
            "                Method method = AssetManager.class.getMethod(\"addAssetPath\", String.class);\n" +
            "                method.invoke(assetManager, path);\n" +
            "            }\n" +
            "        } catch (Throwable e) {\n" +
            "            e.printStackTrace();\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    public static String getPackageName() {\n" +
            "        return packageInfo.packageName;\n" +
            "    }\n" +
            "\n" +
            "}\n";
}
