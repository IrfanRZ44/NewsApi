package id.telkomsel.merchant.ui.pelanggan.account.editProfil

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.google.android.material.textfield.TextInputLayout
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseViewModel
import id.telkomsel.merchant.model.ModelPelanggan
import id.telkomsel.merchant.model.response.ModelResponse
import id.telkomsel.merchant.model.response.ModelResponsePelanggan
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
    private val editNama: TextInputLayout,
    private val editAlamat: TextInputLayout,
    private val editNoHp: TextInputLayout,
    private val editNoWa: TextInputLayout,
    private val editTglLahir: TextInputLayout,
    private val savedData: DataSave
) : BaseViewModel() {
    val etNama = MutableLiveData<String>()
    val etAlamat = MutableLiveData<String>()
    val etTglLahir = MutableLiveData<String>()
    val etPhone = MutableLiveData<String>()
    val etWA = MutableLiveData<String>()
    val etFotoProfil = MutableLiveData<Uri>()
    var dataPelanggan: ModelPelanggan? = null

    fun setDataPelanggan(){
        etNama.value = dataPelanggan?.nama
        etAlamat.value = dataPelanggan?.alamat
        etTglLahir.value = dataPelanggan?.tgl_lahir

        etPhone.value = dataPelanggan?.nomor_hp?.replaceFirst("+62", "0")
        etWA.value = dataPelanggan?.nomor_wa?.replaceFirst("+62", "0")

        etFotoProfil.value = Uri.parse(dataPelanggan?.foto)
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

    private fun setNullError(){
        editNama.error = null
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
        val alamat = etAlamat.value
        val noHp = etPhone.value
        val noWa = etWA.value
        val tglLahir = etTglLahir.value

        if (dataEdit != null && !nama.isNullOrEmpty() && !alamat.isNullOrEmpty()
            && !noHp.isNullOrEmpty() && noHp.take(1) == "0"
            && !noWa.isNullOrEmpty() && noWa.take(1) == "0"
        ) {
            val hp = noHp.replaceFirst("0", "+62")
            val wa = noWa.replaceFirst("0", "+62")

            isShowLoading.value = true

            dataEdit.nama = nama
            dataEdit.tgl_lahir = tglLahir?:""
            dataEdit.alamat = alamat
            dataEdit.nomor_hp = hp
            dataEdit.nomor_wa = wa

            updateProfilPelanggan(dataEdit)
        }
        else{
            if (dataEdit == null){
                message.value = "Error, terjadi kesalahan database"
            }
            else if (nama.isNullOrEmpty()){
                setTextError("Error, mohon masukkan nama", editNama)
            }
            else if (alamat.isNullOrEmpty()){
                setTextError("Error, mohon masukkan alamat", editAlamat)
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