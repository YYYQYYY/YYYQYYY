package com.yuqinyidev.android.azaz.trainingdiary.mvp.model.entity;

/**
 * Created by RDX64 on 2017/6/29.
 */

public class TrainingDiary {
    private int id;
    private String date;
    private String name;
    private int level;
    private int groupNo;
    private int count;

    public TrainingDiary(int id, String date, String name, int level, int groupNo, int count) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.level = level;
        this.groupNo = groupNo;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
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
