package id.telkomsel.merchandise.ui.auth.splash

import androidx.navigation.fragment.findNavController
import id.telkomsel.merchandise.R
import id.telkomsel.merchandise.databinding.FragmentSplashBinding
import id.telkomsel.merchandise.base.BaseFragmentBind

class SplashFragment : BaseFragmentBind<FragmentSplashBinding>() {
    override fun getLayoutResource(): Int = R.layout.fragment_splash
    lateinit var viewModel: SplashViewModel

    override fun myCodeHere() {
        supportActionBar?.hide()
        bind.lifecycleOwner = this
        viewModel = SplashViewModel(findNavController(), savedData, activity)
        bind.viewModel = viewModel
        viewModel.getInfoApps()
    }
}