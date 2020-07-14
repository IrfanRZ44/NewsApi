package com.exomatik.ballighadmin.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelRiwayat(
    var isFirst: Boolean = false,
    var tanggal: String = ""
) : Parcelable