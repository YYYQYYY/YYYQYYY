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
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                TrainingDiaryEntity.COLUMN_NAME_ID,
                TrainingDiaryEntity.COLUMN_NAME_NAME,
                TrainingDiaryEntity.COLUMN_NAME_DATE,
                TrainingDiaryEntity.COLUMN_NAME_LEVEL,
                TrainingDiaryEntity.COLUMN_NAME_GROUP_NO,
                TrainingDiaryEntity.COLUMN_NAME_COUNT
        };

        Cursor c = db.query(TrainingDiaryEntity.TABLE_NAME, projection, null, null, null, null, null);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                int id = c.getInt(c.getColumnIndexOrThrow(TrainingDiaryEntity.COLUMN_NAME_ID));
                String name = c.getString(c.getColumnIndexOrThrow(TrainingDiaryEntity.COLUMN_NAME_NAME));
                String date = c.getString(c.getColumnIndexOrThrow(TrainingDiaryEntity.COLUMN_NAME_DATE));
                int level = c.getInt(c.getColumnIndexOrThrow(TrainingDiaryEntity.COLUMN_NAME_LEVEL));
                int groupNo = c.getInt(c.getColumnIndexOrThrow(TrainingDiaryEntity.COLUMN_NAME_GROUP_NO));
                int count = c.getInt(c.getColumnIndexOrThrow(TrainingDiaryEntity.COLUMN_NAME_COUNT));
                TrainingDiary TrainingDiary = new TrainingDiary(id, name, date, level, groupNo, count);
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
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                TrainingDiaryEntity.COLUMN_NAME_ID,
                TrainingDiaryEntity.COLUMN_NAME_NAME,
                TrainingDiaryEntity.COLUMN_NAME_DATE,
                TrainingDiaryEntity.COLUMN_NAME_LEVEL,
                TrainingDiaryEntity.COLUMN_NAME_GROUP_NO,
                TrainingDiaryEntity.COLUMN_NAME_COUNT
        };

        Cursor c = db.query(TrainingDiaryEntity.TABLE_NAME, projection, null, null, null, null, null);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                int id = c.getInt(c.getColumnIndexOrThrow(TrainingDiaryEntity.COLUMN_NAME_ID));
                String name = c.getString(c.getColumnIndexOrThrow(TrainingDiaryEntity.COLUMN_NAME_NAME));
                String date = c.getString(c.getColumnIndexOrThrow(TrainingDiaryEntity.COLUMN_NAME_DATE));
                int level = c.getInt(c.getColumnIndexOrThrow(TrainingDiaryEntity.COLUMN_NAME_LEVEL));
                int groupNo = c.getInt(c.getColumnIndexOrThrow(TrainingDiaryEntity.COLUMN_NAME_GROUP_NO));
                int count = c.getInt(c.getColumnIndexOrThrow(TrainingDiaryEntity.COLUMN_NAME_COUNT));
                TrainingDiary TrainingDiary = new TrainingDiary(id, name, date, level, groupNo, count);
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
    public void getTrainingDiaryById(TrainingDiariesDbHelper dbHelper, @NonNull int trainingDiaryId, @NonNull GetTrainingDiaryCallback callback) {

    }

    @Override
    public void saveTrainingDiary(TrainingDiariesDbHelper dbHelper, @NonNull TrainingDiary trainingDiary) {
        checkNotNull(trainingDiary);
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
    public void deleteTrainingDiary(TrainingDiariesDbHelper dbHelper, @NonNull TrainingDiary trainingDiary) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String selection = TrainingDiaryEntity.COLUMN_NAME_ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(trainingDiary.getId())};

        db.delete(TrainingDiaryEntity.TABLE_NAME, selection, selectionArgs);

        db.close();
    }
}
