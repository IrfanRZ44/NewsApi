package id.telkomsel.merchant.ui.pelanggan.verifyRegisterPelanggan

import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseViewModel
import id.telkomsel.merchant.model.ModelPelanggan
import id.telkomsel.merchant.model.response.ModelResponsePelanggan
import id.telkomsel.merchant.services.timer.TListener
import id.telkomsel.merchant.services.timer.TimeFormatEnum
import id.telkomsel.merchant.services.timer.TimerView
import id.telkomsel.merchant.ui.pelanggan.registerPelanggan.RegisterPelangganFragment
import id.telkomsel.merchant.utils.Constant
import id.telkomsel.merchant.utils.DataSave
import id.telkomsel.merchant.utils.FirebaseUtils
import id.telkomsel.merchant.utils.RetrofitUtils
import id.telkomsel.merchant.utils.adapter.dismissKeyboard
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

@SuppressLint("StaticFieldLeak")
class VerifyRegisterPelangganViewModel(
    private val activity: Activity?,
    private val progressTimer: TimerView,
    private val etText1: AppCompatEditText,
    private val etText2: AppCompatEditText,
    private val etText3: AppCompatEditText,
    private val etText4: AppCompatEditText,
    private val etText5: AppCompatEditText,
    private val etText6: AppCompatEditText,
    private val navController: NavController,
    private val saveData: DataSave
) : BaseViewModel() {
    val phoneCode = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()
    var unverify = true
    var verifyId = ""
    lateinit var dataPelanggan : ModelPelanggan
    val etFotoProfil = MutableLiveData<Uri>()
    val noHp = MutableLiveData<String>()

    fun onClick(requestEvent: Int) {
        activity?.let { dismissKeyboard(it) }

        when (requestEvent) {
            1 -> {
                onClickBack()
            }
            2 -> {
                isShowLoading.value = true
                verifyUser()
            }
            3 -> {
                isShowLoading.value = true
                sendCode()
            }
        }
    }

    fun onClickBack(){
        val bundle = Bundle()
        val fragmentTujuan = RegisterPelangganFragment()
        bundle.putParcelable(Constant.reffPelanggan, dataPelanggan)
        bundle.putParcelable(Constant.dataModelFotoProfil, etFotoProfil.value)
        fragmentTujuan.arguments = bundle

        val navOption = NavOptions.Builder().setPopUpTo(R.id.registerPelangganFragment, true).build()
        navController.navigate(R.id.registerPelangganFragment,bundle, navOption)
    }

    private fun verifyUser() {
        try {
            val credential = PhoneAuthProvider.getCredential(
                verifyId, phoneCode.value ?: throw Exception("Error, kode verifikasi tidak boleh kosong")
            )

            val onCoCompleteListener =
                OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
                        isShowLoading.value = false

                        etFotoProfil.value?.path?.let { createPelanggan(dataPelanggan, it) }
                    } else {
                        message.value = "Error, kode verifikasi salah"
                        isShowLoading.value = false
                        setEmptyEditText()
                    }
                }

            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(onCoCompleteListener)
        } catch (e: java.lang.Exception) {
            message.value = e.message
            isShowLoading.value = false
            setEmptyEditText()
        }
    }

    private fun setEmptyEditText() {
        etText6.setText("")
        etText5.setText("")
        etText4.setText("")
        etText3.setText("")
        etText2.setText("")
        etText1.setText("")
        etText6.clearFocus()
        etText1.findFocus()
        etText1.requestFocus()
    }

    private fun createPelanggan(dataPelanggan: ModelPelanggan, urlFoto: String){
        RetrofitUtils.createPelanggan(dataPelanggan, urlFoto,
            object : Callback<ModelResponsePelanggan> {
                override fun onResponse(
                    call: Call<ModelResponsePelanggan>,
                    response: Response<ModelResponsePelanggan>
                ) {
                    isShowLoading.value = false
                    val result = response.body()

                    if (result?.message == Constant.reffSuccessRegisterPelanggan){
                        saveData.setDataObject(result.data, Constant.reffPelanggan)
                        dialogSucces(result.message)

                        val navOption = NavOptions.Builder().setPopUpTo(R.id.pelangganFragment, true).build()
                        navController.navigate(R.id.pelangganFragment, null, navOption)
                    }
                    else{
                        val msgErr = result?.message
                        if (!msgErr.isNullOrEmpty()){
                            when {
                                msgErr.contains("Duplicate entry '${dataPelanggan.username}' for key 'username'") -> {
                                    message.value = "Username Sudah Digunakan"
                                }
                                msgErr.contains("Duplicate entry '${dataPelanggan.nomor_hp}' for key 'nomor_hp'") -> {
                                    message.value = "Nomor HP Sudah Digunakan"
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
                    call: Call<ModelResponsePelanggan>,
                    t: Throwable
                ) {
                    isShowLoading.value = false
                    message.value = t.message
                }
            })
    }

    fun sendCode() {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                unverify = false
                message.value = "Berhasil memverifikasi nomor ${noHp.value}"
                isShowLoading.value = false
                loading.value = true

                etFotoProfil.value?.path?.let { createPelanggan(dataPelanggan, it) }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                when (e) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        message.value = "Error, Nomor Handphone tidak Valid"
                        setEmptyEditText()
                    }
                    is FirebaseTooManyRequestsException -> {
                        message.value = "Error, Anda sudah terlalu banyak mengirimkan permintaan"
                        setEmptyEditText()
                    }
                    else -> {
                        message.value = e.message
                        setEmptyEditText()
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