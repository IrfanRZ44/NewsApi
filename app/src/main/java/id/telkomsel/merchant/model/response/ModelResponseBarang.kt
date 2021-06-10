package id.telkomsel.merchant.model.response

import android.os.Parcelable
import id.telkomsel.merchant.model.ModelBarang
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelResponseBarang(
    var data: List<ModelBarang> = emptyList(),
    var message: String = ""
    ) : Parcelable