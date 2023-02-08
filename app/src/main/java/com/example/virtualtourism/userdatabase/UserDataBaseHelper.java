package com.example.virtualtourism.userdatabase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class UserDataBaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "UserDataBaseHelper";
    public static final String CREATE_USER_INFO = "create table UserInfo ("
            + "student_num text primary key,"
            + "student_name text ,"
            + "student_age text ,"
            + "student_sex text ,"
            + "pwd text)";

    public static final String TABLE_NAME = "UserInfo";

    public UserDataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_INFO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
