package com.yuqinyidev.android.framework.widget;

/**
 * Created by yuqy on 2017-07-16.
 */

import android.view.View;

public interface RecyListViewOnItemClick {

    void onItemClick(View view, int position);

    void onDeleteClick(int position);

}

