package id.telkomsel.merchant.ui.merchant.listProduk

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseFragmentBind
import id.telkomsel.merchant.databinding.FragmentTabProdukBinding
import id.telkomsel.merchant.ui.merchant.listProduk.daftarProduk.DaftarProdukFragment
import id.telkomsel.merchant.utils.Constant
import id.telkomsel.merchant.utils.adapter.SectionsPagerAdapter
import id.telkomsel.merchant.utils.adapter.dismissKeyboard

class TabProdukFragment : BaseFragmentBind<FragmentTabProdukBinding>() {
    private lateinit var viewModel: TabProdukViewModel
    override fun getLayoutResource(): Int = R.layout.fragment_tab_produk
    private val requestProduk = DaftarProdukFragment(Constant.statusRequest, 1, false)
    private val activeProduk = DaftarProdukFragment(Constant.statusActive, 1, false)
    private val declinedProduk = DaftarProdukFragment(Constant.statusDeclined, 1, false)
    private val isKadaluarsaProduk = DaftarProdukFragment(Constant.statusActive, 1, true)
    private val stokHabisProduk = DaftarProdukFragment(Constant.statusActive, 0, false)
    private var searchView : SearchView? = null
    private var queryTextListener : SearchView.OnQueryTextListener? = null
    private var onCloseListener : SearchView.OnCloseListener? = null

    override fun myCodeHere() {
        supportActionBar?.show()
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        setHasOptionsMenu(true)

        init()
        floatingAction()
    }

    private fun init() {
        bind.lifecycleOwner = this
        viewModel = TabProdukViewModel()
        bind.viewModel = viewModel
    }

    @Suppress("DEPRECATION")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewPager(bind.viewPager)
        bind.tabs.setupWithViewPager(bind.viewPager)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Suppress("DEPRECATION")
    private fun setupViewPager(pager: ViewPager) {
        val adapter = SectionsPagerAdapter(childFragmentManager)

        adapter.addFragment(requestProduk, Constant.statusDiproses)
        adapter.addFragment(activeProduk, Constant.statusDisetujui)
        adapter.addFragment(declinedProduk, Constant.statusDitolak)
        adapter.addFragment(isKadaluarsaProduk, Constant.statusPromoSelesai)
        adapter.addFragment(stokHabisProduk, Constant.statusStokHabis)

        pager.adapter = adapter
    }

    private fun floatingAction() {
        val rfaContent = RapidFloatingActionContentLabelList(context)
        val item = listOf(
            RFACLabelItem<Int>()
                .setLabel("Tambah Produk")
                .setResId(R.drawable.ic_add_white)
                .setIconNormalColor(0xffd84315.toInt())
                .setIconPressedColor(0xffbf360c.toInt())
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
                if (bind.tabs.selectedTabPosition == 0){
                    if (!requestProduk.viewModel.isSearching){
                        requestProduk.viewModel.isSearching = true
                        activity?.let { dismissKeyboard(it) }

                        requestProduk.viewModel.startPage = 0
                        requestProduk.viewModel.listProduk.clear()
                        requestProduk.viewModel.adapterProduk.notifyDataSetChanged()
                        requestProduk.viewModel.textSearch = query
                        requestProduk.viewModel.checkCluster()
                    }
                }
                else if (bind.tabs.selectedTabPosition == 1){
                    if (!activeProduk.viewModel.isSearching){
                        activeProduk.viewModel.isSearching = true
                        activity?.let { dismissKeyboard(it) }

                        activeProduk.viewModel.startPage = 0
                        activeProduk.viewModel.listProduk.clear()
                        activeProduk.viewModel.adapterProduk.notifyDataSetChanged()
                        activeProduk.viewModel.textSearch = query
                        activeProduk.viewModel.checkCluster()
                    }
                }
                else if (bind.tabs.selectedTabPosition == 2){
                    if (!declinedProduk.viewModel.isSearching){
                        declinedProduk.viewModel.isSearching = true
                        activity?.let { dismissKeyboard(it) }

                        declinedProduk.viewModel.startPage = 0
                        declinedProduk.viewModel.listProduk.clear()
                        declinedProduk.viewModel.adapterProduk.notifyDataSetChanged()
                        declinedProduk.viewModel.textSearch = query
                        declinedProduk.viewModel.checkCluster()
                    }
                }

                return true
            }
        }

        onCloseListener = SearchView.OnCloseListener {
            when (bind.tabs.selectedTabPosition) {
                0 -> {
                    requestProduk.viewModel.startPage = 0
                    requestProduk.viewModel.listProduk.clear()
                    requestProduk.viewModel.adapterProduk.notifyDataSetChanged()
                    requestProduk.viewModel.textSearch = ""
                    requestProduk.viewModel.checkCluster()
                }
                1 -> {
                    activeProduk.viewModel.startPage = 0
                    activeProduk.viewModel.listProduk.clear()
                    activeProduk.viewModel.adapterProduk.notifyDataSetChanged()
                    activeProduk.viewModel.textSearch = ""
                    activeProduk.viewModel.checkCluster()
                }
                2 -> {
                    declinedProduk.viewModel.startPage = 0
                    declinedProduk.viewModel.listProduk.clear()
                    declinedProduk.viewModel.adapterProduk.notifyDataSetChanged()
                    declinedProduk.viewModel.textSearch = ""
                    declinedProduk.viewModel.checkCluster()
                }
            }
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
                when (bind.tabs.selectedTabPosition) {
                    0 -> {
                        requestProduk.viewModel.btmSheet.show()
                    }
                    1 -> {
                        activeProduk.viewModel.btmSheet.show()
                    }
                    2 -> {
                        declinedProduk.viewModel.btmSheet.show()
                    }
                }
                return false
            }
        }
        searchView?.setOnQueryTextListener(queryTextListener)
        searchView?.setOnCloseListener(onCloseListener)
        return super.onOptionsItemSelected(item)
    }
}