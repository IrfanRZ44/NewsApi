package id.telkomsel.merchandise.ui.auth.forgetPasswordSales

import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import id.telkomsel.merchandise.R
import id.telkomsel.merchandise.base.BaseFragmentBind
import id.telkomsel.merchandise.databinding.FragmentForgetPasswordSalesBinding

class ForgetPasswordSalesFragment : BaseFragmentBind<FragmentForgetPasswordSalesBinding>(){
    override fun getLayoutResource(): Int = R.layout.fragment_forget_password_sales
    lateinit var viewModel: ForgetPasswordSalesViewModel

    override fun myCodeHere() {
        supportActionBar?.hide()
        bind.lifecycleOwner = this
        viewModel = ForgetPasswordSalesViewModel(
            findNavController(),
            activity,
            bind.etUsername
        )
        bind.viewModel = viewModel
        viewModel.message.value = "Kami akan mengirimkan kode OTP ke nomor HP Sales untuk memverifikasi akun Anda"
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