package id.telkomsel.merchant.model.response

import android.os.Parcelable
import id.telkomsel.merchant.model.ModelRiwayatPoin
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelResponseRiwayatPoin(
    var dataRiwayat: List<ModelRiwayatPoin> = emptyList(),
    var message: String = ""
    ) : Parcelable