package com.yuqinyidev.android.azaz.main.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yuqinyidev.android.azaz.R;
import com.yuqinyidev.android.azaz.main.di.component.DaggerMainMenuComponent;
import com.yuqinyidev.android.azaz.main.di.module.MainMenuModule;
import com.yuqinyidev.android.azaz.main.mvp.contract.MainMenuContract;
import com.yuqinyidev.android.azaz.main.mvp.presenter.MainMenuPresenter;
import com.yuqinyidev.android.framework.base.App;
import com.yuqinyidev.android.framework.base.BaseFragment;
import com.yuqinyidev.android.framework.base.DefaultAdapter;
import com.yuqinyidev.android.framework.di.component.AppComponent;
import com.yuqinyidev.android.framework.utils.UiUtils;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by RDX64 on 2017/6/22.
 */

public class FragmentMainHome extends BaseFragment<MainMenuPresenter> implements MainMenuContract.View {
    private RxPermissions mRxPermissions;

    private CollapsingToolbarLayoutState state;

    @BindView(R.id.fruit_image_view)
    ImageView mFruitImageView;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.appbar_layout)
    AppBarLayout appBarLayout;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;

    @OnClick

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_home, container, false);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        this.mRxPermissions = new RxPermissions(getActivity());
        DaggerMainMenuComponent
                .builder()
                .appComponent(((App) getActivity().getApplicationContext()).getAppComponent())
                .mainMenuModule(new MainMenuModule(this))
                .build()
                .inject(this);
        mPresenter.requestAppItems(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset <= -mFruitImageView.getHeight() / 2) {
                    mCollapsingToolbar.setTitle("安卓安卓应用集");
                    mCollapsingToolbar.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
                    mCollapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.colorAccent));
                } else {
                    mCollapsingToolbar.setTitle("");
                }
            }
        });
    }

    @Override
    public void setData(Object data) {
        Timber.tag(TAG).w("setData");
    }

    @Override
    public void showLoading() {
        Timber.tag(TAG).w("showLoading");
    }

    @Override
    public void hideLoading() {
        Timber.tag(TAG).w("hideLoading");
    }

    @Override
    public void showMessage(String message) {
        UiUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(Intent intent) {
        Timber.tag(TAG).w("launchActivity");
    }

    @Override
    public void killMyself() {
        Timber.tag(TAG).w("killMyself");
        getActivity().finish();
    }

    @Override
    public void setAdapter(DefaultAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
        initRecycleView();
    }

    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }

    private void initRecycleView() {
        UiUtils.configRecycleView(mRecyclerView, new GridLayoutManager(getActivity(), 3));
    }

    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        INTERNEDIATE
    }

}
