package id.telkomsel.merchandise.ui.auth.verifyRegisterSales

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import id.telkomsel.merchandise.R
import id.telkomsel.merchandise.base.BaseViewModel
import id.telkomsel.merchandise.model.ModelSales
import id.telkomsel.merchandise.model.response.ModelResponse
import id.telkomsel.merchandise.services.timer.TListener
import id.telkomsel.merchandise.services.timer.TimeFormatEnum
import id.telkomsel.merchandise.services.timer.TimerView
import id.telkomsel.merchandise.ui.auth.registerSales.RegisterSalesFragment
import id.telkomsel.merchandise.utils.Constant
import id.telkomsel.merchandise.utils.FirebaseUtils
import id.telkomsel.merchandise.utils.RetrofitUtils
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.concurrent.TimeUnit

@SuppressLint("StaticFieldLeak")
class VerifyRegisterSalesViewModel(
    private val activity: Activity?,
    private val progressTimer: TimerView,
    private val navController: NavController
) : BaseViewModel() {
    val etOTP = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()
    var unverify = true
    var verifyId = ""
    lateinit var dataSales : ModelSales
    val etFotoProfil = MutableLiveData<Uri>()
    val etFotoDepanKendaraan = MutableLiveData<Uri>()
    val etFotoBelakangKendaraan = MutableLiveData<Uri>()
    val etFotoWajah = MutableLiveData<Uri>()
    val etFotoSeluruhBadan = MutableLiveData<Uri>()
    val noHp = MutableLiveData<String>()

    fun setText(textInput : Int){
        if (isShowLoading.value == false){
            val textResult = etOTP.value
            if (!textResult.isNullOrEmpty() && textResult.length >= 5){
                etOTP.value = textResult + textInput
                isShowLoading.value = true
                verifyUser()
            }
            else{
                if (textResult.isNullOrEmpty()){
                    etOTP.value = textInput.toString()
                }
                else{
                    etOTP.value = textResult + textInput
                }
            }
        }
    }

    fun clearText(){
        etOTP.value = ""
    }

    fun onClickBack(){
        val bundle = Bundle()
        val fragmentTujuan = RegisterSalesFragment()
        bundle.putParcelable(Constant.reffSales, dataSales)
        bundle.putParcelable(Constant.dataModelFotoProfil, etFotoProfil.value)
        bundle.putParcelable(Constant.dataModelFotoDepanKendaraan, etFotoDepanKendaraan.value)
        bundle.putParcelable(Constant.dataModelFotoBelakangKendaraan, etFotoBelakangKendaraan.value)
        bundle.putParcelable(Constant.dataModelFotoWajah, etFotoWajah.value)
        bundle.putParcelable(Constant.dataModelFotoSeluruhBadan, etFotoSeluruhBadan.value)
        fragmentTujuan.arguments = bundle

        val navOption = NavOptions.Builder().setPopUpTo(R.id.registerSalesFragment, true).build()
        navController.navigate(R.id.registerSalesFragment,bundle, navOption)
    }

    private fun verifyUser() {
        try {
            val credential = PhoneAuthProvider.getCredential(
                verifyId, etOTP.value ?: throw Exception("Error, kode verifikasi tidak boleh kosong")
            )

            val onCoCompleteListener =
                OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
                        isShowLoading.value = false

                        activity?.let { etFotoProfil.value?.path?.let { it1 ->
                            compressImageProfil(it,
                                it1
                            )
                        } }
                    } else {
                        message.value = "Error, kode verifikasi salah"
                        isShowLoading.value = false
                        clearText()
                    }
                }

            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(onCoCompleteListener)
        } catch (e: java.lang.Exception) {
            message.value = e.message
            isShowLoading.value = false
            clearText()
        }
    }

    private fun createSales(dataSales: ModelSales){
        RetrofitUtils.createSales(dataSales,
            object : Callback<ModelResponse> {
                override fun onResponse(
                    call: Call<ModelResponse>,
                    response: Response<ModelResponse>
                ) {
                    isShowLoading.value = false
                    val result = response.body()

                    if (result?.message == Constant.reffSuccessRegister){
                        dialogSucces(result.message)

                        navController.navigate(R.id.splashFragment)
                    }
                    else{
                        val msgErr = result?.message
                        if (!msgErr.isNullOrEmpty()){
                            when {
                                msgErr.contains("Duplicate entry '${dataSales.username}' for key 'username'") -> {
                                    message.value = "Username Sudah Digunakan"
                                }
                                msgErr.contains("Duplicate entry '${dataSales.no_hp_sales}' for key 'no_hp_sales'") -> {
                                    message.value = "Nomor HP Sales Sudah Digunakan"
                                }
                                else -> {
                                    message.value = msgErr
                                }
                            }
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

    private fun compressImageProfil(act: Activity, fotoProfil: String){
        val job = Job()
        val uiScope = CoroutineScope(Dispatchers.IO + job)
        uiScope.launch {
            val compressedImageFile = Compressor.compress(act, File(fotoProfil)) {
                resolution(256, 256)
                quality(70)
                format(Bitmap.CompressFormat.JPEG)
                size(124_000) // 124 KB
            }
            val resultUri = Uri.fromFile(compressedImageFile)

            act.runOnUiThread {
                resultUri?.let { it ->
                    val tempPath = it.path

                    if(!tempPath.isNullOrEmpty()){
                        dataSales.foto_profil = tempPath

                        etFotoDepanKendaraan.value?.path?.let { it1 ->
                            compressImageDepan(act,
                                it1
                            )
                        }
                    }
                    else{
                        dataSales.foto_profil = fotoProfil

                        etFotoDepanKendaraan.value?.path?.let { it1 ->
                            compressImageDepan(act,
                                it1
                            )
                        }
                    }
                }
            }
        }
    }

    private fun compressImageDepan(act: Activity, fotoDepan: String){
        val job = Job()
        val uiScope = CoroutineScope(Dispatchers.IO + job)
        uiScope.launch {
            val compressedImageFile = Compressor.compress(act, File(fotoDepan)) {
                resolution(256, 256)
                quality(70)
                format(Bitmap.CompressFormat.JPEG)
                size(124_000) // 124 KB
            }
            val resultUri = Uri.fromFile(compressedImageFile)

            act.runOnUiThread {
                resultUri?.let { it ->
                    val tempPath = it.path

                    if(!tempPath.isNullOrEmpty()){
                        dataSales.foto_depan_kendaraan = tempPath

                        etFotoBelakangKendaraan.value?.path?.let { it1 ->
                            compressImageBelakang(act,
                                it1
                            )
                        }
                    }
                    else{
                        dataSales.foto_depan_kendaraan = fotoDepan

                        etFotoBelakangKendaraan.value?.path?.let { it1 ->
                            compressImageBelakang(act,
                                it1
                            )
                        }
                    }
                }
            }
        }
    }

    private fun compressImageBelakang(act: Activity, fotoBelakang: String){
        val job = Job()
        val uiScope = CoroutineScope(Dispatchers.IO + job)
        uiScope.launch {
            val compressedImageFile = Compressor.compress(act, File(fotoBelakang)) {
                resolution(256, 256)
                quality(70)
                format(Bitmap.CompressFormat.JPEG)
                size(124_000) // 124 KB
            }
            val resultUri = Uri.fromFile(compressedImageFile)

            act.runOnUiThread {
                resultUri?.let { it ->
                    val tempPath = it.path

                    if(!tempPath.isNullOrEmpty()){
                        dataSales.foto_belakang_kendaraan = tempPath

                        etFotoWajah.value?.path?.let { it1 ->
                            compressImageWajah(act,
                                it1
                            )
                        }
                    }
                    else{
                        dataSales.foto_belakang_kendaraan = fotoBelakang

                        etFotoWajah.value?.path?.let { it1 ->
                            compressImageWajah(act,
                                it1
                            )
                        }
                    }
                }
            }
        }
    }

    private fun compressImageWajah(act: Activity, fotoWajah: String){
        val job = Job()
        val uiScope = CoroutineScope(Dispatchers.IO + job)
        uiScope.launch {
            val compressedImageFile = Compressor.compress(act, File(fotoWajah)) {
                resolution(256, 256)
                quality(70)
                format(Bitmap.CompressFormat.JPEG)
                size(124_000) // 124 KB
            }
            val resultUri = Uri.fromFile(compressedImageFile)

            act.runOnUiThread {
                resultUri?.let { it ->
                    val tempPath = it.path

                    if(!tempPath.isNullOrEmpty()){
                        dataSales.foto_wajah = tempPath

                        etFotoSeluruhBadan.value?.path?.let { it1 ->
                            compressImageBadan(act,
                                it1
                            )
                        }

                    }
                    else{
                        dataSales.foto_wajah = fotoWajah

                        etFotoSeluruhBadan.value?.path?.let { it1 ->
                            compressImageBadan(act,
                                it1
                            )
                        }
                    }
                }
            }
        }
    }

    private fun compressImageBadan(act: Activity, fotoBadan: String){
        val job = Job()
        val uiScope = CoroutineScope(Dispatchers.IO + job)
        uiScope.launch {
            val compressedImageFile = Compressor.compress(act, File(fotoBadan)) {
                resolution(256, 256)
                quality(70)
                format(Bitmap.CompressFormat.JPEG)
                size(124_000) // 124 KB
            }
            val resultUri = Uri.fromFile(compressedImageFile)

            act.runOnUiThread {
                resultUri?.let { it ->
                    val tempPath = it.path

                    if(!tempPath.isNullOrEmpty()){
                        dataSales.foto_seluruh_badan = tempPath

                        createSales(dataSales)
                    }
                    else{
                        dataSales.foto_seluruh_badan = fotoBadan

                        createSales(dataSales)
                    }
                }
            }
        }
    }

    fun sendCode() {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                etOTP.value = credential.smsCode
                unverify = false
                message.value = "Berhasil memverifikasi nomor ${noHp.value}"
                isShowLoading.value = false
                loading.value = true

                activity?.let { etFotoProfil.value?.path?.let { it1 ->
                    compressImageProfil(it,
                        it1
                    )
                } }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                when (e) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        message.value = "Error, Nomor Handphone tidak Valid"
                        clearText()
                    }
                    is FirebaseTooManyRequestsException -> {
                        message.value = "Error, Anda sudah terlalu banyak mengirimkan permintaan"
                        clearText()
                    }
                    else -> {
                        message.value = e.message
                        clearText()
                    }
                }
                isShowLoading.value = false
                loading.value = true
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                object : CountDownTimer(5000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                    }

                    override fun onFinish() {
                        if (unverify) {
                            isShowLoading.value = false
                            loading.value = true

                            message.value = "Kami sudah mengirimkan kode OTP ke nomor ${noHp.value}"
                            unverify = false
                            setProgress()

                            verifyId = verificationId
                        }
                    }
                }.start()
            }
        }

        try {
            FirebaseUtils.registerUser(
                noHp.value?:throw Exception("Error, mohon lakukan pendaftaran ulang")
                , callbacks, activity ?: throw Exception("Error, Mohon mulai ulang aplikasi")
            )
        } catch (e: Exception) {
            message.value = e.message
            isShowLoading.value = false
        }
    }

    private fun setProgress() {
        progressTimer.setCircularTimerListener(object : TListener {
            override fun updateDataOnTick(remainingTimeInMs: Long): String {
                // long seconds = (milliseconds / 1000);
                val seconds = TimeUnit.MILLISECONDS.toSeconds(remainingTimeInMs)
                progressTimer.prefix = ""
                progressTimer.suffix = " detik"
                return seconds.toString()
            }

            override fun onTimerFinished() {
                progressTimer.prefix = ""
                progressTimer.suffix = ""
                progressTimer.text = "Kirim Ulang?"
                isShowLoading.value = false
                loading.value = false
            }
        }, 60, TimeFormatEnum.SECONDS, 1)

        progressTimer.progress = 0F
        progressTimer.startTimer()
    }

    private fun dialogSucces(msg: String){
        if (activity != null){
            val alert = AlertDialog.Builder(activity)
            alert.setTitle(Constant.pendaftaranBerhasil)
            alert.setMessage(msg)
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
}