package com.yuqinyidev.android.azaz.memorandum.mvp.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.yuqinyidev.android.azaz.memorandum.mvp.model.entity.MR_holiday;
import com.yuqinyidev.android.azaz.memorandum.mvp.model.entity.MR_memory;

public class MR_DBAdapter {

	// private static final String PACKAGE_PATH = "com.totyu.hz.memorandum";
	// private static final String DATABASE_PATH = "/data"
	// + Environment.getDataDirectory().getAbsolutePath() + "/"
	// + PACKAGE_PATH + "/databases";

	private static final String SDCARD_PATH = "/sdcard/memorandum";
	private static final String DATABASE_FILENAME = "memorandum.db";

	private static final String DB_TABLE_MEMORY = "memo_info";
	private static final String KEY_pcd = "pcd";
	private static final String KEY_memo_title = "memo_title";
	private static final String KEY_memo_type = "memo_type";
	private static final String KEY_memo_date = "memo_date";
	private static final String KEY_memo_time = "memo_time";
	private static final String KEY_event_where = "event_where";
	private static final String KEY_important_flg = "important_flg";
	private static final String KEY_complete_flg = "complete_flg";
	private static final String KEY_memo_content = "memo_content";

	private static final String DB_TABLE_HOLIDAY = "holiday_info";
	private static final String KEY_nation = "nation";
	private static final String KEY_holiday_date = "holiday_date";
	private static final String KEY_holiday_name = "holiday_name";

	private SQLiteDatabase mSQLiteDatabase;

