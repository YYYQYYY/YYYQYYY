package com.yuqinyidev.android.azaz.kanbook.mvp.model.entity;

public class KBBookInfo {

	private String title;
	private String author;
	private String year;
	private String month;
	private String day;
	private String gender;
	private String publisher;
	private String vendor;

	private short pgkSeed;
	private int cid;
	private int contentLength;

	private byte type;
	private byte[] cover;

	public byte getType() {
		return type;
	}

	public void setType(byte _type) {
		this.type = _type;
	}

	public short getPgkSeed() {
		return pgkSeed;
	}

	public void setPgkSeed(short _pgkSeed) {
		this.pgkSeed = _pgkSeed;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String _title) {
		this.title = _title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String _author) {
		this.author = _author;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String _year) {
		this.year = _year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String _month) {
		this.month = _month;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String _day) {
		this.day = _day;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String _gender) {
		this.gender = _gender;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String _publisher) {
		this.publisher = _publisher;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String _vendor) {
		this.vendor = _vendor;
	}

	public byte[] getCover() {
		return cover;
	}

	public void setCover(byte[] _cover) {
		this.cover = _cover;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int _cid) {
		this.cid = _cid;
	}

	public int getContentLength() {
		return contentLength;
	}

	public void setContentLength(int _contentLength) {
		this.contentLength = _contentLength;
	}

	public String getDate() {
		StringBuffer sb = new StringBuffer();
		sb.append(year);
		sb.append("-");
		sb.append(month);
		sb.append("-");
		sb.append(day);
		return sb.toString();
	}

}
