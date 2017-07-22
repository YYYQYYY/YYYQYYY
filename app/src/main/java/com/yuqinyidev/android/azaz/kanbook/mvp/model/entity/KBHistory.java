package com.yuqinyidev.android.azaz.kanbook.mvp.model.entity;

public class KBHistory {

	private int mHistoryId;
	private int mBookId;
	private int mCurrentOffset = 0;
	private String mBookName;
	private String mBookPath;
	private String mSaveTime = "0000-00-00 00:00:00";
	private String mSummary;

	public KBHistory() {
	}

	public KBHistory(int _id, int _offset, String _bookName, String _bookPath,
                     String _saveTime, String _summary) {
		this.mBookId = _id;
		this.mCurrentOffset = _offset;
		this.mBookName = _bookName;
		this.mBookPath = _bookPath;
		this.mSaveTime = _saveTime;
		this.mSummary = _summary;
	}

	public int getHistoryId() {
		return mHistoryId;
	}

	public void setHistoryId(int _historyId) {
		this.mHistoryId = _historyId;
	}

	public int getBookId() {
		return mBookId;
	}

	public void setBookId(int _id) {
		this.mBookId = _id;
	}

	public int getCurrentOffset() {
		return mCurrentOffset;
	}

	public void setCurrentOffset(int _currentOffset) {
		this.mCurrentOffset = _currentOffset;
	}

	public String getBookName() {
		return mBookName;
	}

	public void setBookName(String _bookName) {
		this.mBookName = _bookName;
	}

	public String getBookPath() {
		return mBookPath;
	}

	public void setBookPath(String _bookPath) {
		this.mBookPath = _bookPath;
	}

	public String getSaveTime() {
		return mSaveTime;
	}

	public void setSaveTime(String _saveTime) {
		this.mSaveTime = _saveTime;
	}

	public String getSummary() {
		return mSummary;
	}

	public void setSummary(String _summary) {
		this.mSummary = _summary;
	}

	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(this.mBookName);
		s.append("   ");
		s.append(this.mSaveTime);
		s.append("   ");
		s.append(this.mSummary);
		return s.toString();
	}
}
