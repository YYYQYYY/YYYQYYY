package com.yuqinyidev.android.azaz.kanbook.mvp.ui.utils;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class KBUtility {

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
        for (String btnCaption : _btnCaptions) {
            lstBtnCaption.add(btnCaption);
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
    public static void putShare(SharedPreferences _preference, String _key, String _value) {
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
    public static void putShare(SharedPreferences _preference, String _key, boolean _value) {
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
    public static void putShare(SharedPreferences _preference, String _key, int _value) {
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
    public static void putShare(SharedPreferences _preference, String _key, float _value) {
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

}
