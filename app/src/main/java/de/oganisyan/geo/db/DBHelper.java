package de.oganisyan.geo.db;

import android.content.*;
import android.database.sqlite.*;
import android.util.*;

public class DBHelper extends SQLiteOpenHelper
{
    public static final String CL_AIR_BASE_TYPE = "BaseType";
    public static final String CL_AIR_BASE_VALUE = "BaseValue";
    public static final String CL_AIR_CLASS = "Class";
    public static final String CL_AIR_ID = "_id";
    public static final String CL_AIR_IS_CIRCLE = "IsCircle";
    public static final String CL_AIR_LATITUDE = "Latitude";
    public static final String CL_AIR_LONGITUDE = "Longitude";
    public static final String CL_AIR_NAME = "Name";
    public static final String CL_AIR_RADIUS = "Radius";
    public static final String CL_AIR_SEGMENTS = "Segments";
    public static final String CL_AIR_SOURCE_NAME = "Source";
    public static final String CL_AIR_TOPS_TYPE = "TopsType";
    public static final String CL_AIR_TOPS_VALUE = "TopsValue";
    private static final String DATABASE_NAME = "airspaces.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_AIRSPACES = "airspaces";
    private static DBHelper dbHelper;
    private static SQLiteDatabase readableInstane;
    private static SQLiteDatabase writableInstane;
    private static final String DATABASE_CREATE = "create table " + TABLE_AIRSPACES + "( " + CL_AIR_ID + " integer primary key autoincrement, "
    		+ CL_AIR_NAME + " text not null, " + CL_AIR_CLASS + " text not null, " + CL_AIR_SOURCE_NAME + " text not null, "
    		+ CL_AIR_BASE_TYPE + " text not null, "	+ CL_AIR_BASE_VALUE + " integer not null, " + CL_AIR_TOPS_TYPE + " text not null, "
    		+ CL_AIR_TOPS_VALUE + " integer not null, " + CL_AIR_IS_CIRCLE  + " integer not null, "	+ CL_AIR_LATITUDE + " real not null, " 
    		+ CL_AIR_LONGITUDE + " real not null, " + CL_AIR_RADIUS + " integer not null, " + CL_AIR_SEGMENTS + " blob not null); ";
    
    static {
        DBHelper.dbHelper = null;
        DBHelper.writableInstane = null;
        DBHelper.readableInstane = null;
    }
    
    public DBHelper(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    public static void open(final Context context) {
        if (DBHelper.dbHelper == null) {
            DBHelper.dbHelper = new DBHelper(context);
            DBHelper.writableInstane = DBHelper.dbHelper.getWritableDatabase();
            DBHelper.readableInstane = DBHelper.dbHelper.getReadableDatabase();
        }
    }
    
    public static SQLiteDatabase readableInstaneOf() {
        if (DBHelper.readableInstane == null) {
            throw new RuntimeException("Database is not open.");
        }
        return DBHelper.readableInstane;
    }
    
    public static SQLiteDatabase writableInstaneOf() {
        if (DBHelper.writableInstane == null) {
            throw new RuntimeException("Database is not open.");
        }
        return DBHelper.writableInstane;
    }
    
    public void onCreate(final SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }
    
    public void onUpgrade(final SQLiteDatabase sqLiteDatabase, final int n, final int n2) {
        Log.w(DBHelper.class.getName(), "Upgrading database from version " + n + " to " + n2 + ", which will destroy all old data");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS airspaces");
        this.onCreate(sqLiteDatabase);
    }
}
