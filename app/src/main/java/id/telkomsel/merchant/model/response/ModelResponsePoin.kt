package id.telkomsel.merchant.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelResponsePoin(
    var message: String = "",
    var poin: Int = 0,
    var totalPoin: Long = 0
    ) : Parcelable