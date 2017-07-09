package com.yuqinyidev.android.azaz.trainingdiary.mvp.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.yuqinyidev.android.azaz.R;
import com.yuqinyidev.android.azaz.trainingdiary.mvp.model.entity.TrainingDiary;
import com.yuqinyidev.android.framework.base.App;
import com.yuqinyidev.android.framework.base.BaseHolder;
import com.yuqinyidev.android.framework.di.component.AppComponent;

import butterknife.BindView;
import io.reactivex.Observable;

/**
 * Created by RDX64 on 2017/6/29.
 */

public class TrainingDiaryItemHolder extends BaseHolder<TrainingDiary> {
    private AppComponent mAppComponent;
    @BindView(R.id.txv_training_dairy_info)
    TextView mTxvInfo;

    public TrainingDiaryItemHolder(View itemView) {
        super(itemView);
        mAppComponent = ((App) itemView.getContext().getApplicationContext()).getAppComponent();
    }

    @Override
    public void setData(TrainingDiary data, int position) {
        String tdInfo = data.getDate() + "，" + data.getName() + "，第" + data.getLevel() + "式，第" + data.getGroupNo() + "组，共练习" + data.getCount() + "次";
        Observable.just(tdInfo).subscribe(s -> mTxvInfo.setText(s));
    }

    @Override
    protected void onRelease() {
    }
}
