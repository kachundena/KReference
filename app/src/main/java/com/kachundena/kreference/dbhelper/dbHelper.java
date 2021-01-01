package com.kachundena.kreference.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbHelper  extends SQLiteOpenHelper {
    private static final String
            NAME_DB = "kreference_v0.0.1",
            NAME_TABLE_REFERENCE = "reference";
    private static final int VERSION_BASE_DE_DATOS = 1;

    public dbHelper(Context context) {
        super(context, NAME_DB, null, VERSION_BASE_DE_DATOS);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String szSQL = "CREATE TABLE IF NOT EXISTS %s(" +
                "reference_id integer primary key autoincrement, " +
                "description text, " +
                "url text, " +
                "start_date text, " +
                "metadata text " +
                ");";
        db.execSQL(String.format(szSQL, NAME_TABLE_REFERENCE));

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }



}
