package com.example.sta_apps.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.sta_apps.data.DatabaseKaryawan.KaryawanColumn.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "dbkaryawan"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE_KARYAWAN =
            "CREATE TABLE $TABLE_NAME" +
                    "(${DatabaseKaryawan.KaryawanColumn._ID} INTEGER PRIMARY KEY," +
                    "${DatabaseKaryawan.KaryawanColumn.TITLE} TEXT NOT NULL," +
                    "${DatabaseKaryawan.KaryawanColumn.DESCRIPTION} TEXT NOT NULL," +
                    "${DatabaseKaryawan.KaryawanColumn.DATE} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_KARYAWAN)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

}