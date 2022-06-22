package de.clinc8686.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper{

    private static boolean today = true;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ToDoApplication";
    private static final String TABLE_NAME = "ToDos";
    public static final String KEY_NAME = "NAME";
    public static final String KEY_DESCRIPTION = "DESCRIPTION";
    public static final String KEY_DATE = "ENDDATE";
    private static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS ToDos(ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TINYTEXT NOT NULL, DESCRIPTION TINYTEXT NOT NULL, ENDDATE DATE NOT NULL)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    public boolean addTask(String name, String description, String date) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, name);
            values.put(KEY_DESCRIPTION, description);
            values.put(KEY_DATE, date);
            db.insert(TABLE_NAME, null, values);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public long countTasks() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            SQLiteStatement statement = db.compileStatement("SELECT COUNT(*) FROM " + TABLE_NAME);
            long counter = statement.simpleQueryForLong();
            return counter;
        } catch (Exception e) {
            return -1;
        }

    }

    public boolean deleteFullData() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DELETE FROM " + TABLE_NAME);
        } catch (Exception e) {
            return false;
        }
        return true;

    }

    public Cursor getData() {
        String selectQuery = "SELECT ID _id, * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
    }

    public Cursor getDataToday() {
        if (today) {
            String selectQuery = "SELECT ID _id, * FROM " + TABLE_NAME + " WHERE " + KEY_DATE + " LIKE STRFTIME('%d-%m-%Y', DATE('now'))";
            SQLiteDatabase db = this.getReadableDatabase();
            today = false;
            return db.rawQuery(selectQuery, null);
        } else {
            today = true;
            return getData();
        }
    }
}
