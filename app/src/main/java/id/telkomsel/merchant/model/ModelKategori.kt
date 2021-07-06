package id.telkomsel.merchant.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelKategori(
    var id: Int = 0,
    var kategori_id: Int = 0,
    var nama: String = "",
    var isSelected: Boolean = false,
    var created_at: String = "",
    var updated_at: String = "",
    var total_produk: Int = 0,
    var nama_kategori: String = "",
    var isHeader: Boolean = false
) : Parcelable