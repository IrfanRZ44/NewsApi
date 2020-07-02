package com.exomatik.ballighadmin.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelChat(
    var senderId: String = "",
    var receiverId: String = "",
    var message: String = "",
    var urlFoto: String = "",
    var urlFile: String = "",
    var idUser: String = "",
    var idTujuan: String = "",
    var idChat: String = "",
    var timeStamp: Long = 0,
    var status: String = ""
) : Parcelable