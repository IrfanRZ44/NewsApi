package id.telkomsel.merchandise.ui.auth.updateRegisterSales

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.net.Uri
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatSpinner
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.google.android.material.textfield.TextInputLayout
import id.telkomsel.merchandise.R
import id.telkomsel.merchandise.base.BaseViewModel
import id.telkomsel.merchandise.model.ModelSales
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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@SuppressLint("StaticFieldLeak")
class UpdateRegisterSalesViewModel(
    private val activity: Activity?,
    private val navController: NavController,
    private val spinnerPernikahan: AppCompatSpinner,
    private val spinnerJenisKelamin: AppCompatSpinner,
    private val spinnerSIM: AppCompatSpinner,
    private val spinnerProvinsi: AppCompatSpinner,
    private val spinnerKabupaten: AppCompatSpinner,
    private val spinnerKecamatan: AppCompatSpinner,
    private val spinnerKelurahan: AppCompatSpinner,
    private val spinnerMotor: AppCompatSpinner,
    private val editNamaLengkap: TextInputLayout,
    private val editNamaPanggilan: TextInputLayout,
    private val editJumlahAnak: TextInputLayout,
    private val editTempatLahir: TextInputLayout,
    private val editTglLahir: TextInputLayout,
    private val editAlamat: TextInputLayout,
    private val editTitikLokasi: TextInputLayout,
    private val editNoHpSales: TextInputLayout,
    private val editNoWaSales: TextInputLayout,
    private val editEmail: TextInputLayout,
    private val editPengalamanKerja: TextInputLayout,
    private val editNomorPolisi: TextInputLayout,
) : BaseViewModel() {
    val etNamaLengkap = MutableLiveData<String>()
    val etNamaPanggilan = MutableLiveData<String>()
    val etJumlahAnak = MutableLiveData<String>()
    val etTempatLahir = MutableLiveData<String>()
    val etTglLahir = MutableLiveData<String>()
    val etAlamat = MutableLiveData<String>()
    val etLatLng = MutableLiveData<String>()
    val latitude = MutableLiveData<String>()
    val longitude = MutableLiveData<String>()
    val etPhoneSales = MutableLiveData<String>()
    val etWASales = MutableLiveData<String>()
    val etEmail = MutableLiveData<String>()
    val etNomorPolisi = MutableLiveData<String>()
    val etPengalamanKerja = MutableLiveData<String>()
    val etFotoProfil = MutableLiveData<Uri>()
    val etFotoDepanKendaraan = MutableLiveData<Uri>()
    val etFotoBelakangKendaraan = MutableLiveData<Uri>()
    val etFotoWajah = MutableLiveData<Uri>()
    val etFotoSeluruhBadan = MutableLiveData<Uri>()
    private val listJenisKelamin = ArrayList<String>()
    private val listPernikahan = ArrayList<String>()
    private val listSIM = ArrayList<String>()
    val listProvinsi = ArrayList<ModelWilayah>()
    val listKabupaten = ArrayList<ModelWilayah>()
    val listKecamatan = ArrayList<ModelWilayah>()
    val listKelurahan = ArrayList<ModelWilayah>()
    private val listMotor = ArrayList<String>()
    private lateinit var adapterPernikahan : SpinnerStringAdapter
    private lateinit var adapterSIM : SpinnerStringAdapter
    private lateinit var adapterJenisKelamin : SpinnerStringAdapter
    lateinit var adapterProvinsi : SpinnerWilayahAdapter
    lateinit var adapterKabupaten : SpinnerWilayahAdapter
    lateinit var adapterKecamatan : SpinnerWilayahAdapter
    lateinit var adapterKelurahan : SpinnerWilayahAdapter
    private lateinit var adapterMotor : SpinnerStringAdapter
    var dataSales: ModelSales? = null

    fun setAdapterPernikahan() {
        listPernikahan.clear()
        listPernikahan.add("Status Pernikahan")
        listPernikahan.add("Belum")
        listPernikahan.add("Sudah")
        listPernikahan.add("Pernah")

        adapterPernikahan = SpinnerStringAdapter(
            activity,
            listPernikahan, true
        )
        spinnerPernikahan.adapter = adapterPernikahan

        val statusPernikahan = dataSales?.status_pernikahan
        if (dataSales != null && !statusPernikahan.isNullOrEmpty()) {
            for (i in listPernikahan.indices) {
                if (listPernikahan[i] == statusPernikahan) {
                    spinnerPernikahan.setSelection(i)
                }
            }
        }
    }

    fun setAdapterJenisKelamin() {
        listJenisKelamin.clear()
        listJenisKelamin.add("Jenis Kelamin")
        listJenisKelamin.add("Laki-Laki")
        listJenisKelamin.add("Perempuan")

        adapterJenisKelamin = SpinnerStringAdapter(
            activity,
            listJenisKelamin, true
        )
        spinnerJenisKelamin.adapter = adapterJenisKelamin

        val jenisKelamin = dataSales?.jenis_kelamin
        if (dataSales != null && !jenisKelamin.isNullOrEmpty()) {
            for (i in listJenisKelamin.indices) {
                if (listJenisKelamin[i] == jenisKelamin) {
                    spinnerJenisKelamin.setSelection(i)
                }
            }
        }
    }

    fun setAdapterSIM() {
        listSIM.clear()
        listSIM.add("Kepemilikan SIM")
        listSIM.add("A")
        listSIM.add("C")
        listSIM.add("A dan C")

        adapterSIM = SpinnerStringAdapter(
            activity,
            listSIM, true
        )
        spinnerSIM.adapter = adapterSIM

        val kepemilikanSim = dataSales?.kepemilikan_sim
        if (dataSales != null && !kepemilikanSim.isNullOrEmpty()) {
            for (i in listSIM.indices) {
                if (listSIM[i] == kepemilikanSim) {
                    spinnerSIM.setSelection(i)
                }
            }
        }
    }

    fun setAdapterMotor() {
        listMotor.clear()
        listMotor.add("Merek Motor")
        listMotor.add("Honda")
        listMotor.add("Yamaha")
        listMotor.add("Suzuki")
        listMotor.add("Kawasaki")
        listMotor.add("Vespa")
        listMotor.add("Astra")
        listMotor.add("Kaisar")
        listMotor.add("Viar")
        listMotor.add("BMW")
        listMotor.add("Hercules")
        listMotor.add("Norton")
        listMotor.add("Scorpa")
        listMotor.add("Triumph")
        listMotor.add("Ducati")
        listMotor.add("Harley Davidson")
        listMotor.add("TVS Motor")

        adapterMotor = SpinnerStringAdapter(
            activity,
            listMotor, true
        )
        spinnerMotor.adapter = adapterMotor

        val kategoriMotor = dataSales?.kategori_motor
        if (dataSales != null && !kategoriMotor.isNullOrEmpty()) {
            for (i in listMotor.indices) {
                if (listMotor[i] == kategoriMotor) {
                    spinnerMotor.setSelection(i)
                }
            }
        }
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

    fun setDataSales(){
        etEmail.value = dataSales?.email
        etPengalamanKerja.value = dataSales?.pengalaman_kerja
        etNamaLengkap.value = dataSales?.nama_lengkap
        etNamaPanggilan.value = dataSales?.nama_panggilan
        etAlamat.value = dataSales?.alamat
        etLatLng.value = dataSales?.latitude + " : " + dataSales?.longitude
        latitude.value = dataSales?.latitude
        longitude.value = dataSales?.longitude
        etTglLahir.value = dataSales?.tgl_lahir
        etTempatLahir.value = dataSales?.tempat_lahir
        etJumlahAnak.value = dataSales?.jumlah_anak
        etNomorPolisi.value = dataSales?.nomor_polisi

        etPhoneSales.value = dataSales?.no_hp_sales?.replaceFirst("+62", "0")
        etWASales.value = dataSales?.no_wa_sales?.replaceFirst("+62", "0")

        etFotoProfil.value = Uri.parse("${Constant.reffURL}${dataSales?.foto_profil}")
        etFotoDepanKendaraan.value = Uri.parse("${Constant.reffURL}${dataSales?.foto_depan_kendaraan}")
        etFotoBelakangKendaraan.value = Uri.parse("${Constant.reffURL}${dataSales?.foto_belakang_kendaraan}")
        etFotoWajah.value = Uri.parse("${Constant.reffURL}${dataSales?.foto_wajah}")
        etFotoSeluruhBadan.value = Uri.parse("${Constant.reffURL}${dataSales?.foto_seluruh_badan}")
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

    private fun dialogSucces() {
        if (activity != null){
            val alert = AlertDialog.Builder(activity)
            alert.setTitle(Constant.pendaftaranBerhasil)
            alert.setMessage(Constant.reffSuccessRegisterAgain)
            alert.setPositiveButton(
                Constant.iya
            ) { dialog, _ ->
                dialog.dismiss()
            }

            alert.show()
        }
        else{
            message.value = "Mohon mulai ulang aplikasi"
        }
    }

    private fun setTextError(msg: String, editText: TextInputLayout){
        message.value = msg
        editText.error = msg
        editText.requestFocus()
        editText.findFocus()
    }

    private fun setNullError(){
        editPengalamanKerja.error = null
        editNamaLengkap.error = null
        editNamaPanggilan.error = null
        editJumlahAnak.error = null
        editTempatLahir.error = null
        editTglLahir.error = null
        editAlamat.error = null
        editTitikLokasi.error = null
        editNoHpSales.error = null
        editNoWaSales.error = null
        editEmail.error = null
        editNomorPolisi.error = null
    }

    fun onClickRegisterSales(){
        setNullError()
        activity?.let { dismissKeyboard(it) }

        val pengalamanKerja = etPengalamanKerja.value
        val namaLengkap = etNamaLengkap.value
        val namaPanggilan = etNamaPanggilan.value
        val jumlahAnak = etJumlahAnak.value
        val tempatLahir = etTempatLahir.value
        val tglLahir = etTglLahir.value
        val alamatSales = etAlamat.value
        val lat = latitude.value
        val lng = longitude.value
        val statusPernikahan = listPernikahan[spinnerPernikahan.selectedItemPosition]
        val jenisKelamin = listJenisKelamin[spinnerJenisKelamin.selectedItemPosition]
        val merekMotor = listMotor[spinnerMotor.selectedItemPosition]
        val sim = listSIM[spinnerSIM.selectedItemPosition]
        val provinsi = listProvinsi[spinnerProvinsi.selectedItemPosition].nama
        val kabupaten = listKabupaten[spinnerKabupaten.selectedItemPosition].nama
        val kecamatan = listKecamatan[spinnerKecamatan.selectedItemPosition].nama
        val kelurahan = listKelurahan[spinnerKelurahan.selectedItemPosition].nama
        val regional = listKelurahan[spinnerKelurahan.selectedItemPosition].REGION_OMS
        val branch = listKelurahan[spinnerKelurahan.selectedItemPosition].BRANCH
        val cluster = listKelurahan[spinnerKelurahan.selectedItemPosition].CLUSTER
        val noHpSales = etPhoneSales.value
        val noWaSales = etWASales.value
        val emailSales = etEmail.value
        val nomorPolisi = etNomorPolisi.value
        val sales = dataSales
        val id = sales?.id

        if (sales != null && id != null && !namaLengkap.isNullOrEmpty() && !namaPanggilan.isNullOrEmpty()
                && !nomorPolisi.isNullOrEmpty() && !tempatLahir.isNullOrEmpty() && !tglLahir.isNullOrEmpty()
                && !alamatSales.isNullOrEmpty()
                && (statusPernikahan.isNotEmpty() && statusPernikahan != "Status Pernikahan")
                && (jenisKelamin.isNotEmpty() && jenisKelamin != "Jenis Kelamin")
                && (sim.isNotEmpty() && sim != "Kepemilikan SIM")
                && (merekMotor.isNotEmpty() && merekMotor != "Merek Motor")
                && (!provinsi.isNullOrEmpty() && provinsi != Constant.pilihProvinsi)
                && (!kabupaten.isNullOrEmpty() && kabupaten != Constant.pilihKabupaten)
                && (!kecamatan.isNullOrEmpty() && kecamatan != Constant.pilihKecamatan)
                && (!kelurahan.isNullOrEmpty() && kelurahan != Constant.pilihKelurahan)
                && !cluster.isNullOrEmpty() && !regional.isNullOrEmpty() && !branch.isNullOrEmpty()
                && !noHpSales.isNullOrEmpty() && noHpSales.take(1) == "0"
                && !noWaSales.isNullOrEmpty() && noWaSales.take(1) == "0"
                && (!emailSales.isNullOrEmpty() && emailSales.matches(Regex(Constant.emailFormat)))
                && !pengalamanKerja.isNullOrEmpty()
                && (noHpSales.length in 10..13) && (noWaSales.length in 10..13)
                && !lat.isNullOrEmpty() && !lng.isNullOrEmpty()
        ) {
            val hpSales = noHpSales.replaceFirst("0", "+62")
            val waSales = noWaSales.replaceFirst("0", "+62")
            isShowLoading.value = true

            sales.status = Constant.statusRequest
            sales.email = emailSales
            sales.pengalaman_kerja = pengalamanKerja
            sales.nama_lengkap = namaLengkap
            sales.nama_panggilan = namaPanggilan
            sales.jenis_kelamin = jenisKelamin
            sales.status_pernikahan = statusPernikahan
            sales.jumlah_anak = jumlahAnak?:"0"
            sales.tempat_lahir = tempatLahir
            sales.tgl_lahir = tglLahir
            sales.alamat = alamatSales
            if (!lat.isNullOrEmpty()) sales.latitude = lat
            if (!lng.isNullOrEmpty()) sales.longitude = lng
            sales.provinsi = provinsi
            sales.kabupaten = kabupaten
            sales.kecamatan = kecamatan
            sales.kelurahan = kelurahan
            sales.regional = regional
            sales.branch = branch
            sales.cluster = cluster
            sales.no_hp_sales = hpSales
            sales.no_wa_sales = waSales
            sales.kepemilikan_sim = sim
            sales.nomor_polisi = nomorPolisi
            sales.kategori_motor = merekMotor

            updateSales(sales)
        }
        else {
            if (sales == null){
                message.value = "Error, terjadi kesalahan saat mengambil data Sales"
            }
            else if (id == null){
                message.value = "Error, terjadi kesalahan saat mengambil ID Sales"
            }
            else if (namaLengkap.isNullOrEmpty()){
                setTextError("Error, mohon masukkan nama merchandise", editNamaLengkap)
            }
            else if (namaPanggilan.isNullOrEmpty()){
                setTextError("Error, mohon masukkan nama panggilan", editNamaPanggilan)
            }
            else if (statusPernikahan.isEmpty() || statusPernikahan == "Status Pernikahan"){
                message.value = "Mohon memilih salah satu Status Pernikahan yang tersedia"
            }
            else if (tempatLahir.isNullOrEmpty()){
                setTextError("Error, mohon masukkan tempat lahir", editTempatLahir)
            }
            else if (tglLahir.isNullOrEmpty()){
                setTextError("Error, mohon masukkan nama panggilan", editTglLahir)
            }
            else if (alamatSales.isNullOrEmpty()){
                setTextError("Error, mohon masukkan alamat", editAlamat)
            }
            else if (jenisKelamin.isEmpty() || jenisKelamin == "Jenis Kelamin"){
                message.value = "Mohon memilih salah satu Jenis Kelamin yang tersedia"
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
            else if (noHpSales.isNullOrEmpty()){
                setTextError("Error, mohon masukkan nomor HP Sales yang valid", editNoHpSales)
            }
            else if (noHpSales.take(1) != "0"){
                setTextError("Error, mohon masukkan nomor HP Sales dengan awalan 0", editNoHpSales)
            }
            else if (noHpSales.length !in 10..13){
                setTextError("Error, nomor HP Sales harus 10-13 digit", editNoHpSales)
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
            else if (noWaSales.length !in 10..13){
                setTextError("Error, nomor WA Sales harus 10-13 digit", editNoWaSales)
            }
            else if(emailSales.isNullOrEmpty()){
                setTextError("Error, mohon masukkan email", editEmail)
            }
            else if (!emailSales.isNullOrEmpty() && !emailSales.matches(Regex(Constant.emailFormat))){
                setTextError("Error, format email salah", editEmail)
            }
            else if (pengalamanKerja.isNullOrEmpty()){
                setTextError("Error, mohon masukkan pengalaman kerja", editPengalamanKerja)
            }
            else if (sim.isEmpty() || sim == "Kepemilikan SIM"){
                message.value = "Mohon memilih salah satu Kepemilikan SIM yang tersedia"
            }
            else if (nomorPolisi.isNullOrEmpty()){
                setTextError("Error, mohon masukkan nama merchandise", editNomorPolisi)
            }
            else if (merekMotor.isEmpty() || merekMotor == "Merek Motor"){
                message.value = "Mohon memilih salah satu Merek Motor yang tersedia"
            }
            else{
                message.value = "Error, terjadi kesalahan yang tidak diketahui"
            }
        }
    }

    private fun updateSales(sales: ModelSales){
        RetrofitUtils.updateSales(sales,
            object : Callback<ModelResponse> {
                override fun onResponse(
                    call: Call<ModelResponse>,
                    response: Response<ModelResponse>
                ) {
                    isShowLoading.value = false
                    val result = response.body()

                    if (result?.message == Constant.reffSuccess){
                        navController.navigate(R.id.splashFragment)
                        dialogSucces()
                    }
                    else{
                        if (result?.message == "Nomor HP Sales sudah digunakan"){
                            setTextError("Error, nomor HP Sales sudah digunakan", editNoHpSales)
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