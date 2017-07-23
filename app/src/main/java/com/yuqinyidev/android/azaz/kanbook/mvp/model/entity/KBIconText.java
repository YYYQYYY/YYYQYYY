package com.yuqinyidev.android.azaz.kanbook.mvp.model.entity;

import android.graphics.drawable.Drawable;

import com.yuqinyidev.android.framework.utils.entity.FileInfo;

public class KBIconText implements Comparable<KBIconText> {

    private String mText = "";
    private String mFilePath = "";
    private Long mLastModified = 0L;
    private Drawable mIcon;
    private boolean mSelectable = true;
    private int mType;

    public KBIconText(FileInfo _fileInfo, Drawable _bullet, int _type) {
        mText = _fileInfo.getFileName().substring(0, _fileInfo.getFileName().lastIndexOf("."));
        mFilePath = _fileInfo.getFilePath();
        mLastModified = _fileInfo.getLastModified();
        mType = _type;
        mIcon = _bullet;
    }

    public int getType() {
        return mType;
    }

    public boolean isSelectable() {
        return mSelectable;
    }

    public void setSelectable(boolean _selectable) {
        mSelectable = _selectable;
    }

    public String getText() {
        return mText;
    }

    public void setText(String _text) {
        mText = _text;
    }

    public String getFilePath() {
        return mFilePath;
    }

    public void setFilePath(String _filePath) {
        mFilePath = _filePath;
    }

    public Long getLastModified() {
        return mLastModified;
    }

    public void setLastModified(long _lastModified) {
        mLastModified = _lastModified;
    }

    public void setIcon(Drawable _icon) {
        mIcon = _icon;
    }

    public Drawable getIcon() {
        return mIcon;
    }

    public int compareTo(KBIconText _other) {
        if (this.mLastModified != null)
            return _other.getLastModified().compareTo(this.mLastModified);
        else
            throw new IllegalArgumentException();
    }

}
