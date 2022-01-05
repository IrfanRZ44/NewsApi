package id.telkomsel.merchandise.ui.sales.accountSales.editPassword

import id.telkomsel.merchandise.R
import id.telkomsel.merchandise.databinding.FragmentEditPasswordSalesBinding
import id.telkomsel.merchandise.base.BaseFragmentBind

class EditPasswordSalesFragment : BaseFragmentBind<FragmentEditPasswordSalesBinding>(){
    override fun getLayoutResource(): Int = R.layout.fragment_edit_password_sales
    lateinit var viewModel: EditPasswordSalesViewModel

    override fun myCodeHere() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Edit Password"
        supportActionBar?.show()
        init()
        onClick()
    }

    private fun init() {
        bind.lifecycleOwner = this
        viewModel = EditPasswordSalesViewModel(
            savedData, activity, bind.etPasswordOld,
            bind.etPasswordNew, bind.etConfirmPasswordNew
        )
        bind.viewModel = viewModel
    }

    private fun onClick(){

    }
}