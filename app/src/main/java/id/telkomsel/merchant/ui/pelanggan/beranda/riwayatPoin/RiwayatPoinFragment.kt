package id.telkomsel.merchant.ui.pelanggan.beranda.riwayatPoin

import androidx.recyclerview.widget.RecyclerView
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseFragmentBind
import id.telkomsel.merchant.databinding.FragmentRiwayatPoinBinding
import id.telkomsel.merchant.utils.Constant

class RiwayatPoinFragment : BaseFragmentBind<FragmentRiwayatPoinBinding>() {
    override fun getLayoutResource(): Int = R.layout.fragment_riwayat_poin
    lateinit var viewModel: RiwayatPoinViewModel

    override fun myCodeHere() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = Constant.riwayatPoin
        supportActionBar?.show()
        init()
    }

    fun init(){
        bind.lifecycleOwner = this
        viewModel = RiwayatPoinViewModel(activity, bind.rcRequest, savedData)
        bind.viewModel = viewModel
        viewModel.initAdapter()

        viewModel.listData.clear()
        viewModel.adapter.notifyDataSetChanged()

        viewModel.setDefaultDate()

        bind.swipeRefresh.setOnRefreshListener {
            bind.swipeRefresh.isRefreshing = false
            viewModel.startPage = 0
            viewModel.listData.clear()
            viewModel.adapter.notifyDataSetChanged()
            viewModel.setDefaultDate()
        }

        bind.rcRequest.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE && viewModel.isShowLoading.value == false) {
                    viewModel.isShowLoading.value = true
                    viewModel.checkRangeDate()
                }
            }
        })
    }
}