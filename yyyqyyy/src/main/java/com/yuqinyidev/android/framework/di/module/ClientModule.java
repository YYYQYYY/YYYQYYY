package com.yuqinyidev.android.framework.di.module;

import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.yuqinyidev.android.framework.http.GlobalHttpHandler;
import com.yuqinyidev.android.framework.http.RequestInterceptor;
import com.yuqinyidev.android.framework.utils.DataHelper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.rx_cache2.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.listener.ResponseErrorListener;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by RDX64 on 2017/6/26.
 */
@Module
public class ClientModule {
    private static final int TIME_OUT = 10;

    /**
     * 提供Retrofit
     *
     * @param application
     * @param configuration
     * @param builder
     * @param client
     * @param httpUrl
     * @param gson
     * @return Retrofit实例
     */
    @Singleton
    @Provides
    Retrofit provideRetrofit(Application application, @Nullable RetrofitConfiguration configuration,
                             Retrofit.Builder builder, OkHttpClient client,
                             HttpUrl httpUrl, Gson gson) {
        builder
                .baseUrl(httpUrl)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson));
        if (configuration != null) {
            configuration.configRetrofit(application, builder);
        }
        return builder.build();
    }

    /**
     * 提供OkHttpClient
     *
     * @param application
     * @param configuration
     * @param builder
     * @param interceptor
     * @param interceptors
     * @param httpHandler
     * @return OkHttpClient实例
     */
    @Singleton
    @Provides
    OkHttpClient provideOkHttpClient(Application application, @Nullable OkHttpConfiguration configuration,
                                     OkHttpClient.Builder builder, Interceptor interceptor,
                                     @Nullable List<Interceptor> interceptors, @Nullable GlobalHttpHandler httpHandler) {
        builder
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .addNetworkInterceptor(interceptor);
        if (httpHandler != null) {
            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    return chain.proceed(httpHandler.onHttpRequestBefore(chain, chain.request()));
                }
            });
        }
        if (interceptors != null) {
            for (Interceptor intercept : interceptors) {
                builder.addInterceptor(intercept);
            }
        }
        if (configuration != null) {
            configuration.configOkHttp(application, builder);
        }
        return builder.build();
    }

    /**
     * 提供Retrofit
     *
     * @return
     */
    @Singleton
    @Provides
    Retrofit.Builder provideRetrofitBuilder() {
        return new Retrofit.Builder();
    }

    /**
     * 提供OkHttpClient
     *
     * @return
     */
    @Singleton
    @Provides
    OkHttpClient.Builder provideOkHttpClientBuilder() {
        return new OkHttpClient.Builder();
    }

    /**
     * 提供Interceptor
     *
     * @param interceptor
     * @return
     */
    @Singleton
    @Provides
    Interceptor provideInterceptor(RequestInterceptor interceptor) {
        return interceptor;
    }

    /**
     * 提供RXCache
     *
     * @param application
     * @param configuration
     * @param cacheDirectory
     * @return
     */
    @Singleton
    @Provides
    RxCache provideRxCache(Application application, @Nullable RxCacheConfiguration configuration,
                           @Named("RxCacheDirectory") File cacheDirectory) {
        RxCache.Builder builder = new RxCache.Builder();
        if (configuration != null) {
            configuration.configRxCache(application, builder);
        }
        return builder.persistence(cacheDirectory, new GsonSpeaker());
    }

    /**
     * 提供RxCacheDirectory
     *
     * @param cacheDir
     * @return
     */
    @Singleton
    @Provides
    @Named("RxCacheDirectory")
    File provideRxCacheDirectory(File cacheDir) {
        File cacheDirectory = new File(cacheDir, "RxCache");
        return DataHelper.makeDirs(cacheDirectory);
    }

    /**
     * 提供RxErrorHandler
     *
     * @param application
     * @param errorListener
     * @return
     */
    @Singleton
    @Provides
    RxErrorHandler provideRxErrorHandler(Application application, ResponseErrorListener errorListener) {
        return RxErrorHandler.builder().with(application).responseErrorListener(errorListener).build();
    }

    /**
     * Retrofit配置接口
     */
    public interface RetrofitConfiguration {
        void configRetrofit(Context context, Retrofit.Builder builder);
    }

    /**
     * OkHttp配置接口
     */
    public interface OkHttpConfiguration {
        void configOkHttp(Context context, OkHttpClient.Builder builder);
    }

    /**
     * RxCache配置接口
     */
    public interface RxCacheConfiguration {
        void configRxCache(Context context, RxCache.Builder builder);
    }
}
