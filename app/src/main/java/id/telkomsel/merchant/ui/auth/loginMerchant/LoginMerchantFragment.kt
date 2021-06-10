package id.telkomsel.merchant.ui.auth.loginMerchant

import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseFragmentBind
import id.telkomsel.merchant.databinding.FragmentLoginMerchantBinding

class LoginMerchantFragment : BaseFragmentBind<FragmentLoginMerchantBinding>(){
    override fun getLayoutResource(): Int = R.layout.fragment_login_merchant
    lateinit var viewModel: LoginMerchantViewModel

    override fun myCodeHere() {
        supportActionBar?.hide()
        bind.lifecycleOwner = this
        viewModel = LoginMerchantViewModel(
            findNavController(),
            savedData,
            activity,
            bind.etUsername,
            bind.etPassword
        )
        bind.viewModel = viewModel

        onClick()
    }

    private fun onClick() {
        bind.etPassword.editText?.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.onClickLogin()
                return@OnEditorActionListener false
            }
            false
        })
    }
}