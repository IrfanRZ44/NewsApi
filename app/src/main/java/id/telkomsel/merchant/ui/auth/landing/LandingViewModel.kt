package id.telkomsel.merchant.ui.auth.landing

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.navigation.NavController
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseViewModel
import id.telkomsel.merchant.ui.pelanggan.PelangganActivity
import id.telkomsel.merchant.utils.Constant
import id.telkomsel.merchant.utils.DataSave

@SuppressLint("StaticFieldLeak")
class LandingViewModel(
    private val navController: NavController,
    private val activity: Activity?,
    private val savedData: DataSave
) : BaseViewModel() {

    fun onClickMerchant(){
        navController.navigate(R.id.loginMerchantFragment)
    }

    fun onClickUser(){
        val intent = Intent(activity, PelangganActivity::class.java)
        savedData.setDataString(Constant.levelPelanggan, Constant.level)
        activity?.startActivity(intent)
        activity?.finish()
    }
}