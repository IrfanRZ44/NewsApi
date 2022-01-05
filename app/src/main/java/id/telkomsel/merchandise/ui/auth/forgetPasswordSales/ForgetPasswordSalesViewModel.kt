package id.telkomsel.merchandise.ui.auth.forgetPasswordSales

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.google.android.material.textfield.TextInputLayout
import id.telkomsel.merchandise.R
import id.telkomsel.merchandise.base.BaseViewModel
import id.telkomsel.merchandise.model.response.ModelResponseSales
import id.telkomsel.merchandise.ui.auth.verifyForgetPassword.VerifyForgetPasswordFragment
import id.telkomsel.merchandise.utils.Constant
import id.telkomsel.merchandise.utils.RetrofitUtils
import id.telkomsel.merchandise.utils.adapter.dismissKeyboard
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("StaticFieldLeak")
class ForgetPasswordSalesViewModel(
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
                forgetPasswordSalesPhone(phone)
            }
            else{
                forgetPasswordSalesUsername(textInput)
            }
        }
        else {
            message.value = "Mohon masukkan username"
            editUsername.error = "Mohon masukkan username"
            editUsername.requestFocus()
            editUsername.findFocus()
        }
    }

    private fun forgetPasswordSalesUsername(username: String){
        RetrofitUtils.forgetPasswordSalesUsername(username,
            object : Callback<ModelResponseSales> {
                override fun onResponse(
                    call: Call<ModelResponseSales>,
                    response: Response<ModelResponseSales>
                ) {
                    isShowLoading.value = false
                    val result = response.body()
                    val dataSales = result?.dataSales

                    if (result?.message == Constant.reffSuccess && dataSales != null){
                        val bundle = Bundle()
                        val fragmentTujuan = VerifyForgetPasswordFragment()
                        bundle.putParcelable(Constant.reffSales, dataSales)
                        fragmentTujuan.arguments = bundle
                        navController.navigate(R.id.verifyForgetPasswordFragment, bundle)
                    }
                    else{
                        dialogWaitVerify(result?.message?:"Error, terjadi kesalahan database")
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

    private fun forgetPasswordSalesPhone(phone: String){
        RetrofitUtils.forgetPasswordSalesPhone(phone,
            object : Callback<ModelResponseSales> {
                override fun onResponse(
                    call: Call<ModelResponseSales>,
                    response: Response<ModelResponseSales>
                ) {
                    isShowLoading.value = false
                    val result = response.body()
                    val dataSales = result?.dataSales

                    if (result?.message == Constant.reffSuccess && dataSales != null){
                        val bundle = Bundle()
                        val fragmentTujuan = VerifyForgetPasswordFragment()
                        bundle.putParcelable(Constant.reffSales, dataSales)
                        fragmentTujuan.arguments = bundle
                        navController.navigate(R.id.verifyForgetPasswordFragment, bundle)
                    }
                    else{
                        dialogWaitVerify(result?.message?:"Error, terjadi kesalahan database")
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