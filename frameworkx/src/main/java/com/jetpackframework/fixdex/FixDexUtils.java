package com.jetpackframework.fixdex;

import android.content.Context;

import com.jetpackframework.ContextUtil;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * 热修复工具:动态装载dex文件,不支持修改Application 以及四大组件
 */
public class FixDexUtils {
    private static class Instance{
        private static final FixDexUtils instance = new FixDexUtils();
    }
    public static synchronized FixDexUtils getInstance(){
        return Instance.instance;
    }
    private Set<File> fileSet;
    private FixDexUtils(){
        fileSet = new HashSet<>();
        fileSet.clear();
    }

    /**
     * 加载dex文件
     * @param dexDir  dex文件路径
     */
    public void loadDex(String dexDir){
        File dexFile = ContextUtil.get().getDir(dexDir, Context.MODE_PRIVATE);
        if (!dexFile.exists()){
            dexFile.mkdirs();
        }
        File[] listFiles = dexFile.listFiles();

        for (int i = 0; i < listFiles.length; i++) {
            //文件名以.dex结尾,且不是主包.dex文件
            if (listFiles[i].getName().endsWith(".dex") && !"classes.dex".equalsIgnoreCase(listFiles[i].getName())) {

                fileSet.add(listFiles[i]);
            }
        }
        //创建自定义的类加载器
        createDexClassLoader(ContextUtil.get() ,dexFile);
    }

    private void createDexClassLoader(Context context, File dexFile) {
        String optimizedDir = dexFile.getAbsolutePath()+File.separator+"opt_dex";
        File fileOpt = new File(optimizedDir);
        if (!fileOpt.exists()) {
            fileOpt.mkdirs();
        }

        for (File dex : fileSet) {
            //创建自己的类加载器,临时的
            DexClassLoader classLoader = new DexClassLoader(dex.getAbsolutePath(), optimizedDir, null, context.getClassLoader());
            //有一个修复文件,就插装一次
            hotFix(classLoader,context);
        }
    }

    public void hotFix(DexClassLoader classLoader, Context context) {
        try {
            //获取系统的PathClassLoader类加载器
            PathClassLoader pathClassLoader = (PathClassLoader)context.getClassLoader();
            //获取自己的dexElements数组
            Object dexElement1 = getDexElementByClassLoader(classLoader);
            //获取系统的dexElements数组
            Object systemElements = getDexElementByClassLoader(pathClassLoader);
            //合并数组,并排序,生成一个新的数组
            Object dexElements=combineArray(dexElement1,systemElements);
            //通过反射,将合并后新的dexElements赋值给系统的pathList
            injectDexElements(pathClassLoader,dexElements);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static Object getDexElementByClassLoader(ClassLoader classLoader) throws Exception {
        Object pathList = getPathList(classLoader);
        Class<?> pathListClass = pathList.getClass();
        Field dexElementsField = pathListClass.getDeclaredField("dexElements");
        dexElementsField.setAccessible(true);
        Object dexElements = dexElementsField.get(pathList);

        return dexElements;
    }


    private static Object getPathList(ClassLoader classLoader) throws Exception {
        Class<?> classLoaderClass = Class.forName("dalvik.system.BaseDexClassLoader");
        Field pathListField = classLoaderClass.getDeclaredField("pathList");
        pathListField.setAccessible(true);
        Object pathList = pathListField.get(classLoader);
        return pathList;
    }
    /**
     * 合并两个dexElements数组
     *
     * @param arrayLhs
     * @param arrayRhs
     * @return
     */
    private static Object combineArray(Object arrayLhs, Object arrayRhs) {
        Class<?> localClass = arrayLhs.getClass().getComponentType();
        int i = Array.getLength(arrayLhs);
        int j = i + Array.getLength(arrayRhs);
        Object result = Array.newInstance(localClass, j);
        for (int k = 0; k < j; ++k) {
            if (k < i) {
                Array.set(result, k, Array.get(arrayLhs, k));
            } else {
                Array.set(result, k, Array.get(arrayRhs, k - i));
            }
        }
        return result;
    }

    /**
     * 把dexElement注入到已运行classLoader中
     *
     * @param classLoader
     * @param dexElement
     * @throws Exception
     */
    private static void injectDexElements(ClassLoader classLoader, Object dexElement) throws Exception {
        Object pathList = getPathList(classLoader);
        Class<?> pathListClass = pathList.getClass();
        Field dexElementsField = pathListClass.getDeclaredField("dexElements");
        dexElementsField.setAccessible(true);
        dexElementsField.set(pathList, dexElement);
    }
}
