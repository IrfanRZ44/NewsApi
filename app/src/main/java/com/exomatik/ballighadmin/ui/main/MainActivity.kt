package com.exomatik.ballighadmin.ui.main

import android.view.View
import androidx.navigation.fragment.NavHostFragment
import com.exomatik.ballighadmin.R
import com.exomatik.ballighadmin.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    override fun getLayoutResource(): Int = R.layout.activity_main

    override fun myCodeHere() {
        NavHostFragment.create(R.navigation.main_nav)
        viewParent.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }
}
