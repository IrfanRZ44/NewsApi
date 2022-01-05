package id.telkomsel.merchandise.model.response

import android.os.Parcelable
import id.telkomsel.merchandise.model.ModelWilayah
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelResponseWilayah(
    var data: List<ModelWilayah> = emptyList(),
    var message: String = ""
    ) : Parcelable