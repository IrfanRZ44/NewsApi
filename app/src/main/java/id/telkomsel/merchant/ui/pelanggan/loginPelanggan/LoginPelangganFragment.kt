package id.telkomsel.merchant.ui.pelanggan.loginPelanggan

import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseFragmentBind
import id.telkomsel.merchant.databinding.FragmentLoginPelangganBinding

class LoginPelangganFragment : BaseFragmentBind<FragmentLoginPelangganBinding>(){
    override fun getLayoutResource(): Int = R.layout.fragment_login_pelanggan
    lateinit var viewModel: LoginPelangganViewModel

    override fun myCodeHere() {
        bind.lifecycleOwner = this
        viewModel = LoginPelangganViewModel(
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