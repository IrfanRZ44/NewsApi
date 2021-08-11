package id.telkomsel.merchant.listener

import id.telkomsel.merchant.model.ModelFotoIklan

open interface ListenerFotoIklan {
    fun clickFotoIklan(rows: ModelFotoIklan){}
    fun clickUploadIklan(position: Int, rows: ModelFotoIklan){}
}