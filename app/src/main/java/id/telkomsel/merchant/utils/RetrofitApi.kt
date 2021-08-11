package id.telkomsel.merchant.utils

import id.telkomsel.merchant.model.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
    @POST(Constant.reffLoginPelangganUsername)
    fun loginPelangganUsername(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("token") token: String
    ): Call<ModelResponsePelanggan>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffLoginPelangganPhone)
    fun loginPelangganPhone(
        @Field("nomor_mkios") nomor_mkios: String,
        @Field("token") email: String,
        @Field("password") password: String
    ): Call<ModelResponsePelanggan>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffCreateMerchant)
    fun createMerchant(
        @Field("username") username: String,
        @Field("kategori_id") kategori_id: Int,
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
    @Multipart
    @POST(Constant.reffCreatePelanggan)
    fun createPelanggan(
        @Part("username") username: RequestBody,
        @Part("nama") nama: RequestBody,
        @Part("nomor_mkios") nomor_mkios: RequestBody,
        @Part("token") token: RequestBody,
        @Part("alamat") alamat: RequestBody,
        @Part("provinsi") provinsi: RequestBody,
        @Part("kabupaten") kabupaten: RequestBody,
        @Part("kecamatan") kecamatan: RequestBody,
        @Part("kelurahan") kelurahan: RequestBody,
        @Part("regional") regional: RequestBody,
        @Part("branch") branch: RequestBody,
        @Part("cluster") cluster: RequestBody,
        @Part("no_hp") no_hp: RequestBody,
        @Part("no_wa") no_wa: RequestBody,
        @Part("verified_phone") verified_phone: RequestBody,
        @Part("password") password: RequestBody,
        @Part("tgl_lahir") tgl_lahir: RequestBody,
        @Part url_foto: MultipartBody.Part?
    ): Call<ModelResponsePelanggan>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffUpdatePelanggan)
    fun updateProfilPelanggan(
        @Field("username") username: String,
        @Field("nama") nama: String,
        @Field("alamat") alamat: String,
        @Field("provinsi") provinsi: String,
        @Field("kabupaten") kabupaten: String,
        @Field("kecamatan") kecamatan: String,
        @Field("kelurahan") kelurahan: String,
        @Field("regional") regional: String,
        @Field("branch") branch: String,
        @Field("cluster") cluster: String,
        @Field("no_hp") no_hp: String,
        @Field("no_wa") no_wa: String,
        @Field("tgl_lahir") tgl_lahir: String,
    ): Call<ModelResponsePelanggan>

    @Headers("Accept:application/json")
    @Multipart
    @POST(Constant.reffEditFotoPelanggan)
    fun updateFotoPelanggan(
        @Part("username") username: RequestBody,
        @Part url_foto: MultipartBody.Part?
    ): Call<ModelResponsePelanggan>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffUpdateMerchant)
    fun updateMerchant(
        @Field("id") id: Int,
        @Field("nama_merchant") nama_merchant: String,
        @Field("kategori_id") kategori_id: Int,
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
    @POST(Constant.reffUpdateAdmin)
    fun updateAdmin(
        @Field("id") id: Int,
        @Field("nama_merchant") nama_merchant: String,
        @Field("alamat_merchant") alamat_merchant: String,
        @Field("status_merchant") status_merchant: String,
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String,
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
    @POST(Constant.reffValidateNewPelanggan)
    fun validateNewPelanggan(
        @Field("username") username: String,
        @Field("nomor_mkios") nomor_mkios: String
    ): Call<ModelResponse>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffValidateNewMerchantPhone)
    fun validateNewMerchantPhone(
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
    @POST(Constant.reffLogoutPelanggan)
    fun logoutPelanggan(
        @Field("username") username: String
    ): Call<ModelResponse>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffUpdatePasswordMerchant)
    fun updatePasswordMerchant(
        @Field("id") id: Int,
        @Field("password_new") password_new: String
    ): Call<ModelResponse>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffUpdatePasswordPelanggan)
    fun updatePasswordPelanggan(
        @Field("id") id: Int,
        @Field("password_new") password_new: String
    ): Call<ModelResponse>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffGetDataMerchant)
    fun getDataMerchant(@Field("username") username: String): Call<ModelResponseMerchant>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffGetDataPelanggan)
    fun getDataPelanggan(@Field("username") username: String): Call<ModelResponsePelanggan>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffGetDataMerchantById)
    fun getDataMerchantById(@Field("merchant_id") merchant_id: String): Call<ModelResponseMerchant>

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
    @POST(Constant.reffForgetPasswordPelangganUsername)
    fun forgetPasswordPelangganUsername(
        @Field("username") username: String
    ): Call<ModelResponsePelanggan>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffForgetPasswordPelangganPhone)
    fun forgetPasswordPelangganPhone(
        @Field("nomor_mkios") nomor_mkios: String
    ): Call<ModelResponsePelanggan>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffDaftarMerchantByAdmin)
    fun getDaftarMerchantByAdmin(
        @Field("cluster") cluster: String,
        @Field("userRequest") userRequest: String,
        @Field("startPage") startPage: Int,
        @Field("status") status: String,
        @Field("search") search: String?
    ): Call<ModelResponseDaftarMerchant>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffDaftarProdukByMerchant)
    fun getDaftarProdukByMerchant(
        @Field("merchant_id") merchant_id: String?,
        @Field("cluster") cluster: String?,
        @Field("level") level: String,
        @Field("startPage") startPage: Int,
        @Field("status") status: String,
        @Field("search") search: String?,
        @Field("sub_kategori_id") sub_kategori_id: String?,
        @Field("stok") stok: Int,
        @Field("isKadaluarsa") isKadaluarsa: Boolean
    ): Call<ModelResponseDaftarProduk>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffDaftarProdukByPelanggan)
    fun getDaftarProdukByPelanggan(
        @Field("startPage") startPage: Int,
        @Field("search") search: String?,
        @Field("sub_kategori_id") sub_kategori_id: String?,
        @Field("username") username: String?
    ): Call<ModelResponseDaftarProduk>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffDaftarProdukFavorit)
    fun getDaftarProdukFavorit(
        @Field("startPage") startPage: Int,
        @Field("username") username: String
    ): Call<ModelResponseDaftarProduk>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffDaftarPickMerchant)
    fun getPickMerchant(
        @Field("cluster") id_branch: String,
        @Field("userRequest") userRequest: String,
        @Field("startPage") startPage: Int,
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

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffUpdateStatusProduk)
    fun updateStatusProduk(
        @Field("id") id: Int,
        @Field("status") status: String,
        @Field("comment") comment: String
    ): Call<ModelResponse>

    @Headers("Accept:application/json")
    @GET(Constant.reffDaftarKategori)
    fun getDaftarKategori(): Call<ModelResponseDaftarKategori>

    @Headers("Accept:application/json")
    @GET(Constant.reffDaftarTopSubKategori)
    fun getTopSubKategori(): Call<ModelResponseDaftarKategori>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffDataKategori)
    fun getDataKategori(
        @Field("kategori_id") kategori_id: Int,
    ): Call<ModelResponseDataKategori>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffDaftarSubKategoriByMerchant)
    fun getDaftarSubKategoriByMerchant(
        @Field("kategori_id") kategori_id: Int,
    ): Call<ModelResponseDaftarKategori>

    @Headers("Accept:application/json")
    @GET(Constant.reffDaftarSubKategori)
    fun getDaftarSubKategori(): Call<ModelResponseDaftarKategori>

    @Headers("Accept:application/json")
    @GET(Constant.reffDaftarSubKategoriFilterKategori)
    fun getDaftarSubKategoriFilterKategori(): Call<ModelResponseDaftarKategori>

    @Headers("Accept:application/json")
    @Multipart
    @POST(Constant.reffCreateProduk)
    fun createProduk(
        @Part("status") status: RequestBody,
        @Part("merchant_id") merchant_id: RequestBody,
        @Part("created_by") created_by: RequestBody,
        @Part("kategori_id") kategori_id: RequestBody,
        @Part("sub_kategori_id") sub_kategori_id: RequestBody,
        @Part("tgl_kadaluarsa") tgl_kadaluarsa: RequestBody,
        @Part("stok") stok: RequestBody,
        @Part("nama") nama: RequestBody,
        @Part("harga") harga: RequestBody,
        @Part("promo") promo: RequestBody,
        @Part("poin") poin: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part("regional") regional: RequestBody,
        @Part("branch") branch: RequestBody,
        @Part("cluster") cluster: RequestBody,
        @Part url_foto: MultipartBody.Part?
    ): Call<ModelResponse>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffEditProduk)
    fun editProduk(
        @Field("status") status: String,
        @Field("produk_id") produk_id: String,
        @Field("merchant_id") merchant_id: String,
        @Field("kategori_id") kategori_id: String,
        @Field("sub_kategori_id") sub_kategori_id: String,
        @Field("tgl_kadaluarsa") tgl_kadaluarsa: String,
        @Field("stok") stok: String,
        @Field("nama") nama: String,
        @Field("harga") harga: String,
        @Field("promo") promo: String,
        @Field("poin") poin: String,
        @Field("deskripsi") deskripsi: String,
        @Field("regional") regional: String,
        @Field("branch") branch: String,
        @Field("cluster") cluster: String,
    ): Call<ModelResponse>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffPelangganViewProduk)
    fun pelangganViewProduk(
        @Field("id_produk") id_produk: String
    ): Call<ModelResponse>

    @Headers("Accept:application/json")
    @Multipart
    @POST(Constant.reffEditFotoProfilProduk)
    fun editFotoProfilProduk(
        @Part("produk_id") produk_id: RequestBody,
        @Part("level") level: RequestBody,
        @Part url_foto: MultipartBody.Part?
    ): Call<ModelResponse>

    @Headers("Accept:application/json")
    @Multipart
    @POST(Constant.reffCreateFotoProduk)
    fun createFotoProduk(
        @Part("produk_id") produk_id: RequestBody,
        @Part("level") level: RequestBody,
        @Part url_foto: MultipartBody.Part?
    ): Call<ModelResponse>

    @Headers("Accept:application/json")
    @Multipart
    @POST(Constant.reffUpdateFotoProduk)
    fun updateFotoProduk(
        @Part("id") id: RequestBody,
        @Part("level") level: RequestBody,
        @Part url_foto: MultipartBody.Part?
    ): Call<ModelResponse>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffDeleteFotoProduk)
    fun deleteFotoProduk(
        @Field("produk_id") produk_id: Int
    ): Call<ModelResponse>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffGetDaftarFotoProduk)
    fun getDaftarFotoProduk(
        @Field("produk_id") produk_id: Int
    ): Call<ModelResponseDaftarFotoProduk>

    @Headers("Accept:application/json")
    @Multipart
    @POST(Constant.reffCreateFotoIklan)
    fun createFotoIklan(
        @Part url_foto: MultipartBody.Part?
    ): Call<ModelResponse>

    @Headers("Accept:application/json")
    @Multipart
    @POST(Constant.reffUpdateFotoIklan)
    fun updateFotoIklan(
        @Part("id") id: RequestBody,
        @Part url_foto: MultipartBody.Part?
    ): Call<ModelResponse>

    @Headers("Accept:application/json")
    @GET(Constant.reffGetDaftarFotoIklan)
    fun getDaftarFotoIklan(): Call<ModelResponseDaftarFotoIklan>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffCreateProdukFavorit)
    fun createProdukFav(
        @Field("produk_id") produk_id: Int,
        @Field("username") username: String
    ): Call<ModelResponse>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffDeleteProdukFavorit)
    fun deleteProdukFav(
        @Field("produk_id") produk_id: Int,
        @Field("username") username: String
    ): Call<ModelResponse>

    companion object {
        const val baseUrl = Constant.reffBaseURL
    }
}