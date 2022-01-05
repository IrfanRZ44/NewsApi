package id.telkomsel.merchandise.ui.auth.loginSales

import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import id.telkomsel.merchandise.R
import id.telkomsel.merchandise.base.BaseFragmentBind
import id.telkomsel.merchandise.databinding.FragmentLoginSalesBinding

class LoginSalesFragment : BaseFragmentBind<FragmentLoginSalesBinding>(){
    override fun getLayoutResource(): Int = R.layout.fragment_login_sales
    lateinit var viewModel: LoginSalesViewModel

    override fun myCodeHere() {
        supportActionBar?.hide()
        bind.lifecycleOwner = this
        viewModel = LoginSalesViewModel(
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