package id.telkomsel.merchandise.utils

import android.app.Activity
import id.telkomsel.merchandise.model.ModelSales
import id.telkomsel.merchandise.model.ModelStore
import id.telkomsel.merchandise.model.response.*
import net.gotev.uploadservice.protocols.multipart.MultipartUploadRequest
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

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

    fun checkToken(username: String, token: String, callback: Callback<ModelResponseSales>){
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

    fun loginSalesUsername(username: String, password: String, token: String, callback: Callback<ModelResponseSales>){
        val call = api.loginSalesUsername(username, password, token)
        call.enqueue(callback)
    }

    fun loginSalesPhone(phone: String, password: String, token: String, callback: Callback<ModelResponseSales>){
        val call = api.loginSalesPhone(phone, token, password)
        call.enqueue(callback)
    }

    fun createSales(dataSales: ModelSales, callback: Callback<ModelResponse>){
        val fileFotoProfil = File(dataSales.foto_profil)
        val fotoProfil = MultipartBody.Part.createFormData("foto_profil", fileFotoProfil.name, RequestBody.create(
            MediaType.get("image/*"), fileFotoProfil))
        val fileFotoDepan = File(dataSales.foto_depan_kendaraan)
        val fotoDepan = MultipartBody.Part.createFormData("foto_depan_kendaraan", fileFotoDepan.name, RequestBody.create(
            MediaType.get("image/*"), fileFotoDepan))
        val fileFotoBelakang = File(dataSales.foto_belakang_kendaraan)
        val fotoBelakang = MultipartBody.Part.createFormData("foto_belakang_kendaraan", fileFotoBelakang.name, RequestBody.create(
            MediaType.get("image/*"), fileFotoBelakang))
        val fileFotoWajah = File(dataSales.foto_wajah)
        val fotoWajah = MultipartBody.Part.createFormData("foto_wajah", fileFotoWajah.name, RequestBody.create(
            MediaType.get("image/*"), fileFotoWajah))
        val fileFotoBadan = File(dataSales.foto_seluruh_badan)
        val fotoBadan = MultipartBody.Part.createFormData("foto_seluruh_badan", fileFotoBadan.name, RequestBody.create(
            MediaType.get("image/*"), fileFotoBadan))

        val username = RequestBody.create(MediaType.get("text/plain"), dataSales.username)
        val email = RequestBody.create(MediaType.get("text/plain"), dataSales.email)
        val pengalamanKerja = RequestBody.create(MediaType.get("text/plain"), dataSales.pengalaman_kerja)
        val password = RequestBody.create(MediaType.get("text/plain"), dataSales.password)
        val namaLengkap = RequestBody.create(MediaType.get("text/plain"), dataSales.nama_lengkap)
        val namaPanggilan = RequestBody.create(MediaType.get("text/plain"), dataSales.nama_panggilan)
        val jenisKelamin = RequestBody.create(MediaType.get("text/plain"), dataSales.jenis_kelamin)
        val statusPernikahan = RequestBody.create(MediaType.get("text/plain"), dataSales.status_pernikahan)
        val jumlahAnak = RequestBody.create(MediaType.get("text/plain"), dataSales.jumlah_anak)
        val tempatLahir = RequestBody.create(MediaType.get("text/plain"), dataSales.tempat_lahir)
        val tglLahir = RequestBody.create(MediaType.get("text/plain"), dataSales.tgl_lahir)
        val alamat = RequestBody.create(MediaType.get("text/plain"), dataSales.alamat)
        val latitude = RequestBody.create(MediaType.get("text/plain"), dataSales.latitude)
        val longitude = RequestBody.create(MediaType.get("text/plain"), dataSales.longitude)
        val provinsi = RequestBody.create(MediaType.get("text/plain"), dataSales.provinsi)
        val kabupaten = RequestBody.create(MediaType.get("text/plain"), dataSales.kabupaten)
        val kecamatan = RequestBody.create(MediaType.get("text/plain"), dataSales.kecamatan)
        val kelurahan = RequestBody.create(MediaType.get("text/plain"), dataSales.kelurahan)
        val regional = RequestBody.create(MediaType.get("text/plain"), dataSales.regional)
        val branch = RequestBody.create(MediaType.get("text/plain"), dataSales.branch)
        val cluster = RequestBody.create(MediaType.get("text/plain"), dataSales.cluster)
        val nomorHP = RequestBody.create(MediaType.get("text/plain"), dataSales.no_hp_sales)
        val nomorWA = RequestBody.create(MediaType.get("text/plain"), dataSales.no_wa_sales)
        val verifiedPhone = RequestBody.create(MediaType.get("text/plain"), dataSales.verified_phone)
        val sim = RequestBody.create(MediaType.get("text/plain"), dataSales.kepemilikan_sim)
        val nomorPolisi = RequestBody.create(MediaType.get("text/plain"), dataSales.nomor_polisi)
        val kategoriMotor = RequestBody.create(MediaType.get("text/plain"), dataSales.kategori_motor)

        val call = api.createSales(username, email, pengalamanKerja, password, namaLengkap, namaPanggilan,
            jenisKelamin, statusPernikahan, jumlahAnak, tempatLahir, tglLahir, alamat,
            latitude, longitude, provinsi, kabupaten, kecamatan, kelurahan,
            regional, branch, cluster, nomorHP, nomorWA, verifiedPhone,
            sim, nomorPolisi, kategoriMotor, fotoProfil, fotoDepan, fotoBelakang, fotoWajah, fotoBadan
        )
        call.enqueue(callback)
    }

    fun updateSales(dataSales: ModelSales, callback: Callback<ModelResponse>){
        val call = api.updateSales(dataSales.id,
            dataSales.email, dataSales.status,
            dataSales.pengalaman_kerja,
            dataSales.nama_lengkap,
            dataSales.nama_panggilan,
            dataSales.jenis_kelamin,
            dataSales.status_pernikahan,
            dataSales.jumlah_anak,
            dataSales.tempat_lahir,
            dataSales.tgl_lahir,
            dataSales.alamat,
            dataSales.latitude,
            dataSales.longitude,
            dataSales.provinsi, dataSales.kabupaten, dataSales.kecamatan, dataSales.kelurahan,
            dataSales.regional, dataSales.branch, dataSales.cluster,
            dataSales.no_hp_sales,
            dataSales.no_wa_sales,
            dataSales.kepemilikan_sim,
            dataSales.nomor_polisi,
            dataSales.kategori_motor
        )
        call.enqueue(callback)
    }

    fun updateAdmin(dataSales: ModelSales, callback: Callback<ModelResponse>){
//        val call = api.updateAdmin(dataSales.id, dataSales.nama_sales,
//            dataSales.alamat_sales,
//            dataSales.status_sales, dataSales.latitude, dataSales.longitude,
//            dataSales.provinsi, dataSales.kabupaten, dataSales.kecamatan, dataSales.kelurahan,
//            dataSales.regional, dataSales.branch, dataSales.cluster, dataSales.no_hp_sales,
//            dataSales.no_wa_sales, dataSales.email_sales,
//            dataSales.nama_lengkap, dataSales.tgl_lahir,
//            dataSales.no_hp_pemilik, dataSales.no_wa_pemilik
//        )
//        call.enqueue(callback)
    }

    fun validateNewSales(dataSales: ModelSales, callback: Callback<ModelResponse>){
        val call = api.validateNewSales(dataSales.no_hp_sales, dataSales.username)
        call.enqueue(callback)
    }

    fun logoutSales(username: String, callback: Callback<ModelResponse>){
        val call = api.logoutSales(username)
        call.enqueue(callback)
    }

    fun updatePasswordSales(id: Int,
                       passwordNew: String,
                       callback: Callback<ModelResponse>){
        val call = api.updatePasswordSales(id, passwordNew)
        call.enqueue(callback)
    }

    fun getDataSales(username: String, callback: Callback<ModelResponseSales>){
        val call = api.getDataSales(username)
        call.enqueue(callback)
    }

    fun getDataSalesById(sales_id: String, callback: Callback<ModelResponseSales>){
        val call = api.getDataSalesById(sales_id)
        call.enqueue(callback)
    }

    fun forgetPasswordSalesUsername(username: String, callback: Callback<ModelResponseSales>){
        val call = api.forgetPasswordSalesUsername(username)
        call.enqueue(callback)
    }

    fun forgetPasswordSalesPhone(phone: String, callback: Callback<ModelResponseSales>){
        val call = api.forgetPasswordSalesPhone(phone)
        call.enqueue(callback)
    }

    fun getDaftarSalesByAdmin(cluster: String, userRequest: String, startPage: Int, status: String, search: String?, callback: Callback<ModelResponseDaftarSales>){
        val call = api.getDaftarSalesByAdmin(cluster, userRequest, startPage, status, search)
        call.enqueue(callback)
    }

    fun getDaftarStoreBySales(cluster: String, level: String, startPage: Int, status: String, search: String?, callback: Callback<ModelResponseDaftarStore>){
        val call = api.getDaftarStoreBySales(cluster, level, startPage, status, search)
        call.enqueue(callback)
    }

    fun updateStatusSales(id: Int, status_sales: String, comment: String, callback: Callback<ModelResponse>){
        val call = api.updateStatusSales(id, status_sales, comment)
        call.enqueue(callback)
    }

    fun updateStatusStore(id: Int, status: String, comment: String, callback: Callback<ModelResponse>){
        val call = api.updateStatusStore(id, status, comment)
        call.enqueue(callback)
    }

    fun createStore(dataStore: ModelStore,
                     callback: Callback<ModelResponse>){
        val fileFotoDepanAtas = File(dataStore.foto_depan_atas)
        val fotoDepanAtas = MultipartBody.Part.createFormData("foto_depan_atas", fileFotoDepanAtas.name, RequestBody.create(
            MediaType.get("image/*"), fileFotoDepanAtas))

        val fileFotoDepanBawah = File(dataStore.foto_depan_bawah)
        val fotoDepanBawah = MultipartBody.Part.createFormData("foto_depan_bawah", fileFotoDepanBawah.name, RequestBody.create(
            MediaType.get("image/*"), fileFotoDepanBawah))

        val fileFotoEtalasePerdana = File(dataStore.foto_etalase_perdana)
        val fotoEtalasePerdana = MultipartBody.Part.createFormData("foto_etalase_perdana", fileFotoEtalasePerdana.name, RequestBody.create(
            MediaType.get("image/*"), fileFotoEtalasePerdana))

        val fileFotoMejaPembayaran = File(dataStore.foto_meja_pembayaran)
        val fotoMejaPembayaran = MultipartBody.Part.createFormData("foto_meja_pembayaran", fileFotoMejaPembayaran.name, RequestBody.create(
            MediaType.get("image/*"), fileFotoMejaPembayaran))

        val fileFotoBelakangKasir = File(dataStore.foto_belakang_kasir)
        val fotoBelakangKasir = MultipartBody.Part.createFormData("foto_belakang_kasir", fileFotoBelakangKasir.name, RequestBody.create(
            MediaType.get("image/*"), fileFotoBelakangKasir))

        val jenisStore = RequestBody.create(MediaType.get("text/plain"), dataStore.jenis_store)
        val kodeToko = RequestBody.create(MediaType.get("text/plain"), dataStore.kode_toko)
        val alamat = RequestBody.create(MediaType.get("text/plain"), dataStore.alamat)
        val latitude = RequestBody.create(MediaType.get("text/plain"), dataStore.latitude)
        val longitude = RequestBody.create(MediaType.get("text/plain"), dataStore.longitude)
        val provinsi = RequestBody.create(MediaType.get("text/plain"), dataStore.provinsi)
        val kabupaten = RequestBody.create(MediaType.get("text/plain"), dataStore.kabupaten)
        val kecamatan = RequestBody.create(MediaType.get("text/plain"), dataStore.kecamatan)
        val kelurahan = RequestBody.create(MediaType.get("text/plain"), dataStore.kelurahan)
        val regional = RequestBody.create(MediaType.get("text/plain"), dataStore.regional)
        val branch = RequestBody.create(MediaType.get("text/plain"), dataStore.branch)
        val cluster = RequestBody.create(MediaType.get("text/plain"), dataStore.cluster)
        val namaPic = RequestBody.create(MediaType.get("text/plain"), dataStore.nama_pic)
        val noHpPic = RequestBody.create(MediaType.get("text/plain"), dataStore.no_hp_pic)
        val noWAPic = RequestBody.create(MediaType.get("text/plain"), dataStore.no_wa_pic)
        val namaKasir1 = RequestBody.create(MediaType.get("text/plain"), dataStore.nama_kasir_1)
        val noHpKasir1 = RequestBody.create(MediaType.get("text/plain"), dataStore.no_hp_kasir_1)
        val noWAKasir1 = RequestBody.create(MediaType.get("text/plain"), dataStore.no_wa_kasir_1)
        val namaKasir2 = RequestBody.create(MediaType.get("text/plain"), dataStore.nama_kasir_2)
        val noHpKasir2 = RequestBody.create(MediaType.get("text/plain"), dataStore.no_hp_kasir_2)
        val noWAKasir2 = RequestBody.create(MediaType.get("text/plain"), dataStore.no_wa_kasir_2)
        val namaKasir3= RequestBody.create(MediaType.get("text/plain"), dataStore.nama_kasir_3)
        val noHpKasir3= RequestBody.create(MediaType.get("text/plain"), dataStore.no_hp_kasir_3)
        val noWAKasir3= RequestBody.create(MediaType.get("text/plain"), dataStore.no_wa_kasir_3)
        val namaKasir4= RequestBody.create(MediaType.get("text/plain"), dataStore.nama_kasir_4)
        val noHpKasir4= RequestBody.create(MediaType.get("text/plain"), dataStore.no_hp_kasir_4)
        val noWAKasir4= RequestBody.create(MediaType.get("text/plain"), dataStore.no_wa_kasir_4)

        val call = api.createStore(jenisStore, kodeToko, alamat, latitude, longitude,
            provinsi, kabupaten, kecamatan, kelurahan, regional, branch, cluster,
            namaPic, noHpPic, noWAPic,
            namaKasir1, noHpKasir1, noWAKasir1,
            namaKasir2, noHpKasir2, noWAKasir2,
            namaKasir3, noHpKasir3, noWAKasir3,
            namaKasir4, noHpKasir4, noWAKasir4,
            fotoDepanAtas, fotoDepanBawah, fotoEtalasePerdana, fotoMejaPembayaran, fotoBelakangKasir
        )
        call.enqueue(callback)
    }

    fun editStore(status: String, store_id: String, sales_id: String, kategori_id: String,
                     sub_kategori_id: String, tgl_kadaluarsa: String,
                     stok: String, nama: String, harga: String,
                     promo: String, poin: String,
                     deskripsi: String, regional: String, branch: String,
                     cluster: String,
                     callback: Callback<ModelResponse>){
        val call = api.editStore(status, store_id, sales_id, kategori_id, sub_kategori_id, tgl_kadaluarsa,
            stok, nama, harga, promo, poin, deskripsi, regional, branch, cluster)
        call.enqueue(callback)
    }

    fun createFotoStore(store_id: RequestBody, level: RequestBody,
                     url_foto: MultipartBody.Part,
                     callback: Callback<ModelResponse>){
        val call = api.createFotoStore(store_id, level, url_foto)
        call.enqueue(callback)
    }

    fun updateFotoStore(id: RequestBody, level: RequestBody,
                         url_foto: MultipartBody.Part,
                         callback: Callback<ModelResponse>){
        val call = api.updateFotoStore(id, level, url_foto)
        call.enqueue(callback)
    }

//    fun getDaftarFotoStore(store_id: Int, callback: Callback<ModelResponseDaftarFotoStore>){
//        val call = api.getDaftarFotoStore(store_id)
//        call.enqueue(callback)
//    }
}