package com.venkat.macyssdcardscanner.model;

import android.support.annotation.NonNull;

/**
 * Created by venkat on 4/28/18.
 */

public class FileExt implements Comparable<FileExt> {
    String fileName;
    Integer fileSize;

    public void setFileSize(String name, Integer size) {
        fileName = name;
        fileSize = size;
    }


    public String getFileName() {
        return fileName;
    }

    public Integer getFileSize() {
        return fileSize;
    }


    @Override
    public int compareTo(@NonNull FileExt o) {
        return o.fileSize - fileSize;
    }
}
