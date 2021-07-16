# binder
no aidl

## 关于集成：
- **在项目的根目录的`build.gradle`添加：**
```
allprojects {
    repositories {
        ...
	maven { url 'https://jitpack.io' }
    }
}
```
- **在应用模块的`build.gradle`添加：**
```
dependencies {
        implementation 'com.github.zero615.Box:box:1.0.0'
}
```
## 使用示例：
```
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //初始化
        Sdk.initialize(new SimpleSdkConfig(this, new File(getFilesDir(), "sdk"), "assets/sdk", ".sdk"));
        //加载插件
        Sdk.load("plugin1", base.getClassLoader().getParent());
    }
```
## 作为 plugin 集成
```
 dependencies {
    ...
    classpath 'com.github.zero615.Box:compiler:1.0.0'
 }
```
 
- **在应用模块的`build.gradle`添加：**
```
    apply plugin: 'AppBox'

    插件自动在BuildConfig同级目录下生成BoxRuntime
```
## 使用示例：

- **插件启动入口**
```
public class App  extends Application implements IBoxPlugin {

    @Override
    public void onPluginLoaded(Context context) {
        //当作为插件加载时， 插件会创建一个临时Application 对象， 并调用该方法
    }
}
```

```
public class TestActivity extends Activity {
    @Override
    protected void attachBaseContext(Context newBase) {
        //重写该方法后，就可以正常使用 R 文件
        super.attachBaseContext(BoxContext.newBoxContext(this,newBase));
    }
}
```

