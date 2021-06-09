package com.jetpackframework.rxjetpack;

public class DisposableHelper {
    public synchronized static void dispose(Dispose dispose){
        dispose.dispose();
    }
    public synchronized static boolean isDispose(Dispose dispose){
        return dispose.isDispose();
    }
}
