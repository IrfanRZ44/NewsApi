@file:Suppress("DEPRECATION")

package id.telkomsel.merchant.ui.auth.loginMerchant

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.iid.InstanceIdResult
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseViewModel
import id.telkomsel.merchant.model.ModelMerchant
import id.telkomsel.merchant.model.response.ModelResponseMerchant
import id.telkomsel.merchant.ui.auth.changePassword.ChangePasswordFragment
import id.telkomsel.merchant.ui.auth.updateRegisterMerchant.UpdateRegisterMerchantFragment
import id.telkomsel.merchant.utils.Constant
import id.telkomsel.merchant.utils.DataSave
import id.telkomsel.merchant.utils.FirebaseUtils
import id.telkomsel.merchant.utils.RetrofitUtils
import id.telkomsel.merchant.utils.adapter.dismissKeyboard
import id.telkomsel.merchant.utils.adapter.getDate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("StaticFieldLeak")
class LoginMerchantViewModel(
    private val navController: NavController,
    private val savedData: DataSave?,
    private val activity: Activity?,
    private val editUsername: TextInputLayout,
    private val editPassword: TextInputLayout
) : BaseViewModel() {
    val etUsername = MutableLiveData<String>()
    val etPassword = MutableLiveData<String>()

    fun onClickRegister(){
        activity?.let { dismissKeyboard(it) }
        checkPermission(activity)
    }

    private fun checkPermission(context: Context?) {
        context?.let {
            if (ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    it, Manifest.permission.ACCESS_COARSE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                navController.navigate(R.id.registerMerchantFragment)
            }
            else {
                message.value = "Anda belum mengizinkan akses lokasi aplikasi ini"

                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    Constant.codeRequestLocation
                )
            }
        }
    }

    fun onClickForgetPassword(){
        activity?.let { dismissKeyboard(it) }
        navController.navigate(R.id.forgetPasswordMerchantFragment)
    }

    fun onClickLogin(){
        setNullError()
        activity?.let { dismissKeyboard(it) }
        val username = etUsername.value
        val password = etPassword.value

        if (!username.isNullOrEmpty() && !password.isNullOrEmpty()) {
            isShowLoading.value = true
            getUserToken(username, password)
        }
        else {
            if (username.isNullOrEmpty()) {
                setTextError("Mohon masukkan username", editUsername)
            } else if (password.isNullOrEmpty()) {
                setTextError("Mohon masukkan password", editPassword)
            }
        }
    }

    private fun setNullError(){
        editUsername.error = null
        editPassword.error = null
    }

    private fun setTextError(msg: String, editText: TextInputLayout){
        message.value = msg
        editText.error = msg
        editText.requestFocus()
        editText.findFocus()
    }

    private fun getUserToken(textInput: String, password: String) {
        isShowLoading.value = true

        val onCompleteListener =
            OnCompleteListener<InstanceIdResult> { result ->
                if (result.isSuccessful) {
                    try {
                        val tkn = result.result?.token ?: throw Exception("Error, kesalahan saat menyimpan token")

                        when {
                            textInput.take(1) == "0" -> {
                                val phone = textInput.replaceFirst("0", "+62")
                                loginMerchantPhone(phone, password, tkn)
                            }
                            textInput.take(3) == "+62" -> {
                                val phone = textInput.replaceFirst("0", "+62")
                                loginMerchantPhone(phone, password, tkn)
                            }
                            else -> {
                                loginMerchantUsername(textInput, password, tkn)
                            }
                        }
                    } catch (e: Exception) {
                        isShowLoading.value = false
                        message.value = e.message
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

    private fun loginMerchantUsername(username: String, password: String, token: String){
        RetrofitUtils.loginMerchantUsername(username, password, token,
            object : Callback<ModelResponseMerchant> {
                override fun onResponse(
                    call: Call<ModelResponseMerchant>,
                    response: Response<ModelResponseMerchant>
                ) {
                    isShowLoading.value = false
                    val result = response.body()
                    val dataMerchant = result?.dataMerchant

                    if (result?.message == Constant.reffSuccess && dataMerchant != null){
                        checkPassword(password, dataMerchant)
                    }
                    else{
                        when (dataMerchant?.status_merchant) {
                            Constant.statusDeclined -> {
                                dialogSucces(dataMerchant, "${Constant.formUpdateMerchant} : ${dataMerchant.comment} \n \nApakah Anda ingin mendaftarkan ulang akun merchant?")
                            }
                            else -> {
                                dialogWaitVerify(result?.message?:"Error, terjadi kesalahan database")
                            }
                        }
                    }
                }

                override fun onFailure(
                    call: Call<ModelResponseMerchant>,
                    t: Throwable
                ) {
                    isShowLoading.value = false
                    message.value = t.message
                }
            })
    }

    private fun loginMerchantPhone(phone: String, password: String, token: String){
        RetrofitUtils.loginMerchantPhone(phone, password, token,
            object : Callback<ModelResponseMerchant> {
                override fun onResponse(
                    call: Call<ModelResponseMerchant>,
                    response: Response<ModelResponseMerchant>
                ) {
                    isShowLoading.value = false
                    val result = response.body()
                    val dataMerchant = result?.dataMerchant

                    if (result?.message == Constant.reffSuccess && dataMerchant != null){
                        checkPassword(password, dataMerchant)
                    }
                    else{
                        when (dataMerchant?.status_merchant) {
                            Constant.statusDeclined -> {
                                dialogSucces(dataMerchant, "${Constant.formUpdateMerchant} : ${dataMerchant.comment} \n \nApakah Anda ingin mendaftarkan ulang akun merchant?")
                            }
                            else -> {
                                dialogWaitVerify(result?.message?:"Error, terjadi kesalahan database")
                            }
                        }
                    }
                }

                override fun onFailure(
                    call: Call<ModelResponseMerchant>,
                    t: Throwable
                ) {
                    isShowLoading.value = false
                    message.value = t.message
                }
            })
    }

    private fun checkPassword(password: String, dataMerchant: ModelMerchant){
        if (password == "Tsel2021"){
            val bundle = Bundle()
            val fragmentTujuan = ChangePasswordFragment()
            bundle.putParcelable(Constant.reffMerchant, dataMerchant)
            fragmentTujuan.arguments = bundle
            navController.navigate(R.id.changePasswordFragment, bundle)
        }
        else{
            savedData?.setDataObject(dataMerchant, Constant.reffMerchant)
            val dataApps = savedData?.getDataApps()
            dataApps?.lastOnline = getDate(Constant.dateFormat1)
            savedData?.setDataObject(dataApps, Constant.reffInfoApps)
            message.value = "Berhasil login ${savedData?.getDataApps()?.lastOnline}"
            navController.navigate(R.id.splashFragment)
        }
    }

    private fun dialogWaitVerify(messageDialog: String){
        if (activity != null){
            val alert = AlertDialog.Builder(activity)
            alert.setMessage(messageDialog)
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

    private fun dialogSucces(dataMerchant: ModelMerchant, messageDialog: String){
        if (activity != null){
            val alert = AlertDialog.Builder(activity)
            alert.setTitle(Constant.loginBerhasil)
            alert.setMessage(messageDialog)
            alert.setPositiveButton(
                Constant.iya
            ) { dialog, _ ->
                dialog.dismiss()
                    val bundle = Bundle()
                    val fragmentTujuan = UpdateRegisterMerchantFragment()
                    bundle.putParcelable(Constant.reffMerchant, dataMerchant)
                    fragmentTujuan.arguments = bundle
                    navController.navigate(R.id.updateRegisterMerchantFragment, bundle)
            }
            alert.setNegativeButton(
                Constant.tidak
            ){ dialog, _ ->
                dialog.dismiss()
            }

            alert.show()
        }
        else{
            message.value = "Mohon mulai ulang aplikasi"
        }
    }
}