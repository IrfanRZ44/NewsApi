package id.telkomsel.merchant.model.response

import android.os.Parcelable
import id.telkomsel.merchant.model.ModelFotoProduk
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelResponseDaftarFotoProduk(
    var data: List<ModelFotoProduk> = emptyList(),
    var message: String = ""
    ) : Parcelable