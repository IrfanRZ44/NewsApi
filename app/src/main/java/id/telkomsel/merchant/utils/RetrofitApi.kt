package id.telkomsel.merchant.utils

import id.telkomsel.merchant.model.response.*
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by IrfanRZ on 02/08/2019.
 */
interface RetrofitApi {
    @Headers("Accept:application/json")
    @POST(Constant.reffProvinsi)
    fun getDaftarProvinsi(): Call<ModelResponseWilayah>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffKabupaten)
    fun getDaftarKabupaten(@Field("provinsi_id") id_provinsi: Long): Call<ModelResponseWilayah>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffKecamatan)
    fun getDaftarKecamatan(@Field("kabupaten_id") kabupaten_id: Long): Call<ModelResponseWilayah>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffKelurahan)
    fun getDaftarKelurahan(@Field("kecamatan_id") kecamatan_id: Long): Call<ModelResponseWilayah>

    @Headers("Accept:application/json")
    @GET(Constant.reffInfoApps)
    fun getInfoApps(): Call<ModelResponseInfoApps>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffCheckToken)
    fun checkToken(@Field("username") username: String,
                   @Field("token") token: String): Call<ModelResponseMerchant>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST("loginMerchantUsername")
    fun loginMerchantUsername(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("token") token: String
    ): Call<ModelResponseMerchant>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffLoginMerchantPhone)
    fun loginMerchantPhone(
        @Field("phone") phone: String,
        @Field("token") email: String,
        @Field("password") password: String
    ): Call<ModelResponseMerchant>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffCreateMerchant)
    fun createMerchant(
        @Field("username") username: String,
        @Field("nama_merchant") nama_merchant: String,
        @Field("alamat_merchant") alamat_merchant: String,
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String,
        @Field("tgl_peresmian_merchant") tgl_peresmian_merchant: String,
        @Field("provinsi") provinsi: String,
        @Field("kabupaten") kabupaten: String,
        @Field("kecamatan") kecamatan: String,
        @Field("kelurahan") kelurahan: String,
        @Field("regional") regional: String,
        @Field("branch") branch: String,
        @Field("cluster") cluster: String,
        @Field("no_hp_merchant") no_hp_merchant: String,
        @Field("no_wa_merchant") no_wa_merchant: String,
        @Field("verified_phone") verified_phone: String,
        @Field("email_merchant") email_merchant: String,
        @Field("password") password: String,
        @Field("nama_lengkap") nama_lengkap: String,
        @Field("tgl_lahir") tgl_lahir: String,
        @Field("no_hp_pemilik") no_hp_pemilik: String,
        @Field("no_wa_pemilik") no_wa_pemilik: String,
    ): Call<ModelResponse>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffUpdateMerchant)
    fun updateMerchant(
        @Field("id") id: Int,
        @Field("nama_merchant") nama_merchant: String,
        @Field("alamat_merchant") alamat_merchant: String,
        @Field("status_merchant") status_merchant: String,
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String,
        @Field("tgl_peresmian_merchant") tgl_peresmian_merchant: String,
        @Field("provinsi") provinsi: String,
        @Field("kabupaten") kabupaten: String,
        @Field("kecamatan") kecamatan: String,
        @Field("kelurahan") kelurahan: String,
        @Field("regional") regional: String,
        @Field("branch") branch: String,
        @Field("cluster") cluster: String,
        @Field("no_hp_merchant") no_hp_merchant: String,
        @Field("no_wa_merchant") no_wa_merchant: String,
        @Field("email_merchant") email_merchant: String,
        @Field("nama_lengkap") nama_lengkap: String,
        @Field("tgl_lahir") tgl_lahir: String,
        @Field("no_hp_pemilik") no_hp_pemilik: String,
        @Field("no_wa_pemilik") no_wa_pemilik: String,
        @Field("status") status: String
        ): Call<ModelResponse>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffValidateNewMerchant)
    fun validateNewMerchant(
        @Field("username") username: String,
        @Field("no_hp_merchant") no_hp_merchant: String
    ): Call<ModelResponse>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffLogoutMerchant)
    fun logoutMerchant(
        @Field("username") username: String
    ): Call<ModelResponse>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffUpdatePassword)
    fun updatePassword(
        @Field("id") id: Int,
        @Field("password_new") password_new: String
    ): Call<ModelResponse>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffGetDataMerchant)
    fun getDataMerchant(@Field("username") username: String): Call<ModelResponseMerchant>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffForgetPasswordMerchantUsername)
    fun forgetPasswordMerchantUsername(
        @Field("username") username: String
    ): Call<ModelResponseMerchant>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffForgetPasswordMerchantPhone)
    fun forgetPasswordMerchantPhone(
        @Field("phone") phone: String
    ): Call<ModelResponseMerchant>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffDaftarMerchantByAdmin)
    fun getDaftarMerchantByAdmin(
        @Field("cluster") id_branch: String,
        @Field("userRequest") userRequest: String,
        @Field("startPage") startPage: Int,
        @Field("status") status: String,
        @Field("search") search: String?
    ): Call<ModelResponseDaftarMerchant>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffUpdateStatusMerchant)
    fun updateStatusMerchant(
        @Field("id") id: Int,
        @Field("status_merchant") status_merchant: String,
        @Field("comment") comment: String
    ): Call<ModelResponse>

    companion object {
        const val baseUrl = Constant.reffBaseURL
    }
}