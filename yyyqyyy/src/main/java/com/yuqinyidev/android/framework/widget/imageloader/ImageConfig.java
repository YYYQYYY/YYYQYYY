package com.yuqinyidev.android.framework.widget.imageloader;

import android.widget.ImageView;

/**
 * 图片加载配置信息的基类
 * Created by RDX64 on 2017/6/28.
 */

public class ImageConfig {
    protected int resourceId;
    protected String url;
    protected ImageView imageView;
    protected int placeholder;
    protected int errorPic;

    public int getResourceId() {
        return resourceId;
    }

    public String getUrl() {
        return url;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public int getPlaceholder() {
        return placeholder;
    }

    public int getErrorPic() {
        return errorPic;
    }
}
