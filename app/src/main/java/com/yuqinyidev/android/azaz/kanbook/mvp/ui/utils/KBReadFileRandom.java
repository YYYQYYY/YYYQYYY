package com.yuqinyidev.android.azaz.kanbook.mvp.ui.utils;

import android.util.Log;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class KBReadFileRandom {

    private InputStream dataInputStream = null;
    private String filePath = null;

    public KBReadFileRandom(String path) {
        String tag = "ReadFileRandom";
        this.filePath = path;
        try {
            dataInputStream = new DataInputStream(new FileInputStream(filePath));
        } catch (FileNotFoundException e) {
            Log.d(tag, "Exception :" + e.getMessage());
        }
    }

    public void openNewStream() {
        close();
        try {
            dataInputStream = new DataInputStream(new FileInputStream(filePath));
        } catch (FileNotFoundException e) {
        }
    }

    public byte[] readBytes(int length) {
        byte[] b = new byte[length];
        try {
            if (dataInputStream == null) {
                dataInputStream = new DataInputStream(new FileInputStream(
                        filePath));
            }
            dataInputStream.read(b);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return b;
    }

    public int readBytes(byte[] buffer) {
        int i = 0;
        try {
            i = dataInputStream.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        return i;
    }

    public void skip(int length) {
        try {
            dataInputStream.skip(length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fastSkip(int length) {
        readBytes(length);
    }

    public void locate(int location) {
        readBytes(location);
    }

    public long getFileLength() {
        long i = 0;
        try {
            i = new File(filePath).length();
        } catch (Exception e) {
        }
        return i;
    }

    public void close() {
        if (null != dataInputStream)
            try {
                dataInputStream.close();
            } catch (IOException e) {
            }
    }

}
