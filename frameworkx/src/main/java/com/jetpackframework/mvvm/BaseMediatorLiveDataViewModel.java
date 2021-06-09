package com.jetpackframework.mvvm;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import java.util.List;

/**
 * 将多个LiveData 合并到一个LiveData上的ViewModel,适用于一个观察者观察多个对象时
 *      1.只要有一个LiveData数据发生了改变都会通知到观察者
 * @param <T>
 */
public abstract class BaseMediatorLiveDataViewModel<T>  extends ViewModel<T> {
    private MediatorLiveData<T> liveData;

    protected BaseMediatorLiveDataViewModel(){
        liveData = new MediatorLiveData<>();
    }
    @Override
    protected void onCreate(LifecycleOwner owner){
        super.onCreate(owner);
        List<Class<? extends BaseViewModel>> vms = getViewModels();
        if (vms != null || !vms.isEmpty()){
            for (Class<? extends BaseViewModel> vm : vms){
                BaseViewModel viewModel = getViewModel(vm);
                getLifecycle().addObserver(viewModel);
                addSource(viewModel);
            }
        }
    }
    private void addSource(BaseViewModel viewModel){
        liveData.addSource(viewModel.getLiveData(), new Observer() {
            @Override
            public void onChanged(Object o) {
                liveData.setValue(getData(o));
            }
        });
    }

    @Override
    public MediatorLiveData<T> getLiveData() {
        return liveData;
    }

    protected abstract T getData(Object o);

    protected abstract List<Class<? extends BaseViewModel>> getViewModels();

    @NonNull
    protected <T extends ViewModel> T getViewModel(Class<T> clazz){
        return ((IViewModelProvider)owner).getViewModelProvider().get(clazz);
    }
}
