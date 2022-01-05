package id.telkomsel.merchandise.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelWilayah(
    var id: Long = 0,
    var nama: String? = "",
    var CLUSTER_ID: Long = 0,
    var CLUSTER: String? = "",
    var REGION_OMS: String? = "",
    var BRANCH: String? = ""
    ) : Parcelable