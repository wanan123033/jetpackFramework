package com.mindmachine.dataselectmanager.viewmodel;

import android.content.Intent;

import com.jetpackframework.mvvm.BaseViewModel;
import com.mindmachine.common.presenter.RosterDownPresenter;

public class DataManagerViewModel extends BaseViewModel<Object> {
    public void rosterDown() {
        RosterDownPresenter presenter = new RosterDownPresenter();
        presenter.setViewModel(this);
        presenter.rosterDown();
    }

    public void topicDown() {

    }

    public void deleteTopic() {

    }

    public void clearData() {

    }

    public void scoreUpload() {

    }

    public void faceCheck() {

    }

    public void softUpdate() {

    }

    public void rosterPerson(Intent data) {

    }

    public void rosterGroup(Intent data) {

    }

    public void topicImport(Intent data) {

    }

    public void dataBackup(Intent data) {

    }

    public void dataRestore(Intent data) {

    }

    public void scoreExport(Intent data) {

    }

    public void templateExport(Intent data) {

    }

    public void temperatureExport(Intent data) {

    }

    public void videoBackup(Intent data) {

    }
}
