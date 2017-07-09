package com.yuqinyidev.android.azaz.trainingdiary.mvp.ui.adapter;

import android.view.View;

import com.yuqinyidev.android.azaz.trainingdiary.mvp.model.entity.TrainingDiary;
import com.yuqinyidev.android.azaz.trainingdiary.mvp.ui.holder.TrainingDiaryItemHolder;
import com.yuqinyidev.android.framework.base.BaseHolder;
import com.yuqinyidev.android.framework.base.DefaultAdapter;
import com.yuqinyidev.android.azaz.R;

import java.util.List;

/**
 * Created by RDX64 on 2017/6/29.
 */

public class TrainingDiaryAdapter extends DefaultAdapter<TrainingDiary> {

    public TrainingDiaryAdapter(List<TrainingDiary> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<TrainingDiary> getHolder(View view, int viewType) {
        return new TrainingDiaryItemHolder(view);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_training_diary;
    }
}
