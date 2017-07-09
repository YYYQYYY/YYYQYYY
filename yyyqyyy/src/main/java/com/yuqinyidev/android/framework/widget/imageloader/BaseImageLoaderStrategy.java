package com.yuqinyidev.android.framework.widget.imageloader;

import android.content.Context;

/**
 * Created by RDX64 on 2017/6/28.
 */

public interface BaseImageLoaderStrategy<T extends ImageConfig> {
    void loadImage(Context context, T config);

    void clear(Context context, T config);
}
