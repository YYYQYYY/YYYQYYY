package com.yuqinyidev.android.azaz.demo.mvp.contract;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yuqinyidev.android.azaz.demo.mvp.model.entity.User;
import com.yuqinyidev.android.framework.base.DefaultAdapter;
import com.yuqinyidev.android.framework.mvp.model.IModel;
import com.yuqinyidev.android.framework.mvp.view.IView;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by RDX64 on 2017/6/29.
 */

public interface UserContract {
    interface View extends IView {
        void setAdapter(DefaultAdapter adapter);

        void startLoadMore();

        void endLoadMore();

        RxPermissions getRxPermissions();
    }

    interface Model extends IModel {
        Observable<List<User>> getUsers(int lastIdQuered, boolean update);
    }
}
