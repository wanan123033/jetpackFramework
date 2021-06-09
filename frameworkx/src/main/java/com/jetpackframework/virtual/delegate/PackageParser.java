package com.jetpackframework.virtual.delegate;

import android.content.IntentFilter;
import android.util.Log;

import com.jetpackframework.Reflector;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PackageParser {
    public static Map<String, List<IntentFilter>> parserApk(String apkFile) throws Reflector.ReflectedException, IllegalAccessException {
        Map<String,List<IntentFilter>> intentFilterMap = new HashMap<>();
        Object packageParser = Reflector.on("android.content.pm.PackageParser").constructor().newInstance();
        Object parsePackage = Reflector.with(packageParser).method("parsePackage", File.class,int.class).call(new File(apkFile),0);
        List activities = Reflector.with(parsePackage).field("activities").get();
        List services = Reflector.with(parsePackage).field("services").get();
        List providers = Reflector.with(parsePackage).field("providers").get();
        List receivers = Reflector.with(parsePackage).field("receivers").get();
        for (Object activity : activities){
            List<IntentFilter> intentFilters = Reflector.with(activity).field("intents").get();
            Object info = Reflector.with(activity).field("info").get();
            String packageName = Reflector.with(info).field("packageName").get();
            String name = Reflector.with(info).field("name").get();
            intentFilterMap.put(packageName+"/"+name,intentFilters);
        }
        for (Object service : services){
            List<IntentFilter> intentFilters = Reflector.with(service).field("intents").get();
            Object info = Reflector.with(service).field("info").get();
            String packageName = Reflector.with(info).field("packageName").get();
            String name = Reflector.with(info).field("name").get();
            intentFilterMap.put(packageName+"/"+name,intentFilters);
        }
        for (Object provider : providers){
            List<IntentFilter> intentFilters = Reflector.with(provider).field("intents").get();
            Object info = Reflector.with(provider).field("info").get();
            String packageName = Reflector.with(info).field("packageName").get();
            String name = Reflector.with(info).field("name").get();
            intentFilterMap.put(packageName+"/"+name,intentFilters);
        }
        for (Object receiver : receivers){
            List<IntentFilter> intentFilters = Reflector.with(receiver).field("intents").get();
            Object info = Reflector.with(receiver).field("info").get();
            String packageName = Reflector.with(info).field("packageName").get();
            String name = Reflector.with(info).field("name").get();
            intentFilterMap.put(packageName+"/"+name,intentFilters);
        }
        return intentFilterMap;
    }
}
