package id.exomatik.news.model.response

import android.os.Parcelable
import id.exomatik.news.model.ModelNews
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelResponseDaftarNews(
    var articles: List<ModelNews> = emptyList(),
    var totalResults: Int = 0,
    var status: String = ""
    ) : Parcelable