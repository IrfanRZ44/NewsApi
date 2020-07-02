package com.exomatik.ballighadmin.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class ModelDataLembaga (
    var idLembaga: String = "",
    var namaSingkatLembaga: String = "",
    var namaPanjangLembaga: String = "",
    var descLembaga: String = "",
    var fotoLembaga: String = "",
    var provinsiLembaga: String = "",
    var kotaLembaga: String = "",
    var kecamatanLembaga: String = "",
    var alamatLembaga: String = "",
    var titikAlamatLembaga: String = "",
    var showingAlamatPengurus: Boolean = true,
    var showingGelarPendidikan: Boolean = true,
    var showingTTL: Boolean = true,
    var fotoSKPengurus: String = "",
    var fotoSuratTugas: String = "",
    var sampul: ArrayList<String>? = null,
    var status: String = "",
    var indexProvinsi_Kota: String = "",
    var indexProvinsi_Kota_Kecamatan: String = "") : Parcelable