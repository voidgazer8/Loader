package com.example.loader.custom;

import android.content.Context;
import android.os.Environment;

import androidx.core.content.ContextCompat;
import androidx.core.os.EnvironmentCompat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Storages {

    private List<String> rezult=new ArrayList<>();

    public Storages(final Context context, final boolean includePrimaryExternalStorage) {
        final File[] externalCacheDirs= ContextCompat.getExternalCacheDirs(context);
        if(externalCacheDirs.length == 0)
            rezult=null;
        if(externalCacheDirs.length==1) {
            if(externalCacheDirs[0]==null)
                rezult=null;
            assert externalCacheDirs[0] != null;
            final String storageState= EnvironmentCompat.getStorageState(externalCacheDirs[0]);
            if(!Environment.MEDIA_MOUNTED.equals(storageState))
                rezult=null;
        }
        if(includePrimaryExternalStorage||externalCacheDirs.length==1) {
            assert rezult != null;
            rezult.add(getRootOfInnerSdCardFolder(externalCacheDirs[0]));
        }
        for(int i=1;i<externalCacheDirs.length;++i) {
            final File file=externalCacheDirs[i];
            if(file==null) continue;
            final String storageState=EnvironmentCompat.getStorageState(file);
            if(Environment.MEDIA_MOUNTED.equals(storageState))
                rezult.add(getRootOfInnerSdCardFolder(externalCacheDirs[i]));
        }
    }
    private static String getRootOfInnerSdCardFolder(File file) {
        if(file==null) return null;
        final long totalSpace=file.getTotalSpace();
        while(true) {
            final File parentFile=file.getParentFile();
            if(parentFile==null||parentFile.getTotalSpace()!=totalSpace||!parentFile.canRead())
                return file.getAbsolutePath();
            file=parentFile;
        }
    }
    public List<String> getPaths() {
        return rezult;
    }
}

