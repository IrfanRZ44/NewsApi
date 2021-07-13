package id.telkomsel.merchant.ui.merchant.accountMerchant

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseViewModel
import id.telkomsel.merchant.model.ModelMerchant
import id.telkomsel.merchant.model.response.ModelResponse
import id.telkomsel.merchant.model.response.ModelResponseMerchant
import id.telkomsel.merchant.ui.auth.AuthActivity
import id.telkomsel.merchant.utils.Constant
import id.telkomsel.merchant.utils.Constant.attention
import id.telkomsel.merchant.utils.DataSave
import id.telkomsel.merchant.utils.RetrofitUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("StaticFieldLeak")
class AccountMerchantViewModel(
    private val savedData: DataSave?,
    private val activity: Activity?,
    private val navController: NavController,
    private val context: Context?
) : BaseViewModel() {
    val dataMerchant = MutableLiveData<ModelMerchant>()

    fun getDataMerchant(username: String){
        isShowLoading.value = true

        RetrofitUtils.getDataMerchant(username, object : Callback<ModelResponseMerchant> {
            override fun onResponse(
                call: Call<ModelResponseMerchant>,
                response: Response<ModelResponseMerchant>
            ) {
                isShowLoading.value = false
                val result = response.body()

                if (result?.message == Constant.reffSuccess){
                    savedData?.setDataObject(result.dataMerchant, Constant.reffMerchant)
                }
                else{
                    message.value = "Error, gagal mendapatkan data terbaru"
                }
            }

            override fun onFailure(
                call: Call<ModelResponseMerchant>,
                t: Throwable
            ) {
                isShowLoading.value = false
                message.value = "Error, gagal mendapatkan data terbaru"
            }
        })
    }

    private fun removeToken(username: String) {
        isShowLoading.value = true

        RetrofitUtils.logoutMerchant(username, object : Callback<ModelResponse> {
            override fun onResponse(
                call: Call<ModelResponse>,
                response: Response<ModelResponse>
            ) {
                isShowLoading.value = false
                val result = response.body()

                if (result?.message == Constant.reffSuccess){
                    Toast.makeText(context, "Berhasil Keluar", Toast.LENGTH_LONG).show()

                    savedData?.setDataObject(ModelMerchant(), Constant.reffMerchant)

                    val intent = Intent(context, AuthActivity::class.java)
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

    fun onClickEditProfil(){
        if (savedData?.getDataMerchant()?.level == Constant.levelMerchant){
            navController.navigate(R.id.editProfilMerchantFragment)
        }
        else{
            navController.navigate(R.id.editProfilAdminFragment)
        }
    }

    fun onClickEditPassword(){
        navController.navigate(R.id.editPasswordAdminFragment)
    }

    fun onClickRating(){
        activity?.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=${activity.packageName}")
            )
        )
    }

    fun onClickLogout(){
        val ctx = context

        if (ctx != null){
            val alert = AlertDialog.Builder(ctx)
            alert.setTitle(attention)
            alert.setMessage(Constant.alertLogout)
            alert.setPositiveButton(
                Constant.iya
            ) { _, _ ->
                val username = savedData?.getDataMerchant()?.username
                if (!username.isNullOrEmpty()){
                    removeToken(username)
                }
            }
            alert.setNegativeButton(
                Constant.tidak
            ) { dialog, _ -> dialog.dismiss() }

            alert.show()
        }
        else {
            message.value = "Error, terjadi kesalahan yang tidak diketahui"
        }
    }
}