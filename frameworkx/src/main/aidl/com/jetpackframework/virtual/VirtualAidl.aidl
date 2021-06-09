// VirtualAidl.aidl
package com.jetpackframework.virtual;

import com.jetpackframework.virtual.IVirtualInstallListenner;

parcelable VirtualApk;
// Declare any non-default types here with import statements
interface VirtualAidl {
    String install(String apkPath,IVirtualInstallListenner listenner);
    PackageInfo getPackageInfo(String packageName);
    VirtualApk getVirtualApk(String packageName);
}