	public void open(InputStream _is) {
		try {
			if (mSQLiteDatabase != null) {
				return;
			}
			// String databaseFilename = DATABASE_PATH + "/" +
			// DATABASE_FILENAME;
			// File dir = new File(DATABASE_PATH);
			String databaseFilename = SDCARD_PATH + "/" + DATABASE_FILENAME;
			File dir = new File(SDCARD_PATH);
			if (!dir.exists()) {
				dir.mkdir();
				try {
					String command = "chmod 751 " + dir.getAbsolutePath();
					Runtime.getRuntime().exec(command);
				} catch (IOException e) {
					e.printStackTrace();
					Log.e("chmod", "chmod fail!!!!");
				}
			}
			if (!(new File(databaseFilename)).exists()) {
				InputStream is = _is;
				FileOutputStream fos = new FileOutputStream(databaseFilename);
				byte[] buffer = new byte[8192];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}

				fos.close();
				is.close();

				try {
					String command = "chmod 751 "
							+ new File(databaseFilename).getAbsolutePath();
					Runtime.getRuntime().exec(command);
				} catch (IOException e) {
					e.printStackTrace();
					Log.e("chmod", "chmod fail!!!!");
				}
			}
			mSQLiteDatabase = SQLiteDatabase.openOrCreateDatabase(
					databaseFilename, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		if (mSQLiteDatabase != null) {
			mSQLiteDatabase.close();
			mSQLiteDatabase = null;
		}
	}

	public long insert(MR_memory _memory) {
		// 新規追加memo_infoテーブル
		if (_memory == null) {
			return -1;
		}
		ContentValues newValues = new ContentValues();
		if (_memory.memo_title != null && _memory.memo_title.length() <= 100) {
			newValues.put(KEY_memo_title, _memory.memo_title);
		} else {
			return -1;
		}
		if (_memory.memo_type != 0) {
			newValues.put(KEY_memo_type, _memory.memo_type);
		} else {
			return -1;
		}
		if (_memory.memo_date != null && _memory.memo_date.length() == 8) {
			newValues.put(KEY_memo_date, _memory.memo_date);
		} else {
			return -1;
		}
		if (_memory.memo_time != null && _memory.memo_time.length() == 6) {
			newValues.put(KEY_memo_time, _memory.memo_time);
		} else {
			return -1;
		}
		if (_memory.memo_type == 1 && _memory.event_where != null
				&& _memory.event_where.length() <= 255) {
			newValues.put(KEY_event_where, _memory.event_where);
		}
		newValues.put(KEY_important_flg, _memory.important_flg);
		newValues.put(KEY_complete_flg, _memory.complete_flg);
		if (_memory.memo_content != null
				&& _memory.memo_content.length() <= 1000) {
			newValues.put(KEY_memo_content, _memory.memo_content);
		} else {
			return -1;
		}
		return mSQLiteDatabase.insert(DB_TABLE_MEMORY, null, newValues);
	}

	public long update(MR_memory _memory) {
		// 更新memo_infoテーブル
		if (_memory == null) {
			return -1;
		}
		ContentValues updateValues = new ContentValues();
		if (_memory.memo_title != null && _memory.memo_title.length() <= 100) {
			updateValues.put(KEY_memo_title, _memory.memo_title);
		}
		if (_memory.memo_type != 0) {
			updateValues.put(KEY_memo_type, _memory.memo_type);
		}
		if (_memory.memo_date != null && _memory.memo_date.length() == 8) {
			updateValues.put(KEY_memo_date, _memory.memo_date);
		}
		if (_memory.memo_time != null && _memory.memo_time.length() == 6) {
			updateValues.put(KEY_memo_time, _memory.memo_time);
		}
		if (_memory.memo_type == 1 && _memory.event_where != null
				&& _memory.event_where.length() <= 255) {
			updateValues.put(KEY_event_where, _memory.event_where);
		}
		updateValues.put(KEY_important_flg, _memory.important_flg);
		updateValues.put(KEY_complete_flg, _memory.complete_flg);
		if (_memory.memo_content != null
				&& _memory.memo_content.length() <= 1000) {
			updateValues.put(KEY_memo_content, _memory.memo_content);
		}

		long result = mSQLiteDatabase.update(DB_TABLE_MEMORY, updateValues,
				KEY_pcd + "=" + _memory.pcd, null);

		return result;
	}

	public MR_memory[] queryMemoryData(MR_memory _memory) {
		Cursor cursor = mSQLiteDatabase.query(DB_TABLE_MEMORY, new String[] {
				KEY_pcd, KEY_memo_title, KEY_memo_type, KEY_memo_date,
				KEY_memo_time, KEY_event_where, KEY_important_flg,
				KEY_complete_flg, KEY_memo_content },
				getMemoryWhereClause(_memory), null, null, null, null);

		MR_memory[] result = ConvertToMemory(cursor);
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return result;
	}

	/** get event info */
	public MR_memory[] queryEventData(MR_memory _memory) {
		if (_memory == null) {
			return null;
		}

		StringBuilder sb = new StringBuilder(100);
		sb.append(KEY_complete_flg + " = 0 and " + KEY_memo_type + " = 1");
		if (_memory.memo_date != null) {
			sb.append(" and " + KEY_memo_date + " >= '" + _memory.memo_date
					+ "' ");
		}
		if (_memory.memo_time != null) {
			sb.append(" and " + KEY_memo_time + " > '" + _memory.memo_time
					+ "' ");
		}
		Cursor cursor = mSQLiteDatabase.query(DB_TABLE_MEMORY, new String[] {
				KEY_pcd, KEY_memo_title, KEY_memo_type, KEY_memo_date,
				KEY_memo_time, KEY_event_where, KEY_important_flg,
				KEY_complete_flg, KEY_memo_content }, sb.toString(), null,
				null, null, null);

		MR_memory[] result = ConvertToMemory(cursor);
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return result;
	}

	public String getEventDate(MR_memory _memory) {
		if (_memory == null || _memory.memo_date == null
				|| _memory.event_query_flg == 0) {
			return null;
		}

		String strClause = "";
		String strOrder = "";
		if (_memory.event_query_flg == 1) {
			strClause = KEY_memo_date + " > '" + _memory.memo_date + "'";
			strOrder = "memo_date asc";
		} else {
			strClause = KEY_memo_date + " < '" + _memory.memo_date + "'";
			strOrder = "memo_date desc";
		}
		String strSql = "select memo_date from memo_info where " + strClause
				+ " and memo_type = 1 order by " + strOrder
				+ " limit 1 offset 0";
		Cursor cursor = mSQLiteDatabase.rawQuery(strSql, null);
		if (cursor == null) {
			return null;
		}
		if (cursor.getCount() == 0 || !cursor.moveToFirst()) {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
			return null;
		}

		String result = cursor.getString(cursor.getColumnIndex(KEY_memo_date));
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return result;
	}

	public MR_memory[] queryMemoryData4MainList() {
		String strSql = "select memo_title, important_flg from memo_info "
				+ "where memo_type = 2 and complete_flg = 0 "
				+ "order by important_flg desc";

		Cursor cursor = mSQLiteDatabase.rawQuery(strSql, null);
		if (cursor == null) {
			return null;
		}
		int count = cursor.getCount();
		if (count == 0 || !cursor.moveToFirst()) {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
			return null;
		}
		MR_memory[] memories = new MR_memory[count];
		for (int i = 0; i < count; i++) {
			memories[i] = new MR_memory();
			memories[i].memo_title = cursor.getString(cursor
					.getColumnIndex(KEY_memo_title));
			memories[i].important_flg = cursor.getInt(cursor
					.getColumnIndex(KEY_important_flg));
			cursor.moveToNext();
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return memories;
	}

	private MR_memory[] ConvertToMemory(Cursor _cursor) {
		if (_cursor == null) {
			return null;
		}
		int count = _cursor.getCount();
		if (count == 0 || !_cursor.moveToFirst()) {
			return null;
		}
		MR_memory[] memories = new MR_memory[count];
		for (int i = 0; i < count; i++) {
			memories[i] = new MR_memory();
			memories[i].pcd = _cursor.getInt(0);
			memories[i].memo_title = _cursor.getString(_cursor
					.getColumnIndex(KEY_memo_title));
			memories[i].memo_type = _cursor.getInt(_cursor
					.getColumnIndex(KEY_memo_type));
			memories[i].memo_date = _cursor.getString(_cursor
					.getColumnIndex(KEY_memo_date));
			memories[i].memo_time = _cursor.getString(_cursor
					.getColumnIndex(KEY_memo_time));
			memories[i].event_where = _cursor.getString(_cursor
					.getColumnIndex(KEY_event_where));
			memories[i].important_flg = _cursor.getInt(_cursor
					.getColumnIndex(KEY_important_flg));
			memories[i].complete_flg = _cursor.getInt(_cursor
					.getColumnIndex(KEY_complete_flg));
			memories[i].memo_content = _cursor.getString(_cursor
					.getColumnIndex(KEY_memo_content));
			_cursor.moveToNext();
		}
		return memories;
	}

	private String getMemoryWhereClause(MR_memory _memory) {
		if (_memory == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder(1000);
		if (_memory.memo_date != null) {
			sb.append(KEY_memo_date + " = '" + _memory.memo_date + "' ");
			if (_memory.memo_type != 0) {
				sb.append(" and ");
			}
		}
		if (_memory.memo_type != 0) {
			sb.append(KEY_memo_type + " = " + _memory.memo_type);
		}
		return sb.toString();
	}

	public MR_holiday[] queryHolidayData(MR_holiday _holiday) {
		Cursor cursor = mSQLiteDatabase.query(DB_TABLE_HOLIDAY, new String[] {
				KEY_pcd, KEY_nation, KEY_holiday_date, KEY_holiday_name },
				getHolidayWhereClause(_holiday), null, null, null, null);

		MR_holiday[] result = ConvertToHoliday(cursor);
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return result;
	}

	private MR_holiday[] ConvertToHoliday(Cursor _cursor) {
		if (_cursor == null) {
			return null;
		}
		int count = _cursor.getCount();
		if (count == 0 || !_cursor.moveToFirst()) {
			return null;
		}
		MR_holiday[] holidays = new MR_holiday[count];
		for (int i = 0; i < count; i++) {
			holidays[i] = new MR_holiday();
			holidays[i].pcd = _cursor.getInt(0);
			holidays[i].nation = _cursor.getString(_cursor
					.getColumnIndex(KEY_nation));
			holidays[i].holiday_date = _cursor.getString(_cursor
					.getColumnIndex(KEY_holiday_date));
			holidays[i].holiday_name = _cursor.getString(_cursor
					.getColumnIndex(KEY_holiday_name));
			_cursor.moveToNext();
		}
		return holidays;
	}

	private String getHolidayWhereClause(MR_holiday _holiday) {
		if (_holiday == null) {
			return null;
		}

		StringBuilder sb = new StringBuilder(1000);
		if (_holiday.nation != null) {
			sb.append(KEY_nation + " = " + _holiday.nation);
			if (_holiday.holiday_date != null) {
				sb.append(" and ");
			}
		}
		if (_holiday.holiday_date != null) {
			sb.append(KEY_holiday_date + " = " + _holiday.holiday_date);
		}
		return sb.toString();
	}

	public long delete(MR_memory _memory) {
		if (_memory == null || _memory.memo_type == 0) {
			return -1;
		}
		String where = KEY_memo_type + " = " + _memory.memo_type;
		if (_memory.memo_date != null && _memory.memo_type == 1) {
			where += " and " + KEY_memo_date + " = " + _memory.memo_date;
		}
		long result = mSQLiteDatabase.delete(DB_TABLE_MEMORY, where, null);
		return result;
	}

	public long deleteMemoryOneData(MR_memory _memory) {
		if (_memory == null || _memory.pcd == 0) {
			return -1;
		}
		long result = mSQLiteDatabase.delete(DB_TABLE_MEMORY, KEY_pcd + " = "
				+ _memory.pcd, null);
		return result;
	}
}
