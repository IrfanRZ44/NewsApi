package com.exomatik.ballighadmin.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelWilayah (
    var id: String? = null,
    var nama: String? = null) : Parcelable