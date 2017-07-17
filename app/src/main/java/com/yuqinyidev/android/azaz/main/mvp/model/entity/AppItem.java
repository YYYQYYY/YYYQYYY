package com.yuqinyidev.android.azaz.main.mvp.model.entity;

/**
 * Created by RDX64 on 2017/7/2.
 */

public class AppItem {
    private String name;
    private int imageId;
    private Class<?> clazz;

    public AppItem(String name, int imageId, Class<?> clazz) {
        this.name = name;
        this.imageId = imageId;
        this.clazz = clazz;
    }

    public String getAppItemName() {
        return name;
    }

    public int getAppItemImageId() {
        return imageId;
    }

    public Class<?> getAppItemClazz() {
        return clazz;
    }
}
