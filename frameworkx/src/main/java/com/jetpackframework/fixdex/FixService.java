package com.jetpackframework.fixdex;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FixService extends JobIntentService {
    public static final String OLDFILE = "OLDFILE";
    public static final String DIFFFILE = "DIFFFILE";
    public static final String NEWFILE = "NEWFILE";
    public static final String TYPE = "TYPE";

    public static final int PATCH_TYPE = 1;
    public static final int DIFF_TYPE = 2;
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        File oldFile = new File(intent.getStringExtra(OLDFILE));
        if (!oldFile.exists() || oldFile.isDirectory()){
            return;
        }
        File newFile = new File(intent.getStringExtra(NEWFILE));
        if (!newFile.exists() || newFile.isDirectory()){
            return;
        }
        File diffFile = new File(intent.getStringExtra(DIFFFILE));
        if (!diffFile.exists() || diffFile.isDirectory()){
            return;
        }
        int type = intent.getIntExtra(TYPE,0);
        if (type == DIFF_TYPE) {
            try {
                BSDiff.bsdiff(oldFile, newFile, diffFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (type == PATCH_TYPE){
            FileInputStream oldIn = null;
            FileInputStream diffIn = null;
            try {
                oldIn = new FileInputStream(oldFile);
                diffIn = new FileInputStream(diffFile);
                BSPatch.patchFast(oldIn,diffIn,newFile);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    oldIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    diffIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
