package id.telkomsel.merchandise.ui.auth.splash

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.CountDownTimer
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import id.telkomsel.merchandise.R
import id.telkomsel.merchandise.base.BaseViewModel
import id.telkomsel.merchandise.model.ModelSales
import id.telkomsel.merchandise.model.response.ModelResponse
import id.telkomsel.merchandise.model.response.ModelResponseInfoApps
import id.telkomsel.merchandise.model.response.ModelResponseSales
import id.telkomsel.merchandise.ui.auth.AuthActivity
import id.telkomsel.merchandise.ui.sales.SalesActivity
import id.telkomsel.merchandise.utils.Constant
import id.telkomsel.merchandise.utils.DataSave
import id.telkomsel.merchandise.utils.RetrofitUtils
import id.telkomsel.merchandise.utils.adapter.getDate
import org.joda.time.Days
import org.joda.time.LocalDate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

@SuppressLint("StaticFieldLeak")
class SplashViewModel(
    private val navController: NavController,
    private val savedData: DataSave?,
    private val activity: Activity?
) : BaseViewModel() {
    val isShowUpdate = MutableLiveData<Boolean>()
    var linkDownload = ""

    private fun checkSavedData(){
        isShowLoading.value = true
        object : CountDownTimer(2000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                isShowLoading.value = false
                val username = savedData?.getDataSales()?.username
                val token = savedData?.getDataSales()?.token

                if (!username.isNullOrEmpty() && !token.isNullOrEmpty()){
                    val tempSaved = savedData?.getDataApps()

                    if (tempSaved != null && tempSaved.lastOnline.isNotEmpty()){
                        if(comparingTimesAfter(tempSaved.lastOnline) >= 2){
                            removeToken(username)
                        }
                        else{
                            tempSaved.lastOnline = getDate(Constant.dateFormat1)
                            savedData?.setDataObject(tempSaved, Constant.reffInfoApps)
                            checkToken(username, token)
                        }
                    }
                    else{
                        tempSaved?.lastOnline = getDate(Constant.dateFormat1)
                        savedData?.setDataObject(tempSaved, Constant.reffInfoApps)
                        checkToken(username, token)
                    }
                }
                else{
                    navController.navigate(R.id.loginSalesFragment)
                }
            }
        }.start()
    }

    @SuppressLint("SimpleDateFormat")
    private fun comparingTimesAfter(waktuMulai: String) : Int {
        val sdf = SimpleDateFormat(Constant.dateFormat1)
        val dateLast = sdf.parse(waktuMulai)

        return Days.daysBetween(LocalDate(dateLast), LocalDate()).days
    }

    private fun checkToken(username: String, token: String){
        isShowLoading.value = true

        RetrofitUtils.checkToken(username, token, object : Callback<ModelResponseSales> {
            override fun onResponse(
                call: Call<ModelResponseSales>,
                response: Response<ModelResponseSales>
            ) {
                isShowLoading.value = false
                val result = response.body()

                if (result?.message == Constant.reffSuccess) {
                    savedData?.setDataObject(result.dataSales, Constant.reffSales)

                    when (savedData?.getDataSales()?.level) {
                        Constant.levelSales, Constant.levelDLS, Constant.levelChannel -> {
                            val intent = Intent(activity, SalesActivity::class.java)
                            activity?.startActivity(intent)
                            activity?.finish()
                        }
                        else -> {
                            navController.navigate(R.id.loginSalesFragment)
                        }
                    }
                } else {
                    Toast.makeText(activity, result?.message, Toast.LENGTH_LONG).show()
                    savedData?.setDataObject(ModelSales(), Constant.reffSales)
                    navController.navigate(R.id.loginSalesFragment)
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
                    Toast.makeText(activity, "Logout otomatis dikarenakan Anda tidak aktif dalam 2 hari terakhir", Toast.LENGTH_LONG).show()

                    savedData?.setDataObject(ModelSales(), Constant.reffSales)
                    val dataApps = savedData?.getDataApps()
                    dataApps?.lastOnline = ""
                    savedData?.setDataObject(dataApps, Constant.reffInfoApps)

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

                if (result?.message == Constant.reffSuccess && act != null) {
                    val manager = act.packageManager
                    val info = manager?.getPackageInfo(
                        act.packageName, 0
                    )
                    val versionApps = info?.versionName

                    if (result.dataApps?.version_apps == versionApps) {
                        val tempSaved = savedData?.getDataApps()
                        if (tempSaved != null && tempSaved.lastOnline.isNotEmpty()) {
                            result.dataApps?.lastOnline = tempSaved.lastOnline
                        }
                        savedData?.setDataObject(result.dataApps, Constant.reffInfoApps)
                        checkSavedData()
                    } else {
                        message.value =
                            "Terjadi kesalahan, mohon update versi aplikasi ke ${result.dataApps?.version_apps}"
                        isShowUpdate.value = true
                        try {
                            linkDownload = result.dataApps?.link
                                ?: throw Exception("Terjadi kesalahan, gagal mendapatkan link aplikasi")
                        } catch (e: Exception) {
                            message.value = e.message
                        }
                    }
                } else {
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
                if (msg != null && msg.contains("Failed to connect to")) {
                    message.value = "Terjadi kesalahan, mohon periksa koneksi internet Anda"
                } else {
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
                val defaultBrowser = Intent.makeMainSelectorActivity(
                    Intent.ACTION_MAIN,
                    Intent.CATEGORY_APP_BROWSER
                )
                defaultBrowser.data = Uri.parse(linkDownload)
                activity?.startActivity(defaultBrowser)
            }
        }catch (e: Exception){
            message.value = e.message
        }
    }
}