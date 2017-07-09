package com.yuqinyidev.android.azaz.splash.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yuqinyidev.android.azaz.R;
import com.yuqinyidev.android.azaz.main.mvp.ui.activity.MainMenuActivity;
import com.yuqinyidev.android.azaz.splash.di.component.DaggerSplashComponent;
import com.yuqinyidev.android.azaz.splash.di.module.SplashModule;
import com.yuqinyidev.android.azaz.splash.mvp.contract.SplashContract;
import com.yuqinyidev.android.azaz.splash.mvp.model.entity.Splash;
import com.yuqinyidev.android.azaz.splash.mvp.presenter.SplashPresenter;
import com.yuqinyidev.android.framework.base.App;
import com.yuqinyidev.android.framework.base.BaseActivity;
import com.yuqinyidev.android.framework.di.component.AppComponent;
import com.yuqinyidev.android.framework.utils.DataHelper;
import com.yuqinyidev.android.framework.utils.DateUtil;
import com.yuqinyidev.android.framework.utils.UiUtils;
import com.yuqinyidev.android.framework.widget.CountDownProgressView;
import com.yuqinyidev.android.framework.widget.imageloader.ImageLoader;
import com.yuqinyidev.android.framework.widget.imageloader.glide.GlideImageConfig;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

import static com.yuqinyidev.android.framework.widget.CountDownProgressView.ProgressType.COUNT_BACK;

/**
 * Created by RDX64 on 2017/6/29.
 */

public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashContract.View {
    private static final String SP_KEY_SPLASH_BG_PATH = "sp_key_splash_bg_path";
    private static final String SP_KEY_SPLASH_BG_REFRESH_DATE = "sp_key_splash_bg_refresh_date";

    private RxPermissions mRxPermissions;
    private AppComponent mAppComponent;
    private ImageLoader mImageLoader;

    @BindView(R.id.imv_splash)
    ImageView mImvSplash;
    @BindView(R.id.txv_jump)
    CountDownProgressView txvJump;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        this.mRxPermissions = new RxPermissions(this);
        DaggerSplashComponent
                .builder()
                .appComponent(appComponent)
                .splashModule(new SplashModule(this))
                .build()
                .inject(this);
        UiUtils.fullScreen(SplashActivity.this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.act_splash;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        this.mAppComponent = ((App) this.getApplicationContext()).getAppComponent();
        this.mImageLoader = mAppComponent.imageLoader();

        String picUrl = DataHelper.getStringSP(this, SP_KEY_SPLASH_BG_PATH);
        int picRefreshDate = DataHelper.getIntegerSP(this, SP_KEY_SPLASH_BG_REFRESH_DATE);
        if (picUrl != null && picRefreshDate >= DateUtil.date2Int()) {
            displaySplash(picUrl);
        } else {
            mPresenter.requestSplashs();
        }
        txvJump.setProgressType(COUNT_BACK);
        txvJump.setOnProgressListener(new CountDownProgressView.OnProgressListener() {
            @Override
            public void onProgress(int progress) {
                switch (txvJump.getProgressType()) {
                    case COUNT:
                        if (progress == 100) {
                            startMainMenuActivity();
                        }
                        break;
                    case COUNT_BACK:
                        if (progress == 0) {
                            startMainMenuActivity();
                        }
                        break;
                }
            }
        });
    }

    @Override
    public boolean useFragment() {
        return false;
    }

    @Override
    public void showLoading() {
        Timber.tag(TAG).d("showLoading");
    }

    @Override
    public void hideLoading() {
        Timber.tag(TAG).d("hideLoading");
    }

    @Override
    public void showMessage(String message) {
        UiUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(Intent intent) {
        UiUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }

    @Override
    public void setSplashBackground(Splash splash) {
        DataHelper.putStringSP(this, SP_KEY_SPLASH_BG_PATH, splash.getUrl());
        DataHelper.putIntegerSP(this, SP_KEY_SPLASH_BG_REFRESH_DATE, DateUtil.date2Int());
        displaySplash(splash.getUrl());
    }

    @OnClick(R.id.txv_jump)
    public void jumpSplash() {
        txvJump.stop();
        startMainMenuActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mImageLoader.clear(mAppComponent.application(),
                GlideImageConfig.builder().imageView(mImvSplash).build());
        this.mRxPermissions = null;
        this.mAppComponent = null;
        this.mImageLoader = null;
    }

    private void startMainMenuActivity() {
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        finish();
    }

    private void displaySplash(String picUrl) {
        mImageLoader.loadImage(
                mAppComponent.appManager().getCurrentActivity() == null ?
                        mAppComponent.application() : mAppComponent.appManager().getCurrentActivity(),
                GlideImageConfig.builder().url(picUrl).imageView(mImvSplash).build()
        );
        txvJump.start();
    }
}
