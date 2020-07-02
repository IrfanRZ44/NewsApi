package com.exomatik.ballighadmin.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelListChat(
    var idUser: String = "",
    var idTujuan: String = "",
    var idChat: String = "",
    var jenisChat: String = "",
    var waktu: String = "",
    var tahun: String = "",
    var bulan: String = "",
    var tanggal: String = "",
    var msg: String = "",
    var timeStmp: Long = 0,
    var nama: String = "",
    var foto: String = "",
    var status: String = ""
) : Parcelable