package com.yuqinyidev.android.azaz.kanbook.mvp.ui.utils;

/**
 * Created by X311 on 2015/8/9.
 */
public class KBFileInfo {
    private String fileName;
    private String filePath;
    private long lastModified;

    public KBFileInfo() {
    }

    public KBFileInfo(String _fileName, String _filePath, long _lastModified) {
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
