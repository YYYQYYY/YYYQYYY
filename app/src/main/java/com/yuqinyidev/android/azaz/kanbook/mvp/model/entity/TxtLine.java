package com.yuqinyidev.android.azaz.kanbook.mvp.model.entity;

public class TxtLine {

	private int offset = 0;
	private int lineLength = 0;
	private int beforeLineLength = 0;

	public int getOffset() {
		return offset;
	}

	public void setOffset(int _offset) {
		this.offset = _offset;
	}

	public int getLineLength() {
		return lineLength;
	}

	public void setLineLength(int _lineLength) {
		this.lineLength = _lineLength;
	}

	public int getBeforeLineLength() {
		return beforeLineLength;
	}

	public void setBeforeLineLength(int _beforeLineLength) {
		this.beforeLineLength = _beforeLineLength;
	}

	public TxtLine() {
		this(0, 0, 0);
	}

	public TxtLine(int _offset, int _lenght, int _beforeLineLength) {
		this.offset = _offset;
		this.lineLength = _lenght;
		this.beforeLineLength = _beforeLineLength;
	}

}
