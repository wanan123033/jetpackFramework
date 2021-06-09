package com.mindmachine.common.base;

import android.os.Bundle;

import com.jetpackframework.base.JetpackApplicationDelegate;
import com.jetpackframework.ioc.IViewBind;
import com.jetpackframework.mvvm.BaseMvvmTitleActivity;
import com.jetpackframework.mvvm.ViewModel;
import com.mindmachine.common.R;
import com.mindmachine.common.bean.Student;
import com.mindmachine.common.contract.MMKVContract;
import com.mindmachine.common.setting.SystemSetting;
import com.mindmachine.common.util.ScannerGunManager;
import com.tencent.mmkv.MMKV;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.mindmachine.common.setting.SystemSetting.RS_CHECKTOOL;

public abstract class BaseCheckActivity<M,VM extends ViewModel<M>,V extends IViewBind> extends BaseMvvmTitleActivity<M,VM,V> {
    private static final int STUDENT_CODE = 1;
    private ScannerGunManager scannerGunManager;
    private SweetAlertDialog dialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MMKV mmkv = JetpackApplicationDelegate.getInstance().getMmkv();
        SystemSetting setting = mmkv.decodeParcelable(MMKVContract.SYSTEM_SETTING_BEAN,SystemSetting.class);
        if (setting == null){
            setting = new SystemSetting();
        }
        if (setting.checkTool == RS_CHECKTOOL){

        }
        scannerGunManager = new ScannerGunManager(new ScannerGunManager.OnScanListener() {
            @Override
            public void onResult(String code) {
                boolean needAdd = checkQulification(code, STUDENT_CODE);
                if (needAdd) {
                    Student student = new Student();
                    student.setStudentCode(code);
                    showAddHint(student);
                }
            }
        });
    }

    private void showAddHint(final Student student) {
        appHandler.post(new Runnable() {
            @Override
            public void run() {
                if (dialog == null) {
                    new SweetAlertDialog(BaseCheckActivity.this).setTitleText(getString(R.string.addStu_dialog_title))
                            .setContentText(getString(R.string.addStu_dialog_content))
                            .setConfirmText(getString(R.string.confirm)).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
//                            new AddStudentDialog(BaseCheckActivity.this).showDialog(student, false);
                        }
                    }).setCancelText(getString(R.string.cancel)).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    });
                }
                if (!dialog.isShowing()){
                    dialog.show();
                }
            }
        });
    }

    private boolean checkQulification(String code, int stuMode) {

        return false;
    }
}
