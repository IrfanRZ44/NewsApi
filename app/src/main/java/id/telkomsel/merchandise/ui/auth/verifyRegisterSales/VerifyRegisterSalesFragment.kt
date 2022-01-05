package id.telkomsel.merchandise.ui.auth.verifyRegisterSales

import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import id.telkomsel.merchandise.R
import id.telkomsel.merchandise.base.BaseFragmentBind
import id.telkomsel.merchandise.databinding.FragmentVerifyRegisterSalesBinding
import id.telkomsel.merchandise.services.timer.TListener
import id.telkomsel.merchandise.services.timer.TimeFormatEnum
import id.telkomsel.merchandise.utils.Constant
import java.util.concurrent.TimeUnit

class VerifyRegisterSalesFragment : BaseFragmentBind<FragmentVerifyRegisterSalesBinding>(){
    override fun getLayoutResource(): Int = R.layout.fragment_verify_register_sales
    lateinit var viewModel: VerifyRegisterSalesViewModel

    override fun myCodeHere() {
        supportActionBar?.hide()
        bind.lifecycleOwner = this
        try {
            viewModel =
                VerifyRegisterSalesViewModel(
                    activity,
                    bind.progressTimer,
                    findNavController()
                )

            viewModel.dataSales = this.arguments?.getParcelable(Constant.reffSales)?:throw Exception("Error, mohon lakukan registrasi ulang")
            viewModel.noHp.value = this.arguments?.getString(Constant.noHp)
            viewModel.etFotoProfil.value = this.arguments?.getParcelable(Constant.dataModelFotoProfil)
            viewModel.etFotoDepanKendaraan.value = this.arguments?.getParcelable(Constant.dataModelFotoDepanKendaraan)
            viewModel.etFotoBelakangKendaraan.value = this.arguments?.getParcelable(Constant.dataModelFotoBelakangKendaraan)
            viewModel.etFotoWajah.value = this.arguments?.getParcelable(Constant.dataModelFotoWajah)
            viewModel.etFotoSeluruhBadan.value = this.arguments?.getParcelable(Constant.dataModelFotoSeluruhBadan)

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