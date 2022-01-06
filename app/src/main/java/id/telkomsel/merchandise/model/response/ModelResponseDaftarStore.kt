package id.telkomsel.merchandise.model.response

import android.os.Parcelable
import id.telkomsel.merchandise.model.ModelStore
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelResponseDaftarStore(
    var dataStore: List<ModelStore> = emptyList(),
    var message: String = ""
    ) : Parcelable