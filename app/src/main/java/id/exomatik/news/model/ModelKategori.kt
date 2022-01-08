package id.exomatik.news.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelKategori(
    var id: String = "",
    var isSelected: Boolean = false,
    var nama_kategori: String = "",
) : Parcelable