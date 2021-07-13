package id.telkomsel.merchant.ui.merchant.accountMerchant

import androidx.navigation.fragment.findNavController
import id.telkomsel.merchant.R
import id.telkomsel.merchant.databinding.FragmentAccountMerchantBinding
import id.telkomsel.merchant.base.BaseFragmentBind

class AccountMerchantFragment : BaseFragmentBind<FragmentAccountMerchantBinding>() {
    override fun getLayoutResource(): Int = R.layout.fragment_account_merchant
    lateinit var viewModel: AccountMerchantViewModel

    override fun myCodeHere() {
        supportActionBar?.show()
        supportActionBar?.title = "Akun"
        setHasOptionsMenu(false)
        init()
    }

    private fun init() {
        bind.lifecycleOwner = this
        viewModel =
            AccountMerchantViewModel(
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