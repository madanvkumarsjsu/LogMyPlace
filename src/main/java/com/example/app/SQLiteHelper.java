package com.example.app;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Madan on 2/16/14.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_PLACE = "PLACE";
    public static final String placeid = "ID";
    public static final String name = "PLACENAME";
    public static final String lat = "LATIT";
    public static final String lon = "LONGI";
    // Database creation sql statement
    private static final String DATABASE_NAME = "PlaceStorage.db";
    private static final int DATABASE_VERSION = 1;


    /*private static final String DATABASE_CREATE = "CREATE  TABLE " + TABLE_PERSON + " (`" + fname
            + "` VARCHAR(10) NOT NULL ,`" + lname
            + "` VARCHAR(10) NOT NULL ,`" + address
            + "` VARCHAR(10) NOT NULL ,`" + creditcard
            + "` VARCHAR(10) NOT NULL ;";*/
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String DATABASE_CREATE =
            "CREATE TABLE " + TABLE_PLACE + " (" +
            		placeid + INTEGER_TYPE + " PRIMARY KEY" + " AUTOINCREMENT"+ COMMA_SEP +
                    name + TEXT_TYPE + COMMA_SEP +
                    lat + TEXT_TYPE + COMMA_SEP +
                    lon + TEXT_TYPE + " )";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i("QueryOnCreate", DATABASE_CREATE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACE);
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }
    public SQLiteHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i("QueryConstructor", DATABASE_CREATE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("QueryOnUpgrade", DATABASE_CREATE);
        Log.w(SQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACE);
        onCreate(db);
    }
}
