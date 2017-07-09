package com.yuqinyidev.android.azaz.main.mvp.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuqinyidev.android.azaz.R;
import com.yuqinyidev.android.azaz.main.mvp.model.entity.AppItem;
import com.yuqinyidev.android.framework.base.App;
import com.yuqinyidev.android.framework.base.BaseHolder;
import com.yuqinyidev.android.framework.di.component.AppComponent;
import com.yuqinyidev.android.framework.widget.imageloader.ImageLoader;
import com.yuqinyidev.android.framework.widget.imageloader.glide.GlideImageConfig;

import butterknife.BindView;
import io.reactivex.Observable;

/**
 * Created by RDX64 on 2017/7/2.
 */

public class MainMenuItemHolder extends BaseHolder<AppItem> {
    private AppComponent mAppComponent;
    private ImageLoader mImageLoader;

    @BindView(R.id.imv_app_item_image)
    ImageView mImvAppItemImage;
    @BindView(R.id.txv_app_item_name)
    TextView mTxvAppItemName;

    public MainMenuItemHolder(View itemView) {
        super(itemView);
        mAppComponent = ((App) itemView.getContext().getApplicationContext()).getAppComponent();
        mImageLoader = mAppComponent.imageLoader();
    }

    @Override
    public void setData(AppItem data, int position) {
        Observable.just(data.getAppItemName()).subscribe(s -> mTxvAppItemName.setText(s));

        mImageLoader.loadImage(
                mAppComponent.appManager().getCurrentActivity() == null ?
                        mAppComponent.application() : mAppComponent.appManager().getCurrentActivity(),
                GlideImageConfig.builder().resourceId(data.getAppItemImageId()).imageView(mImvAppItemImage).build()
        );
    }

    @Override
    protected void onRelease() {
        mImageLoader.clear(mAppComponent.application(),
                GlideImageConfig.builder().imageViews(mImvAppItemImage).build());
    }
}
