package com.yuqinyidev.android.azaz.kanbook.mvp.ui.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class KBFileAdapter {
    private static final String TAG = "FileAdaptor";

    /**
     * 获取指定位置的指定类型的文件
     *
     * @param paths 文件夹路径
     * @param type  文件类型（如“*.jpg;*.png;*.gif”）
     */
    public static void getFileList(String paths, String type,
                                   final OnFileListCallback onFileListCallback) {

        new AsyncTask<String, String, String>() {
            ArrayList<KBFileInfo> list = new ArrayList<KBFileInfo>();

            @Override
            protected void onPostExecute(String result) {
                onFileListCallback.SearchFileListInfo(list);
            }

            @Override
            protected String doInBackground(String... params) {
                String type = params[1].substring(params[1]
                        .lastIndexOf(".") + 1);
                String[] paths;
                if (params[0] != null) {
                    paths = params[0].split("\\|");
                    for (String path : paths){
                        File file = new File(path);
                        scanSDCard(file, type, list);
                    }
                }
                return null;
            }

        }.execute(paths, type, "");
    }

    private static void scanSDCard(File file, String ext, ArrayList<KBFileInfo> list) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File tmp : files) {
                    if (tmp.isFile()) {
                        String fileName = tmp.getName();
                        String filePath = tmp.getName();
                        if (fileName.contains(".")) {
                            fileName = fileName.substring(fileName
                                    .lastIndexOf(".") + 1);
                            if (ext != null && ext.equalsIgnoreCase(fileName)) {
                                Log.i(TAG, filePath);
                                list.add(new KBFileInfo(filePath, tmp.getAbsolutePath(), tmp.lastModified()));
                            }
                        }
                    } else
                        scanSDCard(tmp, ext, list);
                }
            }
        } else {
            if (file.isFile()) {
                String fileName = file.getName();
                String filePath = file.getName();
                if (fileName.contains(".")) {
                    fileName = fileName
                            .substring(fileName.lastIndexOf(".") + 1);
                    if (ext != null && ext.equalsIgnoreCase(fileName)) {
                        Log.i(TAG, filePath);
                        list.add(new KBFileInfo(filePath, file.getAbsolutePath(), file.lastModified()));
                    }
                }
            }
        }
    }

    /**
     * 判断
     */
    public static boolean isSdCardExist() {
        return Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * 目录相关
     */
    public static String getSdCardRootDir() {
        return Environment.getExternalStorageDirectory().getPath() + "/";
    }

    public static String getCacheDir(Context ctx) {
        // /data/data/<package name>/cache
        return ctx.getCacheDir().getPath() + "/";
    }

    public static String getFilesDir(Context ctx) {
        // /data/data/<package name>/files
        return ctx.getFilesDir().getPath() + "/";
    }

    public static String getSharedPrefDir(Context ctx) {
        String path = "/data/data/com.timedee.calendar/shared_prefs/";
        mkDir(path);
        return path;
    }

    public static String getSdDataDir() {
        return getSdCardRootDir() + "timedee/.data/";
    }

    public static String getBackupDir() {
        return getSdCardRootDir() + "timedee/.backup/";
    }

    public static boolean mkDir(String path) {
        File dir = new File(path);
        boolean res = dir.mkdirs();

        return res;
    }

    /**
     * 文件操作相关
     */
    public static boolean writeFile(String path, InputStream is) {
        boolean result = false;
        FileOutputStream os = null;
        BufferedOutputStream bos = null;
        try {
            File file = new File(path);
            os = new FileOutputStream(file, false);
            bos = new BufferedOutputStream(os);
            int readLen = 0;
            byte[] buf = new byte[1024];
            while ((readLen = is.read(buf)) != -1) {
                bos.write(buf, 0, readLen);
            }
            bos.flush();
            bos.close();
            os.close();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static boolean writeTextFile(String path, String data) {
        boolean result = false;
        FileWriter fw = null;
        try {
            File file = new File(path);
            fw = new FileWriter(file);
            fw.write(data);
            fw.close();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fw != null) {
                    fw.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static InputStream readFile(String path) {
        File file = new File(path);
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fis;
    }

    public static boolean CopyFile(String fromFile, String toFile) {
        try {
            InputStream fosFrom = new FileInputStream(fromFile);
            OutputStream fosTo = new FileOutputStream(toFile);
            byte bt[] = new byte[4096];
            int c;
            while ((c = fosFrom.read(bt)) > 0) {
                fosTo.write(bt, 0, c);
            }
            fosFrom.close();
            fosTo.close();
            bt = null;
            return true;

        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean CopyAssetFile(Context ctx, String fromFile, String toFile) {
        try {
            InputStream fosfrom = ctx.getAssets().open(fromFile);
            OutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[4096];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
            bt = null;
            return true;

        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean deleteFile(String path) {
        try {
            File file = new File(path);
            return file.delete();
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean isFileExist(String path) {
        try {
            File file = new File(path);
            return file.exists();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }

    /**
     * 扫描完成后的回调，获取文件列表必须实现
     *
     * @author cola
     */
    public interface OnFileListCallback {
        /**
         * 返回查询的文件列表
         *
         * @param list 文件列表
         */
        public void SearchFileListInfo(ArrayList<KBFileInfo> list);
    }

}
