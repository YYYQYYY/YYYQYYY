package com.yuqinyidev.android.azaz.splash.mvp.model.service;

import com.yuqinyidev.android.azaz.splash.mvp.model.entity.Images;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by RDX64 on 2017/6/29.
 */

public interface SplashService {
    @GET("http://cn.bing.com/HPImageArchive.aspx")
    Observable<Images> getSplashBackground(@Query("format") String format, @Query("idx") int idx, @Query("n") int n);
}
