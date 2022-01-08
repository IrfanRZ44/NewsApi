package id.exomatik.news.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelNews(
    var author: String = "",
    var title: String = "",
    var description: String = "",
    var url: String = "",
    var urlToImage: String = "",
    var publishedAt: String = "",
    var content: String = "",
    var source: ModelSource? = null,
) : Parcelable