package id.telkomsel.merchant.ui.merchant.listMerchant

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseFragmentBind
import id.telkomsel.merchant.databinding.FragmentDaftarMerchantBinding
import id.telkomsel.merchant.utils.Constant

class DaftarMerchantFragment(private val statusRequest: String) : BaseFragmentBind<FragmentDaftarMerchantBinding>() {
    override fun getLayoutResource(): Int = R.layout.fragment_daftar_merchant
    lateinit var viewModel: DaftarMerchantViewModel

    override fun myCodeHere() {
        supportActionBar?.show()
        init()
    }

    fun init(){
        bind.lifecycleOwner = this
        viewModel = DaftarMerchantViewModel(findNavController(), activity, bind.rcRequest, statusRequest, savedData)
        bind.viewModel = viewModel
        viewModel.initAdapter()

        viewModel.listRequest.clear()
        viewModel.adapter.notifyDataSetChanged()
        checkCluster("")

        bind.swipeRefresh.setOnRefreshListener {
            viewModel.startPage = 0
            viewModel.listRequest.clear()
            viewModel.adapter.notifyDataSetChanged()
            bind.swipeRefresh.isRefreshing = false
            checkCluster("")
        }

        bind.rcRequest.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE && viewModel.isShowLoading.value == false) {
                    viewModel.isShowLoading.value = true
                    checkCluster("")
                }
            }
        })
    }

    fun checkCluster(search: String?){
        val cluster = savedData.getDataMerchant()?.cluster
        val regional = savedData.getDataMerchant()?.regional
        val level = savedData.getDataMerchant()?.level

        if (level == Constant.levelChannel && !regional.isNullOrEmpty()){
            viewModel.getDataTeknisi(search, regional, "regional")
        }
        else if ((level == Constant.levelCSO || level == Constant.levelSBP) && !cluster.isNullOrEmpty()){
            viewModel.getDataTeknisi(search, cluster, "cluster")
        }
        else{
            viewModel.message.value = "Error, gagal mendapatkan data cluster Anda"
        }
    }
}