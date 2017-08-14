package com.yuqinyidev.android.framework.utils;

import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

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

    /**
     * 去除List的重复项
     *
     * @param arlList
     */
    public static void removeDuplicate(List arlList) {
        HashSet h = new HashSet(arlList);
        arlList.clear();
        arlList.addAll(h);
    }

    /**
     * csvPath : csv路径，例如 G:\SenimarInfo.csv
     */
//    private void getCSV(String csvPath) {
//        ArrayList<SeminarInfo> dataLists = new ArrayList<SeminarInfo>();
//        ArrayList<String> lists = new ArrayList<String>();
//        SeminarInfo mSeminarInfo = new SeminarInfo();
//        if (dataLists != null) {
//            dataLists.clear();
//        }
//        mSeminarInfoDB = new SeminarInfoDBHelper(mContext);
//        db = mSeminarInfoDB.getWritableDatabase();
//
//        try {
//            BufferedReader br = new BufferedReader(new FileReader(csvFile));
//            String line = "";
//            int i = 0;
//            while ((line = br.readLine()) != null) {//一次一行，lists.size()=14,28,42...
//                // 把一行数据分割成多个字段
//                StringTokenizer st = new StringTokenizer(line, "|");
//                while (st.hasMoreTokens()) {//一次一个 lists.size()=1
//                    String str = st.nextToken();
//                    lists.add(str);
//                    //    System.out.println("_______________tokens__________________"+str);
//                }
//                System.out.println("_______________size__________________" + lists.size());
//                if (lists.size() > 14) {
//                    mSeminarInfo.setId(lists.get(14 + i));
//                    mSeminarInfo.setEventID(lists.get(15 + i));
//                    mSeminarInfo.setCompanyID(lists.get(16 + i));
//                    mSeminarInfo.setBooth(lists.get(17 + i));
//                    mSeminarInfo.setDate(lists.get(18 + i));
//                    mSeminarInfo.setTime(lists.get(19 + i));
//                    mSeminarInfo.setHall(lists.get(20 + i));
//                    mSeminarInfo.setRoomNo(lists.get(21 + i));
//                    mSeminarInfo.setPresentCompany(lists.get(22 + i));
//                    mSeminarInfo.setTopic(lists.get(23 + i));
//                    mSeminarInfo.setSpeaker(lists.get(24 + i));
//                    mSeminarInfo.setLangID(lists.get(25 + i));
//                    dataLists.add(mSeminarInfo);
//                    i = i + 14;
//                    System.out.println("dataLists.toString()——————>" + dataLists.toString());
//                }
//
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
