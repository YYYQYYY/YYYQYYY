package com.yuqinyidev.android.azaz.kanbook.mvp.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.yuqinyidev.android.azaz.kanbook.KBConstants;
import com.yuqinyidev.android.azaz.kanbook.mvp.model.entity.KBBook;
import com.yuqinyidev.android.azaz.kanbook.mvp.model.entity.KBBookMark;
import com.yuqinyidev.android.azaz.kanbook.mvp.model.entity.KBHistory;

import java.util.ArrayList;
import java.util.List;

public class KBDBAdapter extends KBSQLiteOpenHelper {

    private final static int DB_VERSION = 1;

    private final static String BOOK_TABLE_NAME = "book";
    private final static String BOOK_ID = "bid";
    private final static String BOOK_NAME = "bname";
    private final static String BOOK_PATH = "bpath";

    private final static String BOOK_MARK_TABLE_NAME = "bookmark";
    private final static String BOOK_MARK_ID = "bmid";
    private final static String BOOK_MARK_NAME = "bmname";
    private final static String BOOK_MARK_OFFSET = "bmoffset";
    private final static String BOOK_MARK_SAVETIME = "bmsavetime";

    private final static String HISTORY_TABLE_NAME = "history";
    private final static String HISTORY_ID = "hid";
    private final static String HISTORY_BOOK_ID = "hbid";
    private final static String HISTORY_OFFSET = "hoffset";
    private final static String HISTORY_BOOK_NAME = "hbname";
    private final static String HISTORY_BOOK_PATH = "hbpath";
    private final static String HISTORY_SAVETIME = "hsavetime";
    private final static String HISTORY_SUMMARY = "hsummary";

    public final static String CREATE_TABLE_BOOK = "create table IF NOT EXISTS book("
            + "bid integer primary key autoincrement,bname varchar(50) not null,bpath varchar(255) not null);";
    public final static String CREATE_TABLE_BOOK_MARK = "create table IF NOT EXISTS bookmark("
            + "bmid integer primary key autoincrement,bid integer not null,bmname varchar(50) not null,"
            + "bmoffset integer not null,bmsavetime varchar(20) not null);";
    public final static String CREATE_TABLE_HISTORY = "create table IF NOT EXISTS history("
            + "hid integer primary key autoincrement,hbid integer not null,hoffset integer not null,"
            + "hbname varchar(50) not null,hbpath varchar(255) not null,hsavetime varchar(20) not null,hsummary varchar(255) not null);";

    private static final String[] COLUMNS_BOOK = new String[]{BOOK_ID,
            BOOK_NAME, BOOK_PATH};
    private static final String[] COLUMNS_BOOK_MARK = new String[]{
            BOOK_MARK_ID, BOOK_MARK_NAME, BOOK_MARK_OFFSET, BOOK_MARK_SAVETIME};
    private static final String[] COLUMNS_HISTORY = new String[]{HISTORY_ID,
            HISTORY_BOOK_ID, HISTORY_OFFSET, HISTORY_BOOK_NAME,
            HISTORY_BOOK_PATH, HISTORY_SAVETIME, HISTORY_SUMMARY};

    private SQLiteDatabase db = null;
    private boolean isopen = false;

    public KBDBAdapter(Context _context) {
        super(_context, KBConstants.DB_NAME, KBConstants.ROOT_PATH + "/", null,
                DB_VERSION);
    }

    public void close() {
        if (isopen) {
            db.close();
            isopen = false;
        }
        System.gc();
    }

