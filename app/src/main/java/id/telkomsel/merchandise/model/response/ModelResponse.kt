package id.telkomsel.merchandise.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelResponse(
    var message: String = ""
    ) : Parcelable