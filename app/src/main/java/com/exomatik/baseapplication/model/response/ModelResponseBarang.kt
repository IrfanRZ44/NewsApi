package com.exomatik.baseapplication.model.response

import android.os.Parcelable
import com.exomatik.baseapplication.model.ModelBarang
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelResponseBarang(
    var data: List<ModelBarang> = emptyList(),
    var message: String = ""
    ) : Parcelable