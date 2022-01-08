package id.exomatik.news.ui.auth.splash

import id.exomatik.news.R
import id.exomatik.news.databinding.FragmentSplashBinding
import id.exomatik.news.base.BaseFragmentBind

class SplashFragment : BaseFragmentBind<FragmentSplashBinding>() {
    override fun getLayoutResource(): Int = R.layout.fragment_splash
    lateinit var viewModel: SplashViewModel

    override fun myCodeHere() {
        supportActionBar?.hide()
        bind.lifecycleOwner = this
        viewModel = SplashViewModel(activity)
        bind.viewModel = viewModel
        viewModel.moveMainMenu()
    }
}