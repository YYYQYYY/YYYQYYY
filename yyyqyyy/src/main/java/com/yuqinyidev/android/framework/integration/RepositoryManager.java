package com.yuqinyidev.android.framework.integration;

import com.yuqinyidev.android.framework.utils.Preconditions;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.rx_cache2.internal.RxCache;
import retrofit2.Retrofit;

/**
 * 管理网络请求层,以及数据缓存层
 * Created by RDX64 on 2017/6/28.
 */
@Singleton
public class RepositoryManager implements IRepositoryManager {
    private final Map<String, Object> mRetrofitServiceCache = new LinkedHashMap<>();
    private final Map<String, Object> mCacheServiceCache = new LinkedHashMap<>();
    private Retrofit mRetrofit;
    private RxCache mRxCache;

    @Inject
    public RepositoryManager(Retrofit retrofit, RxCache rxCache) {
        this.mRetrofit = retrofit;
        this.mRxCache = rxCache;
    }

    /**
     * 注入RetrofitService
     *
     * @param services
     */
    @Override
    public void injectRetrofitService(Class<?>... services) {
        for (Class<?> service : services) {
            if (mRetrofitServiceCache.containsKey(service.getName())) {
                continue;
            }
            mRetrofitServiceCache.put(service.getName(), mRetrofit.create(service));
        }
    }

    /**
     * 注入CacheService
     *
     * @param services
     */
    @Override
    public void injectCacheService(Class<?>... services) {
        for (Class<?> service : services) {
            if (mCacheServiceCache.containsKey(service.getName())) {
                continue;
            }
            mCacheServiceCache.put(service.getName(), mRxCache.using(service));
        }
    }

    /**
     * 根据传入的Class获取对应的RetriftService
     *
     * @param service
     * @param <T>
     * @return
     */
    @Override
    public <T> T obtainRetrofitService(Class<T> service) {
        Preconditions.checkState(mRetrofitServiceCache.containsKey(service.getName()),
                "Unable to find %s,first call injectRetrofitService(%s) in ConfigModule",
                service.getName(), service.getSimpleName());
        return (T) mRetrofitServiceCache.get(service.getName());
    }

    @Override
    public <T> T obtainCacheService(Class<T> cache) {
        Preconditions.checkState(mCacheServiceCache.containsKey(cache.getName()),
                "Unable to find %s,first call injectCacheService(%s) in ConfigModule",
                cache.getName(), cache.getSimpleName());
        return (T) mCacheServiceCache.get(cache.getName());
    }
}
