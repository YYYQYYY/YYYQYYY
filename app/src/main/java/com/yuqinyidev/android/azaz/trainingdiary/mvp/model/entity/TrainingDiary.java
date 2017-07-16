package com.yuqinyidev.android.azaz.trainingdiary.mvp.model.entity;

/**
 * Created by RDX64 on 2017/6/29.
 */

public class TrainingDiary {
    private String date;
    private String name;
    private String level;
    private int groupNo;
    private int count;

    public TrainingDiary(String date, String name, String level, int groupNo, int count) {
        this.date = date;
        this.name = name;
        this.level = level;
        this.groupNo = groupNo;
        this.count = count;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(int groupNo) {
        this.groupNo = groupNo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
