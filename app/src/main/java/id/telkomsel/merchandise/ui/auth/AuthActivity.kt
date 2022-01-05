package id.telkomsel.merchandise.ui.auth

import android.view.View
import androidx.navigation.fragment.NavHostFragment
import id.telkomsel.merchandise.R
import id.telkomsel.merchandise.base.BaseActivity
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
