package id.telkomsel.merchant.model.response

import android.os.Parcelable
import id.telkomsel.merchant.model.ModelMerchant
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelResponseMerchant(
    var dataMerchant: ModelMerchant? = null,
    var message: String = ""
    ) : Parcelable