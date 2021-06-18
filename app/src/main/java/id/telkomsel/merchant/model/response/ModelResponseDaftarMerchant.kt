package id.telkomsel.merchant.model.response

import android.os.Parcelable
import id.telkomsel.merchant.model.ModelMerchant
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelResponseDaftarMerchant(
    var dataMerchant: List<ModelMerchant> = emptyList(),
    var message: String = ""
    ) : Parcelable