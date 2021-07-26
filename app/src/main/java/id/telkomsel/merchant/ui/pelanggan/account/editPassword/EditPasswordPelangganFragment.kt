package id.telkomsel.merchant.ui.pelanggan.account.editPassword

import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseFragmentBind
import id.telkomsel.merchant.databinding.FragmentEditPasswordPelangganBinding

class EditPasswordPelangganFragment : BaseFragmentBind<FragmentEditPasswordPelangganBinding>(){
    override fun getLayoutResource(): Int = R.layout.fragment_edit_password_pelanggan
    lateinit var viewModel: EditPasswordPelangganViewModel

    override fun myCodeHere() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Edit Password"
        supportActionBar?.show()
        init()
        onClick()
    }

    private fun init() {
        bind.lifecycleOwner = this
        viewModel = EditPasswordPelangganViewModel(
            savedData, activity, bind.etPasswordOld,
            bind.etPasswordNew, bind.etConfirmPasswordNew
        )
        bind.viewModel = viewModel
    }

    private fun onClick(){

    }
}