package id.telkomsel.merchant.ui.pelanggan.beranda

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseFragmentBind
import id.telkomsel.merchant.databinding.FragmentBerandaPelangganBinding
import id.telkomsel.merchant.utils.adapter.dismissKeyboard

class BerandaPelangganFragment : BaseFragmentBind<FragmentBerandaPelangganBinding>() {
    override fun getLayoutResource(): Int = R.layout.fragment_beranda_pelanggan
    lateinit var viewModel: BerandaPelangganViewModel
    private var searchView : SearchView? = null
    private var queryTextListener : SearchView.OnQueryTextListener? = null
    private var onCloseListener : SearchView.OnCloseListener? = null

    override fun myCodeHere() {
        setHasOptionsMenu(true)

        init()
        onClick()
    }

    fun init(){
        bind.lifecycleOwner = this
        viewModel = BerandaPelangganViewModel(
            findNavController(), activity, context, bind.rcKategori, bind.cardRating, bind.rcProduk, bind.rcRating, savedData,
            bind.viewPager, bind.dotsIndicator
        )
        bind.viewModel = viewModel

        viewModel.initHeader(bind.cardHeader)
        viewModel.initAdapterKategori()
        viewModel.initAdapterProduk()
        viewModel.showDialogFilter(bind.root, layoutInflater)

        viewModel.getDataKategori()
        viewModel.getDaftarProdukByPelanggan(
            if (viewModel.idSubKategori == 0) "" else viewModel.idSubKategori.toString()
        )

        bind.swipeRefresh.setOnRefreshListener {
            viewModel.startPage = 0
            viewModel.listProduk.clear()
            viewModel.adapterProduk.notifyDataSetChanged()
            bind.swipeRefresh.isRefreshing = false
            viewModel.getDaftarProdukByPelanggan(
                if (viewModel.idSubKategori == 0) "" else viewModel.idSubKategori.toString()
            )
        }

        bind.rcProduk.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE && viewModel.isShowLoading.value == false) {
                    viewModel.isShowLoading.value = true
                    viewModel.getDaftarProdukByPelanggan(
                        if (viewModel.idSubKategori == 0) "" else viewModel.idSubKategori.toString()
                    )
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_search_filter, menu)


        val searchItem = menu.findItem(R.id.actionSearch)
        val filterItem = menu.findItem(R.id.actionFilter)
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        filterItem.isVisible = true
        searchView = searchItem.actionView as SearchView
        searchView?.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))

        queryTextListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                if (!viewModel.isSearching){
                    viewModel.isSearching = true
                    activity?.let { dismissKeyboard(it) }

                    viewModel.startPage = 0
                    viewModel.listProduk.clear()
                    viewModel.adapterProduk.notifyDataSetChanged()
                    viewModel.textSearch = query
                    viewModel.getDaftarProdukByPelanggan(
                        if (viewModel.idSubKategori == 0) "" else viewModel.idSubKategori.toString()
                    )
                }

                return true
            }
        }

        onCloseListener = SearchView.OnCloseListener {
            viewModel.startPage = 0
            viewModel.listProduk.clear()
            viewModel.adapterProduk.notifyDataSetChanged()
            viewModel.textSearch = ""

            viewModel.getDaftarProdukByPelanggan(
                if (viewModel.idSubKategori == 0) "" else viewModel.idSubKategori.toString()
            )
            false
        }

        searchView?.setOnQueryTextListener(queryTextListener)
        searchView?.setOnCloseListener(onCloseListener)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.actionSearch ->{
                return false
            }
            R.id.actionFilter ->{
                viewModel.btmSheet.show()
                return false
            }
        }
        searchView?.setOnQueryTextListener(queryTextListener)
        searchView?.setOnCloseListener(onCloseListener)
        return super.onOptionsItemSelected(item)
    }

    @Suppress("DEPRECATION")
    @SuppressLint("UseCompatLoadingForColorStateLists")
    private fun onClick(){
        bind.btnSortStok.backgroundTintList = resources.getColorStateList(R.color.gray1)
        bind.btnSortPoin.backgroundTintList = resources.getColorStateList(R.color.gray1)
        bind.btnSortTglKadaluarsa.backgroundTintList = resources.getColorStateList(R.color.gray1)

        bind.btnSortStok.setOnClickListener {
            bind.btnSortStok.backgroundTintList = resources.getColorStateList(R.color.colorPrimary)
            bind.btnSortPoin.backgroundTintList = resources.getColorStateList(R.color.gray1)
            bind.btnSortTglKadaluarsa.backgroundTintList = resources.getColorStateList(R.color.gray1)

            when (viewModel.sortProduk) {
                "" -> {
                    bind.btnSortStok.setImageResource(R.drawable.ic_up_white)
                    viewModel.sortProduk = "STOK_DESC"
                    filterDaftarProduk()
                }
                "STOK_DESC" -> {
                    bind.btnSortStok.setImageResource(R.drawable.ic_down_white)
                    viewModel.sortProduk = "STOK_ASC"
                    filterDaftarProduk()
                }
                else -> {
                    bind.btnSortStok.setImageResource(R.drawable.ic_up_white)
                    viewModel.sortProduk = "STOK_DESC"
                    filterDaftarProduk()
                }
            }
        }

        bind.btnSortPoin.setOnClickListener {
            bind.btnSortStok.backgroundTintList = resources.getColorStateList(R.color.gray1)
            bind.btnSortPoin.backgroundTintList = resources.getColorStateList(R.color.colorPrimary)
            bind.btnSortTglKadaluarsa.backgroundTintList = resources.getColorStateList(R.color.gray1)

            when (viewModel.sortProduk) {
                "" -> {
                    bind.btnSortPoin.setImageResource(R.drawable.ic_up_white)
                    viewModel.sortProduk = "POIN_DESC"
                    filterDaftarProduk()
                }
                "POIN_DESC" -> {
                    bind.btnSortPoin.setImageResource(R.drawable.ic_down_white)
                    viewModel.sortProduk = "POIN_ASC"
                    filterDaftarProduk()
                }
                else -> {
                    bind.btnSortPoin.setImageResource(R.drawable.ic_up_white)
                    viewModel.sortProduk = "POIN_DESC"
                    filterDaftarProduk()
                }
            }
        }

        bind.btnSortTglKadaluarsa.setOnClickListener {
            bind.btnSortStok.backgroundTintList = resources.getColorStateList(R.color.gray1)
            bind.btnSortPoin.backgroundTintList = resources.getColorStateList(R.color.gray1)
            bind.btnSortTglKadaluarsa.backgroundTintList = resources.getColorStateList(R.color.colorPrimary)

            when (viewModel.sortProduk) {
                "" -> {
                    bind.btnSortTglKadaluarsa.setImageResource(R.drawable.ic_up_white)
                    viewModel.sortProduk = "TANGGAL_DESC"
                    filterDaftarProduk()
                }
                "TANGGAL_DESC" -> {
                    bind.btnSortTglKadaluarsa.setImageResource(R.drawable.ic_down_white)
                    viewModel.sortProduk = "TANGGAL_ASC"
                    filterDaftarProduk()
                }
                else -> {
                    bind.btnSortTglKadaluarsa.setImageResource(R.drawable.ic_up_white)
                    viewModel.sortProduk = "TANGGAL_DESC"
                    filterDaftarProduk()
                }
            }
        }
    }

    private fun filterDaftarProduk(){
        viewModel.startPage = 0
        viewModel.listProduk.clear()
        viewModel.adapterProduk.notifyDataSetChanged()
        bind.swipeRefresh.isRefreshing = false
        viewModel.getDaftarProdukByPelanggan(
            if (viewModel.idSubKategori == 0) "" else viewModel.idSubKategori.toString()
        )
    }
}