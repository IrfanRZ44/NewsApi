package id.telkomsel.merchandise.ui.sales.accountSales

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import id.telkomsel.merchandise.R
import id.telkomsel.merchandise.base.BaseViewModel
import id.telkomsel.merchandise.model.ModelSales
import id.telkomsel.merchandise.model.response.ModelResponse
import id.telkomsel.merchandise.model.response.ModelResponseSales
import id.telkomsel.merchandise.ui.auth.AuthActivity
import id.telkomsel.merchandise.utils.Constant
import id.telkomsel.merchandise.utils.Constant.attention
import id.telkomsel.merchandise.utils.DataSave
import id.telkomsel.merchandise.utils.RetrofitUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("StaticFieldLeak")
class AccountSalesViewModel(
    private val savedData: DataSave?,
    private val activity: Activity?,
    private val navController: NavController,
    private val context: Context?
) : BaseViewModel() {
    val dataSales = MutableLiveData<ModelSales>()

    fun getDataSales(username: String){
        isShowLoading.value = true

        RetrofitUtils.getDataSales(username, object : Callback<ModelResponseSales> {
            override fun onResponse(
                call: Call<ModelResponseSales>,
                response: Response<ModelResponseSales>
            ) {
                isShowLoading.value = false
                val result = response.body()

                if (result?.message == Constant.reffSuccess){
                    savedData?.setDataObject(result.dataSales, Constant.reffSales)
                }
                else{
                    message.value = "Error, gagal mendapatkan data terbaru"
                }
            }

            override fun onFailure(
                call: Call<ModelResponseSales>,
                t: Throwable
            ) {
                isShowLoading.value = false
                message.value = "Error, gagal mendapatkan data terbaru"
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
                    Toast.makeText(context, "Berhasil Keluar", Toast.LENGTH_LONG).show()
                    savedData?.setDataObject(ModelSales(), Constant.reffSales)
                    val dataApps = savedData?.getDataApps()
                    dataApps?.lastOnline = ""
                    savedData?.setDataObject(dataApps, Constant.reffInfoApps)

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
        if (savedData?.getDataSales()?.level == Constant.levelSales){
            navController.navigate(R.id.editProfilSalesFragment)
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
                val username = savedData?.getDataSales()?.username
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