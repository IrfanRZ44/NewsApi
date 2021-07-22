package id.telkomsel.merchant.ui.pelanggan.auth.forgetPassword

import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseFragmentBind
import id.telkomsel.merchant.databinding.FragmentForgetPasswordBinding

class ForgetPasswordFragment : BaseFragmentBind<FragmentForgetPasswordBinding>(){
    override fun getLayoutResource(): Int = R.layout.fragment_forget_password
    lateinit var viewModel: ForgetPasswordViewModel

    override fun myCodeHere() {
        supportActionBar?.hide()
        bind.lifecycleOwner = this
        viewModel = ForgetPasswordViewModel(
            findNavController(),
            activity,
            bind.etUsername
        )
        bind.viewModel = viewModel
        viewModel.message.value = "Kami akan mengirimkan kode OTP ke nomor HP untuk memverifikasi akun Anda"
        onClick()
    }

    private fun onClick() {
        bind.etUsername.editText?.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.onClickForgetPassword()
                return@OnEditorActionListener false
            }
            false
        })
    }
}