package id.telkomsel.merchant.ui.pelanggan.beranda

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
    }

    fun init(){
        bind.lifecycleOwner = this
        viewModel = BerandaPelangganViewModel(
            findNavController(), activity, bind.rcKategori, bind.rcProduk, savedData
        )
        bind.viewModel = viewModel
        viewModel.initAdapterKategori()
        viewModel.initAdapterProduk()
        viewModel.showDialogFilter(bind.root, layoutInflater)

        viewModel.getDataKategori()
        viewModel.getDaftarProdukByPelanggan(
            "",
            if (viewModel.idSubKategori == 0) "" else viewModel.idSubKategori.toString()
        )

        bind.swipeRefresh.setOnRefreshListener {
            viewModel.startPage = 0
            viewModel.listProduk.clear()
            viewModel.adapterProduk.notifyDataSetChanged()
            bind.swipeRefresh.isRefreshing = false
            viewModel.getDaftarProdukByPelanggan(
                "",
                if (viewModel.idSubKategori == 0) "" else viewModel.idSubKategori.toString()
            )
        }

        bind.rcProduk.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE && viewModel.isShowLoading.value == false) {
                    viewModel.isShowLoading.value = true
                    viewModel.getDaftarProdukByPelanggan(
                        "",
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
                    viewModel.getDaftarProdukByPelanggan(
                        query,
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
            viewModel.getDaftarProdukByPelanggan(
                "",
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
}