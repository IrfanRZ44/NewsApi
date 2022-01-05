package id.telkomsel.merchandise.ui.auth.verifyForgetPassword

import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import id.telkomsel.merchandise.R
import id.telkomsel.merchandise.databinding.FragmentVerifyForgetPasswordBinding
import id.telkomsel.merchandise.base.BaseFragmentBind
import id.telkomsel.merchandise.services.timer.TListener
import id.telkomsel.merchandise.services.timer.TimeFormatEnum
import id.telkomsel.merchandise.utils.Constant
import java.util.concurrent.TimeUnit

class VerifyForgetPasswordFragment : BaseFragmentBind<FragmentVerifyForgetPasswordBinding>(){
    override fun getLayoutResource(): Int = R.layout.fragment_verify_forget_password
    lateinit var viewModel: VerifyForgetPasswordViewModel

    override fun myCodeHere() {
        supportActionBar?.hide()
        bind.lifecycleOwner = this

        bind.etOTP.editText?.keyListener = null
        try {
            viewModel =
                VerifyForgetPasswordViewModel(
                    activity,
                    bind.progressTimer,
                    findNavController()
                )

            viewModel.dataSales = this.arguments?.getParcelable(Constant.reffSales)?:throw Exception("Error, mohon lakukan lupa password ulang")
            viewModel.noHp.value = viewModel.dataSales.no_hp_sales
            viewModel.isShowLoading.value = false
            viewModel.loading.value = true
            bind.viewModel = viewModel
            viewModel.sendCode()
        }catch (e: Exception){
            viewModel.message.value = e.message
        }
        setProgress()
        onBackPressed()
    }

    private fun setProgress() {
        bind.progressTimer.setCircularTimerListener(object : TListener {
            override fun updateDataOnTick(remainingTimeInMs: Long): String {
                // long seconds = (milliseconds / 1000);
                val seconds = TimeUnit.MILLISECONDS.toSeconds(remainingTimeInMs)
                bind.progressTimer.suffix = " s"
                return seconds.toString()
            }

            override fun onTimerFinished() {
                bind.progressTimer.prefix = ""
                bind.progressTimer.suffix = ""
                bind.progressTimer.text = "Kirim Ulang?"
                viewModel.isShowLoading.value = false
                viewModel.loading.value = false
            }
        }, 60, TimeFormatEnum.SECONDS, 1)

        bind.progressTimer.progress = 0F
        bind.progressTimer.startTimer()
    }

    private fun onBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.onClickBack()
            }
        })
    }
}