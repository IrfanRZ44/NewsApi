package id.telkomsel.merchandise.ui.sales.accountSales

import androidx.navigation.fragment.findNavController
import id.telkomsel.merchandise.R
import id.telkomsel.merchandise.databinding.FragmentAccountSalesBinding
import id.telkomsel.merchandise.base.BaseFragmentBind
import id.telkomsel.merchandise.utils.Constant

class AccountSalesFragment : BaseFragmentBind<FragmentAccountSalesBinding>() {
    override fun getLayoutResource(): Int = R.layout.fragment_account_sales
    lateinit var viewModel: AccountSalesViewModel

    override fun myCodeHere() {
        supportActionBar?.show()
        supportActionBar?.title = "Akun"
        setHasOptionsMenu(false)
        init()
    }

    private fun init() {
        bind.lifecycleOwner = this
        viewModel =
            AccountSalesViewModel(
                savedData,
                activity,
                findNavController(),
                context
            )
        bind.viewModel = viewModel
        viewModel.dataSales.value = savedData.getDataSales()
        viewModel.dataSales.value?.foto_profil = "${Constant.reffURL}${viewModel.dataSales.value?.foto_profil}"
        viewModel.dataSales.value?.username?.let { viewModel.getDataSales(it) }
    }
}