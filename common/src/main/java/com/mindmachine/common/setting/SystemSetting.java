package com.mindmachine.common.setting;

import android.os.Parcel;
import android.os.Parcelable;

public class SystemSetting implements Parcelable {
    public static final int QRCODE_CHECKTOOL = 1;   //二维码/条码  检录方式
    public static final int IDCARD_CHECKTOOL = 2;  //身份证  检录方式
    public static final int ICCARD_CHECKTOOL = 3;  //IC卡  检录方式
    public static final int SCAN_CHECKTOOL = 4;   //扫描枪  检录方式
    public static final int RS_CHECKTOOL = 5;     //人脸识别  检录方式

    public static final int PERSON_TEST = 0;     //个人模式
    public static final int GROUP_TEST = 1;     //分组模式
    public static final int TC_TEST = 2;     //体测模式
    public static final int ZY_TEST = 2;     //自由模式
    public String testName;
    public String testAddress;
    public String serverIp;
    public String tcpIp;
    public int hostId;
    public int channelCode;
    public int testPrarren = PERSON_TEST;
    public int checkTool;
    public boolean isAutoPrint;
    public boolean isScoreMedia;
    public boolean isIntoStu;
    public boolean isOnline;
    public boolean isCustomChannel;
    public boolean netCheckTool;

    protected SystemSetting(Parcel in) {
        testName = in.readString();
        testAddress = in.readString();
        serverIp = in.readString();
        tcpIp = in.readString();
        hostId = in.readInt();
        channelCode = in.readInt();
        testPrarren = in.readInt();
        checkTool = in.readInt();
        isAutoPrint = in.readByte() != 0;
        isScoreMedia = in.readByte() != 0;
        isIntoStu = in.readByte() != 0;
        isOnline = in.readByte() != 0;
        isCustomChannel = in.readByte() != 0;
        netCheckTool = in.readByte() != 0;
    }
    public SystemSetting() {
    }

    public static final Creator<SystemSetting> CREATOR = new Creator<SystemSetting>() {
        @Override
        public SystemSetting createFromParcel(Parcel in) {
            return new SystemSetting(in);
        }

        @Override
        public SystemSetting[] newArray(int size) {
            return new SystemSetting[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(testName);
        dest.writeString(testAddress);
        dest.writeString(serverIp);
        dest.writeString(tcpIp);
        dest.writeInt(hostId);
        dest.writeInt(channelCode);
        dest.writeInt(testPrarren);
        dest.writeInt(checkTool);
        dest.writeByte((byte) (isAutoPrint ? 1 : 0));
        dest.writeByte((byte) (isScoreMedia ? 1 : 0));
        dest.writeByte((byte) (isIntoStu ? 1 : 0));
        dest.writeByte((byte) (isOnline ? 1 : 0));
        dest.writeByte((byte) (isCustomChannel ? 1 : 0));
        dest.writeByte((byte) (netCheckTool ? 1 : 0));
    }
}
