@file:Suppress("DEPRECATION")

package id.telkomsel.merchandise.ui.auth.loginSales

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
import id.telkomsel.merchandise.R
import id.telkomsel.merchandise.base.BaseViewModel
import id.telkomsel.merchandise.model.ModelSales
import id.telkomsel.merchandise.model.response.ModelResponseSales
import id.telkomsel.merchandise.ui.auth.changePassword.ChangePasswordFragment
import id.telkomsel.merchandise.ui.auth.updateRegisterSales.UpdateRegisterSalesFragment
import id.telkomsel.merchandise.utils.Constant
import id.telkomsel.merchandise.utils.DataSave
import id.telkomsel.merchandise.utils.FirebaseUtils
import id.telkomsel.merchandise.utils.RetrofitUtils
import id.telkomsel.merchandise.utils.adapter.dismissKeyboard
import id.telkomsel.merchandise.utils.adapter.getDate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("StaticFieldLeak")
class LoginSalesViewModel(
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
                navController.navigate(R.id.registerSalesFragment)
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
        navController.navigate(R.id.forgetPasswordSalesFragment)
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
                                loginSalesPhone(phone, password, tkn)
                            }
                            textInput.take(3) == "+62" -> {
                                val phone = textInput.replaceFirst("0", "+62")
                                loginSalesPhone(phone, password, tkn)
                            }
                            else -> {
                                loginSalesUsername(textInput, password, tkn)
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

    private fun loginSalesUsername(username: String, password: String, token: String){
        RetrofitUtils.loginSalesUsername(username, password, token,
            object : Callback<ModelResponseSales> {
                override fun onResponse(
                    call: Call<ModelResponseSales>,
                    response: Response<ModelResponseSales>
                ) {
                    isShowLoading.value = false
                    val result = response.body()
                    val dataSales = result?.dataSales

                    if (result?.message == Constant.reffSuccess && dataSales != null){
                        checkPassword(password, dataSales)
                    }
                    else{
                        when (dataSales?.status) {
                            Constant.statusDeclined -> {
                                dialogSucces(dataSales, "${Constant.formUpdateSales} : ${dataSales.comment} \n \nApakah Anda ingin mendaftarkan ulang akun merchandise?")
                            }
                            else -> {
                                dialogWaitVerify(result?.message?:"Error, terjadi kesalahan database")
                            }
                        }
                    }
                }

                override fun onFailure(
                    call: Call<ModelResponseSales>,
                    t: Throwable
                ) {
                    isShowLoading.value = false
                    message.value = t.message
                }
            })
    }

    private fun loginSalesPhone(phone: String, password: String, token: String){
        RetrofitUtils.loginSalesPhone(phone, password, token,
            object : Callback<ModelResponseSales> {
                override fun onResponse(
                    call: Call<ModelResponseSales>,
                    response: Response<ModelResponseSales>
                ) {
                    isShowLoading.value = false
                    val result = response.body()
                    val dataSales = result?.dataSales

                    if (result?.message == Constant.reffSuccess && dataSales != null){
                        checkPassword(password, dataSales)
                    }
                    else{
                        when (dataSales?.status) {
                            Constant.statusDeclined -> {
                                dialogSucces(dataSales, "${Constant.formUpdateSales} : ${dataSales.comment} \n \nApakah Anda ingin mendaftarkan ulang akun merchandise?")
                            }
                            else -> {
                                dialogWaitVerify(result?.message?:"Error, terjadi kesalahan database")
                            }
                        }
                    }
                }

                override fun onFailure(
                    call: Call<ModelResponseSales>,
                    t: Throwable
                ) {
                    isShowLoading.value = false
                    message.value = t.message
                }
            })
    }

    private fun checkPassword(password: String, dataSales: ModelSales){
        if (password == "Tsel2021"){
            val bundle = Bundle()
            val fragmentTujuan = ChangePasswordFragment()
            bundle.putParcelable(Constant.reffSales, dataSales)
            fragmentTujuan.arguments = bundle
            navController.navigate(R.id.changePasswordFragment, bundle)
        }
        else{
            savedData?.setDataObject(dataSales, Constant.reffSales)
            val dataApps = savedData?.getDataApps()
            dataApps?.lastOnline = getDate(Constant.dateFormat1)
            savedData?.setDataObject(dataApps, Constant.reffInfoApps)
            message.value = "Berhasil login"
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

    private fun dialogSucces(dataSales: ModelSales, messageDialog: String){
        if (activity != null){
            val alert = AlertDialog.Builder(activity)
            alert.setTitle(Constant.loginBerhasil)
            alert.setMessage(messageDialog)
            alert.setPositiveButton(
                Constant.iya
            ) { dialog, _ ->
                dialog.dismiss()
                    val bundle = Bundle()
                    val fragmentTujuan = UpdateRegisterSalesFragment()
                    bundle.putParcelable(Constant.reffSales, dataSales)
                    fragmentTujuan.arguments = bundle
                    navController.navigate(R.id.updateRegisterSalesFragment, bundle)
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