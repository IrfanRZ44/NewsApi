package id.telkomsel.merchant.ui.auth.changePassword

import android.annotation.SuppressLint
import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.google.android.material.textfield.TextInputLayout
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseViewModel
import id.telkomsel.merchant.model.ModelMerchant
import id.telkomsel.merchant.model.response.ModelResponse
import id.telkomsel.merchant.utils.Constant
import id.telkomsel.merchant.utils.RetrofitUtils
import id.telkomsel.merchant.utils.adapter.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("StaticFieldLeak")
class ChangePasswordViewModel(
    private val activity: Activity?,
    private val navController: NavController,
    private val editPasswordNew: TextInputLayout,
    private val editConfirmPasswordNew: TextInputLayout
) : BaseViewModel() {
    lateinit var dataMerchant : ModelMerchant
    val etPasswordNew = MutableLiveData<String>()
    val etConfirmPasswordNew = MutableLiveData<String>()

    private fun setTextError(msg: String, editText: TextInputLayout){
        message.value = msg
        editText.error = msg
        editText.requestFocus()
        editText.findFocus()
    }

    private fun setNullError(){
        editPasswordNew.error = null
        editConfirmPasswordNew.error = null
    }

    fun onClickEditProfil(){
        setNullError()
        activity?.let { dismissKeyboard(it) }

        val idMerchant = dataMerchant.id
        val passwordNew = etPasswordNew.value
        val confirmPasswordNew = etConfirmPasswordNew.value

        if (!passwordNew.isNullOrEmpty() && !confirmPasswordNew.isNullOrEmpty() && passwordNew == confirmPasswordNew && passwordNew.length >= 6 && isContainNumber(passwordNew) && (isContainSmallText(passwordNew) || isContainBigText(passwordNew))
        ) {
            val md5PasswordNew = stringToMD5(passwordNew)

            isShowLoading.value = true
            updatePassword(idMerchant, md5PasswordNew)
        }
        else {
            if (passwordNew.isNullOrEmpty()) {
                setTextError("Error, mohon masukkan password baru", editPasswordNew)
            }
            else if (confirmPasswordNew.isNullOrEmpty()) {
                setTextError("Error, mohon masukkan konfirmasi password baru", editConfirmPasswordNew)
            }
            else if (passwordNew != confirmPasswordNew) {
                setTextError("Error, password yang Anda masukkan berbeda", editConfirmPasswordNew)
            }
            else if (passwordNew.length < 6){
                setTextError("Error, password harus minimal 6 digit", editPasswordNew)
            }
            else if (!isContainNumber(passwordNew)){
                setTextError("Error, password harus memiliki kombinasi angka", editPasswordNew)
            }
            else if (!isContainSmallText(passwordNew) && !isContainBigText(passwordNew)){
                setTextError("Error, password harus memiliki kombinasi huruf", editPasswordNew)
            }
        }
    }

    private fun updatePassword(idMerchant: Int, passwordNew: String){
        RetrofitUtils.updatePassword(idMerchant, passwordNew,
            object : Callback<ModelResponse> {
                override fun onResponse(
                    call: Call<ModelResponse>,
                    response: Response<ModelResponse>
                ) {
                    isShowLoading.value = false

                    val result = response.body()

                    if (result?.message == Constant.reffSuccess){
                        activity?.let { dismissKeyboard(it) }
                        message.value = "Berhasil mengganti password"
                        navController.navigate(R.id.loginMerchantFragment)
                    }
                    else{
                        message.value = result?.message
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
}