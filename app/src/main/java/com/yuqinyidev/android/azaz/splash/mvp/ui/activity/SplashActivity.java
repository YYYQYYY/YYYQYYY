package com.yuqinyidev.android.azaz.splash.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
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
import com.yuqinyidev.android.framework.utils.DateUtils;
import com.yuqinyidev.android.framework.utils.FileUtils;
import com.yuqinyidev.android.framework.utils.NetworkUtils;
import com.yuqinyidev.android.framework.utils.UiUtils;
import com.yuqinyidev.android.framework.widget.CountDownProgressView;
import com.yuqinyidev.android.framework.widget.imageloader.ImageLoader;

import java.io.File;
import java.io.FileOutputStream;

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
    private static final String SPLASH_BG_NAME = "splash_bg.jpg";

    private RxPermissions mRxPermissions;
    private AppComponent mAppComponent;
    private ImageLoader mImageLoader;
    private String mSavePath;

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
        this.mSavePath = FileUtils.packageName2CachePath(getPackageName());

        String picUrl = DataHelper.getStringSP(this, SP_KEY_SPLASH_BG_PATH);
        int picRefreshDate = DataHelper.getIntegerSP(this, SP_KEY_SPLASH_BG_REFRESH_DATE);
        if (NetworkUtils.isConnected(SplashActivity.this)) {
            if (picUrl != null && picRefreshDate >= DateUtils.date2Int()) {
                displaySplash(picUrl);
            } else {
                mPresenter.requestSplashes();
            }
        } else {
            displayDefaultSplash();
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
        DataHelper.putIntegerSP(this, SP_KEY_SPLASH_BG_REFRESH_DATE, DateUtils.date2Int());
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
//        mImageLoader.clear(mAppComponent.application(),
//                GlideImageConfig.builder().imageView(mImvSplash).build());
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
        new getImageCacheAsyncTask(this).execute(picUrl);

//        mImageLoader.loadImage(
//                mAppComponent.appManager().getCurrentActivity() == null ?
//                        mAppComponent.application() : mAppComponent.appManager().getCurrentActivity(),
//                GlideImageConfig.builder().url(picUrl).imageView(mImvSplash).build()
//        );
        txvJump.start();
    }

    private void displayDefaultSplash() {
//        SPLASH_BG_NAME
        File f = new File(mSavePath, SPLASH_BG_NAME);
        if (f.exists()) {
            Glide.with(SplashActivity.this)
                    .load(f)
                    .into(mImvSplash);
        } else {
            Glide.with(SplashActivity.this)
                    .load(R.drawable.applegray)
                    .into(mImvSplash);
        }
        txvJump.start();
    }

    private class getImageCacheAsyncTask extends AsyncTask<String, Void, File> {
        private final Context context;

        public getImageCacheAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected File doInBackground(String... params) {
            String imgUrl = params[0];
            try {
                return Glide.with(context)
                        .load(imgUrl)
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();
            } catch (Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(File result) {
            if (result == null) {
                return;
            }
            //此path就是对应文件的缓存路径
            String path = result.getPath();
            Log.e("path", path);
            Bitmap bmp = BitmapFactory.decodeFile(path);
            if (!TextUtils.isEmpty(mSavePath)) {
                FileOutputStream fos = null;
                try {
                    File p = new File(mSavePath);
                    if (!p.exists()) {
                        p.mkdirs();
                    }
                    File f = new File(mSavePath, SPLASH_BG_NAME);
                    fos = new FileOutputStream(f);
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            mImvSplash.setImageBitmap(bmp);
        }
    }
}
