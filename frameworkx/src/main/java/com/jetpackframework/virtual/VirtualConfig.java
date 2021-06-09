package com.jetpackframework.virtual;

import android.content.Context;

public class VirtualConfig {
    private int sdkMode;
    private boolean isDebug;
    private Context context;

    private VirtualConfig(Builder builder){
        sdkMode = builder.sdkMode;
        isDebug = builder.isDebug;
        context = builder.context;
    }

    public Context getContext() {
        return context;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public int getSdkMode() {
        return sdkMode;
    }

    public static class Builder{
        private int sdkMode;
        private boolean isDebug;
        private Context context;

        public VirtualConfig builder(){
            return new VirtualConfig(this);
        }

        public Builder setSdkMode(int sdkMode) {
            this.sdkMode = sdkMode;
            return this;
        }

        public Builder setDebug(boolean debug) {
            isDebug = debug;
            return this;
        }

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }
    }
}
