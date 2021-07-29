package id.telkomsel.merchant.ui.pelanggan.favorit

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseFragmentBind
import id.telkomsel.merchant.databinding.FragmentFavoritProdukBinding

class FavoritProdukFragment : BaseFragmentBind<FragmentFavoritProdukBinding>() {
    override fun getLayoutResource(): Int = R.layout.fragment_favorit_produk
    lateinit var viewModel: FavoritProdukViewModel

    override fun myCodeHere() {
        setHasOptionsMenu(false)

        init()
    }

    fun init(){
        bind.lifecycleOwner = this
        viewModel = FavoritProdukViewModel(
            findNavController(), activity, bind.rcProduk, savedData
        )
        bind.viewModel = viewModel
        viewModel.initAdapterProduk()

        savedData.getDataPelanggan()?.username?.let { viewModel.getDaftarProdukFavorit(it) }

        bind.swipeRefresh.setOnRefreshListener {
            viewModel.startPage = 0
            viewModel.listProduk.clear()
            viewModel.adapterProduk.notifyDataSetChanged()
            bind.swipeRefresh.isRefreshing = false
            savedData.getDataPelanggan()?.username?.let { viewModel.getDaftarProdukFavorit(it) }
        }

        bind.rcProduk.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE && viewModel.isShowLoading.value == false) {
                    viewModel.isShowLoading.value = true
                    savedData.getDataPelanggan()?.username?.let { viewModel.getDaftarProdukFavorit(it) }
                }
            }
        })
    }
}