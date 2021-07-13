package id.telkomsel.merchant.model.response

import android.os.Parcelable
import id.telkomsel.merchant.model.ModelMerchant
import id.telkomsel.merchant.model.ModelPelanggan
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelResponsePelanggan(
    var data: ModelPelanggan? = null,
    var message: String = ""
    ) : Parcelable