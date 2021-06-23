package id.telkomsel.merchant.ui.merchant.produk

import android.app.Activity
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
import id.telkomsel.merchant.databinding.FragmentDaftarProdukBinding
import id.telkomsel.merchant.utils.adapter.dismissKeyboard

class DaftarProdukFragment : BaseFragmentBind<FragmentDaftarProdukBinding>() {
    override fun getLayoutResource(): Int = R.layout.fragment_daftar_produk
    lateinit var viewModel: DaftarProdukViewModel
    private var searchView : SearchView? = null
    private var queryTextListener : SearchView.OnQueryTextListener? = null
    private var onCloseListener : SearchView.OnCloseListener? = null

    override fun myCodeHere() {
        supportActionBar?.show()
        setHasOptionsMenu(true)
        init()
    }

    fun init(){
        bind.lifecycleOwner = this
        viewModel = DaftarProdukViewModel(findNavController(), activity, bind.rcRequest, savedData)
        bind.viewModel = viewModel
        viewModel.initAdapter()

        viewModel.listRequest.clear()
        viewModel.adapter.notifyDataSetChanged()
        checkCluster()

        bind.swipeRefresh.setOnRefreshListener {
            viewModel.listRequest.clear()
            viewModel.adapter.notifyDataSetChanged()
            bind.swipeRefresh.isRefreshing = false
            checkCluster()
        }

        bind.rcRequest.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE && viewModel.isShowLoading.value == false) {
                    viewModel.isShowLoading.value = true
                    checkCluster()
                }
            }
        })
    }

    private fun checkCluster() {
        val cluster = savedData.getDataMerchant()?.cluster
        if (!cluster.isNullOrEmpty()){
            viewModel.getDataKategori()
        }
        else{
            viewModel.message.value = "Error, gagal mendapatkan data cluster Anda"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_search, menu)

        val searchItem = menu.findItem(R.id.actionSearch)
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        searchView = searchItem.actionView as SearchView
        searchView?.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))

        queryTextListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                if (!viewModel.isSearching){
                    viewModel.isSearching = true
                    val act : Activity? = activity
                    act?.let { dismissKeyboard(it) }

                    viewModel.listRequest.clear()
                    viewModel.adapter.notifyDataSetChanged()
                    checkCluster()
                }

                return true
            }
        }

        onCloseListener = SearchView.OnCloseListener {
            viewModel.listRequest.clear()
            viewModel.adapter.notifyDataSetChanged()
            checkCluster()
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
        }
        searchView?.setOnQueryTextListener(queryTextListener)
        searchView?.setOnCloseListener(onCloseListener)
        return super.onOptionsItemSelected(item)
    }
}