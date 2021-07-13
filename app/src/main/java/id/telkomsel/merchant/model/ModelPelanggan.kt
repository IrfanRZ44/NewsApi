package id.telkomsel.merchant.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelPelanggan(
    var id: Int = 0,
    var status: String = "",
    var username: String = "",
    var password: String = "",
    var poin: Long = 0,
    var nama: String = "",
    var foto: String = "",
    var tgl_lahir: String = "",
    var alamat: String = "",
    var nomor_hp: String = "",
    var nomor_wa: String = "",
    var verified_phone: String = "",
    var token: String = "",
    var created_at: String = "",
    var updated_at: String = ""
    ) : Parcelable