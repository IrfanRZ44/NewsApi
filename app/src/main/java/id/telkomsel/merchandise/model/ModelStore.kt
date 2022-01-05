package id.telkomsel.merchandise.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelStore(
    var id: Int = 0,
    var jenis_store: String = "",
    var kode_toko: String = "",
    var status: String = "",
    var comment: String = "",
    var alamat: String = "",
    var latitude: String = "",
    var longitude: String = "",
    var provinsi: String = "",
    var kabupaten: String = "",
    var kecamatan: String = "",
    var kelurahan: String = "",
    var regional: String = "",
    var branch: String = "",
    var cluster: String = "",
    var nama_pic: String = "",
    var no_hp_pic: String = "",
    var no_wa_pic: String = "",
    var nama_kasir_1: String = "",
    var no_hp_kasir_1: String = "",
    var no_wa_kasir_1: String = "",
    var nama_kasir_2: String = "",
    var no_hp_kasir_2: String = "",
    var no_wa_kasir_2: String = "",
    var nama_kasir_3: String = "",
    var no_hp_kasir_3: String = "",
    var no_wa_kasir_3: String = "",
    var nama_kasir_4: String = "",
    var no_hp_kasir_4: String = "",
    var no_wa_kasir_4: String = "",
    var foto_depan_atas: String = "",
    var foto_depan_bawah: String = "",
    var foto_etalase_perdana: String = "",
    var foto_meja_pembayaran: String = "",
    var foto_belakang_kasir: String = "",
    var created_at: String = "",
    var updated_at: String = ""
    ) : Parcelable