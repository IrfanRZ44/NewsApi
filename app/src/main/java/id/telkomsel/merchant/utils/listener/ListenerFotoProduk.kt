package id.telkomsel.merchant.utils.listener

import id.telkomsel.merchant.model.ModelFotoProduk

open interface ListenerFotoProduk {
    fun clickFotoProduk(rows: ModelFotoProduk){}
    fun clickUploadProduk(position: Int, rows: ModelFotoProduk){}
    fun clickEditProduk(){}
}