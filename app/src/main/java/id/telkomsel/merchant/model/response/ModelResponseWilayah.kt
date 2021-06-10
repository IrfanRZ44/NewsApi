package id.telkomsel.merchant.model.response

import android.os.Parcelable
import id.telkomsel.merchant.model.ModelWilayah
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelResponseWilayah(
    var data: List<ModelWilayah> = emptyList(),
    var message: String = ""
    ) : Parcelable