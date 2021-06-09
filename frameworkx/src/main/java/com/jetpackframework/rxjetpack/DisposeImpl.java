package com.jetpackframework.rxjetpack;

public class DisposeImpl implements Dispose {
    private boolean dispose = false;
    @Override
    public void dispose() {
        dispose = true;
    }

    @Override
    public boolean isDispose() {
        return dispose;
    }
}
