package id.telkomsel.merchant.ui.auth.login

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseViewModel
import id.telkomsel.merchant.model.ModelMerchant
import id.telkomsel.merchant.ui.main.MainActivity
import id.telkomsel.merchant.utils.Constant
import id.telkomsel.merchant.utils.DataSave
import id.telkomsel.merchant.utils.adapter.dismissKeyboard

@SuppressLint("StaticFieldLeak")
class LoginViewModel(
    private val navController: NavController,
    private val savedData: DataSave?,
     private val activity: Activity?) : BaseViewModel() {
    val userName = MutableLiveData<String>()
    val dataUser = MutableLiveData<ModelMerchant>()

    fun onClickBack(){
        activity?.let { dismissKeyboard(it) }
        navController.navigate(R.id.splashFragment)
    }

    fun onClickRegister(){
        activity?.let { dismissKeyboard(it) }
        checkPermission(activity)
    }

    fun onClickLogin(){
        activity?.let { dismissKeyboard(it) }
        val intent = Intent(activity, MainActivity::class.java)
        activity?.startActivity(intent)
        activity?.finish()
    }

    private fun checkPermission(context: Context?) {
        context?.let {
            if (ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    it, Manifest.permission.ACCESS_COARSE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                navController.navigate(R.id.registerMerchantFragment)
            }
            else {
                message.value = "Anda belum mengizinkan akses lokasi aplikasi ini"

                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    Constant.codeRequestLocation
                )
            }
        }
    }
}