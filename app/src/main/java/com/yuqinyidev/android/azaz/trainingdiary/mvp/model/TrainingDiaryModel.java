package com.yuqinyidev.android.azaz.trainingdiary.mvp.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.yuqinyidev.android.azaz.trainingdiary.mvp.contract.TrainingDiaryContract;
import com.yuqinyidev.android.azaz.trainingdiary.mvp.model.entity.TrainingDiary;
import com.yuqinyidev.android.azaz.trainingdiary.mvp.model.entity.TrainingDiaryEntity;
import com.yuqinyidev.android.framework.base.App;
import com.yuqinyidev.android.framework.di.scope.ActivityScope;
import com.yuqinyidev.android.framework.integration.IRepositoryManager;
import com.yuqinyidev.android.framework.mvp.model.BaseModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static android.R.attr.id;
import static com.yuqinyidev.android.framework.utils.Preconditions.checkNotNull;

/**
 * Created by RDX64 on 2017/6/29.
 */
@ActivityScope
public class TrainingDiaryModel extends BaseModel implements TrainingDiaryContract.Model {

    @Inject
    public TrainingDiaryModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public List<TrainingDiary> getTrainingDiaries(TrainingDiariesDbHelper dbHelper) {
        List<TrainingDiary> trainingDiaries = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        checkNotNull(db);

        String[] projection = {
                TrainingDiaryEntity.COLUMN_NAME_NAME,
                TrainingDiaryEntity.COLUMN_NAME_DATE,
                TrainingDiaryEntity.COLUMN_NAME_LEVEL,
                TrainingDiaryEntity.COLUMN_NAME_GROUP_NO,
                TrainingDiaryEntity.COLUMN_NAME_COUNT
        };

        Cursor c = db.query(TrainingDiaryEntity.TABLE_NAME, projection, null, null, null, null, null);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String name = c.getString(c.getColumnIndexOrThrow(TrainingDiaryEntity.COLUMN_NAME_NAME));
                String date = c.getString(c.getColumnIndexOrThrow(TrainingDiaryEntity.COLUMN_NAME_DATE));
                String level = c.getString(c.getColumnIndexOrThrow(TrainingDiaryEntity.COLUMN_NAME_LEVEL));
                int groupNo = c.getInt(c.getColumnIndexOrThrow(TrainingDiaryEntity.COLUMN_NAME_GROUP_NO));
                int count = c.getInt(c.getColumnIndexOrThrow(TrainingDiaryEntity.COLUMN_NAME_COUNT));
                TrainingDiary TrainingDiary = new TrainingDiary(date, name, level, groupNo, count);
                trainingDiaries.add(TrainingDiary);
            }
        }
        if (c != null) {
            c.close();
        }

        db.close();

        return trainingDiaries;
    }

    @Override
    public void getTrainingDiaries(TrainingDiariesDbHelper dbHelper, @NonNull LoadTrainingDiariesCallback callback) {
        checkNotNull(callback);
        List<TrainingDiary> trainingDiaries = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        checkNotNull(db);

        String[] projection = {
                TrainingDiaryEntity.COLUMN_NAME_NAME,
                TrainingDiaryEntity.COLUMN_NAME_DATE,
                TrainingDiaryEntity.COLUMN_NAME_LEVEL,
                TrainingDiaryEntity.COLUMN_NAME_GROUP_NO,
                TrainingDiaryEntity.COLUMN_NAME_COUNT
        };

        Cursor c = db.query(TrainingDiaryEntity.TABLE_NAME, projection, null, null, null, null, TrainingDiaryEntity.COLUMN_NAME_DATE + " DESC");

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String name = c.getString(c.getColumnIndexOrThrow(TrainingDiaryEntity.COLUMN_NAME_NAME));
                String date = c.getString(c.getColumnIndexOrThrow(TrainingDiaryEntity.COLUMN_NAME_DATE));
                String level = c.getString(c.getColumnIndexOrThrow(TrainingDiaryEntity.COLUMN_NAME_LEVEL));
                int groupNo = c.getInt(c.getColumnIndexOrThrow(TrainingDiaryEntity.COLUMN_NAME_GROUP_NO));
                int count = c.getInt(c.getColumnIndexOrThrow(TrainingDiaryEntity.COLUMN_NAME_COUNT));
                TrainingDiary TrainingDiary = new TrainingDiary(date, name, level, groupNo, count);
                trainingDiaries.add(TrainingDiary);
            }
        }
        if (c != null) {
            c.close();
        }

        db.close();

        if (trainingDiaries.isEmpty()) {
            callback.onDataNotAvailable();
        } else {
            callback.onTrainingDiariesLoaded(trainingDiaries);
        }
    }

    @Override
    public void getTrainingDiary(TrainingDiariesDbHelper dbHelper, @NonNull TrainingDiary trainingDiary, @NonNull GetTrainingDiaryCallback callback) {

    }

    @Override
    public void saveTrainingDiary(TrainingDiariesDbHelper dbHelper, @NonNull TrainingDiary trainingDiary) {
        checkNotNull(trainingDiary);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        checkNotNull(db);

        ContentValues values = new ContentValues();
        values.put(TrainingDiaryEntity.COLUMN_NAME_NAME, trainingDiary.getName());
        values.put(TrainingDiaryEntity.COLUMN_NAME_DATE, trainingDiary.getDate());
        values.put(TrainingDiaryEntity.COLUMN_NAME_LEVEL, trainingDiary.getLevel());
        values.put(TrainingDiaryEntity.COLUMN_NAME_GROUP_NO, trainingDiary.getGroupNo());
        values.put(TrainingDiaryEntity.COLUMN_NAME_COUNT, trainingDiary.getCount());

        db.insert(TrainingDiaryEntity.TABLE_NAME, null, values);

        db.close();
    }

    @Override
    public void deleteTrainingDiary(TrainingDiariesDbHelper dbHelper, @NonNull TrainingDiary trainingDiary) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        checkNotNull(db);
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

        db.delete(TrainingDiaryEntity.TABLE_NAME, whereClause, whereClauseArgs);

        db.close();
    }
}
