package id.telkomsel.merchant.ui.auth

import android.view.View
import androidx.navigation.fragment.NavHostFragment
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseActivity
import kotlinx.android.synthetic.main.activity_auth.*

@Suppress("DEPRECATION")
class AuthActivity : BaseActivity() {
    override fun getLayoutResource(): Int = R.layout.activity_auth

    override fun myCodeHere() {
        NavHostFragment.create(R.navigation.auth_nav)
        viewParent.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }
}
