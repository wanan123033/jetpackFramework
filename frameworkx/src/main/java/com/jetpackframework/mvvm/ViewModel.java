package com.jetpackframework.mvvm;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class ViewModel<T> extends androidx.lifecycle.ViewModel implements LifecycleObserver, LifecycleOwner,IViewModelProvider {
    protected LifecycleOwner owner;
    private ExecutorService executor;
    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    protected void onAny(LifecycleOwner owner){
        this.owner = owner;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    protected void onCreate(LifecycleOwner owner){
        this.owner = owner;
        executor = Executors.newCachedThreadPool();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected void onStart(LifecycleOwner owner){
        this.owner = owner;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected void onStop(LifecycleOwner owner){
        this.owner = owner;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    protected void onResume(LifecycleOwner owner){
        this.owner = owner;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    protected void onPause(LifecycleOwner owner){
        this.owner = owner;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    protected void onDestory(LifecycleOwner owner){
        this.owner = owner;
        executor.shutdownNow();
        executor = null;
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return owner.getLifecycle();
    }

    public abstract LiveData<T> getLiveData();

    @Override
    public ViewModelProvider getViewModelProvider(){
        return ((IViewModelProvider)owner).getViewModelProvider();
    }

    public void runThreadRunnable(Runnable runnable){
        if (executor != null && !executor.isShutdown())
            executor.execute(runnable);
    }
}
