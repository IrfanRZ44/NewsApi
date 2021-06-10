package id.telkomsel.merchant.utils

import android.app.Activity
import id.telkomsel.merchant.model.ModelMerchant
import id.telkomsel.merchant.model.response.*
import net.gotev.uploadservice.protocols.multipart.MultipartUploadRequest
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitUtils{
    private val retrofit = Retrofit.Builder()
        .baseUrl(RetrofitApi.baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val api = retrofit.create(RetrofitApi::class.java)

    fun getDaftarProvinsi(callback: Callback<ModelResponseWilayah>){
        val call = api.getDaftarProvinsi()
        call.enqueue(callback)
    }

    fun getDaftarKabupaten(idProvinsi: Long, callback: Callback<ModelResponseWilayah>){
        val call = api.getDaftarKabupaten(idProvinsi)
        call.enqueue(callback)
    }

    fun getDaftarKecamatan(idKabupaten: Long, callback: Callback<ModelResponseWilayah>){
        val call = api.getDaftarKecamatan(idKabupaten)
        call.enqueue(callback)
    }

    fun getDaftarKelurahan(idKecamatan: Long, callback: Callback<ModelResponseWilayah>){
        val call = api.getDaftarKelurahan(idKecamatan)
        call.enqueue(callback)
    }

    fun getInfoApps(callback: Callback<ModelResponseInfoApps>){
        val call = api.getInfoApps()
        call.enqueue(callback)
    }

    fun checkToken(username: String, token: String, callback: Callback<ModelResponseMerchant>){
        val call = api.checkToken(username, token)
        call.enqueue(callback)
    }

    fun uploadFoto(dataModel: String, folder: String, fileName: String, id: String, filePath: String, activity: Activity){
        val uploadRequest = MultipartUploadRequest(activity, Constant.reffBaseURLUploadFoto)
        uploadRequest.setMethod("POST")
        uploadRequest.addFileToUpload(
            filePath = filePath,
            parameterName = "image"
        )
        uploadRequest.addParameter(Constant.reffId, id)
        uploadRequest.addParameter(Constant.reffDataModel, dataModel)
        uploadRequest.addParameter(Constant.reffFolder, folder)
        uploadRequest.addParameter(Constant.reffName, fileName)
        uploadRequest.startUpload()
    }

    fun loginMerchantPhone(phone: String, password: String, token: String, callback: Callback<ModelResponseMerchant>){
        val call = api.loginMerchantPhone(phone, token, password)
        call.enqueue(callback)
    }

    fun createMerchant(dataMerchant: ModelMerchant, callback: Callback<ModelResponse>){
        val call = api.createMerchant(dataMerchant.username, dataMerchant.nama_merchant,
            dataMerchant.alamat_merchant, dataMerchant.latitude, dataMerchant.longitude, dataMerchant.tgl_peresmian_merchant,
            dataMerchant.provinsi, dataMerchant.kabupaten, dataMerchant.kecamatan, dataMerchant.kelurahan,
            dataMerchant.regional, dataMerchant.branch, dataMerchant.cluster,
            dataMerchant.no_hp_merchant, dataMerchant.no_wa_merchant, dataMerchant.verified_phone,
            dataMerchant.email_merchant, dataMerchant.password, dataMerchant.nama_lengkap, dataMerchant.tgl_lahir,
            dataMerchant.no_hp_pemilik, dataMerchant.no_wa_pemilik,
        )
        call.enqueue(callback)
    }

    fun updateMerchant(dataMerchant: ModelMerchant, callback: Callback<ModelResponse>){
        val call = api.updateMerchant(dataMerchant.id, dataMerchant.nama_merchant,
            dataMerchant.status_merchant,
            dataMerchant.alamat_merchant, dataMerchant.latitude, dataMerchant.longitude, dataMerchant.tgl_peresmian_merchant,
            dataMerchant.provinsi, dataMerchant.kabupaten, dataMerchant.kecamatan, dataMerchant.kelurahan,
            dataMerchant.regional, dataMerchant.branch, dataMerchant.cluster, dataMerchant.no_hp_merchant,
            dataMerchant.no_wa_merchant, dataMerchant.email_merchant,
            dataMerchant.nama_lengkap, dataMerchant.tgl_lahir,
            dataMerchant.no_hp_pemilik, dataMerchant.no_wa_pemilik,
        )
        call.enqueue(callback)
    }

    fun validateNewMerchant(dataMerchant: ModelMerchant, callback: Callback<ModelResponse>){
        val call = api.validateNewMerchant(dataMerchant.username, dataMerchant.no_hp_merchant)
        call.enqueue(callback)
    }

    fun loginMerchantUsername(username: String, password: String, token: String, callback: Callback<ModelResponseMerchant>){
        val call = api.loginMerchantUsername(username, password, token)
        call.enqueue(callback)
    }
}