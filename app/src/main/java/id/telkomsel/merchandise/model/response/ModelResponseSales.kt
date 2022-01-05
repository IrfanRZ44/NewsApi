package id.telkomsel.merchandise.model.response

import android.os.Parcelable
import id.telkomsel.merchandise.model.ModelSales
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelResponseSales(
    var dataSales: ModelSales? = null,
    var message: String = ""
    ) : Parcelable