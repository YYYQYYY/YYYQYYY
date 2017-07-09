package com.yuqinyidev.android.framework.utils;

import android.support.annotation.NonNull;

import java.util.Random;

/**
 * Created by RDX64 on 2017/7/2.
 */

public class Utility {

    @NonNull
    public static String getRandomLengthName(String name) {
        Random random = new Random();
        int length = random.nextInt(20) + 1;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(name);
        }
        return stringBuilder.toString();
    }

}
