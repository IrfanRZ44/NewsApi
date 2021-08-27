package id.telkomsel.merchant.ui.pelanggan.beranda

import android.app.SearchManager
import android.content.Context
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList
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
    private lateinit var rfabHelper: RapidFloatingActionHelper

    override fun myCodeHere() {
        setHasOptionsMenu(true)

        init()
    }

    fun init(){
        bind.lifecycleOwner = this
        viewModel = BerandaPelangganViewModel(
            findNavController(), activity, context, bind.rcKategori, bind.rcProduk, savedData,
            bind.viewPager, bind.dotsIndicator
        )
        bind.viewModel = viewModel

        viewModel.initHeader(bind.cardHeader)
        viewModel.initAdapterKategori()
        viewModel.initAdapterProduk()
        viewModel.showDialogFilter(bind.root, layoutInflater)
        floatingAction()

        viewModel.getDataKategori()
        viewModel.getDaftarProdukByPelanggan(
            if (viewModel.idSubKategori == 0) "" else viewModel.idSubKategori.toString(), ""
        )

        bind.swipeRefresh.setOnRefreshListener {
            viewModel.startPage = 0
            viewModel.listProduk.clear()
            viewModel.adapterProduk.notifyDataSetChanged()
            bind.swipeRefresh.isRefreshing = false
            viewModel.getDaftarProdukByPelanggan(
                if (viewModel.idSubKategori == 0) "" else viewModel.idSubKategori.toString(), ""
            )
        }

        bind.rcProduk.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE && viewModel.isShowLoading.value == false) {
                    viewModel.isShowLoading.value = true
                    viewModel.getDaftarProdukByPelanggan(
                        if (viewModel.idSubKategori == 0) "" else viewModel.idSubKategori.toString(), ""
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
                        if (viewModel.idSubKategori == 0) "" else viewModel.idSubKategori.toString(), ""
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
                if (viewModel.idSubKategori == 0) "" else viewModel.idSubKategori.toString(), ""
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

    private fun floatingAction() {
        val rfaContent = RapidFloatingActionContentLabelList(context)
        val tempItem = makeFABItem("Stok Terbanyak", R.drawable.ic_down_white,
            "Poin Terbanyak", R.drawable.ic_up_white,
        )

        rfaContent.setItems(tempItem).setIconShadowColor(0xff888888.toInt())

        rfabHelper = RapidFloatingActionHelper(
            context,
            bind.rfaLayout,
            bind.rfaBtn,
            rfaContent
        ).build()

        rfaContent.setOnRapidFloatingActionContentLabelListListener(object :
            RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener<Any> {
            override fun onRFACItemLabelClick(position: Int, item: RFACLabelItem<Any>?) {
                rfabHelper.toggleContent()

                when(position) {
                    0 -> {
                        if (item?.label == "Stok Terbanyak"){
                            val newItem = makeFABItem("Stok Terendah", R.drawable.ic_down_white,
                                "Poin Terbanyak", R.drawable.ic_up_white,
                            )
                            rfaContent.setItems(newItem).setIconShadowColor(0xff888888.toInt())
                            rfabHelper.setRfaContent(rfaContent).build()
                            filterDaftarProduk("DESC")
                        }
                        else{
                            val newItem = makeFABItem("Stok Terbanyak", R.drawable.ic_up_white,
                                "Poin Terbanyak", R.drawable.ic_up_white,
                            )
                            rfaContent.setItems(newItem).setIconShadowColor(0xff888888.toInt())
                            rfabHelper.setRfaContent(rfaContent).build()

                            filterDaftarProduk("ASC")
                        }
                    }
                }
                rfabHelper.toggleContent()
            }

            override fun onRFACItemIconClick(position: Int, item: RFACLabelItem<Any>?) {
                rfabHelper.toggleContent()

                when(position) {
                    0 -> {
                        if (item?.label == "Stok Terbanyak"){
                            val newItem = makeFABItem("Stok Terendah", R.drawable.ic_down_white,
                                "Poin Terbanyak", R.drawable.ic_up_white,
                            )
                            rfaContent.setItems(newItem).setIconShadowColor(0xff888888.toInt())
                            rfabHelper.setRfaContent(rfaContent).build()
                            filterDaftarProduk("DESC")
                        }
                        else{
                            val newItem = makeFABItem("Stok Terbanyak", R.drawable.ic_up_white,
                                "Poin Terbanyak", R.drawable.ic_up_white,
                            )
                            rfaContent.setItems(newItem).setIconShadowColor(0xff888888.toInt())
                            rfabHelper.setRfaContent(rfaContent).build()

                            filterDaftarProduk("ASC")
                        }
                    }
                }
                rfabHelper.toggleContent()
            }
        })
    }

    private fun makeFABItem(stok: String, icStok: Int, poin: String, icPoin: Int) : List<RFACLabelItem<Int>> {
        return listOf(
            RFACLabelItem<Int>()
                .setLabel(stok)
                .setResId(icStok)
                .setIconNormalColor(0xff52af44.toInt())
                .setIconPressedColor(0xff3E8534.toInt())
                .setWrapper(0),
            RFACLabelItem<Int>()
                .setLabel(poin)
                .setResId(icPoin)
                .setIconNormalColor(0xff52af44.toInt())
                .setIconPressedColor(0xff3E8534.toInt())
                .setWrapper(0)
        )
    }

    private fun filterDaftarProduk(stok: String?){
        viewModel.startPage = 0
        viewModel.listProduk.clear()
        viewModel.adapterProduk.notifyDataSetChanged()
        bind.swipeRefresh.isRefreshing = false
        viewModel.getDaftarProdukByPelanggan(
            if (viewModel.idSubKategori == 0) "" else viewModel.idSubKategori.toString(), stok
        )
    }
}