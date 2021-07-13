package id.telkomsel.merchant.ui.merchant.listProduk

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseFragmentBind
import id.telkomsel.merchant.databinding.FragmentDaftarProdukBinding

class DaftarProdukFragment(private val statusRequest: String,
                           private val stok: Int,
                           private val isKadaluarsa: Boolean
) : BaseFragmentBind<FragmentDaftarProdukBinding>() {
    override fun getLayoutResource(): Int = R.layout.fragment_daftar_produk
    lateinit var viewModel: DaftarProdukViewModel


    override fun myCodeHere() {
        supportActionBar?.show()
        supportActionBar?.title = "Produk"
        init()
    }

    fun init(){
        bind.lifecycleOwner = this
        viewModel = DaftarProdukViewModel(
            findNavController(), activity, bind.rcKategori, bind.rcProduk,
            statusRequest, stok, isKadaluarsa, savedData
        )
        bind.viewModel = viewModel
        viewModel.initAdapterKategori()
        viewModel.initAdapterProduk()
        viewModel.showDialogFilter(bind.root, layoutInflater)

        viewModel.getDataKategori()
        viewModel.checkCluster("")

        bind.swipeRefresh.setOnRefreshListener {
            viewModel.startPage = 0
            viewModel.listProduk.clear()
            viewModel.adapterProduk.notifyDataSetChanged()
            bind.swipeRefresh.isRefreshing = false
            viewModel.checkCluster("")
        }

        bind.rcProduk.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE && viewModel.isShowLoading.value == false) {
                    viewModel.isShowLoading.value = true
                    viewModel.checkCluster("")
                }
            }
        })
    }
}