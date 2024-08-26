package com.ddd.shiftloggerv33

import android.content.Context

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(private val context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    companion object{
        private const val DATABASE_NAME = "Shiftlogger.db"
        private const val DATABASE_VERSION = 1;

        private const val TABLE_TEST = "Employees"
        private const val COLUMN_USER_ID = "userId"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_LOGIN_TIME = "loginTime"
        private const val COLUMN_LOGOUT_TIME = "logoutTime"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = """
            CREATE TABLE $TABLE_TEST (
                $COLUMN_USER_ID TEXT,
                $COLUMN_DATE TEXT,
                $COLUMN_LOGIN_TIME TEXT,
                $COLUMN_LOGOUT_TIME TEXT
            )
        """
        if (db != null) {
            db.execSQL(createTable)
        };
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (db != null) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_TEST")
            onCreate(db)
        }
    }
}