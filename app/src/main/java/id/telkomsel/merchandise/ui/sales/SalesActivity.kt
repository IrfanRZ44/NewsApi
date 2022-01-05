package id.telkomsel.merchandise.ui.sales

import android.view.View
import androidx.navigation.fragment.NavHostFragment
import id.telkomsel.merchandise.R
import id.telkomsel.merchandise.base.BaseActivity
import kotlinx.android.synthetic.main.activity_sales.*

class SalesActivity : BaseActivity() {
    override fun getLayoutResource(): Int = R.layout.activity_sales

    @Suppress("DEPRECATION")
    override fun myCodeHere() {
        NavHostFragment.create(R.navigation.sales_nav)
        viewParent.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}