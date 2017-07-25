package com.yuqinyidev.android.azaz.splash.mvp.presenter;

import android.app.Application;

import com.yuqinyidev.android.azaz.splash.mvp.contract.SplashContract;
import com.yuqinyidev.android.azaz.splash.mvp.model.entity.Images;
import com.yuqinyidev.android.framework.di.scope.ActivityScope;
import com.yuqinyidev.android.framework.integration.AppManager;
import com.yuqinyidev.android.framework.mvp.presenter.BasePresenter;
import com.yuqinyidev.android.framework.utils.PermissionUtils;
import com.yuqinyidev.android.framework.utils.RxUtils;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

/**
 * Created by RDX64 on 2017/6/29.
 */
@ActivityScope
public class SplashPresenter extends BasePresenter<SplashContract.Model, SplashContract.View> {
    private RxErrorHandler mErrorHandler;

    @Inject
    public SplashPresenter(SplashContract.Model model, SplashContract.View rootView,
                           RxErrorHandler errorHandler, AppManager appManager, Application application) {
        super(model, rootView);
        this.mErrorHandler = errorHandler;
    }

    public void requestSplashes() {
        PermissionUtils.externalStorage(new PermissionUtils.RequestPermission() {
            @Override
            public void onRequestPermissionSuccess() {
            }

            @Override
            public void onRequestPermissionFailure() {
                mRootView.showMessage("Request permissions failure");
            }
        }, mRootView.getRxPermissions(), mErrorHandler);

        /*
1、format，非必要。我理解为输出格式，不存在或者不等于js，即为xml格式，等于js时，输出json格式；
2、idx，非必要。不存在或者等于0时，输出当天的图片，-1为已经预备用于明天显示的信息，1则为昨天的图片，idx最多获取到前16天的图片信息；*
3、n，必要。这是输出信息的数量，比如n=1，即为1条，以此类推，至多输出8条；*
*号注释：此处我们要注意的时，是否正常的输出信息，与n和idx有关，通过idx的值，我们就可以获得之前bing所使用的背景图片的信息了。
         */
        mModel.getSplashBackground("js", 0, 1)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading();
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(() -> {
                    mRootView.hideLoading();
                })
                .compose(RxUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<Images>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull Images splashBackground) {
                        if (splashBackground.getImages() != null && splashBackground.getImages().length > 0) {
                            mRootView.setSplashBackground(splashBackground.getImages()[0]);
                        } else {
                            mRootView.showMessage("获取数据失败");
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
    }
}
