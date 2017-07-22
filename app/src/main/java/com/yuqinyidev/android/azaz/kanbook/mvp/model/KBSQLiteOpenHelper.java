package com.yuqinyidev.android.azaz.kanbook.mvp.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.yuqinyidev.android.azaz.kanbook.mvp.ui.utils.KBUtility;

import java.io.File;

public abstract class KBSQLiteOpenHelper {

	private static final String TAG = KBSQLiteOpenHelper.class.getSimpleName();
	private final Context mContext;
	private final String mName;
	private final String mPath;
	private final CursorFactory mFactory;
	private final int mNewVersion;
	private SQLiteDatabase mDatabase = null;
	private boolean mIsInitializing = false;

	public KBSQLiteOpenHelper(Context _context, String _name, String _path,
                              CursorFactory _factory, int _version) {
		if (_version < 1)
			throw new IllegalArgumentException("Version must be >= 1, was "
					+ _version);
		mContext = _context;
		mName = _name;
		mPath = _path;
		mFactory = _factory;
		mNewVersion = _version;
	}

	public synchronized SQLiteDatabase getWritableDatabase() {
		if (mDatabase != null && mDatabase.isOpen() && !mDatabase.isReadOnly()) {
			return mDatabase;
		}
		if (mIsInitializing) {
			throw new IllegalStateException(
					"getWritableDatabase called recursively");
		}
		boolean success = false;
		SQLiteDatabase db = null;
		try {
			mIsInitializing = true;
			if (mName == null) {
				db = SQLiteDatabase.create(null);
			} else {
				if (KBUtility.isCanWriteSDCard()) {
					String path = getDatabasePath(mName).getPath();
					db = SQLiteDatabase.openOrCreateDatabase(path, mFactory);
					Log.i("sunny", "create database in sdcard");
				} else {
					db = mContext.openOrCreateDatabase(mName, 0, mFactory);
				}
			}
			int version = db.getVersion();
			if (version != mNewVersion) {
				db.beginTransaction();
				try {
					if (version == 0) {
						onCreate(db);
					} else {
						onUpgrade(db, version, mNewVersion);
					}
					db.setVersion(mNewVersion);
					db.setTransactionSuccessful();
				} finally {
					db.endTransaction();
				}
			}
			onOpen(db);
			success = true;
			return db;
		} finally {
			mIsInitializing = false;
			if (success) {
				if (mDatabase != null) {
					try {
						mDatabase.close();
					} catch (Exception e) {
					}
				}
				mDatabase = db;
			} else {
				if (db != null)
					db.close();
			}
		}
	}

	public synchronized SQLiteDatabase getReadableDatabase() {
		if (mDatabase != null && mDatabase.isOpen()) {
			return mDatabase;
		}
		if (mIsInitializing) {
			throw new IllegalStateException(
					"getReadableDatabase called recursively");
		}
		try {
			return getWritableDatabase();
		} catch (SQLiteException e) {
			if (mName == null)
				throw e;
			Log.e(TAG, "Couldn't open " + mName
					+ " for writing (will try read-only):", e);
		}
		SQLiteDatabase db = null;
		try {
			mIsInitializing = true;
			String path = "";
			if (KBUtility.isCanWriteSDCard()) {
				path = mContext.getDatabasePath(mName).getPath();
				db = SQLiteDatabase.openOrCreateDatabase(path, mFactory);
			} else {
				path = getDatabasePath(mName).getPath();
				db = SQLiteDatabase.openDatabase(path, mFactory,
						SQLiteDatabase.OPEN_READWRITE);
			}
			if (db.getVersion() != mNewVersion) {
				throw new SQLiteException(
						"Can't upgrade read-only database from version "
								+ db.getVersion() + " to " + mNewVersion + ": "
								+ path);
			}
			onOpen(db);
			Log.w(TAG, "Opened " + mName + " in read-only mode");
			mDatabase = db;
			return mDatabase;
		} finally {
			mIsInitializing = false;
			if (db != null && db != mDatabase)
				db.close();
		}
	}

	public synchronized void close() {
		if (mIsInitializing)
			throw new IllegalStateException("Closed during initialization");
		if (mDatabase != null && mDatabase.isOpen()) {
			mDatabase.close();
			mDatabase = null;
		}
	}

	public File getDatabasePath(String _name) {
		return new File(mPath + _name);
	}

	public abstract void onCreate(SQLiteDatabase _db);

	public abstract void onUpgrade(SQLiteDatabase db, int oldVersion,
                                   int newVersion);

	public void onOpen(SQLiteDatabase _db) {
	}

}
