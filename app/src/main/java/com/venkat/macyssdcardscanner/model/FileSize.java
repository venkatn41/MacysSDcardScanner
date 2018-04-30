package com.venkat.macyssdcardscanner.model;

import android.support.annotation.NonNull;

/**
 * Created by venkat on 4/29/18.
 */

public class FileSize implements Comparable<FileSize> {
    String fileName;
    Float fileSize;

    public void setFileSize(String name, Float size) {
        fileName = name;
        fileSize = size;
    }


    public String getFileName() {
        return fileName;
    }

    public Float getFileSize() {
        return fileSize;
    }


    @Override
    public int compareTo(@NonNull FileSize size) {
        if (size.fileSize > fileSize)
            return 1;
        else if (size.fileSize < fileSize)
            return -1;
        else
            return 0;
    }
}
