package com.example.sta_apps.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns._ID
import android.util.Log
import com.example.sta_apps.data.DatabaseKaryawan.KaryawanColumn.Companion.ID
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


    fun queryByFields(
        idKaryawan: String? = null,
        nmKaryawan: String? = null,
        usiaMin: Int? = null,
        usiaMax: Int? = null,
        tglMasukStart: Date? = null,
        tglMasukEnd: Date? = null
    ): List<DataEntity> {
        val selectionParts = mutableListOf<String>()
        val selectionArgs = mutableListOf<String>()

        if (!idKaryawan.isNullOrEmpty()) {
            selectionParts.add("$ID = ?")
            selectionArgs.add(idKaryawan)
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

        val list = mutableListOf<DataEntity>()
        val cursor = database.query(
            DATABASE_TABLE,
            null,
            selection,
            if (selectionArgs.isNotEmpty()) selectionArgs.toTypedArray() else null,
            null,
            null,
            null
        )

        cursor.use {
            if (cursor.moveToFirst()) {
                do {
                    val data = DataEntity(
                        idKaryawan = cursor.getString(cursor.getColumnIndexOrThrow(ID)),
                        nmKaryawan = cursor.getString(cursor.getColumnIndexOrThrow("nmKaryawan")),
                        usia = cursor.getInt(cursor.getColumnIndexOrThrow("usia")),
                        tglMasukKerja = formatter.parse(cursor.getString(cursor.getColumnIndexOrThrow("tglMasukKerja")))
                    )
                    list.add(data)
                } while (cursor.moveToNext())
            }
        }

        Log.d("QueryFields", "selection: $selection")
        Log.d("QueryFields", "selectionArgs: ${selectionArgs.joinToString()}")

        return list
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