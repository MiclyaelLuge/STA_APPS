package com.example.sta_apps.data

import android.provider.BaseColumns

internal class DatabaseKaryawan {
    internal class KaryawanColumn : BaseColumns {
        companion object {
            const val TABLE_NAME = "karyawan"
            const val _ID = "_id"
            const val TITLE = "title"
            const val DESCRIPTION = "description"
            const val DATE = "date"
        }
    }
}