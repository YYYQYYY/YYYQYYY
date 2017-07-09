package com.yuqinyidev.android.azaz.main.mvp.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yuqinyidev.android.azaz.main.mvp.ui.fragment.FragmentMainDiscover;
import com.yuqinyidev.android.azaz.main.mvp.ui.fragment.FragmentMainHome;
import com.yuqinyidev.android.azaz.main.mvp.ui.fragment.FragmentMainMine;
import com.yuqinyidev.android.azaz.main.mvp.ui.fragment.FragmentMainNotice;

/**
 * Created by RDX64 on 2017/6/22.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private String[] mTitles = new String[]{"首页", "发现", "通知", "我的"};

    public MyFragmentPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                return new FragmentMainDiscover();
            case 2:
                return new FragmentMainNotice();
            case 3:
                return new FragmentMainMine();
        }
        return new FragmentMainHome();
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

}
