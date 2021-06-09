// IVirtualInstallListenner.aidl
package com.jetpackframework.virtual;

// Declare any non-default types here with import statements

interface IVirtualInstallListenner {
    void installSuccess(String packageName);
    void installFail(String errMessage,int errCode);
}