package com.yuqinyidev.android.azaz.main.mvp.presenter;

import android.app.Application;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.yuqinyidev.android.azaz.demo.mvp.model.entity.User;
import com.yuqinyidev.android.azaz.demo.mvp.ui.adapter.UserAdapter;
import com.yuqinyidev.android.azaz.main.mvp.contract.MainMenuContract;
import com.yuqinyidev.android.azaz.main.mvp.model.entity.AppItem;
import com.yuqinyidev.android.azaz.main.mvp.ui.adapter.MainMenuAdapter;
import com.yuqinyidev.android.azaz.trainingdiary.mvp.ui.activity.TrainingDiaryActivity;
import com.yuqinyidev.android.framework.base.DefaultAdapter;
import com.yuqinyidev.android.framework.integration.AppManager;
import com.yuqinyidev.android.framework.mvp.presenter.BasePresenter;
import com.yuqinyidev.android.framework.utils.PermissionUtil;
import com.yuqinyidev.android.framework.utils.RxUtils;
import com.yuqinyidev.android.framework.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import static com.yuqinyidev.android.framework.utils.UiUtils.startActivity;

/**
 * Created by RDX64 on 2017/7/2.
 */

public class MainMenuPresenter extends BasePresenter<MainMenuContract.Model, MainMenuContract.View> {
    private RxErrorHandler mErrorHandler;
    private AppManager mAppManager;
    private Application mApplication;
    private List<AppItem> mAppItems = new ArrayList<>();
    private DefaultAdapter mAdapter;

    @Inject
    public MainMenuPresenter(MainMenuContract.Model model, MainMenuContract.View rootView,
                             RxErrorHandler errorHandler, AppManager appManager, Application application) {
        super(model, rootView);
        this.mErrorHandler = errorHandler;
        this.mAppManager = appManager;
        this.mApplication = application;
    }

    public void requestAppItems(final boolean pullToRefresh) {
        mAppItems = mModel.getAppItems();
        if (mAdapter == null) {
            mAdapter = new MainMenuAdapter(mAppItems);
            mRootView.setAdapter(mAdapter);
        }
        mAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                AppItem appItem = mAppItems.get(position);
                Toast.makeText(view.getContext(), "You clicked view: " + appItem.getAppItemName(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(mApplication, TrainingDiaryActivity.class);
                UiUtils.startActivity(intent);
            }
        });

        PermissionUtil.externalStorage(new PermissionUtil.RequestPermission() {
            @Override
            public void onRequestPermissionSuccess() {

            }

            @Override
            public void onRequestPermissionFailure() {
                mRootView.showMessage("Request permissions failure");
            }
        }, mRootView.getRxPermissions(), mErrorHandler);

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mApplication = null;
        this.mAdapter = null;
        this.mAppItems = null;
    }
}
