package com.yuqinyidev.android.azaz.trainingdiary.mvp.ui.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yuqinyidev.android.azaz.R;
import com.yuqinyidev.android.azaz.trainingdiary.mvp.contract.TrainingDiaryContract;
import com.yuqinyidev.android.azaz.trainingdiary.mvp.model.TrainingDiariesDbHelper;
import com.yuqinyidev.android.azaz.trainingdiary.mvp.model.entity.JsonBean;
import com.yuqinyidev.android.azaz.trainingdiary.mvp.model.entity.TrainingDiary;
import com.yuqinyidev.android.azaz.trainingdiary.mvp.model.entity.TrainingDiaryEntity;
import com.yuqinyidev.android.azaz.trainingdiary.mvp.presenter.TrainingDiaryPresenter;
import com.yuqinyidev.android.framework.base.BaseActivity;
import com.yuqinyidev.android.framework.base.DefaultAdapter;
import com.yuqinyidev.android.framework.di.component.AppComponent;
import com.yuqinyidev.android.framework.utils.CharacterHandler;
import com.yuqinyidev.android.framework.utils.UiUtils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by RDX64 on 2017/6/29.
 */

public class SaveTrainingDiaryActivity extends BaseActivity<TrainingDiaryPresenter> implements TrainingDiaryContract.View {
    private RxPermissions mRxPermissions;
    private List<JsonBean> mOptions1Items = new ArrayList<>();
    private List<List<String>> mOptions2Items = new ArrayList<>();
    private List<List<List<Integer>>> mOptions3Items = new ArrayList<>();
    private int mOption1, mOption2, mOption3;

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
    @BindView(R.id.txv_address)
    TextView mTvAddress;


    @OnClick(R.id.txv_address)
    public void chooseTrainingPicker() {
        showPickerView();
    }

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
        killMyself();
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
        return R.layout.act_training_diary_save;
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
        initJsonData();
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
        this.mOptions1Items = null;
        this.mOptions2Items = null;
        this.mOptions3Items = null;
    }

    private void showPickerView() {
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String text = mOptions1Items.get(mOption1 = options1).getPickerViewText() +
                        mOptions2Items.get(options1).get(mOption2 = options2) +
                        mOptions3Items.get(options1).get(options2).get(mOption3 = options3);
                mTvAddress.setText(text);
            }
        }).setTitleText("")
                .setDividerColor(Color.GRAY)
                .setTextColorCenter(Color.GRAY)
                .setContentTextSize(13)
                .setOutSideCancelable(false)
                .build();

        pvOptions.setPicker(mOptions1Items, mOptions2Items, mOptions3Items);//三级选择器
        pvOptions.setSelectOptions(mOption1, mOption2, mOption3);
        pvOptions.show();
    }

    private void initJsonData() {
        List<String> lvl2List = null;
        List<List<Integer>> lvl3List = null;
        List<Integer> lvl3ItemList = null;

        String jsonData = CharacterHandler.getAssetsJson(this, "training_data.json");
        List<JsonBean> level1List = parseData(jsonData);

        //添加一级数据
        mOptions1Items = level1List;

        //遍历一级数据
        for (JsonBean level1Value : level1List) {
            //二级数据列表
            lvl2List = new ArrayList<>();
            //三级数据列表
            lvl3List = new ArrayList<>();

            //遍历二级数据
            for (JsonBean.Level2Bean level2Bean : level1Value.getLevel2List()) {
                //添加二级数据
                lvl2List.add(level2Bean.getLevel2Key());

                //三级数据列表
                lvl3ItemList = new ArrayList<>();

                if (level2Bean.getLevel3List() == null || level2Bean.getLevel3List().size() == 0) {
                    lvl3ItemList.add(0);
                } else {
                    //遍历三级数据
                    for (Integer level3Value : level2Bean.getLevel3List()) {
                        //添加三级数据
                        lvl3ItemList.add(level3Value);
                    }
                }
                //添加三级数据列表
                lvl3List.add(lvl3ItemList);
            }

            //添加二级数据
            mOptions2Items.add(lvl2List);

            //添加三级数据
            mOptions3Items.add(lvl3List);
        }
    }

    private List<JsonBean> parseData(String result) {
        List<JsonBean> resultList = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                resultList.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }

}
