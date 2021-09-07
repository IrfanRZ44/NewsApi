package id.telkomsel.merchant.model.response

import android.os.Parcelable
import id.telkomsel.merchant.model.ModelVoucher
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelResponseVoucher(
    var data: List<ModelVoucher> = emptyList(),
    var message: String = ""
    ) : Parcelable