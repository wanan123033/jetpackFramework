package com.mindmachine.common.presenter;

import com.jetpackframework.rxjetpack.Observable;
import com.mindmachine.common.base.BaseEntryDataPresenter;
import com.mindmachine.common.bean.RosterDownBean;

public class RosterDownPresenter extends BaseEntryDataPresenter<RosterDownPresenter.RosterDownInfo, RosterDownBean> {

    public RosterDownPresenter() {
        super(RosterDownInfo.class);
    }

    public void rosterDown() {
        String token = getToken();
        Observable<RosterDownBean> rosterdown = getHttpPresenter().rosterdown(token, null);
        addHttpSubscriber(rosterdown);
    }
    @Override
    protected void onNextResult(RosterDownBean response, int id) {

    }

    @Override
    protected void onErrorResult(Exception e, int id) {

    }



    public interface RosterDownInfo extends BaseEntryDataPresenter.HttpBaseBean{
        
    }
}
