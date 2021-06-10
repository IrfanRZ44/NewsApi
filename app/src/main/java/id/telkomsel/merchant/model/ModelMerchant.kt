package id.telkomsel.merchant.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelMerchant(
    var id: Int = 0,
    var username: String = "",
    var level: String = "",
    var token: String = "",
    var verified_phone: String = "",
    var status_merchant: String = "",
    var comment: String = "",
    var nama_merchant: String = "",
    var alamat_merchant: String = "",
    var latitude: String = "",
    var longitude: String = "",
    var tgl_peresmian_merchant: String = "",
    var provinsi: String = "",
    var kabupaten: String = "",
    var kecamatan: String = "",
    var kelurahan: String = "",
    var regional: String = "",
    var branch: String = "",
    var cluster: String = "",
    var no_hp_merchant: String = "",
    var no_wa_merchant: String = "",
    var email_merchant: String = "",
    var password: String = "",
    var nama_lengkap: String = "",
    var tgl_lahir: String = "",
    var no_hp_pemilik: String = "",
    var no_wa_pemilik: String = "",
    var foto_profil: String = "",
    var created_at: String = "",
    var updated_at: String = ""
    ) : Parcelable