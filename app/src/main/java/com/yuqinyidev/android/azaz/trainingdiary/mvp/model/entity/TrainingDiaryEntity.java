package com.yuqinyidev.android.azaz.trainingdiary.mvp.model.entity;

import android.provider.BaseColumns;

/**
 * Created by RDX64 on 2017/6/29.
 */

public class TrainingDiaryEntity implements BaseColumns {
    public static final String TABLE_NAME = "TrainingDiaries";
    public static final String COLUMN_NAME_NAME = "td_name";
    public static final String COLUMN_NAME_DATE = "td_date";
    public static final String COLUMN_NAME_LEVEL = "td_level";
    public static final String COLUMN_NAME_GROUP_NO = "td_group_no";
    public static final String COLUMN_NAME_COUNT = "td_count";
}
