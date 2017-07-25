package com.yuqinyidev.android.azaz.main.mvp.presenter;

import android.app.Application;
import android.content.Intent;
import android.os.Parcelable;
import android.view.View;
import android.widget.Toast;

import com.yuqinyidev.android.azaz.R;
import com.yuqinyidev.android.azaz.main.mvp.contract.MainMenuContract;
import com.yuqinyidev.android.azaz.main.mvp.model.entity.AppItem;
import com.yuqinyidev.android.azaz.main.mvp.ui.adapter.MainMenuAdapter;
import com.yuqinyidev.android.framework.base.DefaultAdapter;
import com.yuqinyidev.android.framework.integration.AppManager;
import com.yuqinyidev.android.framework.mvp.presenter.BasePresenter;
import com.yuqinyidev.android.framework.utils.PermissionUtils;
import com.yuqinyidev.android.framework.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

/**
 * Created by RDX64 on 2017/7/2.
 */

public class MainMenuPresenter extends BasePresenter<MainMenuContract.Model, MainMenuContract.View> {
    public static final String ACTION_ADD_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
    public static final String ACTION_REMOVE_SHORTCUT = "com.android.launcher.action.UNINSTALL_SHORTCUT";
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
//                Toast.makeText(view.getContext(), "You clicked view: " + appItem.getAppItemName(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(mApplication, appItem.getAppItemClazz());
                UiUtils.startActivity(intent);
            }
        });
        mAdapter.setOnItemLongClickListener(new DefaultAdapter.OnRecyclerViewItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int viewType, Object data, int position) {
                AppItem appItem = mAppItems.get(position);
                Toast.makeText(view.getContext(), "You onItemLongClicked view: " + appItem.getAppItemName(), Toast.LENGTH_SHORT).show();

                //创建一个添加快捷方式的Intent
                Intent addSC = new Intent(ACTION_ADD_SHORTCUT);
                //快捷键的标题
                String title = appItem.getAppItemName();
                //快捷键的图标
                Parcelable icon = Intent.ShortcutIconResource.fromContext(mApplication, R.mipmap.ic_launcher);
                //创建单击快捷键启动本程序的Intent
                Intent launcherIntent = new Intent(mApplication, appItem.getAppItemClazz());
                //设置快捷键的标题
                addSC.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
                //设置快捷键的图标
                addSC.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
                //设置单击此快捷键启动的程序
                addSC.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launcherIntent);
                //向系统发送添加快捷键的广播
                mApplication.sendBroadcast(addSC);
            }
        });

        PermissionUtils.externalStorage(new PermissionUtils.RequestPermission() {
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
