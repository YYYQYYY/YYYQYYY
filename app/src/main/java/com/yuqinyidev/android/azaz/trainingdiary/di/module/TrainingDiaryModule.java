package com.yuqinyidev.android.azaz.trainingdiary.di.module;

import com.yuqinyidev.android.azaz.trainingdiary.mvp.contract.TrainingDiaryContract;
import com.yuqinyidev.android.azaz.trainingdiary.mvp.model.TrainingDiaryModel;
import com.yuqinyidev.android.framework.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by RDX64 on 2017/6/29.
 */
@Module
public class TrainingDiaryModule {
    private TrainingDiaryContract.View view;

    public TrainingDiaryModule(TrainingDiaryContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    TrainingDiaryContract.View provideTrainingDiaryView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    TrainingDiaryContract.Model provideTrainingDiaryModel(TrainingDiaryModel model) {
        return model;
    }
}
