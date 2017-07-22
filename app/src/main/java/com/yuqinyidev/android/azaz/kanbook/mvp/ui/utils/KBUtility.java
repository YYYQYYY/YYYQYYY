package com.yuqinyidev.android.azaz.kanbook.mvp.ui.utils;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class KBUtility {

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
     * get date's string.
     *
     * @param _date
     * @return date string
     */
    public static String dateToString(Date _date, String _format) {
        return new SimpleDateFormat(_format).format(_date);
    }

    /**
     * dialog creater.
     *
     * @param _context         context
     * @param _onClickListener onPositiveButtonClickListener
     * @param _title           dialog's title
     * @param _msg             dialog's message
     * @param _btnCaptions     dialog's button caption (0|1:positive only; 2:positive and
     *                         negative; 3:positive, negative and neutral.)
     * @return created dialog.
     */
    public static Dialog buildDialog(Context _context,
                                     OnClickListener _onClickListener, String _title, String _msg,
                                     String... _btnCaptions) {
        Builder builder = new Builder(_context);

        List<String> lstBtnCaption = new ArrayList<String>();
        for (String btncaption : _btnCaptions) {
            lstBtnCaption.add(btncaption);
        }
        int btnTextCnt = lstBtnCaption.size();
        switch (btnTextCnt) {
            case 0:
                break;
            case 1:
                builder.setPositiveButton(lstBtnCaption.get(0), _onClickListener);
                break;
            case 2:
                builder.setPositiveButton(lstBtnCaption.get(0), _onClickListener);
                builder.setNegativeButton(lstBtnCaption.get(1), null);
                break;
            case 3:
                builder.setPositiveButton(lstBtnCaption.get(0), _onClickListener);
                builder.setNegativeButton(lstBtnCaption.get(1), null);
                builder.setNeutralButton(lstBtnCaption.get(2), null);
                break;
        }

        return builder.setTitle(_title).setMessage(_msg).create();
    }

    /**
     * modify sharedpreferences.
     *
     * @param _preference sharedpreference
     * @param _key        key
     * @param _value      value
     */
    public static void putShare(SharedPreferences _preference, String _key,
                                String _value) {
        Editor edit = _preference.edit();
        edit.putString(_key, _value);
        edit.commit();
    }

    /**
     * modify sharedpreferences.
     *
     * @param _preference sharedpreference
     * @param _key        key
     * @param _value      value
     */
    public static void putShare(SharedPreferences _preference, String _key,
                                boolean _value) {
        Editor edit = _preference.edit();
        edit.putBoolean(_key, _value);
        edit.commit();
    }

    /**
     * modify sharedpreferences.
     *
     * @param _preference sharedpreference
     * @param _key        key
     * @param _value      value
     */
    public static void putShare(SharedPreferences _preference, String _key,
                                int _value) {
        Editor edit = _preference.edit();
        edit.putInt(_key, _value);
        edit.commit();
    }

    /**
     * modify sharedpreferences.
     *
     * @param _preference sharedpreference
     * @param _key        key
     * @param _value      value
     */
    public static void putShare(SharedPreferences _preference, String _key,
                                float _value) {
        Editor edit = _preference.edit();
        edit.putFloat(_key, _value);
        edit.commit();
    }

    /**
     * get file name from file absolute path.
     *
     * @param _filePath file absolute path
     * @return file name.
     */
    public static String getBookName(String _filePath) {
        return _filePath.substring(_filePath.lastIndexOf("/") + 1, _filePath
                .indexOf("."));
    }

    public static void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) {
                InputStream inStream = new FileInputStream(oldPath);
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread;
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
        }
    }

    public static void copyFile(File _oldfile, File _newFile) {
        try {
            int bytesum = 0;
            int byteread = 0;

            InputStream inStream = new FileInputStream(_oldfile);
            FileOutputStream fs = new FileOutputStream(_newFile);

            byte[] buffer = new byte[1444];
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                System.out.println(bytesum);
                fs.write(buffer, 0, byteread);
            }
            inStream.close();
        } catch (Exception e) {
        }
    }

    public static void copyFolder(String oldPath, String newPath) {
        try {
            (new File(newPath)).mkdirs();
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath
                            + "/" + (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        String[] toSearch = KBFileUtils.readFile("/system/etc/vold.fstab").split(" ");
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

}
