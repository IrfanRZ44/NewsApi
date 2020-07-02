package com.exomatik.ballighadmin.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelDataChat(
    var idUser: String = "",
    var idTujuan: String = "",
    var idChat: String = "",
    var message: String = "",
    var timeStamp: Long = 0
) : Parcelable