package com.example.lostandfound.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database info
    private static final String DATABASE_NAME    = "lostandfound.db";
    private static final int    DATABASE_VERSION = 1;

    // Table and column names
    private static final String TABLE_NAME    = "items";
    private static final String COL_ID        = "id";
    private static final String COL_POST_TYPE = "post_type";
    private static final String COL_NAME      = "name";
    private static final String COL_PHONE     = "phone";
    private static final String COL_DESC      = "description";
    private static final String COL_DATE      = "date";
    private static final String COL_LOCATION  = "location";
    private static final String COL_IMAGE     = "image_path";
    private static final String COL_TIMESTAMP = "timestamp";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the items table
        String createTable = "CREATE TABLE " + TABLE_NAME + " ("
                + COL_ID        + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_POST_TYPE + " TEXT, "
                + COL_NAME      + " TEXT, "
                + COL_PHONE     + " TEXT, "
                + COL_DESC      + " TEXT, "
                + COL_DATE      + " TEXT, "
                + COL_LOCATION  + " TEXT, "
                + COL_IMAGE     + " TEXT, "
                + COL_TIMESTAMP + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // INSERT — add a new lost/found item
    public void insertItem(LostFoundItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_POST_TYPE, item.postType);
        values.put(COL_NAME,      item.name);
        values.put(COL_PHONE,     item.phone);
        values.put(COL_DESC,      item.description);
        values.put(COL_DATE,      item.date);
        values.put(COL_LOCATION,  item.location);
        values.put(COL_IMAGE,     item.imagePath);
        values.put(COL_TIMESTAMP, item.timestamp);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // READ — get all items, optionally filtered by category
    public List<LostFoundItem> getAllItems(String filter) {
        List<LostFoundItem> itemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Build the query depending on whether a filter is applied
        String query;
        if (filter == null || filter.equals("All")) {
            query = "SELECT * FROM " + TABLE_NAME
                    + " ORDER BY " + COL_TIMESTAMP + " DESC";
        } else {
            query = "SELECT * FROM " + TABLE_NAME
                    + " WHERE " + COL_POST_TYPE + " = '" + filter + "'"
                    + " ORDER BY " + COL_TIMESTAMP + " DESC";
        }

        Cursor cursor = db.rawQuery(query, null);

        // Loop through all rows and add them to the list
        if (cursor.moveToFirst()) {
            do {
                LostFoundItem item = new LostFoundItem(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_POST_TYPE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_DESC)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_LOCATION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_IMAGE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_TIMESTAMP))
                );
                itemList.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return itemList;
    }

    // DELETE — remove an item by its ID
    public void deleteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }
}