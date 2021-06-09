package com.jetpackframework.virtual;

import android.content.IntentFilter;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class ReceiverInfo implements Parcelable {
    private String name;
    private String packageName;
    private IntentFilter intentFilter;

    public ReceiverInfo(String mainfestFile){
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new FileReader(mainfestFile));
            int eventType = parser.getEventType();
            switch (eventType){
                case XmlPullParser.START_TAG:
                    String name = parser.getName();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected ReceiverInfo(Parcel in) {
        name = in.readString();
        packageName = in.readString();
        intentFilter = in.readParcelable(IntentFilter.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(packageName);
        dest.writeParcelable(intentFilter, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ReceiverInfo> CREATOR = new Creator<ReceiverInfo>() {
        @Override
        public ReceiverInfo createFromParcel(Parcel in) {
            return new ReceiverInfo(in);
        }

        @Override
        public ReceiverInfo[] newArray(int size) {
            return new ReceiverInfo[size];
        }
    };
}
