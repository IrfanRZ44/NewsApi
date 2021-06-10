package id.telkomsel.merchant.ui.auth.landing

import androidx.navigation.NavController
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseViewModel

class LandingViewModel(
    private val navController: NavController
) : BaseViewModel() {

    fun onClickMerchant(){
        navController.navigate(R.id.loginMerchantFragment)
    }

    fun onClickUser(){
//        navController.navigate(R.id.loginUserFragment)
    }
}