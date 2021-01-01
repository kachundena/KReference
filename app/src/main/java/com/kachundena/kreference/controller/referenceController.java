package com.kachundena.kreference.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kachundena.kreference.dbhelper.dbHelper;
import com.kachundena.kreference.model.reference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class referenceController {
    private dbHelper DBHelper;
    private String TABLE_NAME = "reference";
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

    public referenceController(Context contexto) {
        DBHelper = new dbHelper(contexto);
    }


    public int deleteReference(reference Reference) {

        SQLiteDatabase DB = DBHelper.getWritableDatabase();
        String[] argumentos = {String.valueOf(Reference.getReference_id())};
        return DB.delete(TABLE_NAME, "reference_id = ?", argumentos);
    }

    public int deleteAllReferences() {

        SQLiteDatabase DB = DBHelper.getWritableDatabase();
        return DB.delete(TABLE_NAME, null, null);
    }

    public long newReference(reference Reference) {
        SQLiteDatabase DB = DBHelper.getWritableDatabase();
        ContentValues InsertValues = new ContentValues();
        InsertValues.put("description", Reference.getDescription());
        InsertValues.put("url", Reference.getUrl());
        InsertValues.put("start_date", dateFormat.format(Reference.getStart_date()));
        InsertValues.put("metadata", Reference.getMetadata());
        return DB.insert(TABLE_NAME, null, InsertValues);
    }

    public int updateReference(reference Reference) {
        SQLiteDatabase DB = DBHelper.getWritableDatabase();
        ContentValues UpdateValues = new ContentValues();
        UpdateValues.put("description", Reference.getDescription());
        UpdateValues.put("url", Reference.getUrl());
        UpdateValues.put("start_date", dateFormat.format(Reference.getStart_date()));
        UpdateValues.put("metadata", Reference.getMetadata());
        // where id...
        String PKField = "reference_id = ?";
        //
        String[] ValuesPKField = {String.valueOf(Reference.getReference_id())};
        return DB.update(TABLE_NAME, UpdateValues, PKField, ValuesPKField);
    }

    public reference getReference(int referenceid) throws ParseException {
        reference Reference = null;
        SQLiteDatabase DB = DBHelper.getReadableDatabase();
        String[] QueryCols = {"description","url","start_date", "metadata"};
        Cursor cursor = DB.query(
                TABLE_NAME,
                QueryCols,
                "reference_id=?",
                new String[] { String.valueOf(referenceid) },
                null,
                null,
                null
        );

        if (cursor == null) {
            return Reference;
        }
        if (!cursor.moveToFirst()) return Reference;
        do {
            Reference = new reference();
            Reference.setReference_id(referenceid);
            Reference.setDescription(cursor.getString(0));
            Reference.setUrl(cursor.getString(1));
            Reference.setStart_date(dateFormat.parse(cursor.getString(2)));
            Reference.setMetadata(cursor.getString(3));
        } while (cursor.moveToNext());
        cursor.close();
        return Reference;
    }

    public ArrayList<reference> getReferences() throws ParseException {
        ArrayList<reference> References = new ArrayList<>();
        SQLiteDatabase DB = DBHelper.getReadableDatabase();
        String[] QueryCols = {"reference_id","description","url","start_date", "metadata"};
        Cursor cursor = DB.query(
                TABLE_NAME,
                QueryCols,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor == null) {
            return References;
        }
        if (!cursor.moveToFirst()) return References;
        do {
            reference Reference = new reference();
            Reference.setReference_id(cursor.getInt(0));
            Reference.setDescription(cursor.getString(1));
            Reference.setUrl(cursor.getString(2));
            Reference.setStart_date(dateFormat.parse(cursor.getString(3)));
            Reference.setMetadata(cursor.getString(4));
            References.add(Reference);
        } while (cursor.moveToNext());
        cursor.close();
        return References;
    }

    public ArrayList<reference> getReferences(String filter) throws ParseException {
        ArrayList<reference> References = new ArrayList<>();
        SQLiteDatabase DB = DBHelper.getReadableDatabase();
        String[] QueryCols = {"reference_id","description","url","start_date", "metadata"};
        Cursor cursor = DB.query(
                TABLE_NAME,
                QueryCols,
                "description LIKE '%" + filter + "%'",
                null,
                null,
                null,
                null
        );

        if (cursor == null) {
            return References;
        }
        if (!cursor.moveToFirst()) return References;
        do {
            reference Reference = new reference();
            Reference.setReference_id(cursor.getInt(0));
            Reference.setDescription(cursor.getString(1));
            Reference.setUrl(cursor.getString(2));
            Reference.setStart_date(dateFormat.parse(cursor.getString(3)));
            Reference.setMetadata(cursor.getString(4));
            References.add(Reference);
        } while (cursor.moveToNext());
        cursor.close();
        return References;
    }

}
