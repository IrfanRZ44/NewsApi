package id.telkomsel.merchant.ui.auth.forgetPasswordMerchant

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.google.android.material.textfield.TextInputLayout
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseViewModel
import id.telkomsel.merchant.model.response.ModelResponseMerchant
import id.telkomsel.merchant.ui.auth.verifyForgetPassword.VerifyForgetPasswordFragment
import id.telkomsel.merchant.utils.Constant
import id.telkomsel.merchant.utils.RetrofitUtils
import id.telkomsel.merchant.utils.adapter.dismissKeyboard
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("StaticFieldLeak")
class ForgetPasswordMerchantViewModel(
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
                forgetPasswordMerchantPhone(phone)
            }
            else{
                forgetPasswordMerchantUsername(textInput)
            }
        }
        else {
            message.value = "Mohon masukkan username"
            editUsername.error = "Mohon masukkan username"
            editUsername.requestFocus()
            editUsername.findFocus()
        }
    }

    private fun forgetPasswordMerchantUsername(username: String){
        RetrofitUtils.forgetPasswordMerchantUsername(username,
            object : Callback<ModelResponseMerchant> {
                override fun onResponse(
                    call: Call<ModelResponseMerchant>,
                    response: Response<ModelResponseMerchant>
                ) {
                    isShowLoading.value = false
                    val result = response.body()
                    val dataMerchant = result?.dataMerchant

                    if (result?.message == Constant.reffSuccess && dataMerchant != null){
                        val bundle = Bundle()
                        val fragmentTujuan = VerifyForgetPasswordFragment()
                        bundle.putParcelable(Constant.reffMerchant, dataMerchant)
                        fragmentTujuan.arguments = bundle
                        navController.navigate(R.id.verifyForgetPasswordFragment, bundle)
                    }
                    else{
                        dialogWaitVerify(result?.message?:"Error, terjadi kesalahan database")
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

    private fun forgetPasswordMerchantPhone(phone: String){
        RetrofitUtils.forgetPasswordMerchantPhone(phone,
            object : Callback<ModelResponseMerchant> {
                override fun onResponse(
                    call: Call<ModelResponseMerchant>,
                    response: Response<ModelResponseMerchant>
                ) {
                    isShowLoading.value = false
                    val result = response.body()
                    val dataMerchant = result?.dataMerchant

                    if (result?.message == Constant.reffSuccess && dataMerchant != null){
                        val bundle = Bundle()
                        val fragmentTujuan = VerifyForgetPasswordFragment()
                        bundle.putParcelable(Constant.reffMerchant, dataMerchant)
                        fragmentTujuan.arguments = bundle
                        navController.navigate(R.id.verifyForgetPasswordFragment, bundle)
                    }
                    else{
                        dialogWaitVerify(result?.message?:"Error, terjadi kesalahan database")
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