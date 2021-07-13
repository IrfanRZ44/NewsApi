package id.telkomsel.merchant.ui.auth.splash

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.CountDownTimer
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseViewModel
import id.telkomsel.merchant.model.ModelMerchant
import id.telkomsel.merchant.model.response.ModelResponseInfoApps
import id.telkomsel.merchant.model.response.ModelResponseMerchant
import id.telkomsel.merchant.ui.merchant.MerchantActivity
import id.telkomsel.merchant.ui.pelanggan.PelangganActivity
import id.telkomsel.merchant.utils.Constant
import id.telkomsel.merchant.utils.DataSave
import id.telkomsel.merchant.utils.RetrofitUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

@SuppressLint("StaticFieldLeak")
class SplashViewModel(
    private val navController: NavController,
    private val savedData: DataSave?,
    private val activity: Activity?) : BaseViewModel() {
    val isShowUpdate = MutableLiveData<Boolean>()
    var linkDownload = ""

    private fun checkSavedData(){
        isShowLoading.value = true
        object : CountDownTimer(2000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                isShowLoading.value = false
                val username = savedData?.getDataMerchant()?.username
                val token = savedData?.getDataMerchant()?.token
                val level = savedData?.getKeyString(Constant.level)

                if (!username.isNullOrEmpty() && !token.isNullOrEmpty()){
                    checkToken(username, token)
                }
                else{
                    when (level) {
                        Constant.levelPelanggan -> {
                            val intent = Intent(activity, PelangganActivity::class.java)
                            activity?.startActivity(intent)
                            activity?.finish()
                        }
                        Constant.levelMerchant -> {
                            savedData?.setDataString("", Constant.level)
                            navController.navigate(R.id.loginMerchantFragment)
                        }
                        else -> {
                            navController.navigate(R.id.landingFragment)
                        }
                    }
                }
            }
        }.start()
    }

    private fun checkToken(username: String, token: String){
        isShowLoading.value = true

        RetrofitUtils.checkToken(username, token, object : Callback<ModelResponseMerchant> {
            override fun onResponse(
                call: Call<ModelResponseMerchant>,
                response: Response<ModelResponseMerchant>
            ) {
                isShowLoading.value = false
                val result = response.body()

                if (result?.message == Constant.reffSuccess){
                    savedData?.setDataObject(result.dataMerchant, Constant.reffMerchant)

                    when (savedData?.getDataMerchant()?.level) {
                        Constant.levelMerchant, Constant.levelCSO, Constant.levelSBP, Constant.levelChannel -> {
                            val intent = Intent(activity, MerchantActivity::class.java)
                            activity?.startActivity(intent)
                            activity?.finish()
                        }
                        else -> {
                            navController.navigate(R.id.landingFragment)
                        }
                    }
                }
                else{
                    Toast.makeText(activity, result?.message, Toast.LENGTH_LONG).show()
                    savedData?.setDataObject(ModelMerchant(), Constant.reffMerchant)
                    navController.navigate(R.id.landingFragment)
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

    fun getInfoApps(){
        isShowLoading.value = true

        RetrofitUtils.getInfoApps(object : Callback<ModelResponseInfoApps> {
                override fun onResponse(
                    call: Call<ModelResponseInfoApps>,
                    response: Response<ModelResponseInfoApps>
                ) {
                    isShowLoading.value = false
                    val result = response.body()
                    val act = activity

                    if (result?.message == Constant.reffSuccess && act != null){
                        val manager = act.packageManager
                        val info = manager?.getPackageInfo(
                            act.packageName, 0
                        )
                        val versionApps = info?.versionName

                        if (result.dataApps?.version_apps == versionApps){
                            savedData?.setDataObject(result.dataApps, Constant.reffInfoApps)
                            checkSavedData()
                        }
                        else{
                            message.value = "Terjadi kesalahan, mohon update versi aplikasi ke ${result.dataApps?.version_apps}"
                            isShowUpdate.value = true
                            try {
                                linkDownload = result.dataApps?.link?:throw Exception("Terjadi kesalahan, gagal mendapatkan link aplikasi")
                            }catch (e: Exception){
                                message.value = e.message
                            }
                        }
                    }
                    else{
                        message.value = result?.message
                        isShowUpdate.value = true
                    }
                }

                override fun onFailure(
                    call: Call<ModelResponseInfoApps>,
                    t: Throwable
                ) {
                    isShowLoading.value = false
                    val msg = t.message
                    if (msg != null && msg.contains("Failed to connect to")){
                        message.value = "Terjadi kesalahan, mohon periksa koneksi internet Anda"
                    }
                    else{
                        message.value = t.message
                    }
                }
            })
    }

    fun onClickUpdate(){
        try {
            if (linkDownload.isEmpty()){
                message.value = "Terjadi kesalahan, link download aplikasi tidak ditemukan"
            }
            else{
                val defaultBrowser = Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_BROWSER)
                defaultBrowser.data = Uri.parse(linkDownload)
                activity?.startActivity(defaultBrowser)
            }
        }catch (e: Exception){
            message.value = e.message
        }
    }
}