package id.telkomsel.merchandise.ui.auth.verifyForgetPassword

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import id.telkomsel.merchandise.R
import id.telkomsel.merchandise.base.BaseViewModel
import id.telkomsel.merchandise.model.ModelSales
import id.telkomsel.merchandise.services.timer.TListener
import id.telkomsel.merchandise.services.timer.TimeFormatEnum
import id.telkomsel.merchandise.services.timer.TimerView
import id.telkomsel.merchandise.ui.auth.changePassword.ChangePasswordFragment
import id.telkomsel.merchandise.utils.Constant
import id.telkomsel.merchandise.utils.FirebaseUtils
import java.util.concurrent.TimeUnit

@SuppressLint("StaticFieldLeak")
class VerifyForgetPasswordViewModel(
    private val activity: Activity?,
    private val progressTimer: TimerView,
    private val navController: NavController
) : BaseViewModel() {
    val etOTP = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()
    var unverify = true
    var verifyId = ""
    lateinit var dataSales : ModelSales
    val noHp = MutableLiveData<String>()

    fun onClickBack(){
        isShowLoading.value = true
        navController.popBackStack()
    }

    fun setText(textInput : Int){
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

    fun clearText(){
        etOTP.value = ""
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

                        onSuccess(dataSales)
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

    fun sendCode() {
        isShowLoading.value = true
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                unverify = false
                message.value = "Berhasil memverifikasi nomor ${noHp.value}"
                isShowLoading.value = false
                loading.value = true

                onSuccess(dataSales)
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

    private fun onSuccess(data: ModelSales){
        val bundle = Bundle()
        val fragmentTujuan = ChangePasswordFragment()
        bundle.putParcelable(Constant.reffSales, data)
        fragmentTujuan.arguments = bundle
        navController.navigate(R.id.changePasswordFragment, bundle)
    }
}