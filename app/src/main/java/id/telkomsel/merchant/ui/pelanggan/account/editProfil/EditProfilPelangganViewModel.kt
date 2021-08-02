package id.telkomsel.merchant.ui.pelanggan.account.editProfil

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.widget.AppCompatSpinner
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.google.android.material.textfield.TextInputLayout
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseViewModel
import id.telkomsel.merchant.model.ModelPelanggan
import id.telkomsel.merchant.model.ModelWilayah
import id.telkomsel.merchant.model.response.ModelResponse
import id.telkomsel.merchant.model.response.ModelResponsePelanggan
import id.telkomsel.merchant.model.response.ModelResponseWilayah
import id.telkomsel.merchant.ui.pelanggan.auth.verifyRegisterPelanggan.VerifyRegisterPelangganFragment
import id.telkomsel.merchant.utils.Constant
import id.telkomsel.merchant.utils.DataSave
import id.telkomsel.merchant.utils.RetrofitUtils
import id.telkomsel.merchant.utils.adapter.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("StaticFieldLeak")
class EditProfilPelangganViewModel(
    private val activity: Activity?,
    private val navController: NavController,
    private val spinnerProvinsi: AppCompatSpinner,
    private val spinnerKabupaten: AppCompatSpinner,
    private val spinnerKecamatan: AppCompatSpinner,
    private val spinnerKelurahan: AppCompatSpinner,
    private val editNama: TextInputLayout,
    private val editOutlet: TextInputLayout,
    private val editAlamat: TextInputLayout,
    private val editNoHp: TextInputLayout,
    private val editNoWa: TextInputLayout,
    private val editTglLahir: TextInputLayout,
    private val savedData: DataSave
) : BaseViewModel() {
    val etOutlet = MutableLiveData<String>()
    val etNama = MutableLiveData<String>()
    val etAlamat = MutableLiveData<String>()
    val etTglLahir = MutableLiveData<String>()
    val etPhone = MutableLiveData<String>()
    val etWA = MutableLiveData<String>()
    val etFotoProfil = MutableLiveData<Uri>()
    var dataPelanggan: ModelPelanggan? = null
    val listProvinsi = ArrayList<ModelWilayah>()
    val listKabupaten = ArrayList<ModelWilayah>()
    val listKecamatan = ArrayList<ModelWilayah>()
    val listKelurahan = ArrayList<ModelWilayah>()
    lateinit var adapterProvinsi : SpinnerWilayahAdapter
    lateinit var adapterKabupaten : SpinnerWilayahAdapter
    lateinit var adapterKecamatan : SpinnerWilayahAdapter
    lateinit var adapterKelurahan : SpinnerWilayahAdapter

    fun setDataPelanggan(){
        etNama.value = dataPelanggan?.nama
        etOutlet.value = dataPelanggan?.id_outlet
        etAlamat.value = dataPelanggan?.alamat
        etTglLahir.value = dataPelanggan?.tgl_lahir

        etPhone.value = dataPelanggan?.nomor_hp?.replaceFirst("+62", "0")
        etWA.value = dataPelanggan?.nomor_wa?.replaceFirst("+62", "0")

        etFotoProfil.value = Uri.parse(dataPelanggan?.foto)
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

    fun getDateTglLahir() {
        activity?.let { dismissKeyboard(it) }
        val datePickerDialog: DatePickerDialog
        val localCalendar = Calendar.getInstance()

        try {
            datePickerDialog = DatePickerDialog(
                activity ?: throw Exception("Error, mohon mulai ulang aplikasi"),
                { _, paramAnonymousInt1, paramAnonymousInt2, paramAnonymousInt3 ->
                    val dateSelected = Calendar.getInstance()
                    dateSelected[paramAnonymousInt1, paramAnonymousInt2] = paramAnonymousInt3
                    val dateFormatter = SimpleDateFormat(Constant.dateFormat1, Locale.US)
                    etTglLahir.value = dateFormatter.format(dateSelected.time)
                },
                localCalendar[Calendar.YEAR],
                localCalendar[Calendar.MONTH],
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

                    if (result?.message == Constant.reffSuccess) {
                        val data = result.data
                        listProvinsi.clear()
                        listProvinsi.add(ModelWilayah(0, Constant.pilihProvinsi))

                        for (i in data.indices) {
                            listProvinsi.add(data[i])
                        }
                        adapterProvinsi.notifyDataSetChanged()

                        val provinsi = dataPelanggan?.provinsi
                        if (dataPelanggan != null) {
                            for (i in listProvinsi.indices) {
                                if (listProvinsi[i].nama == provinsi) {
                                    spinnerProvinsi.setSelection(i)
                                }
                            }
                        }
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

                        val kabupaten = dataPelanggan?.kabupaten
                        if (dataPelanggan != null) {
                            for (i in listKabupaten.indices) {
                                if (listKabupaten[i].nama == kabupaten) {
                                    spinnerKabupaten.setSelection(i)
                                }
                            }
                        }
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

                        val kecamatan = dataPelanggan?.kecamatan
                        if (dataPelanggan != null) {
                            for (i in listKecamatan.indices) {
                                if (listKecamatan[i].nama == kecamatan) {
                                    spinnerKecamatan.setSelection(i)
                                }
                            }
                        }
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

                        val kelurahan = dataPelanggan?.kelurahan
                        if (dataPelanggan != null) {
                            for (i in listKelurahan.indices) {
                                if (listKelurahan[i].nama == kelurahan) {
                                    spinnerKelurahan.setSelection(i)
                                }
                            }
                        }
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
        editNama.error = null
        editOutlet.error = null
        editAlamat.error = null
        editNoHp.error = null
        editNoWa.error = null
        editTglLahir.error = null
    }

    private fun setTextError(msg: String, editText: TextInputLayout){
        message.value = msg
        editText.error = msg
        editText.requestFocus()
        editText.findFocus()
    }

    fun onClickEditProfil(){
        setNullError()
        activity?.let { dismissKeyboard(it) }

        val dataEdit = savedData.getDataPelanggan()
        val nama = etNama.value
        val idOutlet = etOutlet.value
        val alamat = etAlamat.value
        val provinsi = listProvinsi[spinnerProvinsi.selectedItemPosition].nama
        val kabupaten = listKabupaten[spinnerKabupaten.selectedItemPosition].nama
        val kecamatan = listKecamatan[spinnerKecamatan.selectedItemPosition].nama
        val kelurahan = listKelurahan[spinnerKelurahan.selectedItemPosition].nama
        val regional = listKelurahan[spinnerKelurahan.selectedItemPosition].REGION_OMS
        val branch = listKelurahan[spinnerKelurahan.selectedItemPosition].BRANCH
        val cluster = listKelurahan[spinnerKelurahan.selectedItemPosition].CLUSTER
        val noHp = etPhone.value
        val noWa = etWA.value
        val tglLahir = etTglLahir.value

        if (dataEdit != null && !idOutlet.isNullOrEmpty() && !nama.isNullOrEmpty() && !alamat.isNullOrEmpty()
            && (!provinsi.isNullOrEmpty() && provinsi != Constant.pilihProvinsi)
            && (!kabupaten.isNullOrEmpty() && kabupaten != Constant.pilihKabupaten)
            && (!kecamatan.isNullOrEmpty() && kecamatan != Constant.pilihKecamatan)
            && (!kelurahan.isNullOrEmpty() && kelurahan != Constant.pilihKelurahan)
            && !cluster.isNullOrEmpty() && !regional.isNullOrEmpty() && !branch.isNullOrEmpty()
            && !noHp.isNullOrEmpty() && noHp.take(1) == "0"
            && !noWa.isNullOrEmpty() && noWa.take(1) == "0"
        ) {
            val hp = noHp.replaceFirst("0", "+62")
            val wa = noWa.replaceFirst("0", "+62")

            isShowLoading.value = true

            dataEdit.nama = nama
            dataEdit.id_outlet = idOutlet
            dataEdit.tgl_lahir = tglLahir?:""
            dataEdit.alamat = alamat
            dataEdit.provinsi = provinsi
            dataEdit.kabupaten = kabupaten
            dataEdit.kecamatan = kecamatan
            dataEdit.kelurahan = kelurahan
            dataEdit.regional = regional
            dataEdit.branch = branch
            dataEdit.cluster = cluster
            dataEdit.nomor_hp = hp
            dataEdit.nomor_wa = wa

            updateProfilPelanggan(dataEdit)
        }
        else{
            when {
                dataEdit == null -> {
                    message.value = "Error, terjadi kesalahan database"
                }
                nama.isNullOrEmpty() -> {
                    setTextError("Error, mohon masukkan nama", editNama)
                }
                idOutlet.isNullOrEmpty() -> {
                    setTextError("Error, mohon masukkan id outlet", editOutlet)
                }
                alamat.isNullOrEmpty() -> {
                    setTextError("Error, mohon masukkan alamat", editAlamat)
                }
                provinsi == Constant.pilihProvinsi -> {
                    message.value = "Mohon memilih salah satu Provinsi yang tersedia"
                }
                kabupaten == Constant.pilihKabupaten -> {
                    message.value = "Mohon memilih salah satu Kabupaten yang tersedia"
                }
                kecamatan == Constant.pilihKecamatan -> {
                    message.value = "Mohon memilih salah satu Kecamatan yang tersedia"
                }
                kelurahan == Constant.pilihKelurahan -> {
                    message.value = "Mohon memilih salah satu Kelurahan yang tersedia"
                }
                tglLahir.isNullOrEmpty() -> {
                    message.value = "Error, Mohon pilih tanggal lahir"
                    editNama.clearFocus()
                    editAlamat.clearFocus()
                    editTglLahir.requestFocus()
                    editTglLahir.findFocus()
                    editTglLahir.error = "Error, Mohon pilih tanggal lahir"
                }
                noHp.isNullOrEmpty() -> {
                    setTextError("Error, mohon masukkan nomor HP yang valid", editNoHp)
                }
                noHp.take(1) != "0" -> {
                    setTextError("Error, mohon masukkan nomor HP dengan awalan 0", editNoHp)
                }
                noHp.length !in 10..13 -> {
                    setTextError("Error, nomor HP harus 10-13 digit", editNoHp)
                }
                noWa.isNullOrEmpty() -> {
                    setTextError("Error, mohon masukkan nomor WA yang valid", editNoWa)
                }
                noWa.take(1) != "0" -> {
                    setTextError("Error, mohon masukkan nomor WA dengan awalan 0", editNoWa)
                }
                noWa.length !in 10..13 -> {
                    setTextError("Error, nomor WA harus 10-13 digit", editNoWa)
                }
                noHp.isNullOrEmpty() -> {
                    setTextError("Error, Mohon masukkan nomor HP yang valid", editNoHp)
                }
                noHp.take(1) != "0" -> {
                    setTextError(
                        "Error, mohon masukkan nomor HP dengan awalan 0",
                        editNoHp
                    )
                }
                noHp.length !in 10..13 -> {
                    setTextError("Error, nomor HP harus 10-13 digit", editNoHp)
                }
                noWa.isNullOrEmpty() -> {
                    setTextError("Error, Mohon masukkan nomor WA yang valid", editNoWa)
                }
                noWa.take(1) != "0" -> {
                    setTextError(
                        "Error, mohon masukkan nomor WA dengan awalan 0",
                        editNoWa
                    )
                }
                noWa.length !in 10..13 -> {
                    setTextError("Error, nomor WA harus 10-13 digit", editNoWa)
                }
            }
        }
    }

    private fun updateProfilPelanggan(resultPelanggan: ModelPelanggan){
        RetrofitUtils.updateProfilPelanggan(resultPelanggan,
            object : Callback<ModelResponsePelanggan> {
                override fun onResponse(
                    call: Call<ModelResponsePelanggan>,
                    response: Response<ModelResponsePelanggan>
                ) {
                    isShowLoading.value = false
                    val result = response.body()

                    if (result?.message == Constant.reffSuccess) {
                        message.value = "Berhasil mengedit profil"
                        savedData.setDataObject(result.data, Constant.reffPelanggan)
                        navController.popBackStack()
                    } else {
                        when (result?.message) {
                            "Error, Nomor HP sudah terdaftar!" -> {
                                setTextError(result.message, editNoHp)
                            }
                            else -> {
                                message.value = result?.message
                            }
                        }
                    }
                }

                override fun onFailure(
                    call: Call<ModelResponsePelanggan>,
                    t: Throwable
                ) {
                    isShowLoading.value = false
                    message.value = t.message
                }
            })
    }

    fun updateFotoPelanggan(username: RequestBody,
                             url_foto: MultipartBody.Part){
        isShowLoading.value = true

        RetrofitUtils.updateFotoPelanggan(username, url_foto,
            object : Callback<ModelResponsePelanggan> {
                override fun onResponse(
                    call: Call<ModelResponsePelanggan>,
                    response: Response<ModelResponsePelanggan>
                ) {
                    isShowLoading.value = false
                    val result = response.body()

                    if (result?.message == Constant.reffSuccess){
                        savedData.setDataObject(result.data, Constant.reffPelanggan)
                        message.value = "Berhasil mengupload foto profil"
                    }
                    else{
                        message.value = result?.message
                    }
                }

                override fun onFailure(
                    call: Call<ModelResponsePelanggan>,
                    t: Throwable
                ) {
                    isShowLoading.value = false
                    message.value = t.message
                }
            })
    }
}