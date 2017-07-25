package com.yuqinyidev.android.azaz.splash.mvp.model;

import com.yuqinyidev.android.azaz.splash.mvp.contract.SplashContract;
import com.yuqinyidev.android.azaz.splash.mvp.model.cache.SplashCache;
import com.yuqinyidev.android.azaz.splash.mvp.model.entity.Images;
import com.yuqinyidev.android.azaz.splash.mvp.model.service.SplashService;
import com.yuqinyidev.android.framework.di.scope.ActivityScope;
import com.yuqinyidev.android.framework.integration.IRepositoryManager;
import com.yuqinyidev.android.framework.mvp.model.BaseModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.Reply;

/**
 * Created by RDX64 on 2017/6/29.
 */
@ActivityScope
public class SplashModel extends BaseModel implements SplashContract.Model {

    @Inject
    public SplashModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<Images> getSplashBackground(String format, int idx, int n) {
        SplashService ss = mRepositoryManager
                .obtainRetrofitService(SplashService.class);
        Observable<Images> splashs = ss.getSplashBackground(format, idx, n);

        return mRepositoryManager.obtainCacheService(SplashCache.class)
                .getSplashBackground(splashs, new DynamicKey(format + idx + n), new EvictDynamicKey(false))
                .flatMap(new Function<Reply<Images>, ObservableSource<Images>>() {
                    @Override
                    public ObservableSource<Images> apply(@NonNull Reply<Images> listReply) throws Exception {
                        return Observable.just(listReply.getData());
                    }
                });
    }

}
