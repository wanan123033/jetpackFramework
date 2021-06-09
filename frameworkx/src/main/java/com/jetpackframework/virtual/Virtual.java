package com.jetpackframework.virtual;

import android.content.Context;

public class Virtual {
    public static void install(VirtualConfig config){
        getInstaller(config.getContext());
    }

    public static VirtualInstaller getInstaller(Context context){
        return VirtualInstaller.getInstance(context);
    }
}
