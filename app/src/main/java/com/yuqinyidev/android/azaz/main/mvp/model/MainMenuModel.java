package com.yuqinyidev.android.azaz.main.mvp.model;

import com.yuqinyidev.android.azaz.R;
import com.yuqinyidev.android.azaz.kanbook.mvp.ui.activity.KBMainActivity;
import com.yuqinyidev.android.azaz.main.mvp.contract.MainMenuContract;
import com.yuqinyidev.android.azaz.main.mvp.model.entity.AppItem;
import com.yuqinyidev.android.azaz.trainingdiary.mvp.ui.activity.TrainingDiaryActivity;
import com.yuqinyidev.android.azaz.weather.mvp.ui.activity.WeatherMainActivity;
import com.yuqinyidev.android.framework.di.scope.FragmentScope;
import com.yuqinyidev.android.framework.integration.IRepositoryManager;
import com.yuqinyidev.android.framework.mvp.model.BaseModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by RDX64 on 2017/7/2.
 */
@FragmentScope
public class MainMenuModel extends BaseModel implements MainMenuContract.Model {

    @Inject
    public MainMenuModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public List<AppItem> getAppItems() {
        List<AppItem> appItemList = new ArrayList<>();

        appItemList.add(new AppItem("训练日记", R.drawable.apple_pic, TrainingDiaryActivity.class));
        appItemList.add(new AppItem("天气预报", R.drawable.apple_pic, WeatherMainActivity.class));
        appItemList.add(new AppItem("晨光看书", R.drawable.apple_pic, KBMainActivity.class));

        return appItemList;
    }
}
