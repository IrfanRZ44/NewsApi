package id.exomatik.news.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelCountry(
    var id: String = "",
    var isSelected: Boolean = false,
    var name: String = ""
) : Parcelable