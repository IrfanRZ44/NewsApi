package id.telkomsel.merchandise.utils

import id.telkomsel.merchandise.model.response.*
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
                   @Field("token") token: String): Call<ModelResponseSales>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST("loginSalesUsername")
    fun loginSalesUsername(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("token") token: String
    ): Call<ModelResponseSales>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffLoginSalesPhone)
    fun loginSalesPhone(
        @Field("phone") phone: String,
        @Field("token") email: String,
        @Field("password") password: String
    ): Call<ModelResponseSales>

    @Headers("Accept:application/json")
    @Multipart
    @POST(Constant.reffCreateSales)
    fun createSales(
        @Part("username") username: RequestBody,
        @Part("email") email: RequestBody,
        @Part("pengalaman_kerja") pengalaman_kerja: RequestBody,
        @Part("password") password: RequestBody,
        @Part("nama_lengkap") nama_lengkap: RequestBody,
        @Part("nama_panggilan") nama_panggilan: RequestBody,
        @Part("jenis_kelamin") jenis_kelamin: RequestBody,
        @Part("status_pernikahan") status_pernikahan: RequestBody,
        @Part("jumlah_anak") jumlah_anak: RequestBody,
        @Part("tempat_lahir") tempat_lahir: RequestBody,
        @Part("tgl_lahir") tgl_lahir: RequestBody,
        @Part("alamat") alamat: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("provinsi") provinsi: RequestBody,
        @Part("kabupaten") kabupaten: RequestBody,
        @Part("kecamatan") kecamatan: RequestBody,
        @Part("kelurahan") kelurahan: RequestBody,
        @Part("regional") regional: RequestBody,
        @Part("branch") branch: RequestBody,
        @Part("cluster") cluster: RequestBody,
        @Part("no_hp_sales") no_hp_sales: RequestBody,
        @Part("no_wa_sales") no_wa_sales: RequestBody,
        @Part("verified_phone") verified_phone: RequestBody,
        @Part("kepemilikan_sim") kepemilikan_sim: RequestBody,
        @Part("nomor_polisi") nomor_polisi: RequestBody,
        @Part("kategori_motor") kategori_motor: RequestBody,
        @Part foto_profil: MultipartBody.Part?,
        @Part foto_depan_kendaraan: MultipartBody.Part?,
        @Part foto_belakang_kendaraan: MultipartBody.Part?,
        @Part foto_wajah: MultipartBody.Part?,
        @Part foto_seluruh_badan: MultipartBody.Part?
    ): Call<ModelResponse>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffUpdateSales)
    fun updateSales(
        @Field("id") id: Int,
        @Field("email") email: String,
        @Field("status") status: String,
        @Field("pengalaman_kerja") pengalaman_kerja: String,
        @Field("nama_lengkap") nama_lengkap: String,
        @Field("nama_panggilan") nama_panggilan: String,
        @Field("jenis_kelamin") jenis_kelamin: String,
        @Field("status_pernikahan") status_pernikahan: String,
        @Field("jumlah_anak") jumlah_anak: String,
        @Field("tempat_lahir") tempat_lahir: String,
        @Field("tgl_lahir") tgl_lahir: String,
        @Field("alamat") alamat: String,
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String,
        @Field("provinsi") provinsi: String,
        @Field("kabupaten") kabupaten: String,
        @Field("kecamatan") kecamatan: String,
        @Field("kelurahan") kelurahan: String,
        @Field("regional") regional: String,
        @Field("branch") branch: String,
        @Field("cluster") cluster: String,
        @Field("no_hp_sales") no_hp_sales: String,
        @Field("no_wa_sales") no_wa_sales: String,
        @Field("kepemilikan_sim") kepemilikan_sim: String,
        @Field("nomor_polisi") nomor_polisi: String,
        @Field("kategori_motor") kategori_motor: String,
    ): Call<ModelResponse>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffUpdateAdmin)
    fun updateAdmin(
        @Field("id") id: Int,
        @Field("nama_sales") nama_sales: String,
        @Field("alamat_sales") alamat_sales: String,
        @Field("status_sales") status_sales: String,
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String,
        @Field("provinsi") provinsi: String,
        @Field("kabupaten") kabupaten: String,
        @Field("kecamatan") kecamatan: String,
        @Field("kelurahan") kelurahan: String,
        @Field("regional") regional: String,
        @Field("branch") branch: String,
        @Field("cluster") cluster: String,
        @Field("no_hp_sales") no_hp_sales: String,
        @Field("no_wa_sales") no_wa_sales: String,
        @Field("email_sales") email_sales: String,
        @Field("nama_lengkap") nama_lengkap: String,
        @Field("tgl_lahir") tgl_lahir: String,
        @Field("no_hp_pemilik") no_hp_pemilik: String,
        @Field("no_wa_pemilik") no_wa_pemilik: String,
    ): Call<ModelResponse>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffValidateNewSales)
    fun validateNewSales(
        @Field("no_hp_sales") no_hp_sales: String,
        @Field("username") username: String
    ): Call<ModelResponse>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffLogoutSales)
    fun logoutSales(
        @Field("username") username: String
    ): Call<ModelResponse>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffUpdatePasswordSales)
    fun updatePasswordSales(
        @Field("id") id: Int,
        @Field("password_new") password_new: String
    ): Call<ModelResponse>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffGetDataSales)
    fun getDataSales(@Field("username") username: String): Call<ModelResponseSales>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffGetDataSalesById)
    fun getDataSalesById(@Field("sales_id") sales_id: String): Call<ModelResponseSales>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffForgetPasswordSalesUsername)
    fun forgetPasswordSalesUsername(
        @Field("username") username: String
    ): Call<ModelResponseSales>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffForgetPasswordSalesPhone)
    fun forgetPasswordSalesPhone(
        @Field("phone") phone: String
    ): Call<ModelResponseSales>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffDaftarSalesByAdmin)
    fun getDaftarSalesByAdmin(
        @Field("cluster") cluster: String,
        @Field("userRequest") userRequest: String,
        @Field("startPage") startPage: Int,
        @Field("status") status: String,
        @Field("search") search: String?
    ): Call<ModelResponseDaftarSales>

