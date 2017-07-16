package com.yuqinyidev.android.azaz.trainingdiary.mvp.contract;

import android.support.annotation.NonNull;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yuqinyidev.android.azaz.trainingdiary.mvp.model.TrainingDiariesDbHelper;
import com.yuqinyidev.android.azaz.trainingdiary.mvp.model.entity.TrainingDiary;
import com.yuqinyidev.android.framework.base.DefaultAdapter;
import com.yuqinyidev.android.framework.mvp.model.IModel;
import com.yuqinyidev.android.framework.mvp.view.IView;

import java.util.List;

/**
 * Created by RDX64 on 2017/6/29.
 */

public interface TrainingDiaryContract {
    interface View extends IView {
        void setAdapter(DefaultAdapter adapter);

//        void showTrainingDiaries(List<TrainingDiary> trainingDiaries);

        void showTrainingDiary(TrainingDiary trainingDiary);

        RxPermissions getRxPermissions();
    }

    interface Model extends IModel {
        interface LoadTrainingDiariesCallback {

            void onTrainingDiariesLoaded(List<TrainingDiary> trainingDiaries);

            void onDataNotAvailable();
        }

        interface GetTrainingDiaryCallback {

            void onTrainingDiaryLoaded(TrainingDiary TrainingDiary);

            void onDataNotAvailable();
        }

        List<TrainingDiary> getTrainingDiaries(TrainingDiariesDbHelper dbHelper);

        void getTrainingDiaries(TrainingDiariesDbHelper dbHelper, @NonNull LoadTrainingDiariesCallback callback);

        void getTrainingDiary(TrainingDiariesDbHelper dbHelper, @NonNull TrainingDiary trainingDiary, @NonNull GetTrainingDiaryCallback callback);

        void saveTrainingDiary(TrainingDiariesDbHelper dbHelper, @NonNull TrainingDiary trainingDiary);

        void deleteTrainingDiary(TrainingDiariesDbHelper dbHelper, @NonNull TrainingDiary trainingDiary);
    }
}
