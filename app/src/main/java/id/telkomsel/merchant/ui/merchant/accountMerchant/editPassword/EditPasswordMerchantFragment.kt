package id.telkomsel.merchant.ui.merchant.accountMerchant.editPassword

import id.telkomsel.merchant.R
import id.telkomsel.merchant.databinding.FragmentEditPasswordMerchantBinding
import id.telkomsel.merchant.base.BaseFragmentBind

class EditPasswordMerchantFragment : BaseFragmentBind<FragmentEditPasswordMerchantBinding>(){
    override fun getLayoutResource(): Int = R.layout.fragment_edit_password_merchant
    lateinit var viewModel: EditPasswordMerchantViewModel

    override fun myCodeHere() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Edit Password"
        supportActionBar?.show()
        init()
        onClick()
    }

    private fun init() {
        bind.lifecycleOwner = this
        viewModel = EditPasswordMerchantViewModel(
            savedData, activity, bind.etPasswordOld,
            bind.etPasswordNew, bind.etConfirmPasswordNew
        )
        bind.viewModel = viewModel
    }

    private fun onClick(){

    }
}