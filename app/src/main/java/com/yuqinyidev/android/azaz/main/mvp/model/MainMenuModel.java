package com.yuqinyidev.android.azaz.main.mvp.model;

import com.yuqinyidev.android.azaz.R;
import com.yuqinyidev.android.azaz.main.mvp.contract.MainMenuContract;
import com.yuqinyidev.android.azaz.main.mvp.model.entity.AppItem;
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
        for (int i = 0; i < 1; i++) {
            appItemList.add(new AppItem("训练日记", R.drawable.apple_pic));
        }
        return appItemList;
    }
}
