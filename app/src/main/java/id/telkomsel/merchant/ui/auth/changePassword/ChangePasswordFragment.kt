package id.telkomsel.merchant.ui.auth.changePassword

import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import id.telkomsel.merchant.R
import id.telkomsel.merchant.databinding.FragmentChangePasswordMerchantBinding
import id.telkomsel.merchant.base.BaseFragmentBind
import id.telkomsel.merchant.utils.Constant

class ChangePasswordFragment : BaseFragmentBind<FragmentChangePasswordMerchantBinding>(){
    override fun getLayoutResource(): Int = R.layout.fragment_change_password_merchant
    lateinit var viewModel: ChangePasswordViewModel

    override fun myCodeHere() {
        init()
        onClick()
    }

    private fun init() {
        bind.lifecycleOwner = this
        viewModel = ChangePasswordViewModel(activity, findNavController(),
            bind.etPasswordNew, bind.etConfirmPasswordNew)
        bind.viewModel = viewModel
        viewModel.dataMerchant = this.arguments?.getParcelable(Constant.reffMerchant)?:throw Exception("Error, mohon lakukan registrasi ulang")
    }

    private fun onClick(){
        bind.etConfirmPasswordNew.editText?.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.onClickEditProfil()
                return@OnEditorActionListener false
            }
            false
        })
    }
}