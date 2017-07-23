package com.yuqinyidev.android.azaz.trainingdiary.mvp.ui.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * 保存日志
 * Created by RDX64 on 2017/6/29.
 */

public class SaveTrainingDiaryActivity extends BaseActivity<TrainingDiaryPresenter> implements TrainingDiaryContract.View {
    private static final String[][] mUpRule = {
            {"初级目标：1组，10次\n中极目标：2组，各25次\n升级目标：3组，各50次", "初级目标：1组，10次\n中极目标：2组，各20次\n升级目标：3组，各40次", "初级目标：1组，10次\n中极目标：2组，各15次\n升级目标：3组，各30次", "初级目标：1组，8次\n中极目标：2组，各12次\n升级目标：2组，各25次", "初级目标：1组，5次\n中极目标：2组，各10次\n升级目标：2组，各20次", "初级目标：1组，5次\n中极目标：2组，各10次\n升级目标：2组，各20次", "初级目标：1组，5次\n中极目标：2组，各10次\n升级目标：2组，各20次", "初级目标：1组，5次\n中极目标：2组，各10次\n升级目标：2组，各20次", "初级目标：1组，5次\n中极目标：2组，各10次\n升级目标：2组，各20次", "初级目标：1组，5次\n中极目标：6组，各10次\n升级目标：1组，各100次"},
            {"初级目标：1组，10次\n中极目标：2组，各25次\n升级目标：3组，各50次", "初级目标：1组，10次\n中极目标：2组，各20次\n升级目标：3组，各40次", "初级目标：1组，10次\n中极目标：2组，各15次\n升级目标：3组，各30次", "初级目标：1组，8次\n中极目标：2组，各35次\n升级目标：3组，各50次", "初级目标：1组，5次\n中极目标：2组，各10次\n升级目标：3组，各30次", "初级目标：1组，5次\n中极目标：2组，各10次\n升级目标：3组，各20次", "初级目标：1组，5次\n中极目标：2组，各10次\n升级目标：3组，各20次", "初级目标：1组，5次\n中极目标：2组，各10次\n升级目标：3组，各20次", "初级目标：1组，5次\n中极目标：2组，各10次\n升级目标：3组，各20次", "初级目标：1组，5次\n中极目标：2组，各10次\n升级目标：2组，各50次"},
            {"初级目标：1组，10次\n中极目标：2组，各20次\n升级目标：3组，各40次", "初级目标：1组，10次\n中极目标：2组，各20次\n升级目标：3组，各30次", "初级目标：1组，10次\n中极目标：2组，各15次\n升级目标：3组，各20次", "初级目标：1组，8次\n中极目标：2组，各11次\n升级目标：3组，各15次", "初级目标：1组，5次\n中极目标：2组，各8次\n升级目标：3组，各10次", "初级目标：1组，5次\n中极目标：2组，各8次\n升级目标：3组，各10次", "初级目标：1组，5次\n中极目标：2组，各7次\n升级目标：3组，各8次", "初级目标：1组，4次\n中极目标：2组，各11次\n升级目标：2组，各8次", "初级目标：1组，3次\n中极目标：2组，各5次\n升级目标：2组，各7次", "初级目标：1组，1次\n中极目标：2组，各3次\n升级目标：2组，各6次"},
            {"初级目标：1组，10次\n中极目标：2组，各25次\n升级目标：3组，各40次", "初级目标：1组，10次\n中极目标：2组，各20次\n升级目标：3组，各35次", "初级目标：1组，10次\n中极目标：2组，各15次\n升级目标：3组，各30次", "初级目标：1组，8次\n中极目标：2组，各15次\n升级目标：3组，各25次", "初级目标：1组，5次\n中极目标：2组，各10次\n升级目标：3组，各20次", "初级目标：1组，5次\n中极目标：2组，各10次\n升级目标：2组，各15次", "初级目标：1组，5次\n中极目标：2组，各10次\n升级目标：2组，各15次", "初级目标：1组，5次\n中极目标：2组，各10次\n升级目标：2组，各15次", "初级目标：1组，5次\n中极目标：2组，各10次\n升级目标：2组，各15次", "初级目标：1组，5次\n中极目标：2组，各10次\n升级目标：2组，各30次"},
            {"初级目标：1组，10次\n中极目标：2组，各25次\n升级目标：3组，各50次", "初级目标：1组，10次\n中极目标：2组，各20次\n升级目标：3组，各40次", "初级目标：1组，8次\n中极目标：2组，各15次\n升级目标：3组，各30次", "初级目标：1组，8次\n中极目标：2组，各15次\n升级目标：3组，各25次", "初级目标：1组，8次\n中极目标：2组，各15次\n升级目标：3组，各20次", "初级目标：1组，6次\n中极目标：2组，各10次\n升级目标：2组，各15次", "初级目标：1组，3次\n中极目标：2组，各6次\n升级目标：2组，各10次", "初级目标：1组，2次\n中极目标：2组，各4次\n升级目标：2组，各8次", "初级目标：1组，1次\n中极目标：2组，各3次\n升级目标：2组，各6次", "初级目标：1组，1次\n中极目标：2组，各3次\n升级目标：2组，各30次"},
            {"初级标准：30秒\n中极标准：1分钟\n升级标准：2分钟", "初级标准：10秒\n中极标准：30秒\n升级标准：1分钟", "初级标准：30秒\n中极标准：1分钟\n升级标准：2分钟", "初级目标：1组，5次\n中极目标：2组，各10次\n升级目标：3组，各20次", "初级目标：1组，5次\n中极目标：2组，各10次\n升级目标：3组，各15次", "初级目标：1组，5次\n中极目标：2组，各9次\n升级目标：2组，各12次", "初级目标：1组，5次\n中极目标：2组，各8次\n升级目标：2组，各10次", "初级目标：1组，4次\n中极目标：2组，各6次\n升级目标：2组，各8次", "初级目标：1组，3次\n中极目标：2组，各4次\n升级目标：2组，各6次", "初级目标：1组，1次\n中极目标：2组，各2次\n升级目标：1组，各5次"},
    };

    private RxPermissions mRxPermissions;
    private List<JsonBean> mOptions1Items = new ArrayList<>();
    private List<List<String>> mOptions2Items = new ArrayList<>();
    private List<List<List<Integer>>> mOptions3Items = new ArrayList<>();
    private int mOption1, mOption2, mOption3;
    private TimePickerView pvCustomTime;
    private OptionsPickerView pvOptions;
    private boolean mIsAdd;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.txv_td_up_rule)
    TextView mTxvTdUpRule;
    @BindView(R.id.txv_td_date)
    TextView mTxvTdDate;
    @BindView(R.id.txv_td_program)
    TextView mTxvTdProgram;
    @BindView(R.id.txv_td_level)
    TextView mTxvTdLevel;
    @BindView(R.id.txv_td_group)
    TextView mTxvTdGroup;
    @BindView(R.id.edt_td_count)
    EditText mEdtTdCount;

    @OnClick({R.id.txv_td_program, R.id.txv_td_level, R.id.txv_td_group})
    public void chooseTrainingPicker() {
        if (mIsAdd) {
            showTrainingPickerView();
        }
    }

    @OnClick(R.id.txv_td_date)
    public void chooseTrainingDatePicker() {
        if (mIsAdd) {
            showTrainingDatePickerView();
        }
    }

    @OnClick(R.id.fab_edit_task_done)
    public void saveTrainingDiary() {
        String date = mTxvTdDate.getText().toString();
        String name = mTxvTdProgram.getText().toString();
        String level = mTxvTdLevel.getText().toString();

        Pattern p = Pattern.compile("[^0-9]");
        Matcher m = p.matcher(mTxvTdGroup.getText().toString());
        int groupNo = Integer.valueOf(m.replaceAll("").trim());
        int count = Integer.valueOf(mEdtTdCount.getText().toString());
        TrainingDiary trainingDiary = new TrainingDiary(date, name, level, groupNo, count);
        if (saveTrainingDiary(TrainingDiariesDbHelper.getInstance(SaveTrainingDiaryActivity.this), trainingDiary) < 1) {
            Snackbar.make(mCoordinatorLayout, "数据保存失败", Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(mCoordinatorLayout, "数据保存成功", Snackbar.LENGTH_SHORT).show();
            killMyself();
        }
    }

    private long saveTrainingDiary(TrainingDiariesDbHelper dbHelper, @NonNull TrainingDiary trainingDiary) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long result;

        ContentValues values = new ContentValues();
        values.put(TrainingDiaryEntity.COLUMN_NAME_NAME, trainingDiary.getName());
        values.put(TrainingDiaryEntity.COLUMN_NAME_DATE, trainingDiary.getDate());
        values.put(TrainingDiaryEntity.COLUMN_NAME_LEVEL, trainingDiary.getLevel());
        values.put(TrainingDiaryEntity.COLUMN_NAME_GROUP_NO, trainingDiary.getGroupNo());
        values.put(TrainingDiaryEntity.COLUMN_NAME_COUNT, trainingDiary.getCount());

        if (checkTrainingDiary(db, trainingDiary)) {
            String whereClause = TrainingDiaryEntity.COLUMN_NAME_DATE + " = ? AND "
                    + TrainingDiaryEntity.COLUMN_NAME_NAME + " = ? AND "
                    + TrainingDiaryEntity.COLUMN_NAME_LEVEL + " = ? AND "
                    + TrainingDiaryEntity.COLUMN_NAME_GROUP_NO + " = ?";
            String[] whereClauseArgs = {
                    trainingDiary.getDate(),
                    trainingDiary.getName(),
                    trainingDiary.getLevel(),
                    String.valueOf(trainingDiary.getGroupNo())
            };
            result = db.update(TrainingDiaryEntity.TABLE_NAME, values, whereClause, whereClauseArgs);
        } else {
            result = db.insert(TrainingDiaryEntity.TABLE_NAME, null, values);
        }

        db.close();

        return result;
    }

    public boolean checkTrainingDiary(SQLiteDatabase db, @NonNull TrainingDiary trainingDiary) {
        String whereClause = TrainingDiaryEntity.COLUMN_NAME_DATE + " = ? AND "
                + TrainingDiaryEntity.COLUMN_NAME_NAME + " = ? AND "
                + TrainingDiaryEntity.COLUMN_NAME_LEVEL + " = ? AND "
                + TrainingDiaryEntity.COLUMN_NAME_GROUP_NO + " = ?";
        String[] whereClauseArgs = {
                trainingDiary.getDate(),
                trainingDiary.getName(),
                trainingDiary.getLevel(),
                String.valueOf(trainingDiary.getGroupNo())
        };
        String[] projection = {
                TrainingDiaryEntity.COLUMN_NAME_NAME,
                TrainingDiaryEntity.COLUMN_NAME_DATE,
                TrainingDiaryEntity.COLUMN_NAME_LEVEL,
                TrainingDiaryEntity.COLUMN_NAME_GROUP_NO
        };

        Cursor cursor = db.query(TrainingDiaryEntity.TABLE_NAME, projection, whereClause, whereClauseArgs, null, null, null);
        boolean result = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) {
            cursor.close();
        }
        return result;
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
        mIsAdd = intent.getBooleanExtra("is_add", true);
        String date = intent.getStringExtra(TrainingDiaryEntity.COLUMN_NAME_DATE);
        String name = intent.getStringExtra(TrainingDiaryEntity.COLUMN_NAME_NAME);
        String level = intent.getStringExtra(TrainingDiaryEntity.COLUMN_NAME_LEVEL);
        int groupNo = intent.getIntExtra(TrainingDiaryEntity.COLUMN_NAME_GROUP_NO, -1);
        int count = intent.getIntExtra(TrainingDiaryEntity.COLUMN_NAME_COUNT, -1);

        if (TextUtils.isEmpty(date)) {
            date = getTime(new Date());
        }
        mTxvTdDate.setText(date);
        if (!TextUtils.isEmpty(level)) {
            mTxvTdLevel.setText(level);
        }
        if (groupNo != -1) {
            mTxvTdGroup.setText("第" + groupNo + "组");
        }
        if (count != -1) {
            mEdtTdCount.setText(String.valueOf(count));
        }
        initJsonData();
        if (!TextUtils.isEmpty(name)) {
            mTxvTdProgram.setText(name);
            mTxvTdUpRule.setText(mUpRule[mOption1][mOption2]);
        } else {
            mTxvTdUpRule.setText("初级目标：\n中极目标：\n升级目标：");
        }
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
        this.pvCustomTime = null;
        this.pvOptions = null;
    }

    private void showTrainingDatePickerView() {
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        endDate.set(2099, 11, 31);
        pvCustomTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                mTxvTdDate.setText(getTime(date));
            }
        })
                .setDate(startDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.custom_pickerview_date, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.txv_done);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.imv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.returnData();
                                pvCustomTime.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.dismiss();
                            }
                        });
                    }
                })
                .setType(new boolean[]{true, true, true, false, false, false})
                .isCenterLabel(false)
                .setDividerColor(Color.RED)
                .build();

        pvCustomTime.show();
    }

    private String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
        return format.format(date);
    }

    private void showTrainingPickerView() {
        pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                mTxvTdProgram.setText(mOptions1Items.get(mOption1 = options1).getPickerViewText());
                mTxvTdLevel.setText(mOptions2Items.get(options1).get(mOption2 = options2));
                mTxvTdGroup.setText("第" + mOptions3Items.get(options1).get(options2).get(mOption3 = options3) + "组");
                mTxvTdUpRule.setText(mUpRule[options1][options2]);
            }
        })
                .setTitleText("选择训练项目")
                .setLayoutRes(R.layout.custom_pickerview_training_program, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.txv_done);
                        ImageView imvCancel = (ImageView) v.findViewById(R.id.imv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvOptions.returnData();
                                pvOptions.dismiss();
                            }
                        });

                        imvCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvOptions.dismiss();
                            }
                        });
                    }
                })
//                .isDialog(true)
                .setDividerColor(Color.GRAY)
                .setTextColorCenter(Color.GRAY)
                .setContentTextSize(20)
                .setOutSideCancelable(false)
                .isCenterLabel(false)
                .setLabels("", "", "组")
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
