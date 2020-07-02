package com.exomatik.ballighadmin.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelPendidikan (
    var s1Nama: String = "",
    var s1Kota: String = "",
    var s1Tahun: String = "",
    var s1Jurusan: String = "",
    var s1Gelar: String = "",
    var s2Nama: String = "",
    var s2Kota: String = "",
    var s2Tahun: String = "",
    var s2Jurusan: String = "",
    var s2Gelar: String = "",
    var s3Nama: String = "",
    var s3Kota: String = "",
    var s3Tahun: String = "",
    var s3Jurusan: String = "",
    var s3Gelar: String = "") : Parcelable