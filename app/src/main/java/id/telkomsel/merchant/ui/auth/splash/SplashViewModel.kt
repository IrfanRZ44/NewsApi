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
import id.telkomsel.merchant.model.ModelPelanggan
import id.telkomsel.merchant.model.response.ModelResponse
import id.telkomsel.merchant.model.response.ModelResponseInfoApps
import id.telkomsel.merchant.model.response.ModelResponseMerchant
import id.telkomsel.merchant.ui.auth.AuthActivity
import id.telkomsel.merchant.ui.merchant.MerchantActivity
import id.telkomsel.merchant.ui.pelanggan.PelangganActivity
import id.telkomsel.merchant.utils.Constant
import id.telkomsel.merchant.utils.DataSave
import id.telkomsel.merchant.utils.RetrofitUtils
import id.telkomsel.merchant.utils.adapter.getDate
import id.telkomsel.merchant.utils.adapter.showLog
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
                val username = savedData?.getDataMerchant()?.username
                val token = savedData?.getDataMerchant()?.token
                val level = savedData?.getKeyString(Constant.level)

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
                    when (level) {
                        Constant.levelPelanggan -> {
                            val userPelanggan = savedData?.getDataPelanggan()?.username
                            val tokenPelanggan = savedData?.getDataPelanggan()?.token

                            if (!userPelanggan.isNullOrEmpty() && !tokenPelanggan.isNullOrEmpty()){
                                val tempSaved = savedData?.getDataApps()

                                if (tempSaved != null && tempSaved.lastOnline.isNotEmpty()){
                                    if(comparingTimesAfter(tempSaved.lastOnline) >= 2){
                                        removeTokenPelanggan(userPelanggan)
                                    }
                                    else{
                                        tempSaved.lastOnline = getDate(Constant.dateFormat1)
                                        savedData?.setDataObject(tempSaved, Constant.reffInfoApps)

                                        val intent = Intent(activity, PelangganActivity::class.java)
                                        activity?.startActivity(intent)
                                        activity?.finish()
                                    }
                                }
                                else{
                                    tempSaved?.lastOnline = getDate(Constant.dateFormat1)
                                    savedData?.setDataObject(tempSaved, Constant.reffInfoApps)

                                    val intent = Intent(activity, PelangganActivity::class.java)
                                    activity?.startActivity(intent)
                                    activity?.finish()
                                }
                            }
                            else{
                                val intent = Intent(activity, PelangganActivity::class.java)
                                activity?.startActivity(intent)
                                activity?.finish()
                            }
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

    @SuppressLint("SimpleDateFormat")
    private fun comparingTimesAfter(waktuMulai: String) : Int {
        val sdf = SimpleDateFormat(Constant.dateFormat1)
        val dateLast = sdf.parse(waktuMulai)

        return Days.daysBetween(LocalDate(dateLast), LocalDate()).days
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

                if (result?.message == Constant.reffSuccess) {
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
                } else {
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

    private fun removeTokenPelanggan(username: String) {
        showLog("Logout")
        isShowLoading.value = true

        RetrofitUtils.logoutPelanggan(username, object : Callback<ModelResponse> {
            override fun onResponse(
                call: Call<ModelResponse>,
                response: Response<ModelResponse>
            ) {
                isShowLoading.value = false
                val result = response.body()

                if (result?.message == Constant.reffSuccess){
                    Toast.makeText(activity, "Logout otomatis dikarenakan Anda tidak aktif dalam 2 hari terakhir", Toast.LENGTH_LONG).show()

                    savedData?.setDataObject(ModelPelanggan(), Constant.reffPelanggan)
                    val dataApps = savedData?.getDataApps()
                    dataApps?.lastOnline = ""
                    savedData?.setDataObject(dataApps, Constant.reffInfoApps)

                    val intent = Intent(activity, PelangganActivity::class.java)
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
                    Toast.makeText(activity, "Logout otomatis dikarenakan Anda tidak aktif dalam 2 hari terakhir", Toast.LENGTH_LONG).show()

                    savedData?.setDataObject(ModelMerchant(), Constant.reffMerchant)
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