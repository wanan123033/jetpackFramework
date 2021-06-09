package com.jetpackframework.fixdex;

import android.os.Parcel;
import android.os.Parcelable;

public class FixConfig implements Parcelable {
    private String dexDir;
    private String resourceDir;

    public String getDexDir() {
        return dexDir;
    }

    public String getResourceDir() {
        return resourceDir;
    }

    public String getSoDir() {
        return soDir;
    }

    private String soDir;

    private FixConfig(String dexDir, String resourceDir,String soDir) {
        this.dexDir = dexDir;
        this.resourceDir = resourceDir;
        this.soDir = soDir;
    }

    private FixConfig(Parcel in) {
        dexDir = in.readString();
        resourceDir = in.readString();
        soDir = in.readString();
    }

    public static final Creator<FixConfig> CREATOR = new Creator<FixConfig>() {
        @Override
        public FixConfig createFromParcel(Parcel in) {
            return new FixConfig(in);
        }

        @Override
        public FixConfig[] newArray(int size) {
            return new FixConfig[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dexDir);
        dest.writeString(resourceDir);
        dest.writeString(soDir);
    }

    public static class Builder{
        private String dexDir;
        private String resourceDir;
        private String soDir;
        public Builder setDexDir(String dexDir){
            this.dexDir = dexDir;
            return this;
        }
        public Builder serResourceDir(String resourceDir){
            this.resourceDir = resourceDir;
            return this;
        }
        public FixConfig builder(){
            return new FixConfig(dexDir,resourceDir,soDir);
        }

        public void setSoDir(String soDir) {
            this.soDir = soDir;
        }
    }
}
