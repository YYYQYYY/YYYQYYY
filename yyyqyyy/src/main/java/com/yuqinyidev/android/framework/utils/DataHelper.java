package com.yuqinyidev.android.framework.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 提供SharedPreferences,File共通操作
 * <p>
 * Created by RDX64 on 2017/6/28.
 */

public class DataHelper {
    private static SharedPreferences mSharedPreferences;
    private static final String SP_NAME = "config";

    private DataHelper() {
    }

    /**
     * 存储字符串对象到SharedPreferences
     *
     * @param key   键
     * @param value 值
     */
    public static void putStringSP(Context context, String key, String value) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        mSharedPreferences.edit().putString(key, value).apply();
    }

    /**
     * 从SharedPreferences取得存储字符串对象
     *
     * @param key 键
     * @return
     */
    public static String getStringSP(Context context, String key) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        return mSharedPreferences.getString(key, null);
    }

    /**
     * 存储整形数值对象到SharedPreferences
     *
     * @param key   键
     * @param value 值
     */
    public static void putIntegerSP(Context context, String key, int value) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        mSharedPreferences.edit().putInt(key, value).apply();
    }

    /**
     * 从SharedPreferences取得存储整形数值对象
     *
     * @param key 键
     * @return
     */
    public static int getIntegerSP(Context context, String key) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        return mSharedPreferences.getInt(key, -1);
    }

    /**
     * 从SharedPreferences删除对象
     *
     * @param key 键
     */
    public static void removeSP(Context context, String key) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        mSharedPreferences.edit().remove(key).apply();
    }

    /**
     * 清除SharedPreferences
     */
    public static void clearSP(Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        mSharedPreferences.edit().clear().apply();
    }

    /**
     * 存储Object对象到SharedPreferences
     *
     * @param key    键
     * @param object 对象
     * @return
     */
    public static <T> boolean pubObject(Context context, String key, T object) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            String strBase64 = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            mSharedPreferences.edit().putString(key, strBase64).apply();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 从SharedPreferences取得存储整形数值对象
     *
     * @param key 键
     * @param <T>
     * @return
     */
    public static <T> T getObject(Context context, String key) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        T object = null;
        String objBase64 = mSharedPreferences.getString(key, null);
        if (objBase64 != null) {
            byte[] base64 = Base64.decode(objBase64.getBytes(), Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(base64);
            try {
                ObjectInputStream ois = new ObjectInputStream(bais);
                object = (T) ois.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return object;
    }

    /**
     * 获取缓存文件夹
     *
     * @return 缓存文件夹
     */
    public static File getCacheFile(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File file = null;
            file = context.getExternalCacheDir();
            if (file == null) {
                file = new File(getCacheFilePath(context));
                makeDirs(file);
            }
            return file;
        } else {
            return context.getCacheDir();
        }
    }

    /**
     * 获取应用缓存文件地址
     *
     * @return 应用缓存文件地址
     */
    @NonNull
    public static String getCacheFilePath(Context context) {
        String pkgName = context.getPackageName();
        return "/mnt/sdcard/".concat(pkgName);
    }

    /**
     * 创建文件夹
     *
     * @param file
     * @return
     */
    public static File makeDirs(File file) {
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 获取文件夹空间大小（包含子文件夹）
     *
     * @param dir 基准文件夹
     * @return 文件夹空间大小
     */
    public static long getDirectorySize(File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += getDirectorySize(file);
            }
        }
        return dirSize;
    }

    /**
     * 删除文件夹（包含子文件夹）
     *
     * @param dir
     * @return
     */
    public static boolean deleteDirectory(File dir) {
        if (dir == null) {
            return false;
        }
        if (!dir.isDirectory()) {
            return false;
        }

        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                deleteDirectory(file);
            }
        }
        return true;
    }

    public static String byteToString(InputStream _is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int size = 0;
        while ((size = _is.read(buffer)) != -1) {
            baos.write(buffer, 0, buffer.length);
        }
        String result = baos.toString();
        baos.close();
        return result;
    }
}