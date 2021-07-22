package id.telkomsel.merchant.ui.pelanggan.auth.forgetPassword

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.google.android.material.textfield.TextInputLayout
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseViewModel
import id.telkomsel.merchant.model.response.ModelResponsePelanggan
import id.telkomsel.merchant.ui.pelanggan.auth.verifyForgetPassword.VerifyForgetPasswordFragment
import id.telkomsel.merchant.utils.Constant
import id.telkomsel.merchant.utils.RetrofitUtils
import id.telkomsel.merchant.utils.adapter.dismissKeyboard
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("StaticFieldLeak")
class ForgetPasswordViewModel(
    private val navController: NavController,
    private val activity: Activity?,
    private val editUsername: TextInputLayout
) : BaseViewModel() {
    val etUsername = MutableLiveData<String>()

    fun onClickLogin(){
        activity?.let { dismissKeyboard(it) }
        navController.popBackStack()
    }

    fun onClickForgetPassword(){
        activity?.let { dismissKeyboard(it) }
        val textInput = etUsername.value

        if (!textInput.isNullOrEmpty()) {
            isShowLoading.value = true
            if (textInput.take(1) == "0"){
                val phone = textInput.replaceFirst("0", "+62")
                forgetPasswordPelangganPhone(phone)
            }
            else{
                forgetPasswordPelangganUsername(textInput)
            }
        }
        else {
            message.value = "Mohon masukkan username"
            editUsername.error = "Mohon masukkan username"
            editUsername.requestFocus()
            editUsername.findFocus()
        }
    }

    private fun forgetPasswordPelangganUsername(username: String){
        RetrofitUtils.forgetPasswordPelangganUsername(username,
            object : Callback<ModelResponsePelanggan> {
                override fun onResponse(
                    call: Call<ModelResponsePelanggan>,
                    response: Response<ModelResponsePelanggan>
                ) {
                    isShowLoading.value = false
                    val result = response.body()
                    val dataPelanggan = result?.data

                    if (result?.message == Constant.reffSuccess && dataPelanggan != null){
                        val bundle = Bundle()
                        val fragmentTujuan = VerifyForgetPasswordFragment()
                        bundle.putParcelable(Constant.reffPelanggan, dataPelanggan)
                        fragmentTujuan.arguments = bundle
                        navController.navigate(R.id.verifyForgetPasswordFragment2, bundle)
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

    private fun forgetPasswordPelangganPhone(phone: String){
        RetrofitUtils.forgetPasswordPelangganPhone(phone,
            object : Callback<ModelResponsePelanggan> {
                override fun onResponse(
                    call: Call<ModelResponsePelanggan>,
                    response: Response<ModelResponsePelanggan>
                ) {
                    isShowLoading.value = false
                    val result = response.body()
                    val dataPelanggan = result?.data

                    if (result?.message == Constant.reffSuccess && dataPelanggan != null){
                        val bundle = Bundle()
                        val fragmentTujuan = VerifyForgetPasswordFragment()
                        bundle.putParcelable(Constant.reffPelanggan, dataPelanggan)
                        fragmentTujuan.arguments = bundle
                        navController.navigate(R.id.verifyForgetPasswordFragment2, bundle)
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
                Constant.baik
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