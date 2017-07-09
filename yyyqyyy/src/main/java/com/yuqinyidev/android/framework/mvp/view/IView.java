package com.yuqinyidev.android.framework.mvp.view;

import android.content.Intent;

/**
 * Created by RDX64 on 2017/6/28.
 */

public interface IView {
    /**
     * 显示加载
     */
    void showLoading();

    /**
     * 隐藏加载
     */
    void hideLoading();

    /**
     * 显示信息
     *
     * @param message 信息
     */
    void showMessage(String message);

    /**
     * 跳转Activity
     *
     * @param intent Intent
     */
    void launchActivity(Intent intent);

    /**
     * 杀死自己
     */
    void killMyself();
}
