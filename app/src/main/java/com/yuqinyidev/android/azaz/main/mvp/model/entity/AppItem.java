package com.yuqinyidev.android.azaz.main.mvp.model.entity;

/**
 * Created by RDX64 on 2017/7/2.
 */

public class AppItem {
    private String name;
    private int imageId;

    public AppItem(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getAppItemName() {
        return name;
    }

    public int getAppItemImageId() {
        return imageId;
    }
}
