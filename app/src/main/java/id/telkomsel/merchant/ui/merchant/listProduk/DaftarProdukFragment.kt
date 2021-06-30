package id.telkomsel.merchant.ui.merchant.listProduk

import android.app.SearchManager
import android.content.Context
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList
import id.telkomsel.merchant.R
import id.telkomsel.merchant.databinding.FragmentDaftarProdukBinding
import id.telkomsel.merchant.base.BaseFragmentBind
import id.telkomsel.merchant.utils.Constant
import id.telkomsel.merchant.utils.adapter.dismissKeyboard

class DaftarProdukFragment(private val statusRequest: String) : BaseFragmentBind<FragmentDaftarProdukBinding>() {
    override fun getLayoutResource(): Int = R.layout.fragment_daftar_produk
    lateinit var viewModel: DaftarProdukViewModel
    private var searchView : SearchView? = null
    private var queryTextListener : SearchView.OnQueryTextListener? = null
    private var onCloseListener : SearchView.OnCloseListener? = null

    override fun myCodeHere() {
        supportActionBar?.show()
        supportActionBar?.title = "Produk"
        setHasOptionsMenu(true)
        init()
    }

    fun init(){
        bind.lifecycleOwner = this
        viewModel = DaftarProdukViewModel(findNavController(), activity, bind.rcKategori, bind.rcProduk, statusRequest, savedData)
        bind.viewModel = viewModel
        viewModel.initAdapterKategori()
        viewModel.initAdapterProduk()

        viewModel.getDataKategori()
        viewModel.checkCluster("")

        if (savedData.getDataMerchant()?.level == Constant.levelCSO || savedData.getDataMerchant()?.level == Constant.levelSBP){
            bind.rfaLayout.visibility = View.VISIBLE
            floatingAction()
        }
        else{
            bind.rfaLayout.visibility = View.GONE
        }

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
                    activity?.let { dismissKeyboard(it) }

                    viewModel.startPage = 0
                    viewModel.listProduk.clear()
                    viewModel.adapterProduk.notifyDataSetChanged()
                    viewModel.checkCluster(query)
                }

                return true
            }
        }

        onCloseListener = SearchView.OnCloseListener {
            viewModel.startPage = 0
            viewModel.listProduk.clear()
            viewModel.adapterProduk.notifyDataSetChanged()
            viewModel.checkCluster("")
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

    private fun floatingAction() {
        val rfaContent = RapidFloatingActionContentLabelList(context)
        val item = listOf(
            RFACLabelItem<Int>()
                .setLabel("Tambah Produk")
                .setResId(R.drawable.ic_add_white)
                .setIconNormalColor(0xff52af44.toInt())
                .setIconPressedColor(0xff3E8534.toInt())
                .setWrapper(0)

        )

        rfaContent.setItems(item).setIconShadowColor(0xff888888.toInt())

        val rfabHelper = RapidFloatingActionHelper(
            context,
            bind.rfaLayout,
            bind.rfaBtn,
            rfaContent
        ).build()

        rfaContent.setOnRapidFloatingActionContentLabelListListener(object :
            RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener<Any> {
            override fun onRFACItemLabelClick(position: Int, item: RFACLabelItem<Any>?) {
                when(position) {
                    0 -> {
                        findNavController().navigate(R.id.addProdukFragment)
                    }
                }
                rfabHelper.toggleContent()
            }

            override fun onRFACItemIconClick(position: Int, item: RFACLabelItem<Any>?) {
                when(position) {
                    0 -> {
                        findNavController().navigate(R.id.addProdukFragment)
                    }
                }
                rfabHelper.toggleContent()
            }
        })
    }
}