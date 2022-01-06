package id.telkomsel.merchandise.ui.sales.listStore

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList
import id.telkomsel.merchandise.R
import id.telkomsel.merchandise.base.BaseFragmentBind
import id.telkomsel.merchandise.databinding.FragmentTabStoreBinding
import id.telkomsel.merchandise.ui.sales.listStore.daftarStore.DaftarStoreFragment
import id.telkomsel.merchandise.utils.Constant
import id.telkomsel.merchandise.utils.adapter.SectionsPagerAdapter
import id.telkomsel.merchandise.utils.adapter.dismissKeyboard

class TabStoreFragment : BaseFragmentBind<FragmentTabStoreBinding>() {
    private lateinit var viewModel: TabStoreViewModel
    override fun getLayoutResource(): Int = R.layout.fragment_tab_store
    private val requestStore = DaftarStoreFragment(Constant.statusRequest)
    private val activeStore = DaftarStoreFragment(Constant.statusActive)
    private val declinedStore = DaftarStoreFragment(Constant.statusDeclined)
    private var searchView : SearchView? = null
    private var queryTextListener : SearchView.OnQueryTextListener? = null
    private var onCloseListener : SearchView.OnCloseListener? = null

    override fun myCodeHere() {
        supportActionBar?.show()
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        setHasOptionsMenu(true)

        init()

        if (savedData.getDataSales()?.level == Constant.levelSales){
            bind.rfaLayout.visibility = View.VISIBLE
            floatingAction()
        }
        else{
            bind.rfaLayout.visibility = View.GONE
        }
    }

    private fun init() {
        bind.lifecycleOwner = this
        viewModel = TabStoreViewModel()
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

        adapter.addFragment(requestStore, Constant.statusDiproses)
        adapter.addFragment(activeStore, Constant.statusDisetujui)
        adapter.addFragment(declinedStore, Constant.statusDitolak)

        pager.adapter = adapter
    }

    private fun floatingAction() {
        val rfaContent = RapidFloatingActionContentLabelList(context)
        val item = listOf(
            RFACLabelItem<Int>()
                .setLabel("Tambah Store")
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
                        findNavController().navigate(R.id.addStoreFragment)
                    }
                }
                rfabHelper.toggleContent()
            }

            override fun onRFACItemIconClick(position: Int, item: RFACLabelItem<Any>?) {
                when(position) {
                    0 -> {
                        findNavController().navigate(R.id.addStoreFragment)
                    }
                }
                rfabHelper.toggleContent()
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
                if (bind.tabs.selectedTabPosition == 0){
                    if (!requestStore.viewModel.isSearching){
                        requestStore.viewModel.isSearching = true
                        activity?.let { dismissKeyboard(it) }

                        requestStore.viewModel.startPage = 0
                        requestStore.viewModel.listStore.clear()
                        requestStore.viewModel.adapterStore.notifyDataSetChanged()
                        requestStore.viewModel.textSearch = query
                        requestStore.viewModel.checkCluster()
                    }
                }
                else if (bind.tabs.selectedTabPosition == 1){
                    if (!activeStore.viewModel.isSearching){
                        activeStore.viewModel.isSearching = true
                        activity?.let { dismissKeyboard(it) }

                        activeStore.viewModel.startPage = 0
                        activeStore.viewModel.listStore.clear()
                        activeStore.viewModel.adapterStore.notifyDataSetChanged()
                        activeStore.viewModel.textSearch = query
                        activeStore.viewModel.checkCluster()
                    }
                }
                else if (bind.tabs.selectedTabPosition == 2){
                    if (!declinedStore.viewModel.isSearching){
                        declinedStore.viewModel.isSearching = true
                        activity?.let { dismissKeyboard(it) }

                        declinedStore.viewModel.startPage = 0
                        declinedStore.viewModel.listStore.clear()
                        declinedStore.viewModel.adapterStore.notifyDataSetChanged()
                        declinedStore.viewModel.textSearch = query
                        declinedStore.viewModel.checkCluster()
                    }
                }

                return true
            }
        }

        onCloseListener = SearchView.OnCloseListener {
            when (bind.tabs.selectedTabPosition) {
                0 -> {
                    requestStore.viewModel.startPage = 0
                    requestStore.viewModel.listStore.clear()
                    requestStore.viewModel.adapterStore.notifyDataSetChanged()
                    requestStore.viewModel.textSearch = ""
                    requestStore.viewModel.checkCluster()
                }
                1 -> {
                    activeStore.viewModel.startPage = 0
                    activeStore.viewModel.listStore.clear()
                    activeStore.viewModel.adapterStore.notifyDataSetChanged()
                    activeStore.viewModel.textSearch = ""
                    activeStore.viewModel.checkCluster()
                }
                2 -> {
                    declinedStore.viewModel.startPage = 0
                    declinedStore.viewModel.listStore.clear()
                    declinedStore.viewModel.adapterStore.notifyDataSetChanged()
                    declinedStore.viewModel.textSearch = ""
                    declinedStore.viewModel.checkCluster()
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