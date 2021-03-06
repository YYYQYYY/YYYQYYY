package com.yuqinyidev.android.azaz.trainingdiary.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yuqinyidev.android.azaz.R;
import com.yuqinyidev.android.azaz.trainingdiary.di.component.DaggerTrainingDiaryComponent;
import com.yuqinyidev.android.azaz.trainingdiary.di.module.TrainingDiaryModule;
import com.yuqinyidev.android.azaz.trainingdiary.mvp.contract.TrainingDiaryContract;
import com.yuqinyidev.android.azaz.trainingdiary.mvp.model.entity.TrainingDiary;
import com.yuqinyidev.android.azaz.trainingdiary.mvp.presenter.TrainingDiaryPresenter;
import com.yuqinyidev.android.framework.base.BaseActivity;
import com.yuqinyidev.android.framework.base.DefaultAdapter;
import com.yuqinyidev.android.framework.di.component.AppComponent;
import com.yuqinyidev.android.framework.utils.UiUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

import static android.view.View.VISIBLE;

/**
 * Created by RDX64 on 2017/6/29.
 */

public class TrainingDiaryActivity extends BaseActivity<TrainingDiaryPresenter>
        implements TrainingDiaryContract.View, SwipeRefreshLayout.OnRefreshListener {
    private RxPermissions mRxPermissions;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;

    @OnClick(R.id.fab_add_task)
    public void saveTrainingDiary() {
        Intent intent = new Intent(TrainingDiaryActivity.this, SaveTrainingDiaryActivity.class);
        intent.putExtra("is_add", true);
        UiUtils.startActivity(intent);
    }

    @Override
    public boolean useFragment() {
        return false;
    }

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        this.mRxPermissions = new RxPermissions(this);
        DaggerTrainingDiaryComponent
                .builder()
                .appComponent(appComponent)
                .trainingDiaryModule(new TrainingDiaryModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.act_training_diary;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mPresenter.getTrainingDiaries();
    }

    @Override
    public void onRefresh() {
        mPresenter.getTrainingDiaries();
    }

    @Override
    public void showLoading() {
        Timber.tag(TAG).w("showLoading");
        Observable
                .just(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> mSwipeRefreshLayout.setRefreshing(true));
    }

    @Override
    public void hideLoading() {
        Timber.tag(TAG).w("hideLoading");
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showMessage(String message) {
//        UiUtils.snackbarText(message);//会遮住
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void launchActivity(Intent intent) {
        UiUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    public void setAdapter(DefaultAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
//        mRecyclerView.setItem_delete(R.id.item_delete);
//        mRecyclerView.setListener(new RecyListViewOnItemClick() {
//            @Override
//            public void onDeleteClick(View view, int position) {
////                mRecyclerView.notifyItemRemoved(position);
//            }
//        });
        initRecycleView();
    }

    @Override
    public void showTrainingDiary(TrainingDiary trainingDiary) {

    }

    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }

    @Override
    protected void onDestroy() {
        DefaultAdapter.releaseAllHolder(mRecyclerView);
        super.onDestroy();
        this.mRxPermissions = null;
    }

    private void autoRefresh() {
        try {
            Field mCircleView = SwipeRefreshLayout.class.getDeclaredField("mCircleView");
            mCircleView.setAccessible(true);
            View progress = (View) mCircleView.get(this);
            progress.setVisibility(VISIBLE);
            Method setRefreshing = SwipeRefreshLayout.class.getDeclaredMethod("setRefreshing", boolean.class, boolean.class);
            setRefreshing.setAccessible(true);
            setRefreshing.invoke(this, true, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initRecycleView() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        UiUtils.configRecycleView(mRecyclerView, new LinearLayoutManager(this));
    }
}
