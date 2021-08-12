package id.telkomsel.merchant.ui.merchant.listMerchant

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
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import id.telkomsel.merchant.R
import id.telkomsel.merchant.utils.Constant
import com.google.android.material.tabs.TabLayout
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList
import id.telkomsel.merchant.databinding.FragmentTabMerchantBinding
import id.telkomsel.merchant.base.BaseFragmentBind
import id.telkomsel.merchant.utils.adapter.SectionsPagerAdapter
import id.telkomsel.merchant.utils.adapter.dismissKeyboard

class TabMerchantFragment : BaseFragmentBind<FragmentTabMerchantBinding>() {
    private lateinit var viewModel: TabMerchantViewModel
    override fun getLayoutResource(): Int = R.layout.fragment_tab_merchant
    private val requestDaftarMerchant = DaftarMerchantFragment(Constant.statusRequest)
    private val activeDaftarMerchant = DaftarMerchantFragment(Constant.statusActive)
    private val declinedDaftarMerchant = DaftarMerchantFragment(Constant.statusDeclined)
    private var searchView : SearchView? = null
    private var queryTextListener : SearchView.OnQueryTextListener? = null
    private var onCloseListener : SearchView.OnCloseListener? = null

    override fun myCodeHere() {
        supportActionBar?.show()
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        setHasOptionsMenu(true)

        init()
        if (savedData.getDataMerchant()?.level == Constant.levelSBP){
            bind.rfaLayout.visibility = View.VISIBLE
            floatingAction()
        }
        else{
            bind.rfaLayout.visibility = View.GONE
        }
    }

    private fun init() {
        bind.lifecycleOwner = this
        viewModel = TabMerchantViewModel()
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
        adapter.addFragment(requestDaftarMerchant, Constant.statusDiproses)
        adapter.addFragment(activeDaftarMerchant, Constant.statusDisetujui)
        adapter.addFragment(declinedDaftarMerchant, Constant.statusDitolak)

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

    private fun floatingAction() {
        val rfaContent = RapidFloatingActionContentLabelList(context)
        val item = listOf(
            RFACLabelItem<Int>()
                .setLabel("Tambah Merchant")
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
                        findNavController().navigate(R.id.addMerchantFragment)
                    }
                }
                rfabHelper.toggleContent()
            }

            override fun onRFACItemIconClick(position: Int, item: RFACLabelItem<Any>?) {
                when(position) {
                    0 -> {
                        findNavController().navigate(R.id.addMerchantFragment)
                    }
                }
                rfabHelper.toggleContent()
            }
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
                    if (!requestDaftarMerchant.viewModel.isSearching){
                        requestDaftarMerchant.viewModel.isSearching = true
                        val act : Activity? = activity
                        act?.let { dismissKeyboard(it) }

                        requestDaftarMerchant.viewModel.startPage = 0
                        requestDaftarMerchant.viewModel.listRequest.clear()
                        requestDaftarMerchant.viewModel.adapter.notifyDataSetChanged()
                        requestDaftarMerchant.checkCluster(query)
                    }
                }
                else if (bind.tabs.selectedTabPosition == 1){
                    if (!activeDaftarMerchant.viewModel.isSearching){
                        activeDaftarMerchant.viewModel.isSearching = true
                        val act : Activity? = activity
                        act?.let { dismissKeyboard(it) }

                        activeDaftarMerchant.viewModel.startPage = 0
                        activeDaftarMerchant.viewModel.listRequest.clear()
                        activeDaftarMerchant.viewModel.adapter.notifyDataSetChanged()
                        activeDaftarMerchant.checkCluster(query)
                    }
                }
                else if (bind.tabs.selectedTabPosition == 2){
                    if (!declinedDaftarMerchant.viewModel.isSearching){
                        declinedDaftarMerchant.viewModel.isSearching = true
                        val act : Activity? = activity
                        act?.let { dismissKeyboard(it) }

                        declinedDaftarMerchant.viewModel.startPage = 0
                        declinedDaftarMerchant.viewModel.listRequest.clear()
                        declinedDaftarMerchant.viewModel.adapter.notifyDataSetChanged()
                        declinedDaftarMerchant.checkCluster(query)
                    }
                }


                return true
            }
        }

        onCloseListener = SearchView.OnCloseListener {
            when (bind.tabs.selectedTabPosition) {
                0 -> {
                    requestDaftarMerchant.viewModel.startPage = 0
                    requestDaftarMerchant.viewModel.listRequest.clear()
                    requestDaftarMerchant.viewModel.adapter.notifyDataSetChanged()
                    requestDaftarMerchant.checkCluster("")
                }
                1 -> {
                    activeDaftarMerchant.viewModel.startPage = 0
                    activeDaftarMerchant.viewModel.listRequest.clear()
                    activeDaftarMerchant.viewModel.adapter.notifyDataSetChanged()
                    activeDaftarMerchant.checkCluster("")
                }
                2 -> {
                    declinedDaftarMerchant.viewModel.startPage = 0
                    declinedDaftarMerchant.viewModel.listRequest.clear()
                    declinedDaftarMerchant.viewModel.adapter.notifyDataSetChanged()
                    declinedDaftarMerchant.checkCluster("")
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