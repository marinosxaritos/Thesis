package com.example.synctest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_TABLE = "create table " + DbContact.TABLE_NAME +
            "(id integer primary key autoincrement, "+ DbContact.NAME + " text, " + DbContact.SYNC_STATUS +
        " integer);";
    private static final String DROP_TABLE = "drop table if exists " + DbContact.TABLE_NAME;

    public DbHelper(Context context) {
        super(context, DbContact.DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(DROP_TABLE);
            onCreate(db);
    }

    public void saveToLocalDatabase(String name, int sync_status, SQLiteDatabase database) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbContact.NAME, name);
        contentValues.put(DbContact.SYNC_STATUS, sync_status);
        database.insert(DbContact.TABLE_NAME, null, contentValues);
    }

    public Cursor readFromLocalDatabase(SQLiteDatabase database) {
        String[] projection = {DbContact.NAME, DbContact.SYNC_STATUS};

        return (database.query(DbContact.TABLE_NAME, projection, null, null, null, null, null));
    }

    /** public void updateLocalDatabase(String name, int sync_status, SQLiteDatabase database) {
        ContentValues contentValues = new ContentValues();
         contentValues.put(DbContact.SYNC_STATUS, sync_status);
         String selection = DbContact.NAME + " LIKE ?";
         String[] selection_args = {name};
         database.update(DbContact.TABLE_NAME, contentValues, selection, selection_args);
    } */

    public void updateLocalDatabase(String name, int sync_status, SQLiteDatabase database) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbContact.SYNC_STATUS, sync_status);
        String selection = DbContact.NAME + " LIKE ?"; // Added space after column name
        String[] selection_args = {name};
        database.update(DbContact.TABLE_NAME, contentValues, selection, selection_args);
    }


}
