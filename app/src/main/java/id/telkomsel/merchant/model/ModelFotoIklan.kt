package id.telkomsel.merchant.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelFotoIklan(
    var id: Int = 0,
    var url_foto: String = "",
    var created_at: String = "",
    var updated_at: String = ""
    ) : Parcelable