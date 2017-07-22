package com.yuqinyidev.android.azaz.memorandum.mvp.model.entity;

import java.io.Serializable;

public class MR_memory implements Serializable {
	private static final long serialVersionUID = -6275816869152593456L;
	public int pcd;
	public String memo_title;
	public int memo_type;
	public String memo_date;
	public String memo_time;
	public String event_where;
	public int important_flg;
	public int complete_flg;
	public String memo_content;

	/** 1:next;2:previous */
	public int event_query_flg;

	@Override
	public String toString() {
		String result = "";
		result += this.pcd + "，";
		result += this.memo_title + "，";
		result += this.memo_type + "， ";
		result += this.memo_date + "，";
		result += this.memo_time + "，";
		result += this.event_where + "，";
		result += this.important_flg + "，";
		result += this.complete_flg + "，";
		result += this.memo_content + "，";
		return result;
	}
}
