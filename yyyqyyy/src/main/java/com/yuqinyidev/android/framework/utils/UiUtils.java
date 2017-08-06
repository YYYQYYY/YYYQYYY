package com.yuqinyidev.android.framework.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.simple.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.yuqinyidev.android.framework.integration.AppManager.APP_MANAGER_MESSAGE;
import static com.yuqinyidev.android.framework.integration.AppManager.EXIT_APPLICATION;
import static com.yuqinyidev.android.framework.integration.AppManager.KILL_ALL;
import static com.yuqinyidev.android.framework.integration.AppManager.SHOW_SNACK_BAR;
import static com.yuqinyidev.android.framework.integration.AppManager.START_ACTIVITY;

/**
 * Created by RDX64 on 2017/6/29.
 */

public class UiUtils {
    static public Toast mToast;

    private UiUtils() {
    }

    public static void setViewHintSize(Context context, int size, TextView v, int res) {
        SpannableString ss = new SpannableString(getResources(context).getString(res));

        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(size, true);
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        v.setHint(new SpannedString(ss));
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = getResources(context).getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5F);
    }

    public static Resources getResources(Context context) {
        return context.getResources();
    }

    public static String[] getStringArray(Context context, int id) {
        return getResources(context).getStringArray(id);
    }

    public static int pix2dip(Context context, int pixValue) {
        final float densityDpi = getResources(context).getDisplayMetrics().density;
        return (int) (pixValue / densityDpi + 0.5F);
    }

    public static int getDimens(Context context, int homePicHeight) {
        return (int) getResources(context).getDimension(homePicHeight);
    }

    public static float getDimens(Context context, String dimenName) {
        return getResources(context).getDimension(getResources(context)
                .getIdentifier(dimenName, "dimen", context.getPackageName()));
    }

    public static String getString(Context context, int stringId) {
        return getResources(context).getString(stringId);
    }

    public static String getString(Context context, String stringName) {
        return getString(context, getResources(context).getIdentifier(stringName, "string", context.getPackageName()));
    }

    public static <T extends View> T findViewByName(Context context, View view, String viewName) {
        int id = getResources(context).getIdentifier(viewName, "id", context.getPackageName());
        return (T) view.findViewById(id);
    }

    public static <T extends View> T findViewByName(Context context, Activity view, String viewName) {
        int id = getResources(context).getIdentifier(viewName, "id", context.getPackageName());
        return (T) view.findViewById(id);
    }

    public static int findLayout(Context context, String layoutName) {
        int id = getResources(context).getIdentifier(layoutName, "layout", context.getPackageName());
        return id;
    }

    public static View inflate(Context context, int detailScreen) {
        return View.inflate(context, detailScreen, null);
    }

    public static void makeText(Context context, String message) {
        if (mToast == null) {
            mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        }
        mToast.setText(message);
        mToast.show();
    }

    public static void snackbarText(String message) {
        Message msg = new Message();
        msg.what = SHOW_SNACK_BAR;
        msg.obj = message;
        msg.arg1 = 0;
        EventBus.getDefault().post(msg, APP_MANAGER_MESSAGE);
    }

    public static void snackbarTextWithLong(String message) {
        Message msg = new Message();
        msg.what = SHOW_SNACK_BAR;
        msg.obj = message;
        msg.arg1 = 1;
        EventBus.getDefault().post(msg, APP_MANAGER_MESSAGE);
    }

    public static Drawable getDrawableByResource(Context context, int resourceId) {
        return getResources(context).getDrawable(resourceId);
    }

    public static void startActivity(Activity activity, Class homeActivityClass) {
        Intent intent = new Intent(activity.getApplicationContext(), homeActivityClass);
        activity.startActivity(intent);
    }

    public static void startActivity(Class homeActivityClass) {
        Message message = new Message();
        message.what = START_ACTIVITY;
        message.obj = homeActivityClass;
        EventBus.getDefault().post(message, APP_MANAGER_MESSAGE);
    }

    public static void startActivity(Intent intent) {
        Message message = new Message();
        message.what = START_ACTIVITY;
        message.obj = intent;
        EventBus.getDefault().post(message, APP_MANAGER_MESSAGE);
    }

    public static void startActivity(Activity activity, Intent intent) {
        activity.startActivity(intent);
    }

    public static int getLayoutId(Context context, String layoutName) {
        return getResources(context).getIdentifier(layoutName, "layout", context.getPackageName());
    }

    public static int getScreenWidth(Context context) {
        return getResources(context).getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return getResources(context).getDisplayMetrics().heightPixels;
    }

    public static int getColor(Context context, int colorId) {
        return getResources(context).getColor(colorId);
    }

    public static int getColor(Context context, String colorName) {
        return getColor(context, getResources(context).getIdentifier(colorName, "color", context.getPackageName()));
    }

    public static void removeChild(View view) {
        ViewParent parent = view.getParent();
        if (parent instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) parent;
            group.removeView(view);
        }
    }

    public static boolean isEmpty(Object obj) {
        return obj == null;
    }

    public static String encodeToMD5(String value) {
        byte[] hash = new byte[0];
        try {
            hash = MessageDigest.getInstance("MD5").digest(value.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    public static void statusInScreen(Activity activity) {
        WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
        attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        activity.getWindow().setAttributes(attrs);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void transparentStatus(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public static void fullScreen(Activity activity) {
        WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static void configRecycleView(final RecyclerView recyclerView,
                                         RecyclerView.LayoutManager layoutManager) {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public static void killAll() {
        Message message = new Message();
        message.what = KILL_ALL;
        EventBus.getDefault().post(message, APP_MANAGER_MESSAGE);
    }

    public static void exitApplication() {
        Message message = new Message();
        message.what = EXIT_APPLICATION;
        EventBus.getDefault().post(message, APP_MANAGER_MESSAGE);
    }
}
