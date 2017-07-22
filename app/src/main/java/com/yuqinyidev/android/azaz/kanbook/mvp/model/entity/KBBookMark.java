package com.yuqinyidev.android.azaz.kanbook.mvp.model.entity;

import java.io.Serializable;

public class KBBookMark implements Serializable {

	private static final long serialVersionUID = 7887363738929016732L;

	private String saveTime = "0000-00-00 00:00:00";
	private String markName = "";
	private int currentOffset = 0;
	private int bookId = 0;
	private int bookMarkId = 0;

	public KBBookMark() {
	}

	public KBBookMark(int _offset, String _markName, int _bookId) {
		this.currentOffset = _offset;
		this.markName = _markName;
		this.bookId = _bookId;
	}

	public int getCurrentOffset() {
		return currentOffset;
	}

	public void setCurrentOffset(int _currentOffset) {
		this.currentOffset = _currentOffset;
	}

	public String getMarkName() {
		return markName;
	}

	public void setMarkName(String _markName) {
		this.markName = _markName;
	}

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int _bookId) {
		this.bookId = _bookId;
	}

	public int getBookMarkId() {
		return bookMarkId;
	}

	public void setBookMarkId(int _bookMarkId) {
		this.bookMarkId = _bookMarkId;
	}

	public String getSaveTime() {
		return saveTime;
	}

	public void setSaveTime(String _saveTime) {
		this.saveTime = _saveTime;
	}

	@Override
	public String toString() {
		return markName + "   " + saveTime;
	}

}
