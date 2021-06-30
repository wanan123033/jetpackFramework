package com.mindmachine.common.base;


import android.content.Context;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import com.common.retrofit.RetrofitUtil;
import com.gwm.annotation.http.JSON;
import com.gwm.annotation.http.Param;
import com.jetpackframework.ContextUtil;
import com.jetpackframework.base.JetpackApplicationDelegate;
import com.jetpackframework.retrofit.BaseJsonDataPresenter;
import com.jetpackframework.retrofit.IRetrofitUtil;
import com.mindmachine.common.contract.HttpContract;
import com.mindmachine.common.contract.MMKVContract;
import com.mindmachine.common.util.EncryptUtil;
import com.tencent.mmkv.MMKV;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Http加密传输基类
 * @param <J> JSON接口
 * @param <D>  回传数据bean
 */
public abstract class BaseEntryDataPresenter<J extends BaseEntryDataPresenter.HttpBaseBean,D> extends BaseJsonDataPresenter<J,HttpContract,D> {
    private MMKV mmkv;
    public BaseEntryDataPresenter(Class<J> clazz) {
        super(clazz);
        mmkv = MMKV.defaultMMKV();
    }
    protected String genJsonString(long bizType,String data){
        String lastUpdateTime = mmkv.getString(MMKVContract.LASTUPDATETIME,null);
        String msEquipment = getDeviceInfo();
        String signData = EncryptUtil.getInstance().getSignData(data);
        String token = mmkv.getString(MMKVContract.TOKEN,"");
        return getJsonCreator().sign(bizType,
                EncryptUtil.getInstance().setEncryptData(data),
                lastUpdateTime,
                msEquipment,
                System.currentTimeMillis()+"",
                signData,
                token);
    }

    public static String getDeviceInfo() {
        TelephonyManager phone = (TelephonyManager) ContextUtil.get().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        WifiManager wifi = (WifiManager) ContextUtil.get().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        return wifi.getConnectionInfo().getMacAddress() + "," + phone.getDeviceId() + "," + getCpuName() + "," + phone.getNetworkOperator();
    }
    private static String getCpuName() {
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr);
            while ((str2 = localBufferedReader.readLine()) != null) {
                if (str2.contains("Hardware")) {
                    return str2.split(":")[1];
                }
            }
            localBufferedReader.close();
        } catch (IOException e) {
        }
        return null;
    }

    @Override
    protected IRetrofitUtil<HttpContract> getRetrifit() {
        return RetrofitUtil.getInstance();
    }

    protected String getToken(){
        return null;
    }
    public interface HttpBaseBean{
        @JSON
        String sign(@Param("bizType")long bizType,
                    @Param("data")String data,
                    @Param("lastUpdateTime")String lastUpdateTime,
                    @Param("msEquipment")String msEquipment,
                    @Param("requestTime")String requestTime,
                    @Param("sign")String sign,
                    @Param("token")String token);
    }
}
