package com.exomatik.ballighadmin.ui.main.splash

import android.app.Activity
import androidx.navigation.NavController
import com.exomatik.ballighadmin.R
import com.exomatik.ballighadmin.base.BaseViewModel
import com.exomatik.ballighadmin.model.ModelUser
import com.exomatik.ballighadmin.utils.Constant.admin
import com.exomatik.ballighadmin.utils.Constant.defaultNoHp
import com.exomatik.ballighadmin.utils.Constant.defaultPhoneCode
import com.exomatik.ballighadmin.utils.Constant.defaultUsernameAdmin
import com.exomatik.ballighadmin.utils.Constant.online
import com.exomatik.ballighadmin.utils.Constant.referenceUser
import com.exomatik.ballighadmin.utils.Constant.token
import com.exomatik.ballighadmin.utils.DataSave
import com.exomatik.ballighadmin.utils.FirebaseUtils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.iid.InstanceIdResult

class SplashViewModel(private val activity: Activity?,
                      private val navController: NavController,
                      private val savedData: DataSave) : BaseViewModel() {

    fun requestCode() {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                getUserToken()
            }

            override fun onVerificationFailed(e: FirebaseException) {
                when (e) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        message.value = "Error, Nomor Handphone tidak Valid"
                    }
                    is FirebaseTooManyRequestsException -> {
                        message.value = "Error, Anda sudah terlalu banyak mengirimkan permintaan"
                    }
                    else -> {
                        message.value = e.message
                    }
                }
                isShowLoading.value = false
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                verifyUser(verificationId)
            }
        }

        activity?.let {
            FirebaseUtils.registerUser(
                defaultNoHp
                , callbacks, it
            )
        }
    }

    private fun verifyUser(verifyId: String) {
        val credential = PhoneAuthProvider.getCredential(
            verifyId
            , defaultPhoneCode
        )

        val onCoCompleteListener =
            OnCompleteListener<AuthResult> { task ->
                if (task.isSuccessful) {
                    isShowLoading.value = false
                    getUserToken()
                } else {
                    message.value = "Error, terjadi kesalahan database"
                    isShowLoading.value = false
                }
            }

        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(onCoCompleteListener)
    }

    private fun getUserToken() {
        val onCompleteListener =
            OnCompleteListener<InstanceIdResult> { result ->
                if (result.isSuccessful) {
                    result.result?.token?.let {
                        saveToken(
                            it
                        )
                    }
                } else {
                    isShowLoading.value = false
                    message.value = "Gagal mendapatkan token"
                }
            }

        FirebaseUtils.getUserToken(
            onCompleteListener
        )
    }

    private fun saveToken(value: String) {
        isShowLoading.value = true
        val onCompleteListener =
            OnCompleteListener<Void> {
                isShowLoading.value = false
                val dataUser = ModelUser(defaultNoHp, value, admin, admin,
                    defaultUsernameAdmin, "", "", "", admin, "",
                    "", "", "", "", "", "",
                    "", "", null, online)
                savedData.setDataObject(dataUser, referenceUser)
                navController.navigate(R.id.action_splashFragment_to_mainFragment)
            }

        val onFailureListener = OnFailureListener { result ->
            isShowLoading.value = false
            message.value = result.message
        }

        FirebaseUtils.setValueWith2ChildString(
            referenceUser
            , defaultUsernameAdmin
            , token
            , value
            , onCompleteListener
            , onFailureListener
        )
    }
}