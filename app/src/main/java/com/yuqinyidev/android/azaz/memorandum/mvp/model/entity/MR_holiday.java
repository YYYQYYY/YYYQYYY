package com.yuqinyidev.android.azaz.memorandum.mvp.model.entity;

import java.io.Serializable;

public class MR_holiday implements Serializable {
    private static final long serialVersionUID = 2857885059999812577L;
    public int pcd;
    public String nation;
    public String holiday_date;
    public String holiday_name;

    @Override
    public String toString() {
        String result = "";
        result += this.pcd + "，";
        result += this.nation + "，";
        result += this.holiday_date + "， ";
        result += this.holiday_name + "，";
        return result;
    }
}
