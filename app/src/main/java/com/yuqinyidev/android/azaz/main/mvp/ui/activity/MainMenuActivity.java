package com.yuqinyidev.android.azaz.main.mvp.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.yuqinyidev.android.azaz.R;
import com.yuqinyidev.android.azaz.kanbook.KBConstants;
import com.yuqinyidev.android.azaz.main.mvp.ui.adapter.MyFragmentPagerAdapter;
import com.yuqinyidev.android.framework.base.BaseActivity;
import com.yuqinyidev.android.framework.di.component.AppComponent;
import com.yuqinyidev.android.framework.utils.UiUtils;
import com.yuqinyidev.android.framework.widget.NoScrollViewPager;

import butterknife.BindView;

import static com.yuqinyidev.android.framework.utils.UiUtils.killAll;

public class MainMenuActivity extends BaseActivity {
    public static final String ACTION_ADD_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
    public static final String ACTION_REMOVE_SHORTCUT = "com.android.launcher.action.UNINSTALL_SHORTCUT";

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

    private boolean isClickedExit = false;

    @Override
    public void onBackPressed() {
        if (isClickedExit) {
            isClickedExit = false;
            killAll();
            System.exit(0);
        } else {
            Toast toast = Toast.makeText(MainMenuActivity.this,
                    KBConstants.EXIT_TOAST_DETAIL, Toast.LENGTH_LONG);
            DisplayMetrics dm = getResources().getDisplayMetrics();
            int mScreenHeight = dm.heightPixels;
            toast.setGravity(0, 0, mScreenHeight / 3);
            toast.show();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    isClickedExit = false;
                }
            }, 2000);
            isClickedExit = true;
        }
    }

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        UiUtils.transparentStatus(MainMenuActivity.this);
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

    private void addShortcut(String name) {
        Intent addShortcutIntent = new Intent(ACTION_ADD_SHORTCUT);

        // 不允许重复创建
        addShortcutIntent.putExtra("duplicate", false);// 经测试不是根据快捷方式的名字判断重复的
        // 应该是根据快链的Intent来判断是否重复的,即Intent.EXTRA_SHORTCUT_INTENT字段的value
        // 但是名称不同时，虽然有的手机系统会显示Toast提示重复，仍然会建立快链
        // 屏幕上没有空间时会提示
        // 注意：重复创建的行为MIUI和三星手机上不太一样，小米上似乎不能重复创建快捷方式

        // 名字
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);

        // 图标
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(MainMenuActivity.this,
                        R.mipmap.ic_launcher));

        // 设置关联程序
        Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
        launcherIntent.setClass(MainMenuActivity.this, MainMenuActivity.class);
        launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        addShortcutIntent
                .putExtra(Intent.EXTRA_SHORTCUT_INTENT, launcherIntent);

        // 发送广播
        sendBroadcast(addShortcutIntent);
    }

    private void removeShortcut(String name) {
        // remove shortcut的方法在小米系统上不管用，在三星上可以移除
        Intent intent = new Intent(ACTION_REMOVE_SHORTCUT);

        // 名字
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);

        // 设置关联程序
        Intent launcherIntent = new Intent(MainMenuActivity.this,
                MainMenuActivity.class).setAction(Intent.ACTION_MAIN);

        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launcherIntent);

        // 发送广播
        sendBroadcast(intent);
    }

    private boolean hasInstallShortcut(String name) {

        boolean hasInstall = false;

        final String AUTHORITY = "com.android.launcher2.settings";
        Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
                + "/favorites?notify=true");

        // 这里总是failed to find provider info
        // com.android.launcher2.settings和com.android.launcher.settings都不行
        Cursor cursor = this.getContentResolver().query(CONTENT_URI,
                new String[]{"title", "iconResource"}, "title=?",
                new String[]{name}, null);

        if (cursor != null && cursor.getCount() > 0) {
            hasInstall = true;
        }

        return hasInstall;

    }

}
