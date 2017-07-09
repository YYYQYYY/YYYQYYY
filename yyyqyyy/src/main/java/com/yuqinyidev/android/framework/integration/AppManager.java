package com.yuqinyidev.android.framework.integration;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.view.View;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

/**
 * Created by RDX64 on 2017/6/27.
 */
@Singleton
public final class AppManager {
    protected final String TAG = this.getClass().getSimpleName();
    public static final String APP_MANAGER_MESSAGE = "app_managermessage";
    public static final String IS_NOT_ADD_ACTIVITY_LIST = "is_not_add_activity_list";
    public static final int START_ACTIVITY = 0;
    public static final int SHOW_SNACK_BAR = 1;
    public static final int KILL_ALL = 2;
    public static final int EXIT_APPLICATION = 3;

    public List<Activity> mActivityList;

    private Application mApplication;
    private Activity mCurrentActivity;

    @Inject
    public AppManager(Application application) {
        this.mApplication = application;
        EventBus.getDefault().register(this);
    }

    /**
     * 通过EventBus post事件,远程遥控执行对应方法
     *
     * @param message
     */
    @Subscriber(tag = APP_MANAGER_MESSAGE, mode = ThreadMode.MAIN)
    public void onReceive(Message message) {
        switch (message.what) {
            case START_ACTIVITY:
                if (message.obj != null) {
                    dispatchStart(message);
                }
                break;
            case SHOW_SNACK_BAR:
                if (message.obj != null) {
                    showSnackbar((String) message.obj, message.arg1 != 0);
                }
                break;
            case KILL_ALL:
                killAll();
                break;
            case EXIT_APPLICATION:
                exitApplication();
                break;
            default:
                Timber.tag(TAG).w("The message.what not match");
                break;
        }
    }

    /**
     * 结束所有Activity
     */
    public void killAll() {
        Iterator<Activity> iterator = getActivityList().iterator();
        while (iterator.hasNext()) {
            Activity next = iterator.next();
            iterator.remove();
            next.finish();
        }
    }

    /**
     * 退出应用
     */
    public void exitApplication() {
        try {
            killAll();
            if (mActivityList != null) {
                mActivityList = null;
            }
            ActivityManager activityManager = (ActivityManager) mApplication.
                    getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.killBackgroundProcesses(mApplication.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        EventBus.getDefault().unregister(this);
        mActivityList.clear();
        mActivityList = null;
        mCurrentActivity = null;
        mApplication = null;
    }

    /**
     * 显示Snackbar
     *
     * @param message
     * @param isLong
     */
    public void showSnackbar(String message, boolean isLong) {
        if (getCurrentActivity() == null) {
            Timber.tag(TAG).w("Current activity is null when shwoSnackbar");
            return;
        }
        View view = getCurrentActivity().getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar.make(view, message, isLong ? Snackbar.LENGTH_LONG : Snackbar.LENGTH_SHORT).show();
    }

    /**
     * 保存当前的Activity
     *
     * @param currentActivity
     */
    public void setCurrentActivity(Activity currentActivity) {
        this.mCurrentActivity = currentActivity;
    }

    /**
     * 获取当前的Activity
     *
     * @return
     */
    public Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    /**
     * 获取Activity列表
     *
     * @return
     */
    public List<Activity> getActivityList() {
        if (mActivityList == null) {
            mActivityList = new LinkedList<>();
        }
        return mActivityList;
    }

    /**
     * 添加Activity到Activity列表
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        if (mActivityList == null) {
            mActivityList = new LinkedList<>();
        }
        synchronized (AppManager.class) {
            if (!mActivityList.contains(activity)) {
                mActivityList.add(activity);
            }
        }
    }

    /**
     * 从Activity列表中移除Activity
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        if (mActivityList == null) {
            Timber.tag(TAG).w("ActivityList is null when removeActivity");
            return;
        }
        synchronized (AppManager.class) {
            if (mActivityList.contains(activity)) {
                mActivityList.remove(activity);
            }
        }
    }

    /**
     * 从Activity列表中移除指定位置的Activity
     *
     * @param position
     * @return
     */
    public Activity removeActivity(int position) {
        if (mActivityList == null) {
            Timber.tag(TAG).w("ActivityList is null when removeActivity");
            return null;
        }
        synchronized (AppManager.class) {
            if (position > 0 && position < mActivityList.size()) {
                return mActivityList.remove(position);
            }
        }
        return null;
    }

    /**
     * 结束指定Activity
     *
     * @param activityClass
     */
    public void killActivity(Class<?> activityClass) {
        if (mActivityList == null) {
            Timber.tag(TAG).w("ActivityList is null when killActivity");
            return;
        }
        for (Activity activity : mActivityList) {
            if (activity.getClass().equals(activityClass)) {
                activity.finish();
            }
        }
    }

    /**
     * 判断指定Activity实例是否存活
     *
     * @param activity
     * @return
     */
    public boolean isLivedActivity(Activity activity) {
        if (mActivityList == null) {
            Timber.tag(TAG).w("ActivityList is null when isLivedActivity");
            return false;
        }
        return mActivityList.contains(activity);
    }

    public boolean isLivedActivityClass(Class<?> activityClass) {
        if (mActivityList == null) {
            Timber.tag(TAG).w("ActivityList is null when isLivedActivityClass");
            return false;
        }
        for (Activity activity : mActivityList) {
            if (activity.getClass().equals(activityClass)) {
                return true;
            }
        }
        return false;
    }

    private void dispatchStart(Message message) {
        if (message.obj instanceof Intent) {
            startActivity((Intent) message.obj);
        } else if (message.obj instanceof Class) {
            startActivity((Class) message.obj);
        }
    }

    /**
     * 启动Activity
     *
     * @param intent
     */
    public void startActivity(Intent intent) {
        if (getCurrentActivity() == null) {
            Timber.tag(TAG).w("Current activity is null when startActivity");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mApplication.startActivity(intent);
            return;
        }
        getCurrentActivity().startActivity(intent);
    }

    public void startActivity(Class activityClass) {
        startActivity(new Intent(mApplication, activityClass));
    }
}
