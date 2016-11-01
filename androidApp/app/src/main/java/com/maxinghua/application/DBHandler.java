package com.maxinghua.application;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ma on 2016/5/26.
 */

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "beacons.db";
    public static final String TABLE_NAME = "beaconDatabase";
    public static final String ID = "_id";
    public static final String UUID = "_uuid";
    public static final String MAJOR = "_major";
    public static final String MINOR = "_minor";
    public static final String DISTANCE = "_distance";
    public static final String RSSI = "_rssi";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                UUID + " TEXT ," +
                MAJOR + " TEXT ," +
                MINOR + " TEXT ," +
                DISTANCE + " TEXT ," +
                RSSI + " TEXT" +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST " + TABLE_NAME);
        onCreate(db);
    }

    //Add a new record to database
    public void addRecord(String uuid, String major, String minor, double distance, int rssi){
        ContentValues values = new ContentValues();
        values.put(UUID, uuid);
        values.put(MAJOR, major);
        values.put(MINOR, minor);
        values.put(DISTANCE, distance);
        values.put(RSSI, rssi);

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public boolean beaconExist(String uuid, String major, String minor) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE _uuid = '" + uuid + "' AND _major = '" + major  + "' AND _minor = '" + minor +  "'";

        //Cursor point to a location in your result
        Cursor c = db.rawQuery(query, null);
        boolean exist = c.moveToFirst();
        //Check if the beacon already exist
        db.close();
        return exist;
    }

    //update new record to database
    public void updateRecord(String uuid, String major, String minor, double distance, int rssi){
        SQLiteDatabase db = getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME+ " SET _distance = '" + distance + "' , _rssi = '" + rssi + "' WHERE _uuid = '" + uuid + "' AND _major = '" + major  + "' AND _minor = '" + minor +  "'";
        db.execSQL(query);
        db.close();
    }

    /*
    //Add new row to database
    public void addActivity(String activity){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, activity);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_ACTIVITY, null, values);
        db.close();
    }

    //Delete product from the database
    public void deleteActivity(String activity){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_ACTIVITY + " WHERE " + COLUMN_NAME + "=\"" + activity + "\";");
    }
    */

    //Print out the database as a string
    public String databaseToString(){
        String dbString = "";
        SQLiteDatabase db =  getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE 1";

        //Cursor point to a location in your result
        Cursor c = db.rawQuery(query, null);
        //Move to first row in your result
        boolean exist = c.moveToFirst();

        //Position after the last row means the end of the results
        while (!c.isAfterLast() && exist) {
            if (c.getString(c.getColumnIndex("_uuid")) != null) {
                dbString += "UUID: " + c.getString(c.getColumnIndex("_uuid")) + "\n" +
                        " Major: " + c.getString(c.getColumnIndex("_major")) + "\n" +
                        " Minor: " + c.getString(c.getColumnIndex("_minor")) + "\n" +
                        " Distance: " + c.getString(c.getColumnIndex("_distance")) + "\n" +
                        " RSSI: " + c.getString(c.getColumnIndex("_rssi")) + "\n";
                dbString += "\n";
            }
            c.moveToNext();
        }
        db.close();
        return dbString;
    }
}