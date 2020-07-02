package com.exomatik.ballighadmin.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelSearchChat(
    var nama: String = "",
    var id: String = "",
    var jenisChat: String = "",
    var timeStamp: Long = 0
    ) : Parcelable