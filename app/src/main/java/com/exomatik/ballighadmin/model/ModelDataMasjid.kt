package com.exomatik.ballighadmin.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class ModelDataMasjid (
    var idMasjid: String = "",
    var kategoriMasjid: String = "",
    var namaMasjid: String = "",
    var fotoMasjid: String = "",
    var provinsiMasjid: String = "",
    var kotaMasjid: String = "",
    var kecamatanMasjid: String = "",
    var alamatMasjid: String = "",
    var titikAlamatMasjid: String = "",
    var showingAlamatPengurus: Boolean = true,
    var showingGelarPendidikan: Boolean = true,
    var showingTTL: Boolean = true,
    var fotoSKPengurus: String = "",
    var fotoSuratTugas: String = "",
    var sampul: ArrayList<String>? = null,
    var status: String = "",
    var indexProvinsi_Kota: String = "",
    var indexProvinsi_Kota_Kecamatan: String = ""
) : Parcelable