package id.telkomsel.merchandise.ui.sales.listSales

import android.annotation.SuppressLint
import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import id.telkomsel.merchandise.R
import id.telkomsel.merchandise.base.BaseFragmentBind
import id.telkomsel.merchandise.databinding.FragmentTabSalesBinding
import id.telkomsel.merchandise.utils.Constant
import id.telkomsel.merchandise.utils.adapter.SectionsPagerAdapter
import id.telkomsel.merchandise.utils.adapter.dismissKeyboard

class TabSalesFragment : BaseFragmentBind<FragmentTabSalesBinding>() {
    private lateinit var viewModel: TabSalesViewModel
    override fun getLayoutResource(): Int = R.layout.fragment_tab_sales
    private val requestDaftarSales = DaftarSalesFragment(Constant.statusRequest)
    private val activeDaftarSales = DaftarSalesFragment(Constant.statusActive)
    private val declinedDaftarSales = DaftarSalesFragment(Constant.statusDeclined)
    private var searchView : SearchView? = null
    private var queryTextListener : SearchView.OnQueryTextListener? = null
    private var onCloseListener : SearchView.OnCloseListener? = null

    override fun myCodeHere() {
        supportActionBar?.show()
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        setHasOptionsMenu(true)

        init()
        bind.rfaLayout.visibility = View.GONE
    }

    private fun init() {
        bind.lifecycleOwner = this
        viewModel = TabSalesViewModel()
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
        adapter.addFragment(requestDaftarSales, Constant.statusDiproses)
        adapter.addFragment(activeDaftarSales, Constant.statusDisetujui)
        adapter.addFragment(declinedDaftarSales, Constant.statusDitolak)

        pager.adapter = adapter

        bind.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {
                        bind.tabs.getTabAt(0)?.icon?.setColorFilter(resources.getColor(R.color.white), PorterDuff.Mode.SRC_IN)
                        bind.tabs.getTabAt(1)?.icon?.setColorFilter(resources.getColor(R.color.gray1), PorterDuff.Mode.SRC_IN)
                        bind.tabs.getTabAt(2)?.icon?.setColorFilter(resources.getColor(R.color.gray1), PorterDuff.Mode.SRC_IN)
                    }
                    1-> {
                        bind.tabs.getTabAt(0)?.icon?.setColorFilter(resources.getColor(R.color.gray1), PorterDuff.Mode.SRC_IN)
                        bind.tabs.getTabAt(1)?.icon?.setColorFilter(resources.getColor(R.color.white), PorterDuff.Mode.SRC_IN)
                        bind.tabs.getTabAt(2)?.icon?.setColorFilter(resources.getColor(R.color.gray1), PorterDuff.Mode.SRC_IN)
                    }
                    else -> {
                        bind.tabs.getTabAt(0)?.icon?.setColorFilter(resources.getColor(R.color.gray1), PorterDuff.Mode.SRC_IN)
                        bind.tabs.getTabAt(1)?.icon?.setColorFilter(resources.getColor(R.color.gray1), PorterDuff.Mode.SRC_IN)
                        bind.tabs.getTabAt(2)?.icon?.setColorFilter(resources.getColor(R.color.white), PorterDuff.Mode.SRC_IN)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_search, menu)

        val searchItem = menu.findItem(R.id.actionSearch)
        val filterItem = menu.findItem(R.id.actionFilter)
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        if (filterItem != null){
            filterItem.isVisible = false
        }
        searchView = searchItem.actionView as SearchView
        searchView?.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))

        queryTextListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                if (bind.tabs.selectedTabPosition == 0){
                    if (!requestDaftarSales.viewModel.isSearching){
                        requestDaftarSales.viewModel.isSearching = true
                        val act : Activity? = activity
                        act?.let { dismissKeyboard(it) }

                        requestDaftarSales.viewModel.startPage = 0
                        requestDaftarSales.viewModel.listRequest.clear()
                        requestDaftarSales.viewModel.adapter.notifyDataSetChanged()
                        requestDaftarSales.checkCluster(query)
                    }
                }
                else if (bind.tabs.selectedTabPosition == 1){
                    if (!activeDaftarSales.viewModel.isSearching){
                        activeDaftarSales.viewModel.isSearching = true
                        val act : Activity? = activity
                        act?.let { dismissKeyboard(it) }

                        activeDaftarSales.viewModel.startPage = 0
                        activeDaftarSales.viewModel.listRequest.clear()
                        activeDaftarSales.viewModel.adapter.notifyDataSetChanged()
                        activeDaftarSales.checkCluster(query)
                    }
                }
                else if (bind.tabs.selectedTabPosition == 2){
                    if (!declinedDaftarSales.viewModel.isSearching){
                        declinedDaftarSales.viewModel.isSearching = true
                        val act : Activity? = activity
                        act?.let { dismissKeyboard(it) }

                        declinedDaftarSales.viewModel.startPage = 0
                        declinedDaftarSales.viewModel.listRequest.clear()
                        declinedDaftarSales.viewModel.adapter.notifyDataSetChanged()
                        declinedDaftarSales.checkCluster(query)
                    }
                }


                return true
            }
        }

        onCloseListener = SearchView.OnCloseListener {
            when (bind.tabs.selectedTabPosition) {
                0 -> {
                    requestDaftarSales.viewModel.startPage = 0
                    requestDaftarSales.viewModel.listRequest.clear()
                    requestDaftarSales.viewModel.adapter.notifyDataSetChanged()
                    requestDaftarSales.checkCluster("")
                }
                1 -> {
                    activeDaftarSales.viewModel.startPage = 0
                    activeDaftarSales.viewModel.listRequest.clear()
                    activeDaftarSales.viewModel.adapter.notifyDataSetChanged()
                    activeDaftarSales.checkCluster("")
                }
                2 -> {
                    declinedDaftarSales.viewModel.startPage = 0
                    declinedDaftarSales.viewModel.listRequest.clear()
                    declinedDaftarSales.viewModel.adapter.notifyDataSetChanged()
                    declinedDaftarSales.checkCluster("")
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
        }
        searchView?.setOnQueryTextListener(queryTextListener)
        searchView?.setOnCloseListener(onCloseListener)
        return super.onOptionsItemSelected(item)
    }
}