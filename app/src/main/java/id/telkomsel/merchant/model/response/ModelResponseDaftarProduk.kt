package id.telkomsel.merchant.model.response

import android.os.Parcelable
import id.telkomsel.merchant.model.ModelMerchant
import id.telkomsel.merchant.model.ModelPelanggan
import id.telkomsel.merchant.model.ModelProduk
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelResponseDaftarProduk(
    var data: List<ModelProduk> = emptyList(),
    var dataUser: ModelPelanggan? = null,
    var message: String = ""
    ) : Parcelable