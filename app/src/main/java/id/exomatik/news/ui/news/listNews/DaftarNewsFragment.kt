package id.exomatik.news.ui.news.listNews

import android.app.SearchManager
import android.content.Context
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import id.exomatik.news.R
import id.exomatik.news.base.BaseFragmentBind
import id.exomatik.news.databinding.FragmentDaftarNewsBinding
import id.exomatik.news.utils.Constant
import id.exomatik.news.utils.adapter.dismissKeyboard

class DaftarNewsFragment : BaseFragmentBind<FragmentDaftarNewsBinding>() {
    override fun getLayoutResource(): Int = R.layout.fragment_daftar_news
    lateinit var viewModel: DaftarNewsViewModel
    private var searchView : SearchView? = null
    private var queryTextListener : SearchView.OnQueryTextListener? = null
    private var onCloseListener : SearchView.OnCloseListener? = null

    override fun myCodeHere() {
        supportActionBar?.show()
        supportActionBar?.title = Constant.menuBeranda
        setHasOptionsMenu(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        init()
    }

    private fun init(){
        bind.lifecycleOwner = this
        viewModel = DaftarNewsViewModel(
            findNavController(), activity, bind.rcKategori, bind.rcNews
        )
        bind.viewModel = viewModel

        viewModel.initAdapterKategoriNews()
        viewModel.initAdapterNews()

        viewModel.getDaftarNews()
        viewModel.showDialogFilter(bind.root, layoutInflater)

        bind.swipeRefresh.setOnRefreshListener {
            viewModel.startPage = 1
            viewModel.listNews.clear()
            viewModel.adapterNews.notifyDataSetChanged()
            bind.swipeRefresh.isRefreshing = false
            viewModel.getDaftarNews()
        }

        bind.rcNews.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE && viewModel.isShowLoading.value == false) {
                    viewModel.isShowLoading.value = true
                    viewModel.getDaftarNews()
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
                activity?.let { dismissKeyboard(it) }

                viewModel.startPage = 1
                viewModel.listNews.clear()
                viewModel.adapterNews.notifyDataSetChanged()
                viewModel.textSearch = query
                viewModel.getDaftarNews()

                return true
            }
        }

        onCloseListener = SearchView.OnCloseListener {
            viewModel.startPage = 1
            viewModel.listNews.clear()
            viewModel.adapterNews.notifyDataSetChanged()
            viewModel.textSearch = ""
            viewModel.getDaftarNews()
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