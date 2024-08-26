package com.ddd.shiftloggerv33

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.content.contentValuesOf

class Authentication(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    companion object{
        val DATABASE_NAME = "user_db"
        val DATABASE_VERSION = 1
        val TABLE_USER = "EMPLOYEES"

        val COLUMN_FULLNAME = "FULLNAME"
        val COLUMN_ID = "ID"
        val COLUMN_AGE = "AGE"
        val COLUMN_USERNAME = "USERNAME"
        val COLUMN_PASSWORD = "PASSWORD"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_USERS_TABLE = """
            CREATE TABLE $TABLE_USER (
            $COLUMN_FULLNAME TEXT,
            $COLUMN_ID TEXT,
            $COLUMN_AGE TEXT,
            $COLUMN_USERNAME TEXT,
            $COLUMN_PASSWORD TEXT
            )
        """
        if(db != null){
            db.execSQL(CREATE_USERS_TABLE)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (db != null) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
            onCreate(db)
        }
    }

    fun addUser(RegUserName: String, RegPassword: String, RegId: String, RegAge: String, RegFullName: String){
        val db = this.writableDatabase
        val values = contentValuesOf()
        values.put("$COLUMN_FULLNAME",RegFullName)
        values.put("$COLUMN_ID",RegId)
        values.put("$COLUMN_AGE",RegAge)
        values.put("$COLUMN_USERNAME",RegUserName)
        values.put("$COLUMN_PASSWORD",RegPassword)
        db.insert(TABLE_USER, null, values)
        db.close()
    }

    fun authenticationUser(InputUser: String, InputPassword: String): Pair<Boolean, String?> {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_USER WHERE $COLUMN_USERNAME=? AND $COLUMN_PASSWORD=?",
            arrayOf(InputUser, InputPassword)
        )
        return if(cursor.moveToFirst()){
            val userInfo = cursor.getString(cursor.getColumnIndexOrThrow("FULLNAME"))
            cursor.close()
            Pair(true, userInfo)
        }else{
            cursor.close()
            Pair(false, null)
        }
    }


}