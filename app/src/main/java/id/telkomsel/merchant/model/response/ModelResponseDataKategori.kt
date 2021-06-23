package id.telkomsel.merchant.model.response

import android.os.Parcelable
import id.telkomsel.merchant.model.ModelKategori
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelResponseDataKategori(
    var data: ModelKategori? = null,
    var message: String = ""
    ) : Parcelable