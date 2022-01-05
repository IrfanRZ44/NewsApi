package id.telkomsel.merchandise.ui.sales.accountSales.editProfilAdmin

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.net.Uri
import androidx.appcompat.widget.AppCompatSpinner
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.google.android.material.textfield.TextInputLayout
import id.telkomsel.merchandise.base.BaseViewModel
import id.telkomsel.merchandise.model.ModelSales
import id.telkomsel.merchandise.model.ModelWilayah
import id.telkomsel.merchandise.model.response.ModelResponse
import id.telkomsel.merchandise.model.response.ModelResponseWilayah
import id.telkomsel.merchandise.utils.Constant
import id.telkomsel.merchandise.utils.DataSave
import id.telkomsel.merchandise.utils.RetrofitUtils
import id.telkomsel.merchandise.utils.adapter.SpinnerWilayahAdapter
import id.telkomsel.merchandise.utils.adapter.dismissKeyboard
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@SuppressLint("StaticFieldLeak")
class EditProfilAdminViewModel(
    private val activity: Activity?,
    private val navController: NavController,
    private val spinnerProvinsi: AppCompatSpinner,
    private val spinnerKabupaten: AppCompatSpinner,
    private val spinnerKecamatan: AppCompatSpinner,
    private val spinnerKelurahan: AppCompatSpinner,
    private val editNamaSales: TextInputLayout,
    private val editAlamatSales: TextInputLayout,
    private val editTitikLokasi: TextInputLayout,
    private val editCluster: TextInputLayout,
    private val editNoHpSales: TextInputLayout,
    private val editNoWaSales: TextInputLayout,
    private val editEmail: TextInputLayout,
    private val editNamaLengkap: TextInputLayout,
    private val editTglLahir: TextInputLayout,
    private val editNoHpPemilik: TextInputLayout,
    private val editNoWaPemilik: TextInputLayout,
    private val savedData: DataSave
) : BaseViewModel() {
    var dataSales: ModelSales? = null
    val etNamaSales = MutableLiveData<String>()
    val etAlamatSales = MutableLiveData<String>()
    val etLatLng = MutableLiveData<String>()
    val latitude = MutableLiveData<String>()
    val longitude = MutableLiveData<String>()
    val etNamaLengkap = MutableLiveData<String>()
    val etTglLahir = MutableLiveData<String>()
    val etFotoWajah = MutableLiveData<Uri>()
    val etPhonePemilik = MutableLiveData<String>()
    val etWAPemilik = MutableLiveData<String>()
    val etHPSales = MutableLiveData<String>()
    val etWASales = MutableLiveData<String>()
    val etEmailSales = MutableLiveData<String>()
    val listProvinsi = ArrayList<ModelWilayah>()
    val listKabupaten = ArrayList<ModelWilayah>()
    val listKecamatan = ArrayList<ModelWilayah>()
    val listKelurahan = ArrayList<ModelWilayah>()
    val etCluster = MutableLiveData<String>()
    lateinit var adapterProvinsi : SpinnerWilayahAdapter
    lateinit var adapterKabupaten : SpinnerWilayahAdapter
    lateinit var adapterKecamatan : SpinnerWilayahAdapter
    lateinit var adapterKelurahan : SpinnerWilayahAdapter

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

    fun setDataSales(){
//        etEmailSales.value = dataSales?.email
//        etNamaSales.value = dataSales?.nama_sales
//        etAlamatSales.value = dataSales?.alamat_sales
//        etLatLng.value = dataSales?.latitude + " : " + dataSales?.longitude
//        latitude.value = dataSales?.latitude
//        longitude.value = dataSales?.longitude
//        etCluster.value = dataSales?.cluster
//        etTglLahir.value = dataSales?.tgl_lahir
//        etNamaLengkap.value = dataSales?.nama_lengkap
//
//        etHPSales.value = dataSales?.no_hp_sales?.replaceFirst("+62", "0")
//        etWASales.value = dataSales?.no_wa_sales?.replaceFirst("+62", "0")
//        etPhonePemilik.value = dataSales?.no_hp_pemilik?.replaceFirst("+62", "0")
//        etWAPemilik.value = dataSales?.no_wa_pemilik?.replaceFirst("+62", "0")
//
//        etFotoWajah.value = Uri.parse(dataSales?.foto_profil)
    }

    fun getDateTglLahir() {
        activity?.let { dismissKeyboard(it) }
        val datePickerDialog: DatePickerDialog
        val localCalendar = Calendar.getInstance()

        try {
            datePickerDialog = DatePickerDialog(activity ?: throw Exception("Error, mohon mulai ulang aplikasi"),
                { _, paramAnonymousInt1, paramAnonymousInt2, paramAnonymousInt3 ->
                    val dateSelected = Calendar.getInstance()
                    dateSelected[paramAnonymousInt1, paramAnonymousInt2] = paramAnonymousInt3
                    val dateFormatter = SimpleDateFormat(Constant.dateFormat1, Locale.US)
                    etTglLahir.value = dateFormatter.format(dateSelected.time)
                },
                localCalendar[Calendar.YEAR]
                ,
                localCalendar[Calendar.MONTH]
                ,
                localCalendar[Calendar.DATE]
            )

            datePickerDialog.show()
        } catch (e: java.lang.Exception) {
            message.value = e.message
        }
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

                    if (result?.message == Constant.reffSuccess){
                        val data = result.data
                        listProvinsi.clear()
                        listProvinsi.add(ModelWilayah(0,  Constant.pilihProvinsi))

                        for (i in data.indices){
                            listProvinsi.add(data[i])
                        }
                        adapterProvinsi.notifyDataSetChanged()

                        val provinsi = dataSales?.provinsi
                        if (dataSales != null){
                            for (i in listProvinsi.indices){
                                if (listProvinsi[i].nama == provinsi){
                                    spinnerProvinsi.setSelection(i)
                                }
                            }
                        }
                    }
                    else{
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

                    if (result?.message == Constant.reffSuccess){
                        val data = result.data
                        listKabupaten.clear()
                        listKabupaten.add(ModelWilayah(0,  Constant.pilihKabupaten))

                        for (i in data.indices){
                            listKabupaten.add(data[i])
                        }
                        adapterKabupaten.notifyDataSetChanged()

                        val kabupaten = dataSales?.kabupaten
                        if (dataSales != null){
                            for (i in listKabupaten.indices){
                                if (listKabupaten[i].nama == kabupaten){
                                    spinnerKabupaten.setSelection(i)
                                }
                            }
                        }
                    }
                    else{
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

                    if (result?.message == Constant.reffSuccess){
                        val data = result.data
                        listKecamatan.clear()
                        listKecamatan.add(ModelWilayah(0,  Constant.pilihKecamatan))

                        for (i in data.indices){
                            listKecamatan.add(data[i])
                        }
                        adapterKecamatan.notifyDataSetChanged()

                        val kecamatan = dataSales?.kecamatan
                        if (dataSales != null){
                            for (i in listKecamatan.indices){
                                if (listKecamatan[i].nama == kecamatan){
                                    spinnerKecamatan.setSelection(i)
                                }
                            }
                        }
                    }
                    else{
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

                    if (result?.message == Constant.reffSuccess){
                        val data = result.data
                        listKelurahan.clear()
                        listKelurahan.add(ModelWilayah(0,  Constant.pilihKelurahan))

                        for (i in data.indices){
                            listKelurahan.add(data[i])
                        }
                        adapterKelurahan.notifyDataSetChanged()

                        val kelurahan = dataSales?.kelurahan
                        if (dataSales != null){
                            for (i in listKelurahan.indices){
                                if (listKelurahan[i].nama == kelurahan){
                                    spinnerKelurahan.setSelection(i)
                                }
                            }
                        }
                    }
                    else{
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

    private fun setTextError(msg: String, editText: TextInputLayout){
        message.value = msg
        editText.error = msg
        editText.requestFocus()
        editText.findFocus()
    }

    private fun setNullError(){
        editNoHpPemilik.error = null
        editNoWaPemilik.error = null
        editNoWaSales.error = null
        editEmail.error = null
        editNamaSales.error = null
        editAlamatSales.error = null
        editTitikLokasi.error = null
        editCluster.error = null
        editNamaLengkap.error = null
        editTglLahir.error = null
    }

    fun onClickRegisterSales(){
        setNullError()
        activity?.let { dismissKeyboard(it) }

        val namaSales = etNamaSales.value
        val alamatSales = etAlamatSales.value
        val lat = latitude.value
        val lng = longitude.value
        val provinsi = listProvinsi[spinnerProvinsi.selectedItemPosition].nama
        val kabupaten = listKabupaten[spinnerKabupaten.selectedItemPosition].nama
        val kecamatan = listKecamatan[spinnerKecamatan.selectedItemPosition].nama
        val kelurahan = listKelurahan[spinnerKelurahan.selectedItemPosition].nama
        val cluster = etCluster.value
        val regional = listKelurahan[spinnerKelurahan.selectedItemPosition].REGION_OMS
        val branch = listKelurahan[spinnerKelurahan.selectedItemPosition].BRANCH
        val noHpSales = etHPSales.value
        val noWaSales = etWASales.value
        val emailSales = etEmailSales.value
        val namaLengkap = etNamaLengkap.value
        val tglLahir = etTglLahir.value
        val noHpPemilik = etPhonePemilik.value
        val noWaPemilik = etWAPemilik.value
        val fotoWajah = etFotoWajah.value?.path
        val dataSales = savedData.getDataSales()

        if (dataSales != null && !namaSales.isNullOrEmpty() && !alamatSales.isNullOrEmpty() && !lat.isNullOrEmpty() && !lng.isNullOrEmpty()
            && (!provinsi.isNullOrEmpty() && provinsi != Constant.pilihProvinsi)
            && (!kabupaten.isNullOrEmpty() && kabupaten != Constant.pilihKabupaten)
            && (!kecamatan.isNullOrEmpty() && kecamatan != Constant.pilihKecamatan)
            && (!kelurahan.isNullOrEmpty() && kelurahan != Constant.pilihKelurahan)
            && !cluster.isNullOrEmpty() && !regional.isNullOrEmpty() && !branch.isNullOrEmpty()
            && !noWaSales.isNullOrEmpty() && noWaSales.take(1) == "0"
            && !noHpSales.isNullOrEmpty() && noHpSales.take(1) == "0"
            && !emailSales.isNullOrEmpty() && emailSales.matches(Regex(Constant.emailFormat))
            && !namaLengkap.isNullOrEmpty() && !tglLahir.isNullOrEmpty()
            && !noHpPemilik.isNullOrEmpty() && noHpPemilik.take(1) == "0"
            && !noWaPemilik.isNullOrEmpty() && noWaPemilik.take(1) == "0"
            && !fotoWajah.isNullOrEmpty()
            && (noHpSales.length in 10..13) && (noWaSales.length in 10..13)
            && (noHpPemilik.length in 10..13) && (noWaPemilik.length in 10..13)
            && cluster == savedData.getDataSales()?.cluster
        ) {
//            val hpPemilik = noHpPemilik.replaceFirst("0", "+62")
//            val waPemilik = noWaPemilik.replaceFirst("0", "+62")
//            val hpSales = noHpSales.replaceFirst("0", "+62")
//            val waSales = noWaSales.replaceFirst("0", "+62")
//            isShowLoading.value = true
//
//            dataSales.nama_sales = namaSales
//            dataSales.alamat_sales = alamatSales
//            dataSales.latitude = lat
//            dataSales.longitude = lng
//            dataSales.provinsi = provinsi
//            dataSales.kabupaten = kabupaten
//            dataSales.kecamatan = kecamatan
//            dataSales.kelurahan = kelurahan
//            dataSales.regional = regional
//            dataSales.branch = branch
//            dataSales.cluster = cluster
//            dataSales.no_hp_sales = hpSales
//            dataSales.no_wa_sales = waSales
//            dataSales.email_sales = emailSales
//            dataSales.nama_lengkap = namaLengkap
//            dataSales.tgl_lahir = tglLahir
//            dataSales.no_hp_pemilik = hpPemilik
//            dataSales.no_wa_pemilik = waPemilik
//            dataSales.status_sales = Constant.statusActive
//
//            updateAdmin(dataSales)
        }
        else {
            if (dataSales == null){
                message.value = "Error, terjadi kesalahan saat mengambil Database"
            }
            else if (fotoWajah.isNullOrEmpty()){
                message.value = "Mohon upload foto profil"
            }
            else if (namaSales.isNullOrEmpty()){
                setTextError("Error, mohon masukkan nama merchandise", editNamaSales)
            }
            else if (alamatSales.isNullOrEmpty()){
                setTextError("Error, mohon masukkan alamat merchandise", editAlamatSales)
            }
            else if (lat.isNullOrEmpty() || lng.isNullOrEmpty()){
                message.value = "Error, mohon pilih titik lokasi di maps"
                editAlamatSales.clearFocus()
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
            else if (cluster != savedData.getDataSales()?.cluster){
                message.value = "Error, maaf alamat Admin tidak sesuai dengan wilayah cluster"
            }
            else if (cluster.isNullOrEmpty()){
                setTextError("Error, mohon memilih wilayah yang memiliki cluster", editCluster)
            }
            else if (cluster.isNullOrEmpty()){
                setTextError("Error, mohon memilih wilayah yang memiliki cluster", editCluster)
            }
            else if (regional.isNullOrEmpty()){
                setTextError("Error, wilayah yang dipilih tidak memiliki regional", editCluster)
            }
            else if (branch.isNullOrEmpty()){
                setTextError("Error, wilayah yang dipilih tidak memiliki branch", editCluster)
            }
            else if (noHpSales.isNullOrEmpty()){
                setTextError("Error, mohon masukkan nomor HP Sales yang valid", editNoHpSales)
            }
            else if (noHpSales.take(1) != "0"){
                setTextError("Error, mohon masukkan nomor HP Sales dengan awalan 0", editNoHpSales)
            }
            else if (noHpSales.length !in 10..13){
                setTextError("Error, nomor HP Sales harus 10-13 digit", editNoHpSales)
            }
            else if (noWaSales.isNullOrEmpty()){
                setTextError("Error, mohon masukkan nomor WA Sales yang valid", editNoWaSales)
            }
            else if (noWaSales.take(1) != "0"){
                setTextError("Error, mohon masukkan nomor WA Sales dengan awalan 0", editNoWaSales)
            }
            else if (noWaSales.length !in 10..13){
                setTextError("Error, nomor WA Sales harus 10-13 digit", editNoWaSales)
            }
            else if (emailSales.isNullOrEmpty()){
                setTextError("Error, mohon masukkan email", editEmail)
            }
            else if(!emailSales.matches(Regex(Constant.emailFormat))){
                setTextError("Error, format email salah", editEmail)
            }
            else if (namaLengkap.isNullOrEmpty()){
                setTextError("Error, Mohon masukkan nama lengkap", editNamaLengkap)
            }
            else if (tglLahir.isNullOrEmpty()){
                message.value = "Error, Mohon pilih tanggal lahir"
                editNamaLengkap.clearFocus()
                editTglLahir.requestFocus()
                editTglLahir.findFocus()
                editTglLahir.error = "Error, Mohon pilih tanggal lahir"
            }
            else if (noHpPemilik.isNullOrEmpty()){
                setTextError("Error, Mohon masukkan nomor HP Pemilik yang valid", editNoHpPemilik)
            }
            else if (noHpPemilik.take(1) != "0"){
                setTextError("Error, mohon masukkan nomor HP Pemilik dengan awalan 0", editNoHpPemilik)
            }
            else if (noHpPemilik.length !in 10..13){
                setTextError("Error, nomor HP Pemilik harus 10-13 digit", editNoHpPemilik)
            }
            else if (noWaPemilik.isNullOrEmpty()){
                setTextError("Error, Mohon masukkan nomor WA Pemilik yang valid", editNoWaPemilik)
            }
            else if (noWaPemilik.take(1) != "0"){
                setTextError("Error, mohon masukkan nomor WA Pemilik dengan awalan 0", editNoWaPemilik)
            }
            else if (noWaPemilik.length !in 10..13){
                setTextError("Error, nomor WA Pemilik harus 10-13 digit", editNoWaPemilik)
            }
        }
    }

    private fun updateAdmin(sales: ModelSales){
        RetrofitUtils.updateAdmin(sales,
            object : Callback<ModelResponse> {
                override fun onResponse(
                    call: Call<ModelResponse>,
                    response: Response<ModelResponse>
                ) {
                    isShowLoading.value = false

                    val result = response.body()

                    if (result?.message == Constant.reffSuccess){
                        message.value = "Berhasil edit profil"
                        savedData.setDataObject(sales, Constant.reffSales)
                        navController.popBackStack()
                    }
                    else{
                        if (result?.message == "Nomor HP Sales sudah digunakan"){
                            setTextError("Error, nomor HP Admin sudah digunakan", editNoHpSales)
                        }
                        else{
                            message.value = result?.message
                        }
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