    @Override
    public void onCreate(SQLiteDatabase _db) {
        String tag = "onCreate";
        Log.d(tag, "start create table");
        _db.execSQL(CREATE_TABLE_BOOK);
        _db.execSQL(CREATE_TABLE_BOOK_MARK);
        _db.execSQL(CREATE_TABLE_HISTORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
    }

    public KBBook queryBook(int _bookId) {
        if (!isopen) {
            isopen = true;
            db = getWritableDatabase();
        }
        Cursor cur = db.query(BOOK_TABLE_NAME, COLUMNS_BOOK, BOOK_ID + "="
                + _bookId, null, null, null, null);
        KBBook book = new KBBook();
        if (cur == null || cur.getCount() == 0 || !cur.moveToFirst()) {
            if (cur != null && !cur.isClosed()) {
                cur.close();
            }
            return null;
        }
        book.setBookId(cur.getInt(0));
        book.setBookName(cur.getString(1));
        book.setBookPath(cur.getString(2));
        cur.close();

        return book;
    }

    public int saveBook(KBBook _book) {
        String tag = "saveBook";
        Log.d(tag, "query the book form database");
        Log.d(tag, "query the book path:" + _book.getBookPath());
        if (!isopen) {
            isopen = true;
            db = getWritableDatabase();
        }

        Cursor cur = db.query(BOOK_TABLE_NAME, COLUMNS_BOOK, BOOK_PATH + "=\""
                + _book.getBookPath() + "\"", null, null, null, null);
        int c = cur.getCount();
        int i;
        if (c == 0) {
            ContentValues values = new ContentValues();
            values.put(BOOK_NAME, _book.getBookName());
            values.put(BOOK_PATH, _book.getBookPath());
            cur.close();
            i = (int) db.insert(BOOK_TABLE_NAME, null, values);
            return i;
        } else {
            cur.moveToLast();
            i = cur.getInt(0);
            cur.close();
            return i;
        }
    }

    public boolean updateBook(String _oldPath, String _newPath, String _newName) {
        String tag = "updateBook";
        if (!isopen) {
            isopen = true;
            db = getWritableDatabase();
        }
        Cursor cur = db.query(BOOK_TABLE_NAME, COLUMNS_BOOK, BOOK_PATH + "=\""
                + _oldPath + "\"", null, null, null, null);

        Log.d(tag, "query the book info from the database....");

        int bookid = 0;
        while (cur.moveToNext()) {
            bookid = cur.getInt(0);
        }

        Log.d(tag, "delete all book mark of this book...");
        String s = "update " + BOOK_TABLE_NAME + " set " + BOOK_NAME + " = \""
                + _newName + "\" " + BOOK_PATH + " = \"" + _newPath
                + "\" where " + BOOK_ID + " = " + bookid;
        Log.d(tag, s);
        db.execSQL(s);
        cur.close();
        return true;
    }

    public boolean deleteBook(String _bookPath) {
        String tag = "deleteBook";
        if (!isopen) {
            isopen = true;
            db = getWritableDatabase();
        }
        String[] col = new String[]{BOOK_ID, BOOK_PATH};
        Cursor cur = db.query(BOOK_TABLE_NAME, col, BOOK_PATH + "=\""
                + _bookPath + "\"", null, null, null, null);

        Log.d(tag, "query the book info from the database....");

        int bookid = 0;
        while (cur.moveToNext()) {
            bookid = cur.getInt(0);
        }

        boolean result = true;

        Log.d(tag, "delete all book mark of this book...");
        result = deleteAllBookMark(bookid);

        Log.d(tag, "delete history of this book...");
        result = deleteAllHistory(bookid);

        String s = "delete from " + BOOK_TABLE_NAME + " where " + BOOK_ID + "="
                + bookid;
        Log.d(tag, s);
        db.execSQL(s);
        cur.close();
        return result;
    }

    public List<KBBookMark> queryAllBookMark(int _bookId) {
        String tag = "queryAllBookMark";
        if (!isopen) {
            isopen = true;
            db = getWritableDatabase();
            Log.d(tag, "open the database...");
        }

        Log.d(tag, "query the book mark from database...");
        Cursor cur = db.query(BOOK_MARK_TABLE_NAME, COLUMNS_BOOK_MARK, BOOK_ID
                + "=\"" + _bookId + "\"", null, null, null, BOOK_MARK_ID
                + " desc");
        Log.d(tag, "wrapper the book mark to the list...");
        List<KBBookMark> list = new ArrayList<KBBookMark>();
        if (cur == null) {
            return list;
        }
        while (cur.moveToNext()) {
            KBBookMark bookMark = new KBBookMark();
            bookMark.setBookMarkId(cur.getInt(0));
            bookMark.setMarkName(cur.getString(1));
            bookMark.setCurrentOffset(cur.getInt(2));
            bookMark.setSaveTime(cur.getString(3));
            list.add(bookMark);
        }

        Log.d(tag, "prepare return the book mark list");
        Log.d(tag, "book mark list size = " + list.size());
        cur.close();
        return list;
    }

    public boolean saveBookMark(KBBookMark _bookMark) {
        String tag = "saveBookMark";
        Log.d(tag, "insert the book mark into database");
        if (!isopen) {
            isopen = true;
            db = getWritableDatabase();
            Log.d(tag, "open the database...");
        }
        Log.d(tag, "constructor the new content values...");
        ContentValues values = new ContentValues();
        values.put(BOOK_ID, _bookMark.getBookId());
        values.put(BOOK_MARK_NAME, _bookMark.getMarkName());
        values.put(BOOK_MARK_OFFSET, _bookMark.getCurrentOffset());
        values.put(BOOK_MARK_SAVETIME, _bookMark.getSaveTime());
        Log.d(tag, "insert ...");
        long x = db.insert(BOOK_MARK_TABLE_NAME, null, values);
        if (x > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteBookMark(int _bmId) {
        String tag = "deleteBookMark";
        if (!isopen) {
            isopen = true;
            db = getWritableDatabase();
            Log.d(tag, "open the database...");
        }

        String s = "delete from " + BOOK_MARK_TABLE_NAME + " where "
                + BOOK_MARK_ID + " =" + _bmId;

        Log.d(tag, s);
        db.execSQL(s);
        return true;
    }

    public boolean deleteAllBookMark(int _bookId) {
        String tag = "deleteAllBookMark";
        if (!isopen) {
            isopen = true;
            db = getWritableDatabase();
            Log.d(tag, "open the database...");
        }

        Log.d(tag, "delete all book mark...");
        String s = "delete from " + BOOK_MARK_TABLE_NAME + " where " + BOOK_ID
                + " = " + _bookId;
        Log.d(tag, s);
        db.execSQL(s);
        return true;
    }

    public Cursor queryHistory(int _hid) {
        if (!isopen) {
            isopen = true;
            db = getWritableDatabase();
        }

        return db.query(HISTORY_TABLE_NAME, COLUMNS_HISTORY, HISTORY_ID + "="
                + _hid, null, null, null, null);
    }

    public Cursor queryHistoryWithBookId(int _bookId) {
        if (!isopen) {
            isopen = true;
            db = getWritableDatabase();
        }

        return db.query(HISTORY_TABLE_NAME, COLUMNS_HISTORY, HISTORY_BOOK_ID
                + "=" + _bookId, null, null, null, null);
    }

    public List<KBHistory> queryAllHistory() {
        String tag = "queryAllHistory";
        if (!isopen) {
            isopen = true;
            db = getWritableDatabase();
            Log.d(tag, "open the database...");
        }

        Log.d(tag, "query the hostory from database...");
        Cursor cur = db.query(HISTORY_TABLE_NAME, COLUMNS_HISTORY, null, null,
                null, null, HISTORY_SAVETIME + " desc");
        Log.d(tag, "wrapper the hostory to the list...");
        List<KBHistory> list = new ArrayList<KBHistory>();
        if (cur == null) {
            return list;
        }
        while (cur.moveToNext()) {
            KBHistory hostory = new KBHistory();
            hostory.setHistoryId(cur.getInt(0));
            hostory.setBookId(cur.getInt(1));
            hostory.setCurrentOffset(cur.getInt(2));
            hostory.setBookName(cur.getString(3));
            hostory.setBookPath(cur.getString(4));
            hostory.setSaveTime(cur.getString(5));
            hostory.setSummary(cur.getString(6));
            list.add(hostory);
        }

        Log.d(tag, "prepare return the hostory list");
        Log.d(tag, "hostory list size = " + list.size());
        cur.close();
        return list;
    }

    public int saveHistory(KBHistory _hostory) {
        String tag = "saveHistory";
        Log.d(tag, "query the hostory form database");
        Log.d(tag, "query the hostory path:" + _hostory.getBookId());
        if (!isopen) {
            isopen = true;
            db = getWritableDatabase();
        }

        Cursor cur = queryHistoryWithBookId(_hostory.getBookId());
        if (cur == null) {
            return -1;
        }

        ContentValues values = new ContentValues();
        values.put(HISTORY_OFFSET, _hostory.getCurrentOffset());
        values.put(HISTORY_SAVETIME, _hostory.getSaveTime());
        values.put(HISTORY_SUMMARY, _hostory.getSummary());
        if (cur.getCount() == 0) {
            values.put(HISTORY_BOOK_ID, _hostory.getBookId());
            values.put(HISTORY_BOOK_NAME, _hostory.getBookName());
            values.put(HISTORY_BOOK_PATH, _hostory.getBookPath());
            cur.close();
            return (int) db.insert(HISTORY_TABLE_NAME, null, values);
        } else {
            String where = HISTORY_BOOK_ID + " = " + _hostory.getBookId();
            cur.close();
            return db.update(HISTORY_TABLE_NAME, values, where, null);
        }
    }

    public boolean deleteHistory(int _hid) {
        String tag = "deleteHistory";
        if (!isopen) {
            isopen = true;
            db = getWritableDatabase();
            Log.d(tag, "open the database...");
        }

        String s = "delete from " + HISTORY_TABLE_NAME + " where " + HISTORY_ID
                + " =" + _hid;

        Log.d(tag, s);
        db.execSQL(s);
        return true;
    }

    public boolean deleteAllHistory(int _bookId) {
        String tag = "deleteAllHistory";
        if (!isopen) {
            isopen = true;
            db = getWritableDatabase();
            Log.d(tag, "open the database...");
        }

        Log.d(tag, "delete all history...");
        String s = "delete from " + HISTORY_TABLE_NAME + " where "
                + HISTORY_BOOK_ID + " = " + _bookId;
        Log.d(tag, s);
        db.execSQL(s);
        return true;
    }

}
