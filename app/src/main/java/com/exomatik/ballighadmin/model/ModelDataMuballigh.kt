package com.exomatik.ballighadmin.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class ModelDataMuballigh (
    var idMuballigh: String = "",
    var showingAlamat: Boolean = true,
    var showingKualifikasi: Boolean = true,
    var showingGelar: Boolean = true,
    var showingTTL: Boolean = true,
    var sampul: ArrayList<String>? = null,
    var listKualifikasi: ArrayList<String>? = null,
    var listTabligh: ArrayList<String>? = null,
    var status: String = "") : Parcelable