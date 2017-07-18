package com.yuqinyidev.android.azaz.main.mvp.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yuqinyidev.android.azaz.R;
import com.yuqinyidev.android.azaz.demo.mvp.presenter.UserPresenter;
import com.yuqinyidev.android.azaz.main.mvp.model.entity.AppItem;
import com.yuqinyidev.android.azaz.main.mvp.ui.adapter.MyFragmentPagerAdapter;
import com.yuqinyidev.android.framework.base.BaseActivity;
import com.yuqinyidev.android.framework.di.component.AppComponent;
import com.yuqinyidev.android.framework.utils.Utility;
import com.yuqinyidev.android.framework.widget.NoScrollViewPager;

import butterknife.BindArray;
import butterknife.BindView;

public class MainMenuActivity extends BaseActivity {
    private MyFragmentPagerAdapter myFragmentPagerAdapter;

    private TabLayout.Tab one;
    private TabLayout.Tab two;
    private TabLayout.Tab three;
    private TabLayout.Tab four;

    @BindView(R.id.main_menu_view_pager)
    NoScrollViewPager mViewPager;

    @BindView(R.id.main_menu_tab_layout)
    TabLayout mTabLayout;

// TODO:测试数组资源   @BindArray(R.array.app_items)
//    String[] appItems;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.act_main_menu;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());

        for (int i = 0; i < myFragmentPagerAdapter.getCount(); i++)
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.main_menu_view_pager, myFragmentPagerAdapter.getItem(i))
                    .commit();
        mViewPager.setEnableScroll(true);
        mViewPager.setAdapter(myFragmentPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);

        one = mTabLayout.getTabAt(0);
        two = mTabLayout.getTabAt(1);
        three = mTabLayout.getTabAt(2);
        four = mTabLayout.getTabAt(3);

        one.setIcon(R.drawable.ic_home_black_24dp);
        two.setIcon(R.drawable.ic_dashboard_black_24dp);
        three.setIcon(R.drawable.ic_notifications_black_24dp);
        four.setIcon(R.drawable.ic_mine_black_24dp);
//        if (appItems != null) {
//            String[] appItem = null;
//            for (String appItemA : appItems) {
//                appItem = appItemA.split(",");
//                try {
//                    if (appItem.length == 3) {
//                        AppItem a = new AppItem(appItem[0], Utility.getDrawable(R.drawable.class, "apple_pic"), Class.forName(appItem[2]));
//                    }
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }

}
