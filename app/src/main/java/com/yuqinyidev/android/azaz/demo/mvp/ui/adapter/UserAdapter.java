package com.yuqinyidev.android.azaz.demo.mvp.ui.adapter;

import android.view.View;

import com.yuqinyidev.android.framework.base.BaseHolder;
import com.yuqinyidev.android.framework.base.DefaultAdapter;
import com.yuqinyidev.android.azaz.R;
import com.yuqinyidev.android.azaz.demo.mvp.model.entity.User;
import com.yuqinyidev.android.azaz.demo.mvp.ui.holder.UserItemHolder;

import java.util.List;

/**
 * Created by RDX64 on 2017/6/29.
 */

public class UserAdapter extends DefaultAdapter<User> {

    public UserAdapter(List<User> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<User> getHolder(View view, int viewType) {
        return new UserItemHolder(view);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_demo_user;
    }
}
