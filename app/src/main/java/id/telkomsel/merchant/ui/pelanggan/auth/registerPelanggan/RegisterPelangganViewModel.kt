package id.telkomsel.merchant.ui.pelanggan.auth.registerPelanggan

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
import id.telkomsel.merchant.ui.pelanggan.auth.verifyRegisterPelanggan.VerifyRegisterPelangganFragment
import id.telkomsel.merchant.utils.Constant
import id.telkomsel.merchant.utils.RetrofitUtils
import id.telkomsel.merchant.utils.adapter.dismissKeyboard
import id.telkomsel.merchant.utils.adapter.isContainBigText
import id.telkomsel.merchant.utils.adapter.isContainNumber
import id.telkomsel.merchant.utils.adapter.isContainSmallText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("StaticFieldLeak")
class RegisterPelangganViewModel(
    private val activity: Activity?,
    private val navController: NavController,
    private val editNama: TextInputLayout,
    private val editAlamat: TextInputLayout,
    private val editNoHp: TextInputLayout,
    private val editNoWa: TextInputLayout,
    private val editUsername: TextInputLayout,
    private val editPassword: TextInputLayout,
    private val editConfirmPassword: TextInputLayout,
    private val editTglLahir: TextInputLayout
) : BaseViewModel() {
    val etNama = MutableLiveData<String>()
    val etAlamat = MutableLiveData<String>()
    val etUsername = MutableLiveData<String>()
    val etPassword = MutableLiveData<String>()
    val etPasswordConfirm = MutableLiveData<String>()
    val etTglLahir = MutableLiveData<String>()
    val etPhone = MutableLiveData<String>()
    val etWA = MutableLiveData<String>()
    val etFotoProfil = MutableLiveData<Uri>()
    var dataPelanggan: ModelPelanggan? = null

    fun setDataMerchant(fotoProfil: Uri?){
        etUsername.value = dataPelanggan?.username
        etPassword.value = dataPelanggan?.password
        etPasswordConfirm.value = dataPelanggan?.password
        etNama.value = dataPelanggan?.nama
        etAlamat.value = dataPelanggan?.alamat
        etTglLahir.value = dataPelanggan?.tgl_lahir

        etPhone.value = dataPelanggan?.nomor_hp?.replaceFirst("+62", "0")
        etWA.value = dataPelanggan?.nomor_wa?.replaceFirst("+62", "0")

        etFotoProfil.value = fotoProfil
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

    fun onClickRegisterPelanggan(){
        setNullError()
        activity?.let { dismissKeyboard(it) }

        val nama = etNama.value
        val alamat = etAlamat.value
        val noHp = etPhone.value
        val noWa = etWA.value
        val username = etUsername.value
        val password = etPassword.value
        val confirmPassword = etPasswordConfirm.value
        val tglLahir = etTglLahir.value
        val fotoProfil = etFotoProfil.value?.path

        if (!nama.isNullOrEmpty() && !alamat.isNullOrEmpty()
            && !noHp.isNullOrEmpty() && noHp.take(1) == "0"
            && !noWa.isNullOrEmpty() && noWa.take(1) == "0"
            && !username.isNullOrEmpty()
            && !password.isNullOrEmpty() && !confirmPassword.isNullOrEmpty() && (password == confirmPassword)
            && password.length >= 6 && isContainNumber(password) && (isContainSmallText(password) || isContainBigText(password))
        ) {
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
                "",
                tglLahir?:"",
                alamat,
                hp,
                wa,
                hp
            )

            validatePelanggan(resultMerchant)
        }
        else{
            if (fotoProfil.isNullOrEmpty()){
                message.value = "Mohon upload foto profil"
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