package com.yuqinyidev.android.framework.utils;

import android.support.annotation.NonNull;

import com.yuqinyidev.android.framework.R;

import java.lang.reflect.Field;
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

    public static int getDrawable(Class<?> clazz, String id) {
        Field f;
        try {
            f = clazz.getField(id);
            return f.getInt(null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
