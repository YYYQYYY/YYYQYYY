package com.yuqinyidev.android.framework.integration;

/**
 * Created by RDX64 on 2017/6/27.
 */

public interface IRepositoryManager {
    void injectRetrofitService(Class<?>... services);

    void injectCacheService(Class<?>... services);

    <T> T obtainRetrofitService(Class<T> service);

    <T> T obtainCacheService(Class<T> service);
}
