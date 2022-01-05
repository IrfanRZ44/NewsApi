package id.telkomsel.merchandise.ui.sales.listSales

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import id.telkomsel.merchandise.R
import id.telkomsel.merchandise.databinding.FragmentDaftarSalesBinding
import id.telkomsel.merchandise.base.BaseFragmentBind
import id.telkomsel.merchandise.utils.Constant

class DaftarSalesFragment(private val statusRequest: String) : BaseFragmentBind<FragmentDaftarSalesBinding>() {
    override fun getLayoutResource(): Int = R.layout.fragment_daftar_sales
    lateinit var viewModel: DaftarSalesViewModel

    override fun myCodeHere() {
        supportActionBar?.show()
        init()
    }

    fun init(){
        bind.lifecycleOwner = this
        viewModel = DaftarSalesViewModel(findNavController(), activity, bind.rcRequest, statusRequest, savedData)
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
        val cluster = savedData.getDataSales()?.branch
        val regional = savedData.getDataSales()?.regional
        val level = savedData.getDataSales()?.level

        if (level == Constant.levelChannel && !regional.isNullOrEmpty()){
            viewModel.getDataTeknisi(search, regional, "regional")
        }
        else if (level == Constant.levelDLS && !cluster.isNullOrEmpty()){
            viewModel.getDataTeknisi(search, cluster, "branch")
        }
        else{
            viewModel.message.value = "Error, gagal mendapatkan data cluster Anda"
        }
    }
}