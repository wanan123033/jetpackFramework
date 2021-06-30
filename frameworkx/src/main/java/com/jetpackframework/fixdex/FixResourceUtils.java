package com.jetpackframework.fixdex;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import android.util.ArrayMap;

import com.jetpackframework.ContextUtil;
import com.jetpackframework.Reflector;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;

import static android.os.Build.VERSION.SDK_INT;

/**
 * 热修复工具:动态替换Resouce文件(全量替换,无法差量替换,不支持应用图标替换)
        替换步骤:
 TinkerResourceLoader#checkComplete 检查资源补丁是否存在，存在的话，调用TinkerResourcePatcher#isResourceCanPatch 区分版本拿到 Resources 对象的集合，同时创建新 AssetsManager
 看一下代码
 拿到 addAssetPathMethod 方法留着后面调用
 4.4 以上通过 ResourcesManager 获取 mActiveResources 变量，它是 ArrayMap 类型；在 7.0 上这个变量名称为 mResourceReferences
 4.4 以下通过 ActivityThread 获取 mActiveResources 变量，是一个 HashMap
 保存这些集合后
 TinkerResourceLoader#loadTinkerResource 调用 TinkerResourcePatcher#monkeyPatchExistingResources
 (这个方法的名字跟 InstantRun 的资源补丁方法名是一样的)
 反射调用新建的 AssetManager#addAssetPath 将路径穿进去
 循环遍历持有Resources对象的references集合，依次替换其中的AssetManager为新建的AssetManager
 最后调用Resources.updateConfiguration将Resources对象的配置信息更新到最新状态，完成整个资源替换的过程
 */
public class FixResourceUtils {
    private Reflector assetsFiled;
    private Reflector resourcesImplFiled;
    private Object currentActivityThread;
    private Collection<WeakReference<Resources>> references;
    private AssetManager assetManager;
    private static class Instance{
        private static FixResourceUtils resource;

        static {
            try {
                resource = new FixResourceUtils();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }
    private FixResourceUtils() throws Throwable {
        //获取基本所需的对象及方法
        //1.获取ActivityThread对象
//        Class<?> activityThread = Class.forName("android.app.ActivityThread");
        currentActivityThread = Reflector.on("android.app.ActivityThread").method("currentActivityThread").call();

        //2.获取resource对象
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            Object resoucesManager = Reflector.on("android.app.ResourcesManager").method("getInstance").call();
            try {
                ArrayMap<?, WeakReference<Resources>> mActiveResourcesField = Reflector.with(resoucesManager).field("mActiveResources").get();
                references = mActiveResourcesField.values();
            }catch (Exception e){
                references = Reflector.with(resoucesManager).field("mResourceReferences").get();;
            }
        }else {
            final HashMap<?, WeakReference<Resources>> activeResources7 = Reflector.with(currentActivityThread).field("mActiveResources").get();
            references = activeResources7.values();
        }

        //3.获取 addAssetPathMethod 方法
        final AssetManager assets = ContextUtil.get().getAssets();
        assetManager = Reflector.with(assets).constructor().newInstance();
        Resources resources = ContextUtil.get().getResources();
        if (SDK_INT >= 24) {
            try {
                // N moved the mAssets inside an mResourcesImpl field
                resourcesImplFiled = Reflector.with(resources).field("mResourcesImpl");
            } catch (Throwable ignore) {
                // for safety
                assetsFiled = Reflector.with(resources).field("mAssets");
            }
        } else {
            assetsFiled = Reflector.with(resources).field("mAssets");
        }

    }
    public static synchronized FixResourceUtils getInstance(){
        return Instance.resource;
    }

    /**
     * 动态替换res文件夹
     * @param resourceDir
     *
     */
    public boolean loadResource(String resourceDir){
        File file = new File(resourceDir);
        if (!file.exists() || !file.isDirectory()){
            return false;
        }

        try {
            AssetManager assets = ContextUtil.get().getAssets();
            try {
                Reflector.with(assets).method("addAssetPath",String.class).call(resourceDir);
            } catch (Reflector.ReflectedException e) {
                e.printStackTrace();
            }
            for (WeakReference<Resources> wr : references) {
                final Resources resources = wr.get();
                if (resources == null) {
                    continue;
                }
                // Set the AssetManager of the Resources instance to our brand new one
                try {
                    //pre-N
                    assetsFiled.set(resources, assetManager);
                } catch (Throwable ignore) {
                    // N
                    final Object resourceImpl = resourcesImplFiled.get(resources);
                    // for Huawei HwResourcesImpl
                    try {
                        Reflector.with(resourceImpl).field("mAssets").set(assetManager);
                    } catch (Reflector.ReflectedException e) {
                        e.printStackTrace();
                    }

                }
                resources.updateConfiguration(resources.getConfiguration(), resources.getDisplayMetrics());
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
