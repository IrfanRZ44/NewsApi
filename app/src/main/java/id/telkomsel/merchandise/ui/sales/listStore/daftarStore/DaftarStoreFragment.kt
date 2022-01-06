package id.telkomsel.merchandise.ui.sales.listStore.daftarStore

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import id.telkomsel.merchandise.R
import id.telkomsel.merchandise.base.BaseFragmentBind
import id.telkomsel.merchandise.databinding.FragmentDaftarStoreBinding

class DaftarStoreFragment(
    private val statusRequest: String
) : BaseFragmentBind<FragmentDaftarStoreBinding>() {
    override fun getLayoutResource(): Int = R.layout.fragment_daftar_store
    lateinit var viewModel: DaftarStoreViewModel

    override fun myCodeHere() {
        supportActionBar?.show()
        init()
    }

    fun init(){
        bind.lifecycleOwner = this
        viewModel = DaftarStoreViewModel(
            findNavController(), activity, bind.rcStore, statusRequest,
            savedData
        )
        bind.viewModel = viewModel

        viewModel.initAdapterStore()
        viewModel.checkCluster()

        bind.swipeRefresh.setOnRefreshListener {
            viewModel.startPage = 0
            viewModel.listStore.clear()
            viewModel.adapterStore.notifyDataSetChanged()
            bind.swipeRefresh.isRefreshing = false
            viewModel.checkCluster()
        }

        bind.rcStore.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE && viewModel.isShowLoading.value == false) {
                    viewModel.isShowLoading.value = true
                    viewModel.checkCluster()
                }
            }
        })
    }
}