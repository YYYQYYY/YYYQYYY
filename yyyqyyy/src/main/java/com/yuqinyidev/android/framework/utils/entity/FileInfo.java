package com.yuqinyidev.android.framework.utils.entity;

/**
 * Created by X311 on 2015/8/9.
 */
public class FileInfo {
    private String fileName;
    private String filePath;
    private long lastModified;

    public FileInfo() {
    }

    public FileInfo(String _fileName, String _filePath, long _lastModified) {
        fileName = _fileName;
        filePath = _filePath;
        lastModified = _lastModified;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }
}
