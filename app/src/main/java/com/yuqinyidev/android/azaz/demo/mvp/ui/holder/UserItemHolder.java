package com.yuqinyidev.android.azaz.demo.mvp.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuqinyidev.android.framework.base.App;
import com.yuqinyidev.android.framework.base.BaseHolder;
import com.yuqinyidev.android.framework.di.component.AppComponent;
import com.yuqinyidev.android.framework.widget.imageloader.ImageLoader;
import com.yuqinyidev.android.framework.widget.imageloader.glide.GlideImageConfig;
import com.yuqinyidev.android.azaz.R;
import com.yuqinyidev.android.azaz.demo.mvp.model.entity.User;

import butterknife.BindView;
import io.reactivex.Observable;

/**
 * Created by RDX64 on 2017/6/29.
 */

public class UserItemHolder extends BaseHolder<User> {
    private AppComponent mAppComponent;
    private ImageLoader mImageLoader;

    @BindView(R.id.imv_avator)
    ImageView mImvAvator;
    @BindView(R.id.txv_name)
    TextView mTxvName;

    public UserItemHolder(View itemView) {
        super(itemView);
        mAppComponent = ((App) itemView.getContext().getApplicationContext()).getAppComponent();
        mImageLoader = mAppComponent.imageLoader();
    }

    @Override
    public void setData(User data, int position) {
        Observable.just(data.getlogin()).subscribe(s -> mTxvName.setText(s));

        mImageLoader.loadImage(
                mAppComponent.appManager().getCurrentActivity() == null ?
                        mAppComponent.application() : mAppComponent.appManager().getCurrentActivity(),
                GlideImageConfig.builder().url(data.getAvatarUrl()).imageView(mImvAvator).build()
        );
    }

    @Override
    protected void onRelease() {
        mImageLoader.clear(mAppComponent.application(),
                GlideImageConfig.builder().imageViews(mImvAvator).build());
    }
}
