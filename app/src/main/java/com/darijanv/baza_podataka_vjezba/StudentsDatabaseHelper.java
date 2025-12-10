package com.darijanv.baza_podataka_vjezba;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

public class StudentsDatabaseHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "students.db";
    public static final String TABLE_NAME = "student";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_COURSE = "course";
    public static final String COLUMN_GRADE = "grade";

    private static final String CREATE_SQL = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME + " TEXT NOT NULL, " +
            COLUMN_COURSE + " TEXT NOT NULL, " +
            COLUMN_GRADE + " TEXT NOT NULL);";

    private static final String DROP_SQL = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public StudentsDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_SQL);
        onCreate(db);
    }


    public long insert(ContentValues values) {
        return getWritableDatabase().insert(TABLE_NAME, null, values);
    }

    public Cursor query(String id, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);
        if (id != null) {
            qb.appendWhere(COLUMN_ID + "=" + id);
        }
        return qb.query(getReadableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
    }

    public int update(String id, ContentValues values) {
        if (id == null) {
            return getWritableDatabase().update(TABLE_NAME, values, null, null);
        } else {
            return getWritableDatabase().update(TABLE_NAME, values, COLUMN_ID + "=?", new String[]{id});
        }
    }

    public int delete(String id) {
        if (id == null) {
            return getWritableDatabase().delete(TABLE_NAME, null, null);
        } else {
            return getWritableDatabase().delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{id});
        }
    }
}
