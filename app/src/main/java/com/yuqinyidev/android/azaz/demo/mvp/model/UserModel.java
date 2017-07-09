package com.yuqinyidev.android.azaz.demo.mvp.model;

import com.yuqinyidev.android.azaz.demo.mvp.model.cache.UserCache;
import com.yuqinyidev.android.azaz.demo.mvp.model.service.UserService;
import com.yuqinyidev.android.framework.di.scope.ActivityScope;
import com.yuqinyidev.android.framework.integration.IRepositoryManager;
import com.yuqinyidev.android.framework.mvp.model.BaseModel;
import com.yuqinyidev.android.azaz.demo.mvp.contract.UserContract;
import com.yuqinyidev.android.azaz.demo.mvp.model.entity.User;

import java.util.List;

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
public class UserModel extends BaseModel implements UserContract.Model {
    public static final int USER_PER_PAGE = 10;

    @Inject
    public UserModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<List<User>> getUsers(int lastIdQueried, boolean update) {
        Observable<List<User>> users = mRepositoryManager.obtainRetrofitService(UserService.class)
                .getUser(lastIdQueried, USER_PER_PAGE);
        return mRepositoryManager.obtainCacheService(UserCache.class)
                .getUsers(users, new DynamicKey(lastIdQueried), new EvictDynamicKey(update))
                .flatMap(new Function<Reply<List<User>>, ObservableSource<List<User>>>() {
                    @Override
                    public ObservableSource<List<User>> apply(@NonNull Reply<List<User>> listReply) throws Exception {
                        return Observable.just(listReply.getData());
                    }
                });
    }
}
