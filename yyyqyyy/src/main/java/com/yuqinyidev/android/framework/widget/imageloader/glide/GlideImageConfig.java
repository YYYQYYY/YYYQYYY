package com.yuqinyidev.android.framework.widget.imageloader.glide;

import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.target.Target;
import com.yuqinyidev.android.framework.widget.imageloader.ImageConfig;

/**
 * Glide配置信息
 * <p>
 * Created by RDX64 on 2017/6/27.
 */

public class GlideImageConfig extends ImageConfig {
    private int cacheStrategy;
    private BitmapTransformation transformation;
    private Target[] targets;
    private ImageView[] imageViews;
    private boolean isClearMemory;
    private boolean isClearDiskCache;

    private GlideImageConfig(Builder builder) {
        this.resourceId = builder.resourceId;
        this.url = builder.url;
        this.imageView = builder.imageView;
        this.placeholder = builder.placeholder;
        this.errorPic = builder.errorPic;
        this.cacheStrategy = builder.cacheStrategy;
        this.transformation = builder.transformation;
        this.targets = builder.targets;
        this.imageViews = builder.imageViews;
        this.isClearMemory = builder.isClearMemory;
        this.isClearDiskCache = builder.isClearDiskCache;
    }

    public int getCacheStrategy() {
        return cacheStrategy;
    }

    public BitmapTransformation getTransformation() {
        return transformation;
    }

    public Target[] getTargets() {
        return targets;
    }

    public ImageView[] getImageViews() {
        return imageViews;
    }

    public boolean isClearMemory() {
        return isClearMemory;
    }

    public boolean isClearDiskCache() {
        return isClearDiskCache;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private int resourceId;
        private String url;
        private ImageView imageView;
        private int placeholder;
        private int errorPic;
        private int cacheStrategy;
        private BitmapTransformation transformation;
        private Target[] targets;
        private ImageView[] imageViews;
        private boolean isClearMemory;
        private boolean isClearDiskCache;

        private Builder() {
        }

        /**
         * resourceId
         *
         * @param resourceId
         * @return
         */
        public Builder resourceId(int resourceId) {
            this.resourceId = resourceId;
            return this;
        }

        /**
         * URL
         *
         * @param url
         * @return
         */
        public Builder url(String url) {
            this.url = url;
            return this;
        }

        /**
         * 占位符
         *
         * @param placeholder
         * @return
         */
        public Builder placeholder(int placeholder) {
            this.placeholder = placeholder;
            return this;
        }

        /**
         * 图片错误
         *
         * @param errorPic
         * @return
         */
        public Builder errorPic(int errorPic) {
            this.errorPic = errorPic;
            return this;
        }

        /**
         * 图片
         *
         * @param imageView
         * @return
         */
        public Builder imageView(ImageView imageView) {
            this.imageView = imageView;
            return this;
        }

        /**
         * 0对应DiskCacheStrategy.all,
         * 1对应DiskCacheStrategy.NONE,
         * 2对应DiskCacheStrategy.SOURCE,
         * 3对应DiskCacheStrategy.RESULT
         *
         * @param cacheStrategy
         * @return
         */
        public Builder cacheStrategy(int cacheStrategy) {
            this.cacheStrategy = cacheStrategy;
            return this;
        }

        /**
         * 改变图形的形状
         *
         * @param transformation
         * @return
         */
        public Builder transformation(BitmapTransformation transformation) {
            this.transformation = transformation;
            return this;
        }

        /**
         * The lifecycle events in this class are as follows
         *
         * @param targets
         * @return
         */
        public Builder targets(Target... targets) {
            this.targets = targets;
            return this;
        }

        /**
         * 图片数组
         *
         * @param imageViews
         * @return
         */
        public Builder imageViews(ImageView... imageViews) {
            this.imageViews = imageViews;
            return this;
        }

        /**
         * 清理内存缓存
         *
         * @param isClearMemory
         * @return
         */
        public Builder isClearMemory(boolean isClearMemory) {
            this.isClearMemory = isClearMemory;
            return this;
        }

        /**
         * 清理本地缓存
         *
         * @param isClearDiskCache
         * @return
         */
        public Builder isClearDiskCache(boolean isClearDiskCache) {
            this.isClearDiskCache = isClearDiskCache;
            return this;
        }

        public GlideImageConfig build() {
            return new GlideImageConfig(this);
        }
    }
}
