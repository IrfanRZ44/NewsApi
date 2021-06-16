package id.telkomsel.merchant.ui.admin.accountAdmin.editPassword

import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseFragmentBind
import id.telkomsel.merchant.databinding.FragmentEditPasswordAdminBinding

class EditPasswordAdminFragment : BaseFragmentBind<FragmentEditPasswordAdminBinding>(){
    override fun getLayoutResource(): Int = R.layout.fragment_edit_password_admin
    lateinit var viewModel: EditPasswordAdminViewModel

    override fun myCodeHere() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Edit Password"
        supportActionBar?.show()
        init()
        onClick()
    }

    private fun init() {
        bind.lifecycleOwner = this
        viewModel = EditPasswordAdminViewModel(
            savedData, activity, bind.etPasswordOld,
            bind.etPasswordNew, bind.etConfirmPasswordNew
        )
        bind.viewModel = viewModel
    }

    private fun onClick(){

    }
}