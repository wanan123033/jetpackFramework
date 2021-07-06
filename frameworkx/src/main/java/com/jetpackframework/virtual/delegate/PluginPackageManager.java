package com.jetpackframework.virtual.delegate;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ChangedPackages;
import android.content.pm.FeatureInfo;
import android.content.pm.InstrumentationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.pm.SharedLibraryInfo;
import android.content.pm.VersionedPackage;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.UserHandle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.jetpackframework.virtual.VirtualApk;

import java.util.Arrays;
import java.util.List;

public class PluginPackageManager extends PackageManager {
    private VirtualApk apk;
    private PackageManager packageManager;

    public PluginPackageManager(VirtualApk apk, PackageManager packageManager) {
        this.apk = apk;
        this.packageManager = packageManager;
    }

    @Override
    public PackageInfo getPackageInfo(@NonNull String packageName, int flags) throws NameNotFoundException {
        if (packageName.equals(apk.getPackageName())){
            return apk.getPackageInfo();
        }
        return packageManager.getPackageInfo(packageName,flags);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public PackageInfo getPackageInfo(@NonNull VersionedPackage versionedPackage, int flags) throws NameNotFoundException {
        if (versionedPackage.getPackageName().equals(apk.getPackageName())){
            return apk.getPackageInfo();
        }
        return packageManager.getPackageInfo(versionedPackage,flags);
    }

    @Override
    public String[] currentToCanonicalPackageNames(@NonNull String[] packageNames) {
        return packageManager.currentToCanonicalPackageNames(packageNames);
    }

    @Override
    public String[] canonicalToCurrentPackageNames(@NonNull String[] packageNames) {
        return packageManager.canonicalToCurrentPackageNames(packageNames);
    }

    @Nullable
    @Override
    public Intent getLaunchIntentForPackage(@NonNull String packageName) {
        if (packageName.equals(apk.getPackageName())){
            return apk.getLaunchIntent();
        }
        return packageManager.getLaunchIntentForPackage(packageName);
    }

    @Nullable
    @Override
    public Intent getLeanbackLaunchIntentForPackage(@NonNull String packageName) {
        if (packageName.equals(apk.getPackageName())){
            return apk.getLaunchIntent();
        }
        return packageManager.getLeanbackLaunchIntentForPackage(packageName);
    }

    @Override
    public int[] getPackageGids(@NonNull String packageName) throws NameNotFoundException {
        if (packageName.equals(apk.getPackageName()))
            return apk.getPackageInfo().gids;
        return packageManager.getPackageGids(packageName);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int[] getPackageGids(@NonNull String packageName, int flags) throws NameNotFoundException {
        if (packageName.equals(apk.getPackageName()))
            return apk.getPackageInfo().gids;
        return packageManager.getPackageGids(packageName,flags);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int getPackageUid(@NonNull String packageName, int flags) throws NameNotFoundException {
        return packageManager.getPackageUid(packageName,flags);
    }

    @Override
    public PermissionInfo getPermissionInfo(@NonNull String permissionName, int flags) throws NameNotFoundException {
        return packageManager.getPermissionInfo(permissionName,flags);
    }

    @NonNull
    @Override
    public List<PermissionInfo> queryPermissionsByGroup(@NonNull String permissionGroup, int flags) throws NameNotFoundException {
        return packageManager.queryPermissionsByGroup(permissionGroup, flags);
    }

    @NonNull
    @Override
    public PermissionGroupInfo getPermissionGroupInfo(@NonNull String permissionName, int flags) throws NameNotFoundException {
        return packageManager.getPermissionGroupInfo(permissionName, flags);
    }

    @NonNull
    @Override
    public List<PermissionGroupInfo> getAllPermissionGroups(int flags) {
        return packageManager.getAllPermissionGroups(flags);
    }

    @NonNull
    @Override
    public ApplicationInfo getApplicationInfo(@NonNull String packageName, int flags) throws NameNotFoundException {
        if (packageName.equals(apk.getPackageName())){
            return apk.getPackageInfo().applicationInfo;
        }
        return packageManager.getApplicationInfo(packageName,flags);
    }

    @NonNull
    @Override
    public ActivityInfo getActivityInfo(@NonNull ComponentName component, int flags) throws NameNotFoundException {
        ActivityInfo activityInfo = apk.getActivityInfo(component);
        if (activityInfo != null){
            return activityInfo;
        }
        return packageManager.getActivityInfo(component,flags);
    }

    @NonNull
    @Override
    public ActivityInfo getReceiverInfo(@NonNull ComponentName component, int flags) throws NameNotFoundException {
        ActivityInfo activityInfo = apk.getReceiverInfo(component);
        if (activityInfo != null){
            return activityInfo;
        }
        return packageManager.getReceiverInfo(component,flags);
    }

    @NonNull
    @Override
    public ServiceInfo getServiceInfo(@NonNull ComponentName component, int flags) throws NameNotFoundException {
        ServiceInfo serviceInfo = apk.getServiceInfo(component);
        if (serviceInfo != null){
            return serviceInfo;
        }
        return packageManager.getServiceInfo(component,flags);
    }

    @NonNull
    @Override
    public ProviderInfo getProviderInfo(@NonNull ComponentName component, int flags) throws NameNotFoundException {
        ProviderInfo providerInfo = apk.getProviderInfo(component);
        if (providerInfo != null){
            return providerInfo;
        }
        return packageManager.getProviderInfo(component,flags);
    }

    @NonNull
    @Override
    public List<PackageInfo> getInstalledPackages(int flags) {
        return packageManager.getInstalledPackages(flags);
    }

    @NonNull
    @Override
    public List<PackageInfo> getPackagesHoldingPermissions(@NonNull String[] permissions, int flags) {
        return packageManager.getPackagesHoldingPermissions(permissions,flags);
    }

    @Override
    public int checkPermission(@NonNull String permissionName, @NonNull String packageName) {
        return packageManager.checkPermission(permissionName, packageName);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean isPermissionRevokedByPolicy(@NonNull String permissionName, @NonNull String packageName) {
        return packageManager.isPermissionRevokedByPolicy(permissionName, packageName);
    }

    @Override
    public boolean addPermission(@NonNull PermissionInfo info) {
        return packageManager.addPermission(info);
    }

    @Override
    public boolean addPermissionAsync(@NonNull PermissionInfo info) {
        return packageManager.addPermissionAsync(info);
    }

    @Override
    public void removePermission(@NonNull String permissionName) {
        packageManager.removePermission(permissionName);
    }

    @Override
    public int checkSignatures(@NonNull String packageName1, @NonNull String packageName2) {
        return packageManager.checkSignatures(packageName1,packageName2);
    }

    @Override
    public int checkSignatures(int uid1, int uid2) {
        return packageManager.checkSignatures(uid1, uid2);
    }

    @Nullable
    @Override
    public String[] getPackagesForUid(int uid) {
        return packageManager.getPackagesForUid(uid);
    }

    @Nullable
    @Override
    public String getNameForUid(int uid) {
        return packageManager.getNameForUid(uid);
    }

    @NonNull
    @Override
    public List<ApplicationInfo> getInstalledApplications(int flags) {
        return packageManager.getInstalledApplications(flags);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean isInstantApp() {
        return packageManager.isInstantApp();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean isInstantApp(@NonNull String packageName) {
        return packageManager.isInstantApp(packageName);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int getInstantAppCookieMaxBytes() {
        return packageManager.getInstantAppCookieMaxBytes();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public byte[] getInstantAppCookie() {
        return packageManager.getInstantAppCookie();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void clearInstantAppCookie() {
        packageManager.clearInstantAppCookie();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void updateInstantAppCookie(@Nullable byte[] cookie) {
        packageManager.updateInstantAppCookie(cookie);
    }

    @Nullable
    @Override
    public String[] getSystemSharedLibraryNames() {
        return packageManager.getSystemSharedLibraryNames();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public List<SharedLibraryInfo> getSharedLibraries(int flags) {
        return packageManager.getSharedLibraries(flags);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public ChangedPackages getChangedPackages(int sequenceNumber) {
        return packageManager.getChangedPackages(sequenceNumber);
    }

    @NonNull
    @Override
    public FeatureInfo[] getSystemAvailableFeatures() {
        return packageManager.getSystemAvailableFeatures();
    }

    @Override
    public boolean hasSystemFeature(@NonNull String featureName) {
        return packageManager.hasSystemFeature(featureName);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean hasSystemFeature(@NonNull String featureName, int version) {
        return packageManager.hasSystemFeature(featureName, version);
    }

    @Nullable
    @Override
    public ResolveInfo resolveActivity(@NonNull Intent intent, int flags) {
        ResolveInfo info = apk.resolveActivity(intent, flags);
        if (info != null)
            return info;
        return packageManager.resolveActivity(intent,flags);
    }

    @NonNull
    @Override
    public List<ResolveInfo> queryIntentActivities(@NonNull Intent intent, int flags) {
        ResolveInfo resolveInfo = resolveActivity(intent, flags);
        if (resolveInfo != null){
            return Arrays.asList(resolveInfo);
        }
        return packageManager.queryIntentActivities(intent,flags);
    }

    @NonNull
    @Override
    public List<ResolveInfo> queryIntentActivityOptions(@Nullable ComponentName caller, @Nullable Intent[] specifics, @NonNull Intent intent, int flags) {
        return packageManager.queryIntentActivityOptions(caller, specifics, intent, flags);
    }

    @NonNull
    @Override
    public List<ResolveInfo> queryBroadcastReceivers(@NonNull Intent intent, int flags) {
        return packageManager.queryBroadcastReceivers(intent, flags);
    }

    @Nullable
    @Override
    public ResolveInfo resolveService(@NonNull Intent intent, int flags) {
        ResolveInfo info = apk.resolveService(intent, flags);
        if (info != null)
            return info;
        return packageManager.resolveService(intent,flags);
    }

    @NonNull
    @Override
    public List<ResolveInfo> queryIntentServices(@NonNull Intent intent, int flags) {
        return packageManager.queryIntentServices(intent, flags);
    }

    @NonNull
    @Override
    public List<ResolveInfo> queryIntentContentProviders(@NonNull Intent intent, int flags) {
        return packageManager.queryIntentContentProviders(intent, flags);
    }

    @Nullable
    @Override
    public ProviderInfo resolveContentProvider(@NonNull String authority, int flags) {
        return null;
    }

    @NonNull
    @Override
    public List<ProviderInfo> queryContentProviders(@Nullable String processName, int uid, int flags) {
        return null;
    }

    @NonNull
    @Override
    public InstrumentationInfo getInstrumentationInfo(@NonNull ComponentName className, int flags) throws NameNotFoundException {
        return null;
    }

    @NonNull
    @Override
    public List<InstrumentationInfo> queryInstrumentation(@NonNull String targetPackage, int flags) {
        return null;
    }

    @Nullable
    @Override
    public Drawable getDrawable(@NonNull String packageName, int resid, @Nullable ApplicationInfo appInfo) {
        return null;
    }

    @NonNull
    @Override
    public Drawable getActivityIcon(@NonNull ComponentName activityName) throws NameNotFoundException {
        return null;
    }

    @NonNull
    @Override
    public Drawable getActivityIcon(@NonNull Intent intent) throws NameNotFoundException {
        return null;
    }

    @Nullable
    @Override
    public Drawable getActivityBanner(@NonNull ComponentName activityName) throws NameNotFoundException {
        return null;
    }

    @Nullable
    @Override
    public Drawable getActivityBanner(@NonNull Intent intent) throws NameNotFoundException {
        return null;
    }

    @NonNull
    @Override
    public Drawable getDefaultActivityIcon() {
        return null;
    }

    @NonNull
    @Override
    public Drawable getApplicationIcon(@NonNull ApplicationInfo info) {
        return null;
    }

    @NonNull
    @Override
    public Drawable getApplicationIcon(@NonNull String packageName) throws NameNotFoundException {
        return null;
    }

    @Nullable
    @Override
    public Drawable getApplicationBanner(@NonNull ApplicationInfo info) {
        return null;
    }

    @Nullable
    @Override
    public Drawable getApplicationBanner(@NonNull String packageName) throws NameNotFoundException {
        return null;
    }

    @Nullable
    @Override
    public Drawable getActivityLogo(@NonNull ComponentName activityName) throws NameNotFoundException {
        return null;
    }

    @Nullable
    @Override
    public Drawable getActivityLogo(@NonNull Intent intent) throws NameNotFoundException {
        return null;
    }

    @Nullable
    @Override
    public Drawable getApplicationLogo(@NonNull ApplicationInfo info) {
        return null;
    }

    @Nullable
    @Override
    public Drawable getApplicationLogo(@NonNull String packageName) throws NameNotFoundException {
        return null;
    }

    @NonNull
    @Override
    public Drawable getUserBadgedIcon(@NonNull Drawable drawable, @NonNull UserHandle user) {
        return null;
    }

    @NonNull
    @Override
    public Drawable getUserBadgedDrawableForDensity(@NonNull Drawable drawable, @NonNull UserHandle user, @Nullable Rect badgeLocation, int badgeDensity) {
        return null;
    }

    @NonNull
    @Override
    public CharSequence getUserBadgedLabel(@NonNull CharSequence label, @NonNull UserHandle user) {
        return null;
    }

    @Nullable
    @Override
    public CharSequence getText(@NonNull String packageName, int resid, @Nullable ApplicationInfo appInfo) {
        return null;
    }

    @Nullable
    @Override
    public XmlResourceParser getXml(@NonNull String packageName, int resid, @Nullable ApplicationInfo appInfo) {
        return null;
    }

    @NonNull
    @Override
    public CharSequence getApplicationLabel(@NonNull ApplicationInfo info) {
        return null;
    }

    @NonNull
    @Override
    public Resources getResourcesForActivity(@NonNull ComponentName activityName) throws NameNotFoundException {
        return null;
    }

    @NonNull
    @Override
    public Resources getResourcesForApplication(@NonNull ApplicationInfo app) throws NameNotFoundException {
        return null;
    }

    @NonNull
    @Override
    public Resources getResourcesForApplication(@NonNull String packageName) throws NameNotFoundException {
        return null;
    }

    @Override
    public void verifyPendingInstall(int id, int verificationCode) {

    }

    @Override
    public void extendVerificationTimeout(int id, int verificationCodeAtTimeout, long millisecondsToDelay) {

    }

    @Override
    public void setInstallerPackageName(@NonNull String targetPackage, @Nullable String installerPackageName) {

    }

    @Nullable
    @Override
    public String getInstallerPackageName(@NonNull String packageName) {
        return null;
    }

    @Override
    public void addPackageToPreferred(@NonNull String packageName) {

    }

    @Override
    public void removePackageFromPreferred(@NonNull String packageName) {

    }

    @NonNull
    @Override
    public List<PackageInfo> getPreferredPackages(int flags) {
        return null;
    }

    @Override
    public void addPreferredActivity(@NonNull IntentFilter filter, int match, @Nullable ComponentName[] set, @NonNull ComponentName activity) {

    }

    @Override
    public void clearPackagePreferredActivities(@NonNull String packageName) {

    }

    @Override
    public int getPreferredActivities(@NonNull List<IntentFilter> outFilters, @NonNull List<ComponentName> outActivities, @Nullable String packageName) {
        return 0;
    }

    @Override
    public void setComponentEnabledSetting(@NonNull ComponentName componentName, int newState, int flags) {

    }

    @Override
    public int getComponentEnabledSetting(@NonNull ComponentName componentName) {
        return 0;
    }

    @Override
    public void setApplicationEnabledSetting(@NonNull String packageName, int newState, int flags) {

    }

    @Override
    public int getApplicationEnabledSetting(@NonNull String packageName) {
        return 0;
    }

    @Override
    public boolean isSafeMode() {
        return packageManager.isSafeMode();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void setApplicationCategoryHint(@NonNull String packageName, int categoryHint) {
        packageManager.setApplicationCategoryHint(packageName, categoryHint);
    }

    @NonNull
    @Override
    public PackageInstaller getPackageInstaller() {
        return packageManager.getPackageInstaller();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean canRequestPackageInstalls() {
        return packageManager.canRequestPackageInstalls();
    }
}
