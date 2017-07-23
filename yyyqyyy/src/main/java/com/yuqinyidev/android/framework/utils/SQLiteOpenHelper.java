package com.yuqinyidev.android.framework.utils;

/**
 * Created by yuqy on 2017-07-23.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.io.File;

public abstract class SQLiteOpenHelper {

    private static final String TAG = SQLiteOpenHelper.class.getSimpleName();
    private final Context mContext;
    private final String mName;
    private final String mPath;
    private final SQLiteDatabase.CursorFactory mFactory;
    private final int mNewVersion;
    private SQLiteDatabase mDatabase = null;
    private boolean mIsInitializing = false;

    public SQLiteOpenHelper(Context _context, String _name,
                            SQLiteDatabase.CursorFactory _factory, int _version) {
        if (_version < 1)
            throw new IllegalArgumentException("Version must be >= 1, was "
                    + _version);
        mContext = _context;
        mName = _name;
        mPath = FileUtils.packageName2DBPath(_context.getPackageName());
        mFactory = _factory;
        mNewVersion = _version;
    }

    public SQLiteOpenHelper(Context _context, String _name, String _path,
                            SQLiteDatabase.CursorFactory _factory, int _version) {
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
                if (Utility.isCanWriteSDCard()) {
                    String path = getDatabasePath(mName).getAbsolutePath();
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mIsInitializing = false;
            if (success) {
                if (mDatabase != null) {
                    try {
                        mDatabase.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                mDatabase = db;
            } else {
                if (db != null)
                    db.close();
            }
        }
        return db;
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
            if (Utility.isCanWriteSDCard()) {
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mIsInitializing = false;
            if (db != null && db != mDatabase)
                db.close();
        }
        return mDatabase;
    }

    public synchronized void close() {
        if (mIsInitializing)
            throw new IllegalStateException("Closed during initialization");
        if (mDatabase != null && mDatabase.isOpen()) {
            mDatabase.close();
            mDatabase = null;
        }
    }

    private File getDatabasePath(String _name) {
        FileUtils.makeFolders(mPath);
        return new File(mPath + _name);
    }

    public abstract void onCreate(SQLiteDatabase _db);

    public abstract void onUpgrade(SQLiteDatabase db, int oldVersion,
                                   int newVersion);

    public void onOpen(SQLiteDatabase _db) {
    }

}
