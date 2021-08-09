package id.telkomsel.merchant.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelInfoApps(
    var id_info: Int = 0,
    var info: String = "",
    var profil: String = "",
    var program: String = "",
    var skb: String = "",
    var version_apps: String = "",
    var link: String = "",
    var created_at: String = "",
    var updated_at: String = ""
) : Parcelable