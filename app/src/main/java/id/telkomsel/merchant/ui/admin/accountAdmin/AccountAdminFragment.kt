package id.telkomsel.merchant.ui.admin.accountAdmin

import androidx.navigation.fragment.findNavController
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseFragmentBind
import id.telkomsel.merchant.databinding.FragmentAccountAdminBinding

class AccountAdminFragment : BaseFragmentBind<FragmentAccountAdminBinding>() {
    override fun getLayoutResource(): Int = R.layout.fragment_account_admin
    lateinit var viewModel: AccountAdminViewModel

    override fun myCodeHere() {
        supportActionBar?.show()
        init()
    }

    private fun init() {
        bind.lifecycleOwner = this
        viewModel =
            AccountAdminViewModel(
                savedData,
                activity,
                findNavController(),
                context
            )
        bind.viewModel = viewModel
        viewModel.dataMerchant.value = savedData.getDataMerchant()
        viewModel.dataMerchant.value?.username?.let { viewModel.getDataMerchant(it) }
    }
}