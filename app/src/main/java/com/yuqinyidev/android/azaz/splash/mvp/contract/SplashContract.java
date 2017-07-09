package com.yuqinyidev.android.azaz.splash.mvp.contract;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yuqinyidev.android.azaz.splash.mvp.model.entity.Images;
import com.yuqinyidev.android.azaz.splash.mvp.model.entity.Splash;
import com.yuqinyidev.android.framework.mvp.model.IModel;
import com.yuqinyidev.android.framework.mvp.view.IView;

import io.reactivex.Observable;

/**
 * Created by RDX64 on 2017/6/29.
 */

public interface SplashContract {
    interface View extends IView {
        RxPermissions getRxPermissions();

        void setSplashBackground(Splash splash);
    }

    interface Model extends IModel {
        Observable<Images> getSplashs(String format, int idx, int n);
    }
}
