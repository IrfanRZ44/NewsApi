package id.telkomsel.merchant.ui.pelanggan.account

import androidx.navigation.fragment.findNavController
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseFragmentBind
import id.telkomsel.merchant.databinding.FragmentAccountPelangganBinding

class AccountPelangganFragment : BaseFragmentBind<FragmentAccountPelangganBinding>() {
    override fun getLayoutResource(): Int = R.layout.fragment_account_pelanggan
    lateinit var viewModel: AccountPelangganViewModel

    override fun myCodeHere() {
        supportActionBar?.show()
        supportActionBar?.title = "Akun"
        setHasOptionsMenu(false)
        init()
    }

    private fun init() {
        bind.lifecycleOwner = this
        viewModel =
            AccountPelangganViewModel(
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