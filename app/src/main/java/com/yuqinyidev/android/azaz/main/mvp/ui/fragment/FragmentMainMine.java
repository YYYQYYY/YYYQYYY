package com.yuqinyidev.android.azaz.main.mvp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yuqinyidev.android.azaz.R;
import com.yuqinyidev.android.framework.base.BaseFragment;
import com.yuqinyidev.android.framework.di.component.AppComponent;

import butterknife.BindView;
import jp.wasabeef.glide.transformations.CropTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;

/**
 * Created by RDX64 on 2017/6/22.
 */

public class FragmentMainMine extends BaseFragment {

    @BindView(R.id.fruit_image_view)
    ImageView mFruitImageView;

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {

    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_discover, container, false);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        Glide.with(getActivity()).load(R.drawable.bg)
                .bitmapTransform(new GrayscaleTransformation(getActivity()), new CropTransformation(getActivity(), 560, 396))
                .into(mFruitImageView);
    }

    @Override
    public void setData(Object data) {

    }
}
