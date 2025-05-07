package com.example.sta_apps.data

import android.provider.BaseColumns

internal class DatabaseKaryawan {
    internal class KaryawanColumn : BaseColumns {
        companion object {
            const val TABLE_NAME = "karyawan"
            const val ID = "id"
            const val NAMA = "nmKaryawan"
            const val TGL_MASUK_KERJA = "tglMasukKerja"
            const val USIA = "usia"

        }
    }
}