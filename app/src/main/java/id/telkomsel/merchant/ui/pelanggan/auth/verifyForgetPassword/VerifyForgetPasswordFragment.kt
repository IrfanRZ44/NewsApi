package id.telkomsel.merchant.ui.pelanggan.auth.verifyForgetPassword

import android.text.Editable
import android.text.TextWatcher
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseFragmentBind
import id.telkomsel.merchant.databinding.FragmentVerifyForgetPasswordPelangganBinding
import id.telkomsel.merchant.services.timer.TListener
import id.telkomsel.merchant.services.timer.TimeFormatEnum
import id.telkomsel.merchant.utils.Constant
import id.telkomsel.merchant.utils.adapter.dismissKeyboard
import java.util.concurrent.TimeUnit

class VerifyForgetPasswordFragment : BaseFragmentBind<FragmentVerifyForgetPasswordPelangganBinding>(){
    override fun getLayoutResource(): Int = R.layout.fragment_verify_forget_password_pelanggan
    lateinit var viewModel: VerifyForgetPasswordViewModel

    override fun myCodeHere() {
        supportActionBar?.hide()
        bind.lifecycleOwner = this
        try {
            viewModel =
                VerifyForgetPasswordViewModel(
                    activity,
                    bind.progressTimer,
                    bind.etText1,
                    bind.etText2,
                    bind.etText3,
                    bind.etText4,
                    bind.etText5,
                    bind.etText6,
                    findNavController()
                )

            viewModel.dataPelanggan = this.arguments?.getParcelable(Constant.reffPelanggan)?:throw Exception("Error, mohon lakukan lupa password ulang")
            viewModel.noHp.value = viewModel.dataPelanggan.nomor_hp
            viewModel.isShowLoading.value = false
            viewModel.loading.value = true
            bind.viewModel = viewModel
            viewModel.sendCode()
        }catch (e: Exception){
            viewModel.message.value = e.message
        }
        setUpEditText()
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

    private fun setUpEditText() {
        bind.etText1.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                val text1 = bind.etText1.text.toString()
                if (text1.isNotEmpty()){
                    bind.etText1.clearFocus()
                    bind.etText2.findFocus()
                    bind.etText2.requestFocus()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        bind.etText2.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                val text1 = bind.etText1.text.toString()
                val text2 = bind.etText2.text.toString()
                if (text1.isNotEmpty() && text2.isNotEmpty()){
                    bind.etText2.clearFocus()
                    bind.etText3.findFocus()
                    bind.etText3.requestFocus()
                }
                else if (text2.isEmpty()){
                    bind.etText2.clearFocus()
                    bind.etText1.findFocus()
                    bind.etText1.requestFocus()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        bind.etText3.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                val text1 = bind.etText1.text.toString()
                val text2 = bind.etText2.text.toString()
                val text3 = bind.etText3.text.toString()
                if (text1.isNotEmpty() && text2.isNotEmpty() && text3.isNotEmpty()){
                    bind.etText3.clearFocus()
                    bind.etText4.findFocus()
                    bind.etText4.requestFocus()
                }
                else if (text3.isEmpty()){
                    bind.etText3.clearFocus()
                    bind.etText2.findFocus()
                    bind.etText2.requestFocus()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        bind.etText4.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                val text1 = bind.etText1.text.toString()
                val text2 = bind.etText2.text.toString()
                val text3 = bind.etText3.text.toString()
                val text4 = bind.etText4.text.toString()
                if (text1.isNotEmpty() && text2.isNotEmpty() && text3.isNotEmpty() &&
                    text4.isNotEmpty()){
                    bind.etText4.clearFocus()
                    bind.etText5.findFocus()
                    bind.etText5.requestFocus()
                }
                else if (text4.isEmpty()){
                    bind.etText4.clearFocus()
                    bind.etText3.findFocus()
                    bind.etText3.requestFocus()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        bind.etText5.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                val text1 = bind.etText1.text.toString()
                val text2 = bind.etText2.text.toString()
                val text3 = bind.etText3.text.toString()
                val text4 = bind.etText4.text.toString()
                val text5 = bind.etText5.text.toString()
                if (text1.isNotEmpty() && text2.isNotEmpty() && text3.isNotEmpty() &&
                    text4.isNotEmpty() && text5.isNotEmpty()){
                    bind.etText5.clearFocus()
                    bind.etText6.findFocus()
                    bind.etText6.requestFocus()
                }
                else if (text5.isEmpty()){
                    bind.etText5.clearFocus()
                    bind.etText4.findFocus()
                    bind.etText4.requestFocus()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        bind.etText6.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                val text1 = bind.etText1.text.toString()
                val text2 = bind.etText2.text.toString()
                val text3 = bind.etText3.text.toString()
                val text4 = bind.etText4.text.toString()
                val text5 = bind.etText5.text.toString()
                val text6 = bind.etText6.text.toString()
                if (text1.isNotEmpty() && text2.isNotEmpty() && text3.isNotEmpty() &&
                    text4.isNotEmpty() && text5.isNotEmpty() && text6.isNotEmpty()){
                    activity?.let { dismissKeyboard(it) }
                    viewModel.phoneCode.value = bind.etText1.text.toString() + bind.etText2.text.toString() +
                            bind.etText3.text.toString() + bind.etText4.text.toString() +
                            bind.etText5.text.toString() + bind.etText6.text.toString()
                    viewModel.onClick(2)
                }
                else if (text6.isEmpty()){
                    bind.etText6.clearFocus()
                    bind.etText5.findFocus()
                    bind.etText5.requestFocus()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun onBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.onClickBack()
            }
        })
    }
}