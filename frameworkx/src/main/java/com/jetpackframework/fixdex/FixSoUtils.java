package com.jetpackframework.fixdex;

import android.os.Build;

import com.jetpackframework.ContextUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FixSoUtils {
    public static void installSoABI(String soDir){
        for (String currentABI : Build.SUPPORTED_ABIS){
            ClassLoader classLoader = ContextUtil.get().getClassLoader();
            File file = new File(soDir,currentABI);
            if (!file.exists() && !file.isDirectory()){
                continue;
            }
            if (classLoader == null){
                break;
            }
            try {
                installSoABI(classLoader,file);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            break;
        }
    }

    private static void installSoABI(ClassLoader classLoader, File folder) throws Throwable {
        if ((Build.VERSION.SDK_INT == 25 && Build.VERSION.PREVIEW_SDK_INT != 0) || Build.VERSION.SDK_INT > 25){
            try {
                V25.install(classLoader, folder);
            } catch (Throwable throwable) {
                V23.install(classLoader, folder);
            }
        } else if (Build.VERSION.SDK_INT >= 23) {
            try {
                V23.install(classLoader, folder);
            } catch (Throwable throwable) {
                V14.install(classLoader, folder);
            }
        } else {
            V14.install(classLoader, folder);
        }
    }

    @Deprecated
    private static final class V4 {
        @Deprecated
        private static void install(ClassLoader classLoader, File folder) throws Throwable {
            String addPath = folder.getPath();
            Field pathField = ReflectUtil.findField(classLoader, "libPath");
            final String origLibPaths = (String) pathField.get(classLoader);
            final String[] origLibPathSplit = origLibPaths.split(":");
            final StringBuilder newLibPaths = new StringBuilder(addPath);

            for (String origLibPath : origLibPathSplit) {
                if (origLibPath == null || addPath.equals(origLibPath)) {
                    continue;
                }
                newLibPaths.append(':').append(origLibPath);
            }
            pathField.set(classLoader, newLibPaths.toString());

            final Field libraryPathElementsFiled = ReflectUtil.findField(classLoader, "libraryPathElements");
            final List<String> libraryPathElements = (List<String>) libraryPathElementsFiled.get(classLoader);
            final Iterator<String> libPathElementIt = libraryPathElements.iterator();
            while (libPathElementIt.hasNext()) {
                final String libPath = libPathElementIt.next();
                if (addPath.equals(libPath)) {
                    libPathElementIt.remove();
                    break;
                }
            }
            libraryPathElements.add(0, addPath);
            libraryPathElementsFiled.set(classLoader, libraryPathElements);
        }
    }

    private static final class V14 {
        private static void install(ClassLoader classLoader, File folder) throws Throwable {
            final Field pathListField = ReflectUtil.findField(classLoader, "pathList");
            final Object dexPathList = pathListField.get(classLoader);

            final Field nativeLibDirField = ReflectUtil.findField(dexPathList, "nativeLibraryDirectories");
            final File[] origNativeLibDirs = (File[]) nativeLibDirField.get(dexPathList);

            final List<File> newNativeLibDirList = new ArrayList<>(origNativeLibDirs.length + 1);
            newNativeLibDirList.add(folder);
            for (File origNativeLibDir : origNativeLibDirs) {
                if (!folder.equals(origNativeLibDir)) {
                    newNativeLibDirList.add(origNativeLibDir);
                }
            }
            nativeLibDirField.set(dexPathList, newNativeLibDirList.toArray(new File[0]));
        }
    }

    private static final class V23 {
        private static void install(ClassLoader classLoader, File folder) throws Throwable {
            final Field pathListField = ReflectUtil.findField(classLoader, "pathList");
            final Object dexPathList = pathListField.get(classLoader);

            final Field nativeLibraryDirectories = ReflectUtil.findField(dexPathList, "nativeLibraryDirectories");

            List<File> origLibDirs = (List<File>) nativeLibraryDirectories.get(dexPathList);
            if (origLibDirs == null) {
                origLibDirs = new ArrayList<>(2);
            }
            final Iterator<File> libDirIt = origLibDirs.iterator();
            while (libDirIt.hasNext()) {
                final File libDir = libDirIt.next();
                if (folder.equals(libDir)) {
                    libDirIt.remove();
                    break;
                }
            }
            origLibDirs.add(0, folder);

            final Field systemNativeLibraryDirectories = ReflectUtil.findField(dexPathList, "systemNativeLibraryDirectories");
            List<File> origSystemLibDirs = (List<File>) systemNativeLibraryDirectories.get(dexPathList);
            if (origSystemLibDirs == null) {
                origSystemLibDirs = new ArrayList<>(2);
            }

            final List<File> newLibDirs = new ArrayList<>(origLibDirs.size() + origSystemLibDirs.size() + 1);
            newLibDirs.addAll(origLibDirs);
            newLibDirs.addAll(origSystemLibDirs);

            final Method makeElements = ReflectUtil.findMethod(dexPathList,
                    "makePathElements", List.class, File.class, List.class);
            final ArrayList<IOException> suppressedExceptions = new ArrayList<>();

            final Object[] elements = (Object[]) makeElements.invoke(dexPathList, newLibDirs, null, suppressedExceptions);

            final Field nativeLibraryPathElements = ReflectUtil.findField(dexPathList, "nativeLibraryPathElements");
            nativeLibraryPathElements.set(dexPathList, elements);
        }
    }

    private static final class V25 {
        private static void install(ClassLoader classLoader, File folder)  throws Throwable {
            final Field pathListField = ReflectUtil.findField(classLoader, "pathList");
            final Object dexPathList = pathListField.get(classLoader);

            final Field nativeLibraryDirectories = ReflectUtil.findField(dexPathList, "nativeLibraryDirectories");

            List<File> origLibDirs = (List<File>) nativeLibraryDirectories.get(dexPathList);
            if (origLibDirs == null) {
                origLibDirs = new ArrayList<>(2);
            }
            final Iterator<File> libDirIt = origLibDirs.iterator();
            while (libDirIt.hasNext()) {
                final File libDir = libDirIt.next();
                if (folder.equals(libDir)) {
                    libDirIt.remove();
                    break;
                }
            }
            origLibDirs.add(0, folder);

            final Field systemNativeLibraryDirectories = ReflectUtil.findField(dexPathList, "systemNativeLibraryDirectories");
            List<File> origSystemLibDirs = (List<File>) systemNativeLibraryDirectories.get(dexPathList);
            if (origSystemLibDirs == null) {
                origSystemLibDirs = new ArrayList<>(2);
            }

            final List<File> newLibDirs = new ArrayList<>(origLibDirs.size() + origSystemLibDirs.size() + 1);
            newLibDirs.addAll(origLibDirs);
            newLibDirs.addAll(origSystemLibDirs);

            final Method makeElements = ReflectUtil.findMethod(dexPathList, "makePathElements", List.class);

            final Object[] elements = (Object[]) makeElements.invoke(dexPathList, newLibDirs);

            final Field nativeLibraryPathElements = ReflectUtil.findField(dexPathList, "nativeLibraryPathElements");
            nativeLibraryPathElements.set(dexPathList, elements);
        }
    }

}
