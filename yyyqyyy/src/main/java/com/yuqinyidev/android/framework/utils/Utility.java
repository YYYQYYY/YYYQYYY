package com.yuqinyidev.android.framework.utils;

import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.NonNull;

import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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

    /**
     * get sdcard state.
     *
     * @return sdcard can write
     */
    public static boolean isCanWriteSDCard() {
        return Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState());
    }

    /**
     * get sdcard block count.
     *
     * @return sdcard block count
     */
    public static long getBlockCount() {
        if (isCanWriteSDCard()) {
            StatFs statfs = new StatFs(Environment
                    .getExternalStorageDirectory().getPath());
            return statfs.getBlockCount();
        }
        return 0;
    }

    /**
     * get sdcard block size.
     *
     * @return sdcard block size
     */
    public static long getBlockSize() {
        if (isCanWriteSDCard()) {
            StatFs statfs = new StatFs(Environment
                    .getExternalStorageDirectory().getPath());
            return statfs.getBlockSize();
        }
        return 0;
    }

    /**
     * get sdcard available blocks.
     *
     * @return sdcard available blocks
     */
    public static long getAvailableBlocks() {
        if (isCanWriteSDCard()) {
            StatFs statfs = new StatFs(Environment
                    .getExternalStorageDirectory().getPath());
            return statfs.getAvailableBlocks();
        }
        return 0;
    }

    /**
     * get sdcard free blocks.
     *
     * @return free blocks
     */
    public static long getFreeBlocks() {
        if (isCanWriteSDCard()) {
            StatFs statfs = new StatFs(Environment
                    .getExternalStorageDirectory().getPath());
            return statfs.getFreeBlocks();
        }
        return 0;
    }

    /**
     * get sdcard free size.
     *
     * @return sdcard free size
     */
    public static long getFreeSize() {
        if (isCanWriteSDCard()) {
            StatFs statfs = new StatFs(Environment
                    .getExternalStorageDirectory().getPath());
            return statfs.getAvailableBlocks() * statfs.getBlockSize() / 1024
                    / 1024;
        }
        return 0;
    }

    /**
     * get sdcard total size.
     *
     * @return sdcard total size
     */
    public static long getTotalSize() {
        if (isCanWriteSDCard()) {
            StatFs statfs = new StatFs(Environment
                    .getExternalStorageDirectory().getPath());
            return statfs.getBlockCount() * statfs.getBlockSize() / 1024 / 1024;
        }
        return 0;
    }

    /**
     * 遍历 "system/etc/vold.fstab” 文件，获取全部的Android的挂载点信息
     *
     * @return
     */
    private static ArrayList<String> getDevMountList() {
        String[] toSearch = FileUtils.readFile("/system/etc/vold.fstab").split(" ");
        ArrayList<String> out = new ArrayList<String>();
        for (int i = 0; i < toSearch.length; i++) {
            if (toSearch[i].contains("dev_mount")) {
                if (new File(toSearch[i + 2]).exists()) {
                    out.add(toSearch[i + 2]);
                }
            }
        }
        return out;
    }

    /**
     * 获取扩展SD卡存储目录
     * <p>
     * 如果有外接的SD卡，并且已挂载，则返回这个外置SD卡目录
     * 否则：返回内置SD卡目录
     *
     * @return
     */
    public static String getExternalSdCardPath() {

        if (isCanWriteSDCard()) {
            File sdCardFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
            return sdCardFile.getAbsolutePath();
        }

        String path = null;

        File sdCardFile = null;

        ArrayList<String> devMountList = getDevMountList();

        for (String devMount : devMountList) {
            File file = new File(devMount);

            if (file.isDirectory() && file.canWrite()) {
                path = file.getAbsolutePath();

                String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
                File testWritable = new File(path, "test_" + timeStamp);

                if (testWritable.mkdirs()) {
                    testWritable.delete();
                } else {
                    path = null;
                }
            }
        }

        if (path != null) {
            sdCardFile = new File(path);
            return sdCardFile.getAbsolutePath();
        }

        return null;
    }

    /**
     * Using 0~n to generate random sequence number m.
     *
     * @param _limit n-1
     * @param _need  m
     * @return Generated random sequences
     */
    public static int[] getRandomSerial(int _limit, int _need) {
        int[] temp = new int[_limit];
        int[] result = new int[_need];
        for (int i = 0; i < _limit; i++)
            temp[i] = i;
        int w;
        Random rand = new Random();
        for (int i = 0; i < _need; i++) {
            w = rand.nextInt(_limit - i) + i;
            int t = temp[i];
            temp[i] = temp[w];
            temp[w] = t;
            result[i] = temp[i];
        }
        return result;
    }

    public static void removeDuplicate(List arlList) {
        HashSet h = new HashSet(arlList);
        arlList.clear();
        arlList.addAll(h);
    }

}
