package id.telkomsel.merchant.ui.merchant.voucher.daftarVoucher

import androidx.recyclerview.widget.RecyclerView
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseFragmentBind
import id.telkomsel.merchant.databinding.FragmentDaftarVoucherMerchantBinding
import id.telkomsel.merchant.utils.Constant

class DaftarVoucherFragment(private val statusVoucher: String) : BaseFragmentBind<FragmentDaftarVoucherMerchantBinding>() {
    override fun getLayoutResource(): Int = R.layout.fragment_daftar_voucher_merchant
    lateinit var viewModel: DaftarVoucherViewModel

    override fun myCodeHere() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.title = Constant.voucher
        supportActionBar?.show()
        init()
    }

    fun init(){
        bind.lifecycleOwner = this
        viewModel = DaftarVoucherViewModel(activity, bind.rcRequest, savedData, statusVoucher)
        bind.viewModel = viewModel
        viewModel.initAdapter()

        viewModel.listData.clear()
        viewModel.adapter.notifyDataSetChanged()

        viewModel.checkUsername()

        bind.swipeRefresh.setOnRefreshListener {
            bind.swipeRefresh.isRefreshing = false
            viewModel.startPage = 0
            viewModel.listData.clear()
            viewModel.adapter.notifyDataSetChanged()
//            viewModel.checkUsername()
        }

        bind.rcRequest.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE && viewModel.isShowLoading.value == false) {
                    viewModel.isShowLoading.value = true
                    viewModel.checkUsername()
                }
            }
        })
    }
}