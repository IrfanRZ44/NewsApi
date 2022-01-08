package id.exomatik.news.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelSource(
    var id: String = "",
    var name: String = ""
    ) : Parcelable