package id.telkomsel.merchant.ui.pelanggan.account

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
import id.telkomsel.merchant.model.ModelPelanggan
import id.telkomsel.merchant.model.response.ModelResponse
import id.telkomsel.merchant.model.response.ModelResponsePelanggan
import id.telkomsel.merchant.ui.pelanggan.PelangganActivity
import id.telkomsel.merchant.utils.Constant
import id.telkomsel.merchant.utils.Constant.attention
import id.telkomsel.merchant.utils.DataSave
import id.telkomsel.merchant.utils.RetrofitUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("StaticFieldLeak")
class AccountPelangganViewModel(
    private val savedData: DataSave?,
    private val activity: Activity?,
    private val navController: NavController,
    private val context: Context?
) : BaseViewModel() {
    val dataPelanggan = MutableLiveData<ModelPelanggan>()

    fun getDataPelanggan(username: String){
        isShowLoading.value = true

        RetrofitUtils.getDataPelanggan(username, object : Callback<ModelResponsePelanggan> {
            override fun onResponse(
                call: Call<ModelResponsePelanggan>,
                response: Response<ModelResponsePelanggan>
            ) {
                isShowLoading.value = false
                val result = response.body()

                if (result?.message == Constant.reffSuccess){
                    savedData?.setDataObject(result.data, Constant.reffPelanggan)
                }
                else{
                    message.value = "Error, gagal mendapatkan data terbaru"
                }
            }

            override fun onFailure(
                call: Call<ModelResponsePelanggan>,
                t: Throwable
            ) {
                isShowLoading.value = false
                message.value = "Error, gagal mendapatkan data terbaru"
            }
        })
    }

    private fun removeToken(username: String) {
        isShowLoading.value = true

        RetrofitUtils.logoutPelanggan(username, object : Callback<ModelResponse> {
            override fun onResponse(
                call: Call<ModelResponse>,
                response: Response<ModelResponse>
            ) {
                isShowLoading.value = false
                val result = response.body()

                if (result?.message == Constant.reffSuccess){
                    Toast.makeText(context, "Berhasil Keluar", Toast.LENGTH_LONG).show()

                    savedData?.setDataObject(ModelPelanggan(), Constant.reffPelanggan)

                    val intent = Intent(context, PelangganActivity::class.java)
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
        navController.navigate(R.id.editProfilPelangganFragment)
    }

    fun onClickEditPassword(){
        navController.navigate(R.id.editPasswordPelangganFragment)
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
                val username = savedData?.getDataPelanggan()?.username
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