@file:Suppress("DEPRECATION")

package id.telkomsel.merchant.ui.pelanggan.loginPelanggan

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.iid.InstanceIdResult
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseViewModel
import id.telkomsel.merchant.model.response.ModelResponsePelanggan
import id.telkomsel.merchant.ui.auth.AuthActivity
import id.telkomsel.merchant.utils.Constant
import id.telkomsel.merchant.utils.DataSave
import id.telkomsel.merchant.utils.FirebaseUtils
import id.telkomsel.merchant.utils.RetrofitUtils
import id.telkomsel.merchant.utils.adapter.dismissKeyboard
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("StaticFieldLeak")
class LoginPelangganViewModel(
    private val navController: NavController,
    private val savedData: DataSave,
    private val activity: Activity?,
    private val editUsername: TextInputLayout,
    private val editPassword: TextInputLayout
) : BaseViewModel() {
    val etUsername = MutableLiveData<String>()
    val etPassword = MutableLiveData<String>()

    fun onClickRegister(){
        activity?.let { dismissKeyboard(it) }
        navController.navigate(R.id.registerPelangganFragment)
    }

    fun onClickLoginMerchant(){
        activity?.let { dismissKeyboard(it) }
        val intent = Intent(activity, AuthActivity::class.java)
        intent.putExtra(Constant.levelMerchant, true)
        savedData.setDataString(Constant.levelMerchant, Constant.level)
        activity?.startActivity(intent)
        activity?.finish()
    }

    fun onClickForgetPassword(){
        activity?.let { dismissKeyboard(it) }
        navController.navigate(R.id.forgetPasswordFragment)
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
                                loginPelangganPhone(phone, password, tkn)
                            }
                            textInput.take(3) == "+62" -> {
                                val phone = textInput.replaceFirst("0", "+62")
                                loginPelangganPhone(phone, password, tkn)
                            }
                            else -> {
                                loginPelangganUsername(textInput, password, tkn)
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

    private fun loginPelangganUsername(username: String, password: String, token: String){
        RetrofitUtils.loginPelangganUsername(username, password, token,
            object : Callback<ModelResponsePelanggan> {
                override fun onResponse(
                    call: Call<ModelResponsePelanggan>,
                    response: Response<ModelResponsePelanggan>
                ) {
                    isShowLoading.value = false
                    val result = response.body()
                    val dataPelanggan = result?.data

                    if (result?.message == Constant.reffSuccess && dataPelanggan != null){
                        savedData.setDataObject(dataPelanggan, Constant.reffPelanggan)
                        message.value = "Berhasil login"
                        val navOption = NavOptions.Builder().setPopUpTo(R.id.pelangganFragment, true).build()
                        navController.navigate(R.id.pelangganFragment, null, navOption)
                    }
                    else{
                        dialogWaitVerify(result?.message?:"Error, terjadi kesalahan database")
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

    private fun loginPelangganPhone(nomorMkios: String, password: String, token: String){
        RetrofitUtils.loginPelangganPhone(nomorMkios, password, token,
            object : Callback<ModelResponsePelanggan> {
                override fun onResponse(
                    call: Call<ModelResponsePelanggan>,
                    response: Response<ModelResponsePelanggan>
                ) {
                    isShowLoading.value = false
                    val result = response.body()
                    val dataPelanggan = result?.data

                    if (result?.message == Constant.reffSuccess && dataPelanggan != null){
                        savedData.setDataObject(dataPelanggan, Constant.reffPelanggan)
                        message.value = "Berhasil login"
                        val navOption = NavOptions.Builder().setPopUpTo(R.id.pelangganFragment, true).build()
                        navController.navigate(R.id.pelangganFragment, null, navOption)
                    }
                    else{
                        dialogWaitVerify(result?.message?:"Error, terjadi kesalahan database")
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
}