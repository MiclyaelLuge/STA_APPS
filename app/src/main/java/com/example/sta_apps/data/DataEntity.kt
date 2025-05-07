package com.example.sta_apps.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class DataEntity(
    var idKaryawan : String? = null,
    var nmKaryawan  : String? = null,
    var tglMasukKerja : Date? = null,
    var usia : Int = 0,
): Parcelable
