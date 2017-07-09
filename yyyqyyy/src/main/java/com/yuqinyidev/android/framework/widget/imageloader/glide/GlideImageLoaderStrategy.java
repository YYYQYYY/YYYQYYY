package com.yuqinyidev.android.framework.widget.imageloader.glide;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.yuqinyidev.android.framework.widget.imageloader.BaseImageLoaderStrategy;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by RDX64 on 2017/6/27.
 */

public class GlideImageLoaderStrategy implements BaseImageLoaderStrategy<GlideImageConfig> {
    @Override
    public void loadImage(Context context, GlideImageConfig config) {
        if (context == null) {
            throw new IllegalStateException("Context is required");
        }
        if (config == null) {
            throw new IllegalStateException("GlideImageConfig is required");
        }
        if (TextUtils.isEmpty(config.getUrl()) && config.getResourceId() == 0) {
            throw new IllegalStateException("Url or ResourceId is required");
        }
        if (config.getImageView() == null) {
            throw new IllegalStateException("ImageView is required");
        }
        if (config.getResourceId() > 0) {
            loadLocalImage(context, config);
        } else {
            loadRemoteImage(context, config);
        }
    }

    private void loadRemoteImage(Context context, GlideImageConfig config) {
        RequestManager manager = Glide.with(context);
        DrawableRequestBuilder<String> requestBuilder = manager.load(config.getUrl()).crossFade().centerCrop();

        switch (config.getCacheStrategy()) {
            case 0:
                requestBuilder.diskCacheStrategy(DiskCacheStrategy.ALL);
                break;
            case 1:
                requestBuilder.diskCacheStrategy(DiskCacheStrategy.NONE);
                break;
            case 2:
                requestBuilder.diskCacheStrategy(DiskCacheStrategy.SOURCE);
                break;
            case 3:
                requestBuilder.diskCacheStrategy(DiskCacheStrategy.RESULT);
                break;
            default:
                break;
        }

        if (config.getTransformation() != null) {
            requestBuilder.transform(config.getTransformation());
        }

        if (config.getPlaceholder() != 0) {
            requestBuilder.placeholder(config.getPlaceholder());
        }

        if (config.getErrorPic() != 0) {
            requestBuilder.error(config.getErrorPic());
        }

        requestBuilder.into(config.getImageView());
    }

    private void loadLocalImage(Context context, GlideImageConfig config) {
        RequestManager manager = Glide.with(context);
        DrawableRequestBuilder<Integer> requestBuilder = manager.load(config.getResourceId()).crossFade().centerCrop();

        if (config.getTransformation() != null) {
            requestBuilder.transform(config.getTransformation());
        }

        if (config.getPlaceholder() != 0) {
            requestBuilder.placeholder(config.getPlaceholder());
        }

        if (config.getErrorPic() != 0) {
            requestBuilder.error(config.getErrorPic());
        }

        requestBuilder.into(config.getImageView());
    }

    @Override
    public void clear(final Context context, GlideImageConfig config) {
        if (context == null) {
            throw new IllegalStateException("Context is required");
        }
        if (config == null) {
            throw new IllegalStateException("GlideImageConfig is required");
        }

        if (config.getImageViews() != null && config.getImageViews().length > 0) {
            for (ImageView imageView : config.getImageViews()) {
                Glide.clear(imageView);
            }
        }

        if (config.getTargets() != null && config.getTargets().length > 0) {
            for (Target target : config.getTargets()) {
                Glide.clear(target);
            }
        }

        if (config.isClearDiskCache()) {
            Observable.just(0).observeOn(Schedulers.io()).subscribe(new Consumer<Integer>() {
                @Override
                public void accept(@NonNull Integer integer) throws Exception {
                    Glide.get(context).clearDiskCache();
                }
            });
        }

        if (config.isClearMemory()) {
            Glide.get(context).clearMemory();
        }

    }
}
