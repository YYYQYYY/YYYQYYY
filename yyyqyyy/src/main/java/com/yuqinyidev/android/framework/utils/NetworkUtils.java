package com.yuqinyidev.android.framework.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;

/**
 * Created by yuqy on 2017-07-25.
 */

public class NetworkUtils {

    private final static int NT_WIFI = 1;
    private final static int NT_2G = 2;
    private final static int NT_3G = 3;
    private final static int NT_4G = 4;
    private final static int NT_UNKNOWN = 5;

    public static void openWirelessSettings(Context context) {
        if (Build.VERSION.SDK_INT > 10) {
            UiUtils.startActivity(new Intent(Settings.ACTION_SETTINGS));
        } else {
            UiUtils.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
        }
    }

    public static NetworkInfo getActiveNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    public static boolean isAvailable(Context context) {
        NetworkInfo ni = getActiveNetworkInfo(context);
        return ni != null && ni.isAvailable();
    }

    public static boolean isConnected(Context context) {
        NetworkInfo ni = getActiveNetworkInfo(context);
        return ni != null && ni.isConnected();
    }
}
