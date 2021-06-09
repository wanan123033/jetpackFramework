package com.frameworkx.fileexpoter;

import com.jetpackframework.mvvm.BaseViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileViewModel extends BaseViewModel<Object> {
    public void loadFile(String path) {
        List<FileBean> fileBeans = new ArrayList<>();
        File file = new File(path);
        if (file.exists() && file.isDirectory()){
            File[] files = file.listFiles();
            for (File file1 : files){
                FileBean fileBean = new FileBean();
                fileBean.name = file1.getName();
                fileBean.isDir = file1.isDirectory();
                fileBean.absolutPath = file1.getAbsolutePath();
                fileBeans.add(fileBean);
            }
        }
        postValue(fileBeans);
    }

}
