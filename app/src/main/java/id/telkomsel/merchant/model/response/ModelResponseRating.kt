package id.telkomsel.merchant.model.response

import android.os.Parcelable
import id.telkomsel.merchant.model.ModelPelanggan
import id.telkomsel.merchant.model.ModelVoucher
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelResponseRating(
    var data: ModelVoucher? = null,
    var message: String = ""
    ) : Parcelable