package id.telkomsel.merchant.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelRiwayatPoin(
    var id: Int = 0,
    var nomor_mkios: String = "",
    var transaksi: String = "",
    var poin: Long = 0,
    var poin_total: Long = 0,
    var status: String = "",
    var tahun: String = "",
    var bulan: String = "",
    var tanggal: String = "",
    var created_at: String = "",
    var updated_at: String = ""
) : Parcelable