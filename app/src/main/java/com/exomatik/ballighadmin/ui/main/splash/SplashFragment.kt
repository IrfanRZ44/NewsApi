package com.exomatik.ballighadmin.ui.main.splash

import androidx.navigation.fragment.findNavController
import com.exomatik.ballighadmin.R
import com.exomatik.ballighadmin.base.BaseFragmentBind
import com.exomatik.ballighadmin.databinding.SplashFragmentBinding

class SplashFragment : BaseFragmentBind<SplashFragmentBinding>() {
    override fun getLayoutResource(): Int = R.layout.splash_fragment
    private lateinit var viewModel: SplashViewModel

    override fun myCodeHere() {
        bind.lifecycleOwner = this
        viewModel = SplashViewModel(activity, findNavController(), savedData)
        bind.viewModel = viewModel

        if (savedData.getDataUser()?.username.isNullOrEmpty()) {
            viewModel.requestCode()
        } else {
            findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
        }
    }

}