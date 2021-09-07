package id.telkomsel.merchant.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelVoucher(
    var id: Int = 0,
    var produk_id: Int = 0,
    var username: String = "",
    var kode_voucher: String = "",
    var jumlah: Int = 0,
    var total_harga: Long = 0,
    var status: String = "",
    var created_at: String = "",
    var updated_at: String = "",
    var dataProduk: ModelProduk? = null,
    var dataMerchant: ModelMerchant? = null
    ) : Parcelable