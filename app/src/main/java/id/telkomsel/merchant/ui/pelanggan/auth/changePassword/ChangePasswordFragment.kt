package id.telkomsel.merchant.ui.pelanggan.auth.changePassword

import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseFragmentBind
import id.telkomsel.merchant.databinding.FragmentChangePasswordPelangganBinding
import id.telkomsel.merchant.utils.Constant

class ChangePasswordFragment : BaseFragmentBind<FragmentChangePasswordPelangganBinding>(){
    override fun getLayoutResource(): Int = R.layout.fragment_change_password_pelanggan
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
        viewModel.dataPelanggan = this.arguments?.getParcelable(Constant.reffPelanggan)?:throw Exception("Error, mohon lakukan registrasi ulang")
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