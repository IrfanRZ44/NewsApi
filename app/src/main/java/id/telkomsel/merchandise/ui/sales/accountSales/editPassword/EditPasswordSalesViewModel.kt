package id.telkomsel.merchandise.ui.sales.accountSales.editPassword

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.android.material.textfield.TextInputLayout
import id.telkomsel.merchandise.base.BaseViewModel
import id.telkomsel.merchandise.model.ModelSales
import id.telkomsel.merchandise.model.response.ModelResponse
import id.telkomsel.merchandise.ui.auth.AuthActivity
import id.telkomsel.merchandise.utils.Constant
import id.telkomsel.merchandise.utils.DataSave
import id.telkomsel.merchandise.utils.RetrofitUtils
import id.telkomsel.merchandise.utils.adapter.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("StaticFieldLeak")
class EditPasswordSalesViewModel(
    private val savedData: DataSave,
    private val activity: Activity?,
    private val editPasswordOld: TextInputLayout,
    private val editPasswordNew: TextInputLayout,
    private val editConfirmPasswordNew: TextInputLayout
) : BaseViewModel() {
    val etPasswordOld = MutableLiveData<String>()
    val etPasswordNew = MutableLiveData<String>()
    val etConfirmPasswordNew = MutableLiveData<String>()

    private fun setTextError(msg: String, editText: TextInputLayout){
        message.value = msg
        editText.error = msg
        editText.requestFocus()
        editText.findFocus()
    }

    private fun setNullError(){
        editPasswordOld.error = null
        editPasswordNew.error = null
        editConfirmPasswordNew.error = null
    }

    fun onClickEditProfil(){
        setNullError()
        activity?.let { dismissKeyboard(it) }

        val idSales = savedData.getDataSales()?.id
        val passwordOld = etPasswordOld.value
        val passwordNew = etPasswordNew.value
        val confirmPasswordNew = etConfirmPasswordNew.value


        if (idSales != null && !passwordOld.isNullOrEmpty()
            && !passwordNew.isNullOrEmpty() && !confirmPasswordNew.isNullOrEmpty()
            && passwordNew == confirmPasswordNew
            && passwordNew.length >= 6 && isContainNumber(passwordNew) && (isContainSmallText(passwordNew) || isContainBigText(passwordNew))
        ) {
            val md5PasswordOld = savedData.getDataSales()?.password
            val md5InputPasswordOld = stringToMD5(passwordOld)
            val md5PasswordNew = stringToMD5(passwordNew)

            if (md5PasswordOld == md5InputPasswordOld){
                isShowLoading.value = true
                updatePassword(idSales, md5PasswordNew)
            }
            else{
                setTextError("Error, password lama yang Anda masukkan salah", editPasswordOld)
            }
        }
        else {
            if (idSales == null) {
                message.value = "Error, terjadi kesalahan saat mengambil ID Sales"
            }
            else if (passwordNew.isNullOrEmpty()) {
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

    private fun updatePassword(id: Int, passwordNew: String){
        RetrofitUtils.updatePasswordSales(id, passwordNew,
            object : Callback<ModelResponse> {
                override fun onResponse(
                    call: Call<ModelResponse>,
                    response: Response<ModelResponse>
                ) {
                    isShowLoading.value = false

                    val result = response.body()
                    val username = savedData.getDataSales()?.username

                    showLog("On Response")

                    if (result?.message == Constant.reffSuccess && !username.isNullOrEmpty()){
                        removeToken(username)
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

    private fun removeToken(username: String) {
        isShowLoading.value = true

        RetrofitUtils.logoutSales(username, object : Callback<ModelResponse> {
            override fun onResponse(
                call: Call<ModelResponse>,
                response: Response<ModelResponse>
            ) {
                isShowLoading.value = false
                val result = response.body()

                if (result?.message == Constant.reffSuccess){
                    Toast.makeText(activity, "Berhasil mengganti password", Toast.LENGTH_LONG).show()

                    savedData.setDataObject(ModelSales(), Constant.reffSales)

                    val intent = Intent(activity, AuthActivity::class.java)
                    activity?.startActivity(intent)
                    activity?.finish()
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