//    @Headers("Accept:application/json")
//    @FormUrlEncoded
//    @POST(Constant.reffDaftarStoreBySales)
//    fun getDaftarStoreBySales(
//        @Field("sales_id") sales_id: String?,
//        @Field("cluster") cluster: String?,
//        @Field("level") level: String,
//        @Field("startPage") startPage: Int,
//        @Field("status") status: String,
//        @Field("search") search: String?,
//        @Field("sub_kategori_id") sub_kategori_id: String?,
//        @Field("stok") stok: Int,
//        @Field("isKadaluarsa") isKadaluarsa: Boolean
//    ): Call<ModelResponseDaftarStore>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffUpdateStatusSales)
    fun updateStatusSales(
        @Field("id") id: Int,
        @Field("status") status: String,
        @Field("comment") comment: String
    ): Call<ModelResponse>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffUpdateStatusStore)
    fun updateStatusStore(
        @Field("id") id: Int,
        @Field("status") status: String,
        @Field("comment") comment: String
    ): Call<ModelResponse>

    @Headers("Accept:application/json")
    @Multipart
    @POST(Constant.reffCreateStore)
    fun createStore(
        @Part("jenis_store") jenis_store: RequestBody,
        @Part("kode_toko") kode_toko: RequestBody,
        @Part("alamat") alamat: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("provinsi") provinsi: RequestBody,
        @Part("kabupaten") kabupaten: RequestBody,
        @Part("kecamatan") kecamatan: RequestBody,
        @Part("kelurahan") kelurahan: RequestBody,
        @Part("regional") regional: RequestBody,
        @Part("branch") branch: RequestBody,
        @Part("cluster") cluster: RequestBody,
        @Part("nama_pic") nama_pic: RequestBody,
        @Part("no_hp_pic") no_hp_pic: RequestBody,
        @Part("no_wa_pic") no_wa_pic: RequestBody,
        @Part("nama_kasir_1") nama_kasir_1: RequestBody,
        @Part("no_hp_kasir_1") no_hp_kasir_1: RequestBody,
        @Part("no_wa_kasir_1") no_wa_kasir_1: RequestBody,
        @Part("nama_kasir_2") nama_kasir_2: RequestBody,
        @Part("no_hp_kasir_2") no_hp_kasir_2: RequestBody,
        @Part("no_wa_kasir_2") no_wa_kasir_2: RequestBody,
        @Part("nama_kasir_3") nama_kasir_3: RequestBody,
        @Part("no_hp_kasir_3") no_hp_kasir_3: RequestBody,
        @Part("no_wa_kasir_3") no_wa_kasir_3: RequestBody,
        @Part("nama_kasir_4") nama_kasir_4: RequestBody,
        @Part("no_hp_kasir_4") no_hp_kasir_4: RequestBody,
        @Part("no_wa_kasir_4") no_wa_kasir_4: RequestBody,
        @Part foto_depan_atas: MultipartBody.Part?,
        @Part foto_depan_bawah: MultipartBody.Part?,
        @Part foto_etalase_perdana: MultipartBody.Part?,
        @Part foto_meja_pembayaran: MultipartBody.Part?,
        @Part foto_belakang_kasir: MultipartBody.Part?,
    ): Call<ModelResponse>

    @Headers("Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.reffEditStore)
    fun editStore(
        @Field("status") status: String,
        @Field("store_id") store_id: String,
        @Field("sales_id") sales_id: String,
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
    @Multipart
    @POST(Constant.reffCreateFotoStore)
    fun createFotoStore(
        @Part("store_id") store_id: RequestBody,
        @Part("level") level: RequestBody,
        @Part url_foto: MultipartBody.Part?
    ): Call<ModelResponse>

    @Headers("Accept:application/json")
    @Multipart
    @POST(Constant.reffUpdateFotoStore)
    fun updateFotoStore(
        @Part("id") id: RequestBody,
        @Part("level") level: RequestBody,
        @Part url_foto: MultipartBody.Part?
    ): Call<ModelResponse>

    companion object {
        const val baseUrl = Constant.reffBaseURL
    }
}