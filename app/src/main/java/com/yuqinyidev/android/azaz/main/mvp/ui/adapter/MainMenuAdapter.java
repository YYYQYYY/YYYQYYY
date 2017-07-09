package com.yuqinyidev.android.azaz.main.mvp.ui.adapter;

import android.view.View;

import com.yuqinyidev.android.azaz.R;
import com.yuqinyidev.android.azaz.main.mvp.model.entity.AppItem;
import com.yuqinyidev.android.azaz.main.mvp.ui.holder.MainMenuItemHolder;
import com.yuqinyidev.android.framework.base.BaseHolder;
import com.yuqinyidev.android.framework.base.DefaultAdapter;

import java.util.List;

/**
 * Created by RDX64 on 2017/7/2.
 */

public class MainMenuAdapter extends DefaultAdapter<AppItem> {

    public MainMenuAdapter(List<AppItem> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<AppItem> getHolder(View view, int viewType) {
        return new MainMenuItemHolder(view);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_main_menu_home;
    }
}
