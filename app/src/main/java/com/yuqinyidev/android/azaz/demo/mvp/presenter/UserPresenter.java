package com.yuqinyidev.android.azaz.demo.mvp.presenter;

import android.app.Application;

import com.yuqinyidev.android.framework.base.DefaultAdapter;
import com.yuqinyidev.android.framework.di.scope.ActivityScope;
import com.yuqinyidev.android.framework.integration.AppManager;
import com.yuqinyidev.android.framework.mvp.presenter.BasePresenter;
import com.yuqinyidev.android.framework.utils.PermissionUtils;
import com.yuqinyidev.android.framework.utils.RxUtils;
import com.yuqinyidev.android.azaz.demo.mvp.contract.UserContract;
import com.yuqinyidev.android.azaz.demo.mvp.model.entity.User;
import com.yuqinyidev.android.azaz.demo.mvp.ui.adapter.UserAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

/**
 * Created by RDX64 on 2017/6/29.
 */
@ActivityScope
public class UserPresenter extends BasePresenter<UserContract.Model, UserContract.View> {
    private RxErrorHandler mErrorHandler;
    private AppManager mAppManager;
    private Application mApplication;
    private List<User> mUsers = new ArrayList<>();
    private DefaultAdapter mAdapter;
    private int lastUserId = 1;
    private boolean isFirst = true;
    private int preEndIndex;

    @Inject
    public UserPresenter(UserContract.Model model, UserContract.View rootView,
                         RxErrorHandler errorHandler, AppManager appManager, Application application) {
        super(model, rootView);
        this.mErrorHandler = errorHandler;
        this.mAppManager = appManager;
        this.mApplication = application;
    }

    public void requestUsers(final boolean pullToRefresh) {
        if (mAdapter == null) {
            mAdapter = new UserAdapter(mUsers);
            mRootView.setAdapter(mAdapter);
        }

        PermissionUtils.externalStorage(new PermissionUtils.RequestPermission() {
            @Override
            public void onRequestPermissionSuccess() {

            }

            @Override
            public void onRequestPermissionFailure() {
                mRootView.showMessage("Request permissions failure");
            }
        }, mRootView.getRxPermissions(), mErrorHandler);

        if (pullToRefresh) {
            lastUserId = 1;
        }

        boolean isEvictCache = pullToRefresh;

        if (pullToRefresh && isFirst) {
            isFirst = false;
            isEvictCache = false;
        }

        mModel.getUsers(lastUserId, isEvictCache)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))
                .doOnSubscribe(disposable -> {
                    if (pullToRefresh) {
                        mRootView.showLoading();
                    } else {
                        mRootView.startLoadMore();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(() -> {
                    if (pullToRefresh) {
                        mRootView.hideLoading();
                    } else {
                        mRootView.endLoadMore();
                    }
                })
                .compose(RxUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<List<User>>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull List<User> users) {
                        lastUserId = users.get(users.size() - 1).getId();
                        if (pullToRefresh) {
                            mUsers.clear();
                        }
                        preEndIndex = mUsers.size();
                        mUsers.addAll(users);
                        if (pullToRefresh) {
                            mAdapter.notifyDataSetChanged();
                        } else {
                            mAdapter.notifyItemRangeInserted(preEndIndex, users.size());
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mApplication = null;
        this.mAdapter = null;
        this.mUsers = null;
    }
}
