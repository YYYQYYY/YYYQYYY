package com.yuqinyidev.android.azaz.kanbook.mvp.model.entity;

public class KBContent {

    private long index;
    private long length;

    public KBContent(long _index, long _length) {
        this.index = _index;
        this.length = _length;
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long _index) {
        this.index = _index;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long _length) {
        this.length = _length;
    }

}
