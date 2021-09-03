package id.telkomsel.merchant.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelProduk(
    var id: Int = 0,
    var merchant_id: Int = 0,
    var kategori_id: Int = 0,
    var sub_kategori_id: Int = 0,
    var status: String = "",
    var tgl_kadaluarsa: String = "",
    var stok: Int = 0,
    var nama: String = "",
    var harga: Long = 0,
    var promo: String = "",
    var jumlah_poin: Long = 0,
    var deskripsi: String = "",
    var url_foto: String = "",
    var view: Int = 0,
    var jumlah_rating: Int = 0,
    var total_rating: Int = 0,
    var rating: Int = 0,
    var terjual: Int = 0,
    var created_by: Int = 0,
    var created_at: String = "",
    var updated_at: String = "",
    var isFavorite: Boolean = false,
    var dataMerchant: ModelMerchant? = null
    ) : Parcelable