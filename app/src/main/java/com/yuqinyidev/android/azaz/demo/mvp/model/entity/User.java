package com.yuqinyidev.android.azaz.demo.mvp.model.entity;

import android.text.TextUtils;

/**
 * Created by RDX64 on 2017/6/29.
 */

public class User {
    private final int id;
    private final String login;
    private final String avatar_url;

    public User(int id, String login, String avatarUrl) {
        this.id = id;
        this.login = login;
        this.avatar_url = avatarUrl;
    }

    public int getId() {
        return id;
    }

    public String getlogin() {
        return login;
    }

    public String getAvatarUrl() {
        if (TextUtils.isEmpty(avatar_url)) {
            return avatar_url;
        }
        return avatar_url.split("\\?")[0];
    }

    @Override
    public String toString() {
        return "User's id is: " + id + ", login is: " + login;
    }
}
