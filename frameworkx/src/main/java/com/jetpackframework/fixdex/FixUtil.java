package com.jetpackframework.fixdex;

import android.content.Intent;

import com.jetpackframework.ContextUtil;

import java.util.Objects;

public class FixUtil {
    public static void install(FixConfig config){
        Objects.requireNonNull(config);
        FixDexApplicationDelegate.setFixConfig(config);
    }

    /**
     * 生成差分包
     * @param oldFilePath
     * @param newFilePath
     * @param diffFilePath
     */
    public static void diff(String oldFilePath,String newFilePath,String diffFilePath){
        Intent intent = new Intent(ContextUtil.get(),FixService.class);
        intent.putExtra(FixService.OLDFILE,oldFilePath);
        intent.putExtra(FixService.NEWFILE,newFilePath);
        intent.putExtra(FixService.DIFFFILE,diffFilePath);
        intent.putExtra(FixService.TYPE,FixService.DIFF_TYPE);
        ContextUtil.get().startService(intent);
    }

    /**
     * 合并差分包
     * @param oldFilePath
     * @param diffFilePath
     * @param newFilePath
     */
    public static void patch(String oldFilePath,String diffFilePath,String newFilePath){
        Intent intent = new Intent(ContextUtil.get(),FixService.class);
        intent.putExtra(FixService.OLDFILE,oldFilePath);
        intent.putExtra(FixService.NEWFILE,newFilePath);
        intent.putExtra(FixService.DIFFFILE,diffFilePath);
        intent.putExtra(FixService.TYPE,FixService.PATCH_TYPE);
        ContextUtil.get().startService(intent);
    }
}
