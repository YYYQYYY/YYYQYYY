package com.yuqinyidev.android.azaz.kanbook.mvp.model.entity;

public class KBBook {

    private int mBookId;
    private String mBookName;
    private String mBookPath;

    public KBBook() {
    }

    public int getBookId() {
        return mBookId;
    }

    public void setBookId(int _id) {
        this.mBookId = _id;
    }

    public String getBookName() {
        return mBookName;
    }

    public void setBookName(String _bookName) {
        this.mBookName = _bookName;
    }

    public String getBookPath() {
        return mBookPath;
    }

    public void setBookPath(String _bookPath) {
        this.mBookPath = _bookPath;
    }

}
