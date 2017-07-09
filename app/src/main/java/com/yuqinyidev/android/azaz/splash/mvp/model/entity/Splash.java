package com.yuqinyidev.android.azaz.splash.mvp.model.entity;

import android.text.TextUtils;

/**
 * Created by RDX64 on 2017/6/29.
 */

public class Splash {
    /*
    http://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1
     */
    private final String url;
    private final String copyright;

    public Splash(String url, String copyright) {
        this.url = url;
        this.copyright = copyright;
    }

    public String getCopyright() {
        return copyright;
    }

    public String getUrl() {
        if (TextUtils.isEmpty(url)) {
            return url;
        }
        return "http://cn.bing.com/".concat(url);
    }

    @Override
    public String toString() {
        return "Splash's copyright is: " + copyright + ", url is: " + url;
    }
}
