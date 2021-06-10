package id.telkomsel.merchant.ui.auth.landing

import android.os.CountDownTimer
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import id.telkomsel.merchant.R
import id.telkomsel.merchant.databinding.FragmentLandingBinding
import id.telkomsel.merchant.base.BaseFragmentBind
import id.telkomsel.merchant.utils.adapter.showMessage

class LandingFragment : BaseFragmentBind<FragmentLandingBinding>(){
    override fun getLayoutResource(): Int = R.layout.fragment_landing
    lateinit var viewModel: LandingViewModel
    private var exit = false

    override fun myCodeHere() {
        supportActionBar?.hide()
        bind.lifecycleOwner = this
        viewModel = LandingViewModel(
            findNavController()
        )
        bind.viewModel = viewModel

        onBackPressed()
    }

    private fun onBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (exit) {
                    activity?.finish()
                    return
                } else {
                    showMessage(view, "Tekan Cepat 2 Kali untuk Keluar")
                    exit = true
                    object : CountDownTimer(2000, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                        }

                        override fun onFinish() {
                            exit = false
                        }
                    }.start()
                }
            }
        })
    }
}