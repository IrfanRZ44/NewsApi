package id.telkomsel.merchant.model.response

import android.os.Parcelable
import id.telkomsel.merchant.model.ModelInfoApps
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelResponseInfoApps(
    var dataApps: ModelInfoApps? = null,
    var message: String = ""
    ) : Parcelable