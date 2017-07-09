package com.yuqinyidev.android.azaz.trainingdiary.mvp.presenter;

import android.app.Application;
import android.content.Intent;
import android.view.View;

import com.yuqinyidev.android.azaz.trainingdiary.mvp.contract.TrainingDiaryContract;
import com.yuqinyidev.android.azaz.trainingdiary.mvp.model.TrainingDiariesDbHelper;
import com.yuqinyidev.android.azaz.trainingdiary.mvp.model.entity.TrainingDiary;
import com.yuqinyidev.android.azaz.trainingdiary.mvp.model.entity.TrainingDiaryEntity;
import com.yuqinyidev.android.azaz.trainingdiary.mvp.ui.activity.SaveTrainingDiaryActivity;
import com.yuqinyidev.android.azaz.trainingdiary.mvp.ui.adapter.TrainingDiaryAdapter;
import com.yuqinyidev.android.framework.base.DefaultAdapter;
import com.yuqinyidev.android.framework.di.scope.ActivityScope;
import com.yuqinyidev.android.framework.integration.AppManager;
import com.yuqinyidev.android.framework.mvp.presenter.BasePresenter;
import com.yuqinyidev.android.framework.utils.PermissionUtil;
import com.yuqinyidev.android.framework.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

/**
 * Created by RDX64 on 2017/6/29.
 */
@ActivityScope
public class TrainingDiaryPresenter extends BasePresenter<TrainingDiaryContract.Model, TrainingDiaryContract.View> {
    private RxErrorHandler mErrorHandler;
    private AppManager mAppManager;
    private Application mApplication;
    private List<TrainingDiary> mTrainingDiaries = new ArrayList<>();
    private DefaultAdapter mAdapter;

    @Inject
    public TrainingDiaryPresenter(TrainingDiaryContract.Model model, TrainingDiaryContract.View rootView,
                                  RxErrorHandler errorHandler, AppManager appManager, Application application) {
        super(model, rootView);
        this.mErrorHandler = errorHandler;
        this.mAppManager = appManager;
        this.mApplication = application;
    }

    public void getTrainingDiaries() {
//        mTrainingDiaries = mModel.getTrainingDiaries(TrainingDiariesDbHelper.getInstance(mApplication));
        if (mAdapter == null) {
            mAdapter = new TrainingDiaryAdapter(mTrainingDiaries);
            mRootView.setAdapter(mAdapter);
        }
        mAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                TrainingDiary trainingDiary = mTrainingDiaries.get(position);
                Intent intent = new Intent(mApplication, SaveTrainingDiaryActivity.class);
                intent.putExtra(TrainingDiaryEntity.COLUMN_NAME_ID, trainingDiary.getId());
                intent.putExtra(TrainingDiaryEntity.COLUMN_NAME_NAME, trainingDiary.getName());
                intent.putExtra(TrainingDiaryEntity.COLUMN_NAME_DATE, trainingDiary.getDate());
                intent.putExtra(TrainingDiaryEntity.COLUMN_NAME_LEVEL, trainingDiary.getLevel());
                intent.putExtra(TrainingDiaryEntity.COLUMN_NAME_GROUP_NO, trainingDiary.getGroupNo());
                intent.putExtra(TrainingDiaryEntity.COLUMN_NAME_COUNT, trainingDiary.getCount());
                UiUtils.startActivity(intent);
            }
        });
        mAdapter.setOnItemLongClickListener(new DefaultAdapter.OnRecyclerViewItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int viewType, Object data, int position) {
                TrainingDiary trainingDiary = mTrainingDiaries.get(position);
                mModel.deleteTrainingDiary(TrainingDiariesDbHelper.getInstance(mApplication), trainingDiary);
            }
        });

        PermissionUtil.externalStorage(new PermissionUtil.RequestPermission() {
            @Override
            public void onRequestPermissionSuccess() {

            }

            @Override
            public void onRequestPermissionFailure() {
                mRootView.showMessage("Request permissions failure");
            }
        }, mRootView.getRxPermissions(), mErrorHandler);

//        mRootView.showLoading();
        mModel.getTrainingDiaries(TrainingDiariesDbHelper.getInstance(mApplication),
                new TrainingDiaryContract.Model.LoadTrainingDiariesCallback() {
                    @Override
                    public void onTrainingDiariesLoaded(List<TrainingDiary> trainingDiaries) {
                        mRootView.showMessage("数据获取成功");
                        mTrainingDiaries.clear();
                        mTrainingDiaries.addAll(trainingDiaries);
                        mAdapter.notifyDataSetChanged();
                        mRootView.hideLoading();
                    }

                    @Override
                    public void onDataNotAvailable() {
                        mRootView.showMessage("数据获取失败");
                        mRootView.hideLoading();
                    }
                });
    }

    public void getTrainingDiary(int id) {
        mModel.getTrainingDiaryById(TrainingDiariesDbHelper.getInstance(mApplication), id,
                new TrainingDiaryContract.Model.GetTrainingDiaryCallback() {
                    @Override
                    public void onTrainingDiaryLoaded(TrainingDiary TrainingDiary) {

                    }

                    @Override
                    public void onDataNotAvailable() {
                        mRootView.showMessage("数据获取失败");
                    }
                });
    }

    public void saveTrainingDiary(TrainingDiary trainingDiary) {
        mModel.saveTrainingDiary(TrainingDiariesDbHelper.getInstance(mApplication), trainingDiary);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mApplication = null;
        this.mAdapter = null;
        this.mTrainingDiaries = null;
    }

}
