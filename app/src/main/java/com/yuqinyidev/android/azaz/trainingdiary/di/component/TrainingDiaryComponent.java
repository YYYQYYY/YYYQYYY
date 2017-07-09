package com.yuqinyidev.android.azaz.trainingdiary.di.component;

import com.yuqinyidev.android.azaz.trainingdiary.di.module.TrainingDiaryModule;
import com.yuqinyidev.android.azaz.trainingdiary.mvp.ui.activity.TrainingDiaryActivity;
import com.yuqinyidev.android.framework.di.component.AppComponent;
import com.yuqinyidev.android.framework.di.scope.ActivityScope;

import dagger.Component;

/**
 * Created by RDX64 on 2017/6/29.
 */
@ActivityScope
@Component(modules = TrainingDiaryModule.class, dependencies = AppComponent.class)
public interface TrainingDiaryComponent {
    void inject(TrainingDiaryActivity activity);
}
