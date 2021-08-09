package id.telkomsel.merchant.ui.pelanggan.auth.registerPelanggan

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatSpinner
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.google.android.material.textfield.TextInputLayout
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseViewModel
import id.telkomsel.merchant.model.ModelPelanggan
import id.telkomsel.merchant.model.ModelWilayah
import id.telkomsel.merchant.model.response.ModelResponse
import id.telkomsel.merchant.model.response.ModelResponseWilayah
import id.telkomsel.merchant.ui.pelanggan.auth.verifyRegisterPelanggan.VerifyRegisterPelangganFragment
import id.telkomsel.merchant.utils.Constant
import id.telkomsel.merchant.utils.DataSave
import id.telkomsel.merchant.utils.RetrofitUtils
import id.telkomsel.merchant.utils.adapter.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("StaticFieldLeak")
class RegisterPelangganViewModel(
    private val activity: Activity?,
    private val navController: NavController,
    private val spinnerProvinsi: AppCompatSpinner,
    private val spinnerKabupaten: AppCompatSpinner,
    private val spinnerKecamatan: AppCompatSpinner,
    private val spinnerKelurahan: AppCompatSpinner,
    private val editNama: TextInputLayout,
    private val editMkios: TextInputLayout,
    private val editAlamat: TextInputLayout,
    private val editNoHp: TextInputLayout,
    private val editNoWa: TextInputLayout,
    private val editUsername: TextInputLayout,
    private val editPassword: TextInputLayout,
    private val editConfirmPassword: TextInputLayout,
    private val editTglLahir: TextInputLayout,
    private val savedData: DataSave,
    private val cekKebijakan: AppCompatCheckBox
) : BaseViewModel() {
    val etNama = MutableLiveData<String>()
    val etNomorMkios = MutableLiveData<String>()
    val etAlamat = MutableLiveData<String>()
    val etUsername = MutableLiveData<String>()
    val etPassword = MutableLiveData<String>()
    val etPasswordConfirm = MutableLiveData<String>()
    val etTglLahir = MutableLiveData<String>()
    val etPhone = MutableLiveData<String>()
    val etWA = MutableLiveData<String>()
    val etFotoProfil = MutableLiveData<Uri>()
    val listProvinsi = ArrayList<ModelWilayah>()
    val listKabupaten = ArrayList<ModelWilayah>()
    val listKecamatan = ArrayList<ModelWilayah>()
    val listKelurahan = ArrayList<ModelWilayah>()
    lateinit var adapterProvinsi : SpinnerWilayahAdapter
    lateinit var adapterKabupaten : SpinnerWilayahAdapter
    lateinit var adapterKecamatan : SpinnerWilayahAdapter
    lateinit var adapterKelurahan : SpinnerWilayahAdapter
    var dataPelanggan: ModelPelanggan? = null

    fun setDataPelanggan(fotoProfil: Uri?){
        etUsername.value = dataPelanggan?.username
        etPassword.value = dataPelanggan?.password
        etPasswordConfirm.value = dataPelanggan?.password
        etNama.value = dataPelanggan?.nama
        etNomorMkios.value = dataPelanggan?.nomor_mkios
        etAlamat.value = dataPelanggan?.alamat
        etTglLahir.value = dataPelanggan?.tgl_lahir

        etPhone.value = dataPelanggan?.nomor_hp?.replaceFirst("+62", "0")
        etWA.value = dataPelanggan?.nomor_wa?.replaceFirst("+62", "0")

        etFotoProfil.value = fotoProfil
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
        editMkios.error = null
        editAlamat.error = null
        editNoHp.error = null
        editNoWa.error = null
        editUsername.error = null
        editPassword.error = null
        editConfirmPassword.error = null
        editTglLahir.error = null
    }

    private fun setTextError(msg: String, editText: TextInputLayout){
        message.value = msg
        editText.error = msg
        editText.requestFocus()
        editText.findFocus()
    }

    fun onClickLogin(){
        activity?.let { dismissKeyboard(it) }
        navController.popBackStack()
    }

    fun onClickKebijakan(){
        val act = activity
        if (act != null){
            val alert = AlertDialog.Builder(act)
            alert.setTitle("SKB")
            alert.setMessage(savedData.getDataApps()?.skb)
            alert.setCancelable(true)
            alert.setPositiveButton(
                "Setuju"
            ) { dialog, _ ->
                dialog.dismiss()
            }

            alert.show()
        }
    }

    fun onClickRegisterPelanggan(){
        setNullError()
        activity?.let { dismissKeyboard(it) }

        val nama = etNama.value
        val nomorMkios = etNomorMkios.value
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
        val username = etUsername.value
        val password = etPassword.value
        val confirmPassword = etPasswordConfirm.value
        val tglLahir = etTglLahir.value
        val fotoProfil = etFotoProfil.value?.path

        if (!nama.isNullOrEmpty() && !nomorMkios.isNullOrEmpty() && !alamat.isNullOrEmpty()
            && (!provinsi.isNullOrEmpty() && provinsi != Constant.pilihProvinsi)
            && (!kabupaten.isNullOrEmpty() && kabupaten != Constant.pilihKabupaten)
            && (!kecamatan.isNullOrEmpty() && kecamatan != Constant.pilihKecamatan)
            && (!kelurahan.isNullOrEmpty() && kelurahan != Constant.pilihKelurahan)
            && !cluster.isNullOrEmpty() && !regional.isNullOrEmpty() && !branch.isNullOrEmpty()
            && !noHp.isNullOrEmpty() && noHp.take(1) == "0"
            && !noWa.isNullOrEmpty() && noWa.take(1) == "0"
            && !username.isNullOrEmpty()
            && !password.isNullOrEmpty() && !confirmPassword.isNullOrEmpty() && (password == confirmPassword)
            && password.length >= 6 && isContainNumber(password) && (isContainSmallText(password) || isContainBigText(password))
            && cekKebijakan.isChecked
            && (noHp.length in 10..13) && (noWa.length in 10..13) && (nomorMkios.length in 10..13)
        ) {
            val mkios = nomorMkios.replaceFirst("0", "+62")
            val hp = noHp.replaceFirst("0", "+62")
            val wa = noWa.replaceFirst("0", "+62")

            isShowLoading.value = true

            val resultMerchant = ModelPelanggan(
                0,
                Constant.statusActive,
                username,
                password,
                0,
                nama,
                mkios,
                "",
                tglLahir?:"",
                alamat,
                provinsi,
                kabupaten,
                kecamatan,
                kelurahan,
                regional,
                branch,
                cluster,
                hp,
                wa,
                mkios
            )

            validatePelanggan(resultMerchant)
        }
        else{
            if (!cekKebijakan.isChecked){
                message.value = "Maaf, Anda harus menyetujui SKB"
            }
            else if (fotoProfil.isNullOrEmpty()){
                message.value = "Mohon upload foto profil"
            }
            else if (nama.isNullOrEmpty()){
                setTextError("Error, mohon masukkan nama", editNama)
            }
            else if (nomorMkios.isNullOrEmpty()){
                setTextError("Error, mohon masukkan nomor Mkios", editMkios)
            }
            else if (nomorMkios.take(1) != "0"){
                setTextError("Error, mohon masukkan nomor MKIOS dengan awalan 0", editMkios)
            }
            else if (nomorMkios.length !in 10..13){
                setTextError("Error, nomor MKIOS harus 10-13 digit", editMkios)
            }
            else if (alamat.isNullOrEmpty()){
                setTextError("Error, mohon masukkan alamat", editAlamat)
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
            else if (tglLahir.isNullOrEmpty()){
                message.value = "Error, Mohon pilih tanggal lahir"
                editNama.clearFocus()
                editAlamat.clearFocus()
                editTglLahir.requestFocus()
                editTglLahir.findFocus()
                editTglLahir.error = "Error, Mohon pilih tanggal lahir"
            }
            else if (noHp.isNullOrEmpty()){
                setTextError("Error, mohon masukkan nomor HP yang valid", editNoHp)
            }
            else if (noHp.take(1) != "0"){
                setTextError("Error, mohon masukkan nomor HP dengan awalan 0", editNoHp)
            }
            else if (noHp.length !in 10..13){
                setTextError("Error, nomor HP harus 10-13 digit", editNoHp)
            }
            else if (noWa.isNullOrEmpty()){
                setTextError("Error, mohon masukkan nomor WA yang valid", editNoWa)
            }
            else if (noWa.take(1) != "0"){
                setTextError("Error, mohon masukkan nomor WA dengan awalan 0", editNoWa)
            }
            else if (noWa.length !in 10..13){
                setTextError("Error, nomor WA harus 10-13 digit", editNoWa)
            }
            else if (username.isNullOrEmpty()){
                setTextError("Error, mohon masukkan username", editUsername)
            }
            else if (password.isNullOrEmpty()){
                setTextError("Error, Mohon masukkan password", editPassword)
            }
            else if (password != confirmPassword){
                setTextError("Error, password yang Anda masukkan berbeda", editConfirmPassword)
            }
            else if (password.length < 6){
                setTextError("Error, password harus minimal 6 digit", editPassword)
            }
            else if (!isContainNumber(password)){
                setTextError("Error, password harus memiliki kombinasi angka", editPassword)
            }
            else if (!isContainSmallText(password) && !isContainBigText(password)){
                setTextError("Error, password harus memiliki kombinasi huruf", editPassword)
            }
            else if (noHp.isNullOrEmpty()){
                setTextError("Error, Mohon masukkan nomor HP yang valid", editNoHp)
            }
            else if (noHp.take(1) != "0"){
                setTextError(
                    "Error, mohon masukkan nomor HP dengan awalan 0",
                    editNoHp
                )
            }
            else if (noHp.length !in 10..13){
                setTextError("Error, nomor HP harus 10-13 digit", editNoHp)
            }
            else if (noWa.isNullOrEmpty()){
                setTextError("Error, Mohon masukkan nomor WA yang valid", editNoWa)
            }
            else if (noWa.take(1) != "0"){
                setTextError(
                    "Error, mohon masukkan nomor WA dengan awalan 0",
                    editNoWa
                )
            }
            else if (noWa.length !in 10..13){
                setTextError("Error, nomor WA harus 10-13 digit", editNoWa)
            }
        }
    }

    private fun validatePelanggan(resultPelanggan: ModelPelanggan){
        RetrofitUtils.validateNewPelanggan(resultPelanggan,
            object : Callback<ModelResponse> {
                override fun onResponse(
                    call: Call<ModelResponse>,
                    response: Response<ModelResponse>
                ) {
                    isShowLoading.value = false
                    val result = response.body()

                    if (result?.message == Constant.reffDataCanBeUsed) {
                        val bundle = Bundle()
                        val fragmentTujuan = VerifyRegisterPelangganFragment()
                        bundle.putParcelable(Constant.reffPelanggan, resultPelanggan)
                        bundle.putParcelable(Constant.dataModelFotoProfil, etFotoProfil.value)
                        fragmentTujuan.arguments = bundle
                        navController.navigate(R.id.verifyRegisterPelangganFragment, bundle)
                    } else {
                        when (result?.message) {
                            "Error, Username sudah terdaftar!" -> {
                                setTextError(result.message, editUsername)
                            }
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
                    call: Call<ModelResponse>,
                    t: Throwable
                ) {
                    isShowLoading.value = false
                    message.value = t.message
                }
            })
    }
}