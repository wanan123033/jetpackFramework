package com.mindmachine.appmodule.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import com.appmodule.layout.activity_app_main;
import com.gwm.annotation.layout.Layout;
import com.gwm.annotation.layout.OnClick;
import com.gwm.annotation.router.ARouter;
import com.jetpackframework.base.BaseActivity;
import com.jetpackframework.base.JetpackApplicationDelegate;
import com.jetpackframework.virtual.IVirtualInstallListenner;
import com.jetpackframework.virtual.Virtual;
import com.midmachine.database.DBManager;
import com.midmachine.database.entity.Item;
import com.mindmachine.appmodule.R;
import com.mindmachine.appmodule.R2;
import com.mindmachine.common.contract.MMKVContract;
import com.mindmachine.common.contract.RouterContract;
import com.mindmachine.common.setting.SystemSetting;
import com.tencent.mmkv.MMKV;

import java.io.File;
import java.util.List;

@Layout(R2.layout.activity_app_main)
@ARouter(RouterContract.MAINACTIVITY)
public class MainActivity extends BaseActivity<activity_app_main> {
    private SystemSetting systemSetting;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MMKV mmkv = JetpackApplicationDelegate.getInstance().getMmkv();
        List<Item> items = DBManager.getInstance().getItems();
        Log.e("TAG",items.toString());
        systemSetting = mmkv.decodeParcelable(MMKVContract.SYSTEM_SETTING_BEAN, SystemSetting.class);
        if (systemSetting == null){
            systemSetting = new SystemSetting();
        }
        addFirstToast();


        try {
            Virtual.getInstaller(this).install(new File("/sdcard/app-debug.apk"), new IVirtualInstallListenner.Stub() {
                @Override
                public void installSuccess(String packageName) throws RemoteException {
                    Intent intent = new Intent();
                    ComponentName componentName = new ComponentName("com.fairplay.test","com.fairplay.test.TestAActivity");
                    intent.setComponent(componentName);
                    startActivity(intent);
                }

                @Override
                public void installFail(String errMessage, int errCode) throws RemoteException {

                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R2.id.btn_test,R2.id.btn_select,R2.id.btn_print_test,R2.id.btn_param_setting,R2.id.btn_data_manager,R2.id.btn_system,R2.id.btn_led,R2.id.btn_device_out})
    public void onClick(View view){
        int id = view.getId();
        if (id == R.id.btn_test)
            jumpTest();
        else if (id == R.id.btn_select)
            select();
        else if (id == R.id.btn_print_test)
            printTest();
        else if (id == R.id.btn_param_setting)
            paramSetting();
        else if (id == R.id.btn_data_manager)
            dataManager();
        else if (id == R.id.btn_system)
            systemjump();
        else if (id == R.id.btn_led)
            led();
        else if (id == R.id.btn_device_out)
            deviceOut();

    }

    public void deviceOut() {
//        if (BuildConfig.mindmachine.equals("ks")) {
//            arouterActivity(RouterContract.ROUTER_DEVICEOUTACTIVITY);
//        }else if (BuildConfig.mindmachine.equals("tc")){
//            arouterActivity(RouterContract.ROUTER_TCDEVICEOUTACTIVITY);
//        }
    }

    public void led() {
        arouterActivity(RouterContract.ROUTER_LEDACTIVITY);
    }

    public void systemjump() {
        startActivity(new Intent(Settings.ACTION_SETTINGS));
    }

    public void dataManager() {
        arouterActivity(RouterContract.ROUTER_DATAMANAGERACTIVITY);
    }

    public void paramSetting() {
        arouterActivity(RouterContract.ROUTER_PARAMSETTINGACTIVITY);
    }

    public void printTest() {

    }

    public void select() {
        arouterActivity(RouterContract.ROUTER_DATASELECTACTIVITY);
    }

    public void jumpTest() {
//        if (BuildConfig.mindmachine.equals("ks")) {
//            if (systemSetting.testPrarren == PERSON_TEST)
//                arouterActivity(ItemContract.getPersonRouterUrl(ItemContract.getCurrentItemCode()));
//            else
//                arouterActivity(RouterContract.ROUTER_GROUPTESTACTIVITY);
//        }else if (BuildConfig.mindmachine.equals("tc")){
//            if (systemSetting.testPrarren == TC_TEST){
//                arouterActivity(ItemContract.getTcRouterUrl(ItemContract.getCurrentItemCode()));
//            }else {
//                arouterActivity(ItemContract.getZyRouterUrl(ItemContract.getCurrentItemCode()));
//            }
//        }
    }
}
