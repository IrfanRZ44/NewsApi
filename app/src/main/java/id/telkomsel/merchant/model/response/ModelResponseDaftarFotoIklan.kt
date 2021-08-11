package id.telkomsel.merchant.model.response

import android.os.Parcelable
import id.telkomsel.merchant.model.ModelFotoIklan
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelResponseDaftarFotoIklan(
    var data: List<ModelFotoIklan> = emptyList(),
    var message: String = ""
    ) : Parcelable