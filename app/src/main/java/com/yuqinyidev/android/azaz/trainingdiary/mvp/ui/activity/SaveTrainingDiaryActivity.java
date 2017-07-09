package com.yuqinyidev.android.azaz.trainingdiary.mvp.ui.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.EditText;
import android.widget.TextView;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yuqinyidev.android.azaz.R;
import com.yuqinyidev.android.azaz.trainingdiary.mvp.contract.TrainingDiaryContract;
import com.yuqinyidev.android.azaz.trainingdiary.mvp.model.TrainingDiariesDbHelper;
import com.yuqinyidev.android.azaz.trainingdiary.mvp.model.entity.TrainingDiary;
import com.yuqinyidev.android.azaz.trainingdiary.mvp.model.entity.TrainingDiaryEntity;
import com.yuqinyidev.android.azaz.trainingdiary.mvp.presenter.TrainingDiaryPresenter;
import com.yuqinyidev.android.framework.base.BaseActivity;
import com.yuqinyidev.android.framework.base.DefaultAdapter;
import com.yuqinyidev.android.framework.di.component.AppComponent;
import com.yuqinyidev.android.framework.utils.UiUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import timber.log.Timber;

import static android.R.attr.data;
import static android.R.attr.id;

/**
 * Created by RDX64 on 2017/6/29.
 */

public class SaveTrainingDiaryActivity extends BaseActivity<TrainingDiaryPresenter> implements TrainingDiaryContract.View {
    private RxPermissions mRxPermissions;

    @BindView(R.id.edt_td_id)
    EditText mTdId;
    @BindView(R.id.edt_td_name)
    EditText mTdName;
    @BindView(R.id.edt_td_date)
    EditText mTdDate;
    @BindView(R.id.edt_td_level)
    EditText mTdLevel;
    @BindView(R.id.edt_td_group_no)
    EditText mTdGroupNo;
    @BindView(R.id.edt_td_count)
    EditText mTdCount;

    @OnClick(R.id.fab_edit_task_done)
    public void saveTrainingDiary() {
        int id = Integer.valueOf(mTdId.getText().toString());
        String name = mTdName.getText().toString();
        String date = mTdDate.getText().toString();
        int level = Integer.valueOf(mTdLevel.getText().toString());
        int groupNo = Integer.valueOf(mTdGroupNo.getText().toString());
        int count = Integer.valueOf(mTdCount.getText().toString());
        TrainingDiary trainingDiary = new TrainingDiary(id, date, name, level, groupNo, count);
        saveTrainingDiary(TrainingDiariesDbHelper.getInstance(SaveTrainingDiaryActivity.this), trainingDiary);
    }

    private void saveTrainingDiary(TrainingDiariesDbHelper dbHelper, @NonNull TrainingDiary trainingDiary) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TrainingDiaryEntity.COLUMN_NAME_ID, trainingDiary.getId());
        values.put(TrainingDiaryEntity.COLUMN_NAME_NAME, trainingDiary.getName());
        values.put(TrainingDiaryEntity.COLUMN_NAME_DATE, trainingDiary.getDate());
        values.put(TrainingDiaryEntity.COLUMN_NAME_LEVEL, trainingDiary.getLevel());
        values.put(TrainingDiaryEntity.COLUMN_NAME_GROUP_NO, trainingDiary.getGroupNo());
        values.put(TrainingDiaryEntity.COLUMN_NAME_COUNT, trainingDiary.getCount());

        db.insert(TrainingDiaryEntity.TABLE_NAME, null, values);

        db.close();
    }

    @Override
    public boolean useFragment() {
        return false;
    }

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        this.mRxPermissions = new RxPermissions(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.act_save_training_diary;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
//        mPresenter.getTrainingDiary(id);
        Intent intent = getIntent();
        int tdId = intent.getIntExtra(TrainingDiaryEntity.COLUMN_NAME_ID, -1);
        String name = intent.getStringExtra(TrainingDiaryEntity.COLUMN_NAME_NAME);
        String date = intent.getStringExtra(TrainingDiaryEntity.COLUMN_NAME_DATE);
        int level = intent.getIntExtra(TrainingDiaryEntity.COLUMN_NAME_LEVEL, -1);
        int groupNo = intent.getIntExtra(TrainingDiaryEntity.COLUMN_NAME_GROUP_NO, -1);
        int count = intent.getIntExtra(TrainingDiaryEntity.COLUMN_NAME_COUNT, -1);
        mTdId.setText(String.valueOf(tdId));
        mTdName.setText(name);
        mTdDate.setText(date);
        mTdLevel.setText(String.valueOf(level));
        mTdGroupNo.setText(String.valueOf(groupNo));
        mTdCount.setText(String.valueOf(count));
    }

    @Override
    public void showLoading() {
        Timber.tag(TAG).w("showLoading");
    }

    @Override
    public void hideLoading() {
        Timber.tag(TAG).w("hideLoading");
    }

    @Override
    public void showMessage(String message) {
        UiUtils.snackbarText(message);
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
    }

    @Override
    public void showTrainingDiary(TrainingDiary data) {
//        Observable.just(data.getId()).subscribe(s -> mTdId.setText(s));
//        Observable.just(data.getName()).subscribe(s -> mTdName.setText(s));
//        Observable.just(data.getDate()).subscribe(s -> mTdDate.setText(s));
//        Observable.just(data.getLevel()).subscribe(s -> mTdLevel.setText(s));
//        Observable.just(data.getGroupNo()).subscribe(s -> mTdGroupNo.setText(s));
//        Observable.just(data.getCount()).subscribe(s -> mTdCount.setText(s));
    }

    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mRxPermissions = null;
    }
}
