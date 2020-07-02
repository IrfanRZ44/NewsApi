package com.exomatik.ballighadmin.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelUser(
    var noHp: String = "",
    var token: String = "",
    var jenisAkun: String = "",
    var activeAkun: String = "",
    var username: String = "",
    var idMasjid: String = "",
    var idMuballigh: String = "",
    var idLembaga: String = "",
    var nama: String = "",
    var jk: String = "",
    var tempatLahir: String = "",
    var tanggalLahir: String = "",
    var provinsi: String = "",
    var kota: String = "",
    var kecamatan: String = "",
    var alamat: String = "",
    var titikAlamat: String = "",
    var foto: String = "",
    var dataPendidikan: ModelPendidikan? = null,
    var status: String = "",
    var indexProvinsi_Kota: String = "",
    var indexProvinsi_Kota_Kecamatan: String = ""
    ) : Parcelable