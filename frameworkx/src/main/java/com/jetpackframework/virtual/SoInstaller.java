package com.jetpackframework.virtual;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class SoInstaller {
    private static final String FILE_NAME = "so_version";

    public static void nativeLib(File apk, Context context, PackageInfo packageInfo, File nativeLibDir) throws Exception {
        long startTime = System.currentTimeMillis();
        ZipFile zipfile = new ZipFile(apk.getAbsolutePath());

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Log.e("TAG---", Arrays.toString(Build.SUPPORTED_ABIS));
                for (String cpuArch : Build.SUPPORTED_ABIS) {

                    if (findAndCopyNativeLib(zipfile, context, cpuArch, packageInfo, nativeLibDir)) {
                        //加载so文件
//                        loadLibrary(nativeLibDir.getAbsolutePath());
                        return;
                    }
                }

            } else {
                if (findAndCopyNativeLib(zipfile, context, Build.CPU_ABI, packageInfo, nativeLibDir)) {
                    //加载so文件
//                    loadLibrary(nativeLibDir.getAbsolutePath());
                    return;
                }
            }

            findAndCopyNativeLib(zipfile, context, "armeabi", packageInfo, nativeLibDir);

            //加载so文件
//            loadLibrary(nativeLibDir.getAbsolutePath());

        } finally {
            zipfile.close();
            Log.d("TAG", "Done! +" + (System.currentTimeMillis() - startTime) + "ms");
        }
    }
    private static boolean findAndCopyNativeLib(ZipFile zipfile, Context context, String cpuArch, PackageInfo packageInfo, File nativeLibDir) throws Exception {
        Log.d("TAG", "Try to copy plugin's cup arch: " + cpuArch);
        boolean findLib = false;
        boolean findSo = false;
        byte buffer[] = null;
        String libPrefix = "lib/" + cpuArch + "/";
        ZipEntry entry;
        Enumeration e = zipfile.entries();
        while (e.hasMoreElements()) {
            entry = (ZipEntry) e.nextElement();
            String entryName = entry.getName();

            if (entryName.charAt(0) < 'l') {
                continue;
            }
            if (entryName.charAt(0) > 'l') {
                continue;
            }
            if (!findLib && !entryName.startsWith("lib/")) {
                continue;
            }
            findLib = true;
            if (!entryName.endsWith(".so") || !entryName.startsWith(libPrefix)) {
                continue;
            }

            if (buffer == null) {
                findSo = true;
                Log.d("TAG", "Found plugin's cup arch dir: " + cpuArch);
                buffer = new byte[8192];
            }

            String libName = entryName.substring(entryName.lastIndexOf('/') + 1);
            Log.d("TAG", "verify so " + libName);
            File libFile = new File(nativeLibDir, libName);
            String key = packageInfo.packageName + "_" + libName;
            if (libFile.exists()) {
                int VersionCode = getSoVersion(context, key);
                if (VersionCode == packageInfo.versionCode) {
                    Log.d("TAG", "skip existing so : " + entry.getName());
                    continue;
                }
            }
            FileOutputStream fos = new FileOutputStream(libFile);
            Log.d("TAG", "copy so " + entry.getName() + " of " + cpuArch);
            copySo(buffer, zipfile.getInputStream(entry), fos);
            setSoVersion(context, key, packageInfo.versionCode);
        }

        if (!findLib) {
            Log.d("TAG", "Fast skip all!");
            return true;
        }

        return findSo;
    }
    private static void copySo(byte[] buffer, InputStream input, OutputStream output) throws IOException {
        BufferedInputStream bufferedInput = new BufferedInputStream(input);
        BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);
        int count;

        while ((count = bufferedInput.read(buffer)) > 0) {
            bufferedOutput.write(buffer, 0, count);
        }
        bufferedOutput.flush();
        bufferedOutput.close();
        output.close();
        bufferedInput.close();
        input.close();
    }
    private static void setSoVersion(Context context, String name, int version) {
        SharedPreferences preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(name, version);
        editor.commit();
    }

    private static int getSoVersion(Context context, String name) {
        SharedPreferences preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return preferences.getInt(name, 0);
    }
}
