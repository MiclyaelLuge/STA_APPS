package com.example.sta_apps.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns._ID
import com.example.sta_apps.data.DatabaseKaryawan.KaryawanColumn.Companion.TABLE_NAME
import java.sql.SQLException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.jvm.Throws

class KaryawanHelper(context: Context) {
    private var databaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    @Throws(SQLException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }

    fun close() {
        databaseHelper.close()

        if (database.isOpen)
            database.close()

    }

    fun queryByFieldsSafe(
        idKaryawan: String? = null,
        nmKaryawan: String? = null,
        usiaMin: Int? = null,
        usiaMax: Int? = null,
        tglMasukStart: Date? = null,
        tglMasukEnd: Date? = null
    ): Cursor {
        val selectionParts = mutableListOf<String>()
        val selectionArgs = mutableListOf<String>()

        idKaryawan?.let {
            selectionParts.add("$_ID = ?")
            selectionArgs.add(it)
        }

        nmKaryawan?.let {
            selectionParts.add("nmKaryawan LIKE ?")
            selectionArgs.add("%$it%")
        }

        if (usiaMin != null && usiaMax != null) {
            selectionParts.add("usia BETWEEN ? AND ?")
            selectionArgs.add(usiaMin.toString())
            selectionArgs.add(usiaMax.toString())
        } else if (usiaMin != null) {
            selectionParts.add("usia >= ?")
            selectionArgs.add(usiaMin.toString())
        } else if (usiaMax != null) {
            selectionParts.add("usia <= ?")
            selectionArgs.add(usiaMax.toString())
        }

        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        if (tglMasukStart != null && tglMasukEnd != null) {
            selectionParts.add("tglMasukKerja BETWEEN ? AND ?")
            selectionArgs.add(formatter.format(tglMasukStart))
            selectionArgs.add(formatter.format(tglMasukEnd))
        } else if (tglMasukStart != null) {
            selectionParts.add("tglMasukKerja >= ?")
            selectionArgs.add(formatter.format(tglMasukStart))
        } else if (tglMasukEnd != null) {
            selectionParts.add("tglMasukKerja <= ?")
            selectionArgs.add(formatter.format(tglMasukEnd))
        }

        val selection = if (selectionParts.isNotEmpty()) selectionParts.joinToString(" AND ") else null

        return database.query(
            DATABASE_TABLE,
            null,
            selection,
            if (selectionArgs.isNotEmpty()) selectionArgs.toTypedArray() else null,
            null,
            null,
            null
        )
    }

    fun insert(values: ContentValues?):Long{
        return database.insert(DATABASE_TABLE,null,values)
    }

    fun deleteById(id:String):Int{
        return database.delete(DATABASE_TABLE,"$_ID = '$id'",null)
    }

    fun updateById(id:String,values: ContentValues?):Int{
        return database.update(DATABASE_TABLE,values,"$_ID =?",arrayOf(id))
    }


    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: KaryawanHelper? = null
        fun getInstance(context: Context): KaryawanHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: KaryawanHelper(context)
            }
    }
}