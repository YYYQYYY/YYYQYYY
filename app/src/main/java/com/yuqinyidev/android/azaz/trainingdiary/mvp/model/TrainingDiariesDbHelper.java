package com.yuqinyidev.android.azaz.trainingdiary.mvp.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.yuqinyidev.android.azaz.trainingdiary.mvp.model.entity.TrainingDiaryEntity;
import com.yuqinyidev.android.framework.utils.SQLiteOpenHelper;

/**
 * Created by RDX64 on 2017/7/8.
 */

public class TrainingDiariesDbHelper extends SQLiteOpenHelper {
    private static TrainingDiariesDbHelper INSTANCE;

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "TrainingDiaries.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String INTEGER_TYPE = " INTEGER";

    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TrainingDiaryEntity.TABLE_NAME + " (" +
                    TrainingDiaryEntity.COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
                    TrainingDiaryEntity.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    TrainingDiaryEntity.COLUMN_NAME_LEVEL + TEXT_TYPE + COMMA_SEP +
                    TrainingDiaryEntity.COLUMN_NAME_GROUP_NO + INTEGER_TYPE + COMMA_SEP +
                    TrainingDiaryEntity.COLUMN_NAME_COUNT + INTEGER_TYPE + COMMA_SEP +
                    " PRIMARY KEY (" + TrainingDiaryEntity.COLUMN_NAME_DATE + "," +
                    TrainingDiaryEntity.COLUMN_NAME_NAME + "," +
                    TrainingDiaryEntity.COLUMN_NAME_LEVEL + "," +
                    TrainingDiaryEntity.COLUMN_NAME_GROUP_NO +
                    "))";

    private TrainingDiariesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static TrainingDiariesDbHelper getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new TrainingDiariesDbHelper(context);
        }
        return INSTANCE;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
