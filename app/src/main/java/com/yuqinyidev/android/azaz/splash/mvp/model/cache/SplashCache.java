package com.yuqinyidev.android.azaz.splash.mvp.model.cache;

import com.yuqinyidev.android.azaz.demo.mvp.model.entity.User;
import com.yuqinyidev.android.azaz.splash.mvp.model.entity.Images;
import com.yuqinyidev.android.azaz.splash.mvp.model.entity.Splash;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictProvider;
import io.rx_cache2.LifeCache;
import io.rx_cache2.Reply;

/**
 * Created by RDX64 on 2017/6/29.
 */

public interface SplashCache {
    @LifeCache(duration = 2, timeUnit = TimeUnit.MINUTES)
    Observable<Reply<Images>> getSplashBackground(Observable<Images> splashBackground, DynamicKey url, EvictProvider evictProvider);
}
