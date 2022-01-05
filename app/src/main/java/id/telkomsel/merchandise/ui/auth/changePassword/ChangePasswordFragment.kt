package id.telkomsel.merchandise.ui.auth.changePassword

import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import id.telkomsel.merchandise.R
import id.telkomsel.merchandise.databinding.FragmentChangePasswordSalesBinding
import id.telkomsel.merchandise.base.BaseFragmentBind
import id.telkomsel.merchandise.utils.Constant

class ChangePasswordFragment : BaseFragmentBind<FragmentChangePasswordSalesBinding>(){
    override fun getLayoutResource(): Int = R.layout.fragment_change_password_sales
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
        viewModel.dataSales = this.arguments?.getParcelable(Constant.reffSales)?:throw Exception("Error, mohon lakukan registrasi ulang")
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