package com.jetpackframework.fixdex;

import android.os.Build;

import com.jetpackframework.ContextUtil;
import com.jetpackframework.Reflector;

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
            final String origLibPaths = Reflector.with(classLoader).field("libPath").get();
            final String[] origLibPathSplit = origLibPaths.split(":");
            final StringBuilder newLibPaths = new StringBuilder(addPath);

            for (String origLibPath : origLibPathSplit) {
                if (origLibPath == null || addPath.equals(origLibPath)) {
                    continue;
                }
                newLibPaths.append(':').append(origLibPath);
            }
            Reflector.with(classLoader).field("libPath").set(newLibPaths.toString());

            final Reflector libraryPathElementsFiled = Reflector.with(classLoader).field("libraryPathElements");
            final List<String> libraryPathElements = libraryPathElementsFiled.get();
            final Iterator<String> libPathElementIt = libraryPathElements.iterator();
            while (libPathElementIt.hasNext()) {
                final String libPath = libPathElementIt.next();
                if (addPath.equals(libPath)) {
                    libPathElementIt.remove();
                    break;
                }
            }
            libraryPathElements.add(0, addPath);
            libraryPathElementsFiled.set(libraryPathElements);
        }
    }

    private static final class V14 {
        private static void install(ClassLoader classLoader, File folder) throws Throwable {
            final Object dexPathList = Reflector.with(classLoader).field("pathList").get();

            final Reflector nativeLibDirField = Reflector.with(dexPathList).field("nativeLibraryDirectories");
            final File[] origNativeLibDirs = nativeLibDirField.get();

            final List<File> newNativeLibDirList = new ArrayList<>(origNativeLibDirs.length + 1);
            newNativeLibDirList.add(folder);
            for (File origNativeLibDir : origNativeLibDirs) {
                if (!folder.equals(origNativeLibDir)) {
                    newNativeLibDirList.add(origNativeLibDir);
                }
            }
            nativeLibDirField.set(newNativeLibDirList.toArray(new File[0]));
        }
    }

    private static final class V23 {
        private static void install(ClassLoader classLoader, File folder) throws Throwable {
            final Object dexPathList = Reflector.with(classLoader).field("pathList").get();
            List<File> origLibDirs = Reflector.with(dexPathList).field("nativeLibraryDirectories").get();
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
            List<File> origSystemLibDirs = Reflector.with(dexPathList).field("systemNativeLibraryDirectories").get();
            if (origSystemLibDirs == null) {
                origSystemLibDirs = new ArrayList<>(2);
            }

            final List<File> newLibDirs = new ArrayList<>(origLibDirs.size() + origSystemLibDirs.size() + 1);
            newLibDirs.addAll(origLibDirs);
            newLibDirs.addAll(origSystemLibDirs);

            final ArrayList<IOException> suppressedExceptions = new ArrayList<>();

            final Object[] elements = Reflector.with(dexPathList).method("makePathElements", List.class, File.class, List.class).call(newLibDirs, null, suppressedExceptions);

            Reflector.with(dexPathList).field("nativeLibraryPathElements").set(elements);
        }
    }

    private static final class V25 {
        private static void install(ClassLoader classLoader, File folder)  throws Throwable {
            final Object dexPathList = Reflector.with(classLoader).field("pathList").get();


            List<File> origLibDirs = Reflector.with(dexPathList).field("nativeLibraryDirectories").get();
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

            List<File> origSystemLibDirs = Reflector.with(dexPathList).field("systemNativeLibraryDirectories").get();
            if (origSystemLibDirs == null) {
                origSystemLibDirs = new ArrayList<>(2);
            }

            final List<File> newLibDirs = new ArrayList<>(origLibDirs.size() + origSystemLibDirs.size() + 1);
            newLibDirs.addAll(origLibDirs);
            newLibDirs.addAll(origSystemLibDirs);


            final Object[] elements = Reflector.with(dexPathList).method("makePathElements",List.class).call(newLibDirs);

            Reflector.with(dexPathList).field("nativeLibraryPathElements").set(elements);
        }
    }

}
