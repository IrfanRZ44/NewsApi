package id.telkomsel.merchant.ui.merchant

import android.view.View
import androidx.navigation.fragment.NavHostFragment
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseActivity
import kotlinx.android.synthetic.main.activity_merchant.*

class MerchantActivity : BaseActivity() {
    override fun getLayoutResource(): Int = R.layout.activity_merchant

    @Suppress("DEPRECATION")
    override fun myCodeHere() {
        NavHostFragment.create(R.navigation.merchant_nav)
        viewParent.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}