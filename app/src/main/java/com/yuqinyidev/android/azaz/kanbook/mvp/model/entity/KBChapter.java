package com.yuqinyidev.android.azaz.kanbook.mvp.model.entity;

public class KBChapter {

    private String title;
    private String content;

    public KBChapter(String _title, String _content) {
        this.title = _title;
        this.content = _content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String _title) {
        this.title = _title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String _content) {
        this.content = _content;
    }

}
