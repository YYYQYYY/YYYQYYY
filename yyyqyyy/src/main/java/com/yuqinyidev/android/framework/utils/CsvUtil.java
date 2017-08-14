package com.yuqinyidev.android.framework.utils;

import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * 解析CSV文件
 * <p>
 * AssetManager am = AppClass.getInstance().getAssets();
 * if(!Util.isEmpty(am))
 * {
 * InputStream in = null;
 * try {
 * in = am.open(Constant.PHONE_LOC);
 * CsvUtil csvUtil = new CsvUtil(in);
 * csvUtil.run();
 * writeDataToDb(csvUtil.getSegments());
 * if (null != in) {
 * in.close();
 * }
 * } catch (IOException e) {
 * e.printStackTrace();
 * } catch (Exception e) {
 * e.printStackTrace();
 * }
 * }
 * Created by RDX64 on 2017/7/2.
 */
public class CsvUtil {
    public static final String TAG = "CsvUtil";

    private BufferedReader bufferedreader = null;
    private List<String> list = new ArrayList<String>();
    private List<NumberSegment> locSegments = new ArrayList<NumberSegment>();

    public CsvUtil(InputStream inputStream) throws IOException {

        bufferedreader = new BufferedReader(new InputStreamReader(inputStream));

        String stemp;

        while ((stemp = bufferedreader.readLine()) != null) {

            list.add(stemp);
        }
    }

    public CsvUtil(String filename) throws IOException {

        bufferedreader = new BufferedReader(new FileReader(filename));

        String stemp;

        while ((stemp = bufferedreader.readLine()) != null) {

            list.add(stemp);
        }
    }

    public List getList() throws IOException {

        return list;
    }

    public int getRowNum() {

        return list.size();
    }

    public int getColNum() {

        if (!list.toString().equals("[]")) {

            if (list.get(0).toString().contains(",")) {
                return list.get(0).toString().split(",").length;
            } else if (list.get(0).toString().trim().length() != 0) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public String getRow(int index) {

        if (this.list.size() != 0)
            return (String) list.get(index);
        else
            return null;
    }

    public String getCol(int index) {

        if (this.getColNum() == 0) {
            return null;
        }

        StringBuffer scol = new StringBuffer();
        String temp = null;
        int colnum = this.getColNum();

        if (colnum > 1) {
            for (Iterator it = list.iterator(); it.hasNext(); ) {
                temp = it.next().toString();
                scol = scol.append(temp.split(",")[index] + ",");
            }
        } else {
            for (Iterator it = list.iterator(); it.hasNext(); ) {
                temp = it.next().toString();
                scol = scol.append(temp + ",");
            }
        }
        String str = new String(scol.toString());
        str = str.substring(0, str.length() - 1);
        return str;
    }

    public String getString(int row, int col) {

        String temp = null;
        int colnum = this.getColNum();
        if (colnum > 1) {
            temp = list.get(row).toString().split(",")[col];
        } else if (colnum == 1) {
            temp = list.get(row).toString();
        } else {
            temp = null;
        }
        return temp.replace("\"", "");
    }

    public void CsvClose() throws IOException {
        this.bufferedreader.close();
    }

    public void run() throws IOException {
//the title not used, so we start from 1 row
        for (int i = 1; i < getRowNum() - 1; i++) {

            String preFix = getString(i, 1);

            String startLoc = getString(i, 2);
            String endLoc = getString(i, 3);

            //Util.BIZ_CONF_DEBUG(TAG, preFix + "    " + startLoc + "    " + endLoc);

            NumberSegment segment = new NumberSegment(preFix, startLoc, endLoc);

            locSegments.add(segment);
        }

        CsvClose();
    }

    public List<NumberSegment> getSegments() {
        return locSegments;
    }
}

class NumberSegment {
    //TODO:Dummy construct
    public NumberSegment(String preFix, String startLoc, String endLoc) {

    }
}


