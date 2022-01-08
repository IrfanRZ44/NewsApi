package id.exomatik.news.ui.news

import android.view.View
import androidx.navigation.fragment.NavHostFragment
import id.exomatik.news.R
import id.exomatik.news.base.BaseActivity
import kotlinx.android.synthetic.main.activity_news.*

class NewsActivity : BaseActivity() {
    override fun getLayoutResource(): Int = R.layout.activity_news

    @Suppress("DEPRECATION")
    override fun myCodeHere() {
        NavHostFragment.create(R.navigation.news_nav)
        viewParent.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}