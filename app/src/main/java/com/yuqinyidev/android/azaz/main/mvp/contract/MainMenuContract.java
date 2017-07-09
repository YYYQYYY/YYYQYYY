package com.yuqinyidev.android.azaz.main.mvp.contract;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yuqinyidev.android.azaz.main.mvp.model.entity.AppItem;
import com.yuqinyidev.android.framework.base.DefaultAdapter;
import com.yuqinyidev.android.framework.mvp.model.IModel;
import com.yuqinyidev.android.framework.mvp.view.IView;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by RDX64 on 2017/7/2.
 */

public interface MainMenuContract {
    interface View extends IView {
        void setAdapter(DefaultAdapter adapter);

        RxPermissions getRxPermissions();
    }

    interface Model extends IModel {
        List<AppItem> getAppItems();
    }
}
