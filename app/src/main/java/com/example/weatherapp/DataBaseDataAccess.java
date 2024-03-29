package com.example.weatherapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


    public class DataBaseDataAccess extends SQLiteOpenHelper {

        private static final String TAG = "DatabaseHelper";

        private static final String TABLE_NAME = "weatherqueries";
        private static final String COL1 = "ID";
        private static final String COL2 = "query";


        public DataBaseDataAccess(Context context) {
            super(context, TABLE_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL2 +" TEXT)";
            db.execSQL(createTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
            onCreate(db);
        }

        public boolean addData(String item) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL2, item);

            Log.d(TAG, "addData: Adding " + item + " to " + TABLE_NAME);

            long result = db.insert(TABLE_NAME, null, contentValues);

            //if date as inserted incorrectly it will return -1
            if (result == -1) {
                return false;

            } else {
                return true;
            }

        }

        /**
         * Returns all the data from database
         * @return
         */
        public Cursor getData(){
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "SELECT * FROM " + TABLE_NAME;
            Cursor data = db.rawQuery(query, null);
            return data;
        }




    }

