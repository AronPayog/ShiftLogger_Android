package com.ddd.shiftloggerv33

import android.content.ContentValues
import android.content.Context

class DatabaseManger(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun insertData(userId: String, date: String, loginTime: String, logoutTime: String){
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("userId", userId)
            put("date", date)
            put("loginTime", loginTime)
            put("logoutTime", logoutTime)

        }
        db.insert("Employees", null, values)
        db.close()
    }
}