package id.telkomsel.merchandise.ui.sales.addStore

import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import android.widget.Toast
import androidx.appcompat.widget.AppCompatSpinner
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.google.android.material.textfield.TextInputLayout
import id.telkomsel.merchandise.base.BaseViewModel
import id.telkomsel.merchandise.model.ModelStore
import id.telkomsel.merchandise.model.ModelWilayah
import id.telkomsel.merchandise.model.response.ModelResponse
import id.telkomsel.merchandise.model.response.ModelResponseWilayah
import id.telkomsel.merchandise.utils.Constant
import id.telkomsel.merchandise.utils.RetrofitUtils
import id.telkomsel.merchandise.utils.adapter.SpinnerStringAdapter
import id.telkomsel.merchandise.utils.adapter.SpinnerWilayahAdapter
import id.telkomsel.merchandise.utils.adapter.dismissKeyboard
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

@SuppressLint("StaticFieldLeak")
class AddStoreViewModel(
    private val activity: Activity?,
    private val navController: NavController,
    private val spinnerJenis: AppCompatSpinner,
    private val spinnerProvinsi: AppCompatSpinner,
    private val spinnerKabupaten: AppCompatSpinner,
    private val spinnerKecamatan: AppCompatSpinner,
    private val spinnerKelurahan: AppCompatSpinner,
    private val editKodeToko: TextInputLayout,
    private val editAlamat: TextInputLayout,
    private val editTitikLokasi: TextInputLayout,
    private val editNamaPic: TextInputLayout,
    private val editPhonePic: TextInputLayout,
    private val editWaPic: TextInputLayout,
    private val editNamaKasir1: TextInputLayout,
    private val editPhoneKasir1: TextInputLayout,
    private val editWaKasir1: TextInputLayout,
    private val editNamaKasir2: TextInputLayout,
    private val editPhoneKasir2: TextInputLayout,
    private val editWaKasir2: TextInputLayout,
    private val editNamaKasir3: TextInputLayout,
    private val editPhoneKasir3: TextInputLayout,
    private val editWaKasir3: TextInputLayout,
    private val editNamaKasir4: TextInputLayout,
    private val editPhoneKasir4: TextInputLayout,
    private val editWaKasir4: TextInputLayout,
) : BaseViewModel() {
    val etKodeToko = MutableLiveData<String>()
    val etAlamat = MutableLiveData<String>()
    val etLatLng = MutableLiveData<String>()
    val latitude = MutableLiveData<String>()
    val longitude = MutableLiveData<String>()
    val etNamaPic = MutableLiveData<String>()
    val etPhonePic = MutableLiveData<String>()
    val etWAPic = MutableLiveData<String>()
    val etNamaKasir1 = MutableLiveData<String>()
    val etPhoneKasir1 = MutableLiveData<String>()
    val etWAKasir1 = MutableLiveData<String>()
    val etNamaKasir2 = MutableLiveData<String>()
    val etPhoneKasir2 = MutableLiveData<String>()
    val etWAKasir2 = MutableLiveData<String>()
    val etNamaKasir3 = MutableLiveData<String>()
    val etPhoneKasir3 = MutableLiveData<String>()
    val etWAKasir3 = MutableLiveData<String>()
    val etNamaKasir4 = MutableLiveData<String>()
    val etPhoneKasir4 = MutableLiveData<String>()
    val etWAKasir4 = MutableLiveData<String>()
    val etFotoDepanAtas = MutableLiveData<Uri>()
    val etFotoDepanBawah = MutableLiveData<Uri>()
    val etFotoEtalasePerdana = MutableLiveData<Uri>()
    val etFotoMejaPembayaran = MutableLiveData<Uri>()
    val etFotoBelakangKasir = MutableLiveData<Uri>()
    private val listJenisStore = ArrayList<String>()
    val listProvinsi = ArrayList<ModelWilayah>()
    val listKabupaten = ArrayList<ModelWilayah>()
    val listKecamatan = ArrayList<ModelWilayah>()
    val listKelurahan = ArrayList<ModelWilayah>()
    private lateinit var adapterJenisStore : SpinnerStringAdapter
    lateinit var adapterProvinsi : SpinnerWilayahAdapter
    lateinit var adapterKabupaten : SpinnerWilayahAdapter
    lateinit var adapterKecamatan : SpinnerWilayahAdapter
    lateinit var adapterKelurahan : SpinnerWilayahAdapter

    fun setAdapterJenis() {
        listJenisStore.clear()
        listJenisStore.add("Jenis Store")
        listJenisStore.add("Alfamart")
        listJenisStore.add("Alfamidi")
        listJenisStore.add("Indomaret")

        adapterJenisStore = SpinnerStringAdapter(
            activity,
            listJenisStore, true
        )
        spinnerJenis.adapter = adapterJenisStore
    }

    fun setAdapterProvinsi() {
        listProvinsi.clear()

        adapterProvinsi = SpinnerWilayahAdapter(
            activity,
            listProvinsi, true
        )
        spinnerProvinsi.adapter = adapterProvinsi
    }

    fun setAdapterKabupaten() {
        listKabupaten.clear()

        adapterKabupaten = SpinnerWilayahAdapter(
            activity,
            listKabupaten, true
        )
        spinnerKabupaten.adapter = adapterKabupaten
    }

    fun setAdapterKecamatan() {
        listKecamatan.clear()

        adapterKecamatan = SpinnerWilayahAdapter(
            activity,
            listKecamatan, true
        )
        spinnerKecamatan.adapter = adapterKecamatan
    }

    fun setAdapterKelurahan() {
        listKelurahan.clear()

        adapterKelurahan = SpinnerWilayahAdapter(
            activity,
            listKelurahan, true
        )
        spinnerKelurahan.adapter = adapterKelurahan
    }

    fun getDaftarProvinsi(){
        isShowLoading.value = true

        RetrofitUtils.getDaftarProvinsi(
            object : Callback<ModelResponseWilayah> {
                override fun onResponse(
                    call: Call<ModelResponseWilayah>,
                    response: Response<ModelResponseWilayah>
                ) {
                    isShowLoading.value = false
                    val result = response.body()

                    if (result?.message == Constant.reffSuccess) {
                        val data = result.data
                        listProvinsi.clear()
                        listProvinsi.add(ModelWilayah(0, Constant.pilihProvinsi))

                        for (i in data.indices) {
                            listProvinsi.add(data[i])
                        }
                        adapterProvinsi.notifyDataSetChanged()
                    } else {
                        listProvinsi.clear()
                        listProvinsi.add(ModelWilayah(0, Constant.noDataWilayah))
                        adapterProvinsi.notifyDataSetChanged()
                    }
                }

                override fun onFailure(
                    call: Call<ModelResponseWilayah>,
                    t: Throwable
                ) {
                    isShowLoading.value = false
                    listProvinsi.clear()
                    listProvinsi.add(ModelWilayah(0, Constant.noDataWilayah))
                    adapterProvinsi.notifyDataSetChanged()
                }
            })
    }

    fun getDaftarKabupaten(idProvinsi: Long){
        isShowLoading.value = true

        RetrofitUtils.getDaftarKabupaten(idProvinsi,
            object : Callback<ModelResponseWilayah> {
                override fun onResponse(
                    call: Call<ModelResponseWilayah>,
                    response: Response<ModelResponseWilayah>
                ) {
                    isShowLoading.value = false
                    val result = response.body()

                    if (result?.message == Constant.reffSuccess) {
                        val data = result.data
                        listKabupaten.clear()
                        listKabupaten.add(ModelWilayah(0, Constant.pilihKabupaten))

                        for (i in data.indices) {
                            listKabupaten.add(data[i])
                        }
                        adapterKabupaten.notifyDataSetChanged()
                    } else {
                        listKabupaten.clear()
                        listKabupaten.add(ModelWilayah(0, Constant.noDataWilayah))
                        adapterKabupaten.notifyDataSetChanged()
                    }
                }

                override fun onFailure(
                    call: Call<ModelResponseWilayah>,
                    t: Throwable
                ) {
                    isShowLoading.value = false
                    listKabupaten.clear()
                    listKabupaten.add(ModelWilayah(0, Constant.noDataWilayah))
                    adapterKabupaten.notifyDataSetChanged()
                }
            })
    }

    fun getDaftarKecamatan(idKabupaten: Long){
        isShowLoading.value = true

        RetrofitUtils.getDaftarKecamatan(idKabupaten,
            object : Callback<ModelResponseWilayah> {
                override fun onResponse(
                    call: Call<ModelResponseWilayah>,
                    response: Response<ModelResponseWilayah>
                ) {
                    isShowLoading.value = false
                    val result = response.body()

                    if (result?.message == Constant.reffSuccess) {
                        val data = result.data
                        listKecamatan.clear()
                        listKecamatan.add(ModelWilayah(0, Constant.pilihKecamatan))

                        for (i in data.indices) {
                            listKecamatan.add(data[i])
                        }
                        adapterKecamatan.notifyDataSetChanged()
                    } else {
                        listKecamatan.clear()
                        listKecamatan.add(ModelWilayah(0, Constant.noDataWilayah))
                        adapterKecamatan.notifyDataSetChanged()
                    }
                }

                override fun onFailure(
                    call: Call<ModelResponseWilayah>,
                    t: Throwable
                ) {
                    isShowLoading.value = false
                    listKecamatan.clear()
                    listKecamatan.add(ModelWilayah(0, Constant.noDataWilayah))
                    adapterKecamatan.notifyDataSetChanged()
                }
            })
    }

    fun getDaftarKelurahan(idKecamatan: Long){
        isShowLoading.value = true

        RetrofitUtils.getDaftarKelurahan(idKecamatan,
            object : Callback<ModelResponseWilayah> {
                override fun onResponse(
                    call: Call<ModelResponseWilayah>,
                    response: Response<ModelResponseWilayah>
                ) {
                    isShowLoading.value = false
                    val result = response.body()

                    if (result?.message == Constant.reffSuccess) {
                        val data = result.data
                        listKelurahan.clear()
                        listKelurahan.add(ModelWilayah(0, Constant.pilihKelurahan))

                        for (i in data.indices) {
                            listKelurahan.add(data[i])
                        }
                        adapterKelurahan.notifyDataSetChanged()
                    } else {
                        listKelurahan.clear()
                        listKelurahan.add(ModelWilayah(0, Constant.noDataWilayah))
                        adapterKelurahan.notifyDataSetChanged()
                    }
                }

                override fun onFailure(
                    call: Call<ModelResponseWilayah>,
                    t: Throwable
                ) {
                    isShowLoading.value = false
                    listKelurahan.clear()
                    listKelurahan.add(ModelWilayah(0, Constant.noDataWilayah))
                    adapterKelurahan.notifyDataSetChanged()
                }
            })
    }

    private fun setNullError(){
        editKodeToko.error = null
        editAlamat.error = null
        editNamaPic.error = null
        editPhonePic.error = null
        editWaPic.error = null
        editNamaKasir1.error = null
        editPhoneKasir1.error = null
        editWaKasir1.error = null
        editNamaKasir2.error = null
        editPhoneKasir2.error = null
        editWaKasir2.error = null
        editNamaKasir3.error = null
        editPhoneKasir3.error = null
        editWaKasir3.error = null
        editNamaKasir4.error = null
        editPhoneKasir4.error = null
        editWaKasir4.error = null
    }

    private fun setTextError(msg: String, editText: TextInputLayout){
        message.value = msg
        editText.error = msg
        editText.requestFocus()
        editText.findFocus()
    }

    fun onClickAddStore(){
        setNullError()
        activity?.let { dismissKeyboard(it) }

        val jenisStore = listJenisStore[spinnerJenis.selectedItemPosition]
        val kodeToko = etKodeToko.value
        val alamat = etAlamat.value
        val lat = latitude.value
        val lng = longitude.value
        val provinsi = listProvinsi[spinnerProvinsi.selectedItemPosition].nama
        val kabupaten = listKabupaten[spinnerKabupaten.selectedItemPosition].nama
        val kecamatan = listKecamatan[spinnerKecamatan.selectedItemPosition].nama
        val kelurahan = listKelurahan[spinnerKelurahan.selectedItemPosition].nama
        val regional = listKelurahan[spinnerKelurahan.selectedItemPosition].REGION_OMS
        val branch = listKelurahan[spinnerKelurahan.selectedItemPosition].BRANCH
        val cluster = listKelurahan[spinnerKelurahan.selectedItemPosition].CLUSTER
        val namaPic = etNamaPic.value
        val phonePic = etPhonePic.value
        val waPic = etWAPic.value
        val namaKasir1 = etNamaKasir1.value
        val phoneKasir1 = etPhoneKasir1.value
        val waKasir1 = etWAKasir1.value
        val namaKasir2 = etNamaKasir2.value
        val phoneKasir2 = etPhoneKasir2.value
        val waKasir2 = etWAKasir2.value
        val namaKasir3 = etNamaKasir3.value
        val phoneKasir3 = etPhoneKasir3.value
        val waKasir3 = etWAKasir3.value
        val namaKasir4 = etNamaKasir4.value
        val phoneKasir4 = etPhoneKasir4.value
        val waKasir4 = etWAKasir4.value
        val fotoDepanAtas = etFotoDepanAtas.value
        val fotoDepanBawah = etFotoDepanBawah.value
        val fotoEtalasePerdana = etFotoEtalasePerdana.value
        val fotoMejaPembayaran = etFotoMejaPembayaran.value
        val fotoBelakangKasir = etFotoBelakangKasir.value

        if ((jenisStore.isNotEmpty() && jenisStore != "Jenis Store")
            && !kodeToko.isNullOrEmpty() && !alamat.isNullOrEmpty()
            && !lat.isNullOrEmpty() && !lng.isNullOrEmpty()
            && (!provinsi.isNullOrEmpty() && provinsi != Constant.pilihProvinsi)
            && (!kabupaten.isNullOrEmpty() && kabupaten != Constant.pilihKabupaten)
            && (!kecamatan.isNullOrEmpty() && kecamatan != Constant.pilihKecamatan)
            && (!kelurahan.isNullOrEmpty() && kelurahan != Constant.pilihKelurahan)
            && !cluster.isNullOrEmpty() && !regional.isNullOrEmpty() && !branch.isNullOrEmpty()
            && !namaPic.isNullOrEmpty()
            && !phonePic.isNullOrEmpty() && phonePic.take(1) == "0"
            && !waPic.isNullOrEmpty() && waPic.take(1) == "0"
            && !namaKasir1.isNullOrEmpty()
            && !phoneKasir1.isNullOrEmpty() && phoneKasir1.take(1) == "0"
            && !waKasir1.isNullOrEmpty() && waKasir1.take(1) == "0"
            && !namaKasir2.isNullOrEmpty()
            && !phoneKasir2.isNullOrEmpty() && phoneKasir2.take(1) == "0"
            && !waKasir2.isNullOrEmpty() && waKasir2.take(1) == "0"
            && !namaKasir3.isNullOrEmpty()
            && !phoneKasir3.isNullOrEmpty() && phoneKasir3.take(1) == "0"
            && !waKasir3.isNullOrEmpty() && waKasir3.take(1) == "0"
            && !namaKasir4.isNullOrEmpty()
            && !phoneKasir4.isNullOrEmpty() && phoneKasir4.take(1) == "0"
            && !waKasir4.isNullOrEmpty() && waKasir4.take(1) == "0"
            && fotoDepanAtas != null && fotoDepanBawah != null && fotoEtalasePerdana != null
            && fotoMejaPembayaran != null && fotoBelakangKasir != null
        ) {
            if (isShowLoading.value == false){
                isShowLoading.value = true
                val noHpPic = phonePic.replaceFirst("0", "+62")
                val noWaPic = waPic.replaceFirst("0", "+62")
                val noHpKasir1 = phoneKasir1.replaceFirst("0", "+62")
                val noWaKasir1 = waKasir1.replaceFirst("0", "+62")
                val noHpKasir2 = phoneKasir2.replaceFirst("0", "+62")
                val noWaKasir2 = waKasir2.replaceFirst("0", "+62")
                val noHpKasir3 = phoneKasir3.replaceFirst("0", "+62")
                val noWaKasir3 = waKasir3.replaceFirst("0", "+62")
                val noHpKasir4 = phoneKasir4.replaceFirst("0", "+62")
                val noWaKasir4 = waKasir4.replaceFirst("0", "+62")

                createStore(
                    ModelStore(0, jenisStore, kodeToko, Constant.statusRequest, "",
                        alamat, lat, lng, provinsi, kabupaten, kecamatan, kelurahan, regional, branch, cluster,
                        namaPic, noHpPic, noWaPic,
                        namaKasir1, noHpKasir1, noWaKasir1,
                        namaKasir2, noHpKasir2, noWaKasir2,
                        namaKasir3, noHpKasir3, noWaKasir3,
                        namaKasir4, noHpKasir4, noWaKasir4,
                        fotoDepanAtas.toString(), fotoDepanBawah.toString(),
                        fotoEtalasePerdana.toString(), fotoMejaPembayaran.toString(),
                        fotoBelakangKasir.toString()
                    )
                )
            }
        }
        else{
            if (fotoDepanAtas == null){
                message.value = "Mohon upload foto depan atas"
            }
            else if (fotoDepanBawah == null){
                message.value = "Mohon upload foto depan bawah"
            }
            else if (fotoEtalasePerdana == null){
                message.value = "Mohon upload foto etalase perdana"
            }
            else if (fotoMejaPembayaran == null){
                message.value = "Mohon upload foto meja pembayaran"
            }
            else if (fotoBelakangKasir == null){
                message.value = "Mohon upload foto belakang kasir"
            }
            else if (jenisStore.isEmpty() || jenisStore == "Jenis Store"){
                message.value = "Mohon memilih salah satu Jenis Store yang tersedia"
            }
            else if (kodeToko.isNullOrEmpty()){
                setTextError("Error, mohon masukkan nama kode toko", editKodeToko)
            }
            else if (alamat.isNullOrEmpty()){
                setTextError("Error, mohon masukkan alamat", editAlamat)
            }
            else if (lat.isNullOrEmpty() || lng.isNullOrEmpty()){
                message.value = "Error, mohon pilih titik lokasi di maps"
                editAlamat.clearFocus()
                editTitikLokasi.requestFocus()
                editTitikLokasi.findFocus()
                editTitikLokasi.error = "Error, mohon pilih titik lokasi di maps"
            }
            else if (provinsi == Constant.pilihProvinsi){
                message.value = "Mohon memilih salah satu Provinsi yang tersedia"
            }
            else if (kabupaten == Constant.pilihKabupaten){
                message.value = "Mohon memilih salah satu Kabupaten yang tersedia"
            }
            else if (kecamatan == Constant.pilihKecamatan){
                message.value = "Mohon memilih salah satu Kecamatan yang tersedia"
            }
            else if (kelurahan == Constant.pilihKelurahan){
                message.value = "Mohon memilih salah satu Kelurahan yang tersedia"
            }
            else if (cluster.isNullOrEmpty()){
                message.value = "Error, mohon memilih wilayah yang memiliki cluster"
            }
            else if (regional.isNullOrEmpty()){
                message.value = "Error, mohon memilih wilayah yang memiliki cluster"
            }
            else if (branch.isNullOrEmpty()){
                message.value = "Error, mohon memilih wilayah yang memiliki cluster"
            }
            else if (namaPic.isNullOrEmpty()){
                setTextError("Error, mohon masukkan nama PIC", editNamaPic)
            }
            else if (phonePic.isNullOrEmpty()){
                setTextError("Error, mohon masukkan nomor HP PIC yang valid", editPhonePic)
            }
            else if (phonePic.take(1) != "0"){
                setTextError("Error, mohon masukkan nomor HP PIC dengan awalan 0", editPhonePic)
            }
            else if (phonePic.length !in 10..13){
                setTextError("Error, nomor HP PIC harus 10-13 digit", editPhonePic)
            }
            else if (waPic.isNullOrEmpty()){
                setTextError("Error, mohon masukkan nomor WA PIC yang valid", editWaPic)
            }
            else if (waPic.take(1) != "0"){
                setTextError("Error, mohon masukkan nomor WA PIC dengan awalan 0", editWaPic)
            }
            else if (waPic.length !in 10..13){
                setTextError("Error, nomor WA PIC harus 10-13 digit", editWaPic)
            }
            else if (namaKasir1.isNullOrEmpty()){
                setTextError("Error, mohon masukkan nama kasir 1", editNamaKasir1)
            }
            else if (phoneKasir1.isNullOrEmpty()){
                setTextError("Error, mohon masukkan nomor HP Kasir 1 yang valid", editPhoneKasir1)
            }
            else if (phoneKasir1.take(1) != "0"){
                setTextError("Error, mohon masukkan nomor HP Kasir 1 dengan awalan 0", editPhoneKasir1)
            }
            else if (phoneKasir1.length !in 10..13){
                setTextError("Error, nomor HP Kasir 1 harus 10-13 digit", editPhoneKasir1)
            }
            else if (waKasir1.isNullOrEmpty()){
                setTextError("Error, mohon masukkan nomor WA Kasir 1 yang valid", editWaKasir1)
            }
            else if (waKasir1.take(1) != "0"){
                setTextError("Error, mohon masukkan nomor WA Kasir 1 dengan awalan 0", editWaKasir1)
            }
            else if (waKasir1.length !in 10..13){
                setTextError("Error, nomor WA Kasir 1 harus 10-13 digit", editWaKasir1)
            }
            else if (namaKasir2.isNullOrEmpty()){
                setTextError("Error, mohon masukkan nama Kasir 2", editNamaKasir2)
            }
            else if (phoneKasir2.isNullOrEmpty()){
                setTextError("Error, mohon masukkan nomor HP Kasir 2 yang valid", editPhoneKasir2)
            }
            else if (phoneKasir2.take(1) != "0"){
                setTextError("Error, mohon masukkan nomor HP Kasir 2 dengan awalan 0", editPhoneKasir2)
            }
            else if (phoneKasir2.length !in 10..13){
                setTextError("Error, nomor HP Kasir 2 harus 10-13 digit", editPhoneKasir2)
            }
            else if (waKasir2.isNullOrEmpty()){
                setTextError("Error, mohon masukkan nomor WA Kasir 2 yang valid", editWaKasir2)
            }
            else if (waKasir2.take(1) != "0"){
                setTextError("Error, mohon masukkan nomor WA Kasir 2 dengan awalan 0", editWaKasir2)
            }
            else if (waKasir2.length !in 10..13){
                setTextError("Error, nomor WA Kasir 2 harus 10-13 digit", editWaKasir2)
            }
            else if (namaKasir3.isNullOrEmpty()){
                setTextError("Error, mohon masukkan nama Kasir 3", editNamaKasir3)
            }
            else if (phoneKasir3.isNullOrEmpty()){
                setTextError("Error, mohon masukkan nomor HP Kasir 3 yang valid", editPhoneKasir3)
            }
            else if (phoneKasir3.take(1) != "0"){
                setTextError("Error, mohon masukkan nomor HP Kasir 3 dengan awalan 0", editPhoneKasir3)
            }
            else if (phoneKasir3.length !in 10..13){
                setTextError("Error, nomor HP Kasir 3 harus 10-13 digit", editPhoneKasir3)
            }
            else if (waKasir3.isNullOrEmpty()){
                setTextError("Error, mohon masukkan nomor WA Kasir 3 yang valid", editWaKasir3)
            }
            else if (waKasir3.take(1) != "0"){
                setTextError("Error, mohon masukkan nomor WA Kasir 3 dengan awalan 0", editWaKasir3)
            }
            else if (waKasir3.length !in 10..13){
                setTextError("Error, nomor WA Kasir 3 harus 10-13 digit", editWaKasir3)
            }
            else if (namaKasir4.isNullOrEmpty()){
                setTextError("Error, mohon masukkan nama Kasir 4", editNamaKasir4)
            }
            else if (phoneKasir4.isNullOrEmpty()){
                setTextError("Error, mohon masukkan nomor HP Kasir 4 yang valid", editPhoneKasir4)
            }
            else if (phoneKasir4.take(1) != "0"){
                setTextError("Error, mohon masukkan nomor HP Kasir 4 dengan awalan 0", editPhoneKasir4)
            }
            else if (phoneKasir4.length !in 10..13){
                setTextError("Error, nomor HP Kasir 4 harus 10-13 digit", editPhoneKasir4)
            }
            else if (waKasir4.isNullOrEmpty()){
                setTextError("Error, mohon masukkan nomor WA Kasir 4 yang valid", editWaKasir4)
            }
            else if (waKasir4.take(1) != "0"){
                setTextError("Error, mohon masukkan nomor WA Kasir 4 dengan awalan 0", editWaKasir4)
            }
            else if (waKasir4.length !in 10..13){
                setTextError("Error, nomor WA Kasir 4 harus 10-13 digit", editWaKasir4)
            }
        }
    }

    private fun createStore(dataStore: ModelStore){
        isShowLoading.value = true

        RetrofitUtils.createStore(dataStore,
            object : Callback<ModelResponse> {
                override fun onResponse(
                    call: Call<ModelResponse>,
                    response: Response<ModelResponse>
                ) {
                    isShowLoading.value = false
                    val result = response.body()

                    if (result?.message == Constant.reffSuccess){
                        Toast.makeText(activity, "Berhasil menambah store, mohon tunggu proses verifikasi dalam waktu 1x24 jam", Toast.LENGTH_LONG).show()
                        message.value = "Berhasil menambah store, mohon tunggu proses verifikasi dalam waktu 1x24 jam"
                        navController.popBackStack()
                    }
                    else{
                        message.value = result?.message
                    }
                }

                override fun onFailure(
                    call: Call<ModelResponse>,
                    t: Throwable
                ) {
                    isShowLoading.value = false
                    message.value = t.message
                }
            })
    }
}