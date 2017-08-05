package com.yuqinyidev.android.azaz.main.mvp.ui.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
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
import jp.wasabeef.glide.transformations.BlurTransformation;
import timber.log.Timber;

/**
 * 主界面
 * Created by RDX64 on 2017/6/22.
 */

public class FragmentMainHome extends BaseFragment<MainMenuPresenter> implements MainMenuContract.View {
    private RxPermissions mRxPermissions;
    private CollapsingToolbarLayoutState mState;

    @BindView(R.id.fruit_image_view)
    ImageView mFruitImageView;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.appbar_layout)
    AppBarLayout mAppBarLayout;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_home, container, false);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        this.mRxPermissions = new RxPermissions(getActivity());
        DaggerMainMenuComponent
                .builder()
                .appComponent(((App) getActivity().getApplicationContext()).getAppComponent())
                .mainMenuModule(new MainMenuModule(this))
                .build()
                .inject(this);
        mPresenter.requestAppItems(true);

        mAppBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
//                if (verticalOffset <= -mFruitImageView.getHeight() / 2) {
//                    mCollapsingToolbar.setTitle("安卓安卓应用集");
//                    mCollapsingToolbar.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
//                    mCollapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.colorAccent));
//                } else {
//                    mCollapsingToolbar.setTitle("");
//                }

            if (verticalOffset == 0) {
                if (mState != CollapsingToolbarLayoutState.EXPANDED) {
                    mState = CollapsingToolbarLayoutState.EXPANDED;
                    mCollapsingToolbar.setTitle("安卓安卓应用集");
                }
            } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                if (mState != CollapsingToolbarLayoutState.COLLAPSED) {
                    mCollapsingToolbar.setTitle("安卓安卓");
                    mState = CollapsingToolbarLayoutState.COLLAPSED;
                }
            } else {
                if (mState != CollapsingToolbarLayoutState.INTERNEDIATE) {
//                        if(mState == CollapsingToolbarLayoutState.COLLAPSED){
//                        }
                    mCollapsingToolbar.setTitle("安卓应用集");
                    mState = CollapsingToolbarLayoutState.INTERNEDIATE;
                }
            }
        });
        Glide.with(this).load(R.drawable.bg).bitmapTransform(new BlurTransformation(getActivity(), 100))
                .into(new SimpleTarget<GlideDrawable>() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super
                            GlideDrawable> glideAnimation) {
                        mFruitImageView.setImageDrawable(resource);
                        mCollapsingToolbar.setBackground(resource);
                    }
                });

        Glide.with(this).load(R.drawable.bg).bitmapTransform(new BlurTransformation(getActivity(), 100))
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super
                            GlideDrawable> glideAnimation) {
                        mCollapsingToolbar.setContentScrim(resource);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_main_menu_home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String msg = "";
        switch (item.getItemId()) {
            case R.id.webview:
                msg += "博客跳转";
                break;
            case R.id.weibo:
                msg += "微博跳转";
                break;
            case R.id.action_settings:
                msg += "设置";
                break;
        }
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
