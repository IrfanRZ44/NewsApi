package com.exomatik.ballighadmin.ui.main.pesan

import android.app.SearchManager
import android.content.Context
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.SearchView
import com.exomatik.ballighadmin.R
import com.exomatik.ballighadmin.base.BaseFragmentBind
import com.exomatik.ballighadmin.databinding.FragmentPesanAdminBinding
import androidx.navigation.fragment.findNavController
import com.exomatik.ballighadmin.utils.FirebaseUtils

class PesanAdminFragment : BaseFragmentBind<FragmentPesanAdminBinding>() {
    override fun getLayoutResource(): Int = R.layout.fragment_pesan_admin
    private lateinit var viewModel: PesanAdminViewModel
    private lateinit var searchView : SearchView
    private var queryTextListener : SearchView.OnQueryTextListener? = null
    private var onCloseListener : SearchView.OnCloseListener? = null

    override fun myCodeHere() {
        setHasOptionsMenu(true)
        init()
        onCLick()
    }

    private fun init(){
        bind.lifecycleOwner = this
        viewModel = PesanAdminViewModel(bind.rcChat, context, findNavController())
        bind.viewModel = viewModel
        viewModel.initAdapter()
        viewModel.getChatMuballigh()

        bind.swipeRefresh.isRefreshing = false
    }

    private fun onCLick() {
        bind.swipeRefresh.setOnRefreshListener {
            FirebaseUtils.stopRefreshListChat2()
            FirebaseUtils.stopRefreshListChat3()
            FirebaseUtils.stopRefreshListChat4()
            viewModel.listChat.clear()
            viewModel.listChatSearch.clear()
            viewModel.getChatMuballigh()
            bind.swipeRefresh.isRefreshing = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_search_pesan, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchManager = activity!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        searchView = searchItem.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity!!.componentName))

        queryTextListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {

                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.listChat.clear()
                viewModel.adapter?.notifyDataSetChanged()

                for (i in viewModel.listNama.indices){
                    if (viewModel.listNama[i].nama.contains(query)){
                        var isNotAdded = true

                        if (viewModel.listChat.size != 0){
                            for (a in viewModel.listChat.indices){
                                if (viewModel.listNama[i].id == viewModel.listChat[a].idTujuan){
                                    isNotAdded = false
                                }
                            }
                        }

                        if (isNotAdded){
                            for (a in viewModel.listChatSearch.indices){
                                if (viewModel.listNama[i].id == viewModel.listChatSearch[a].idTujuan){
                                    viewModel.listChat.add(viewModel.listChatSearch[a])
                                    viewModel.adapter?.notifyDataSetChanged()
                                }
                            }
                        }
                    }
                }

                if (viewModel.listChat.size == 0) viewModel.status.value = "Afwan, tidak ada pesan yang ditemukan"
                else viewModel.status.value = ""
                return true
            }
        }

        onCloseListener = SearchView.OnCloseListener {
            viewModel.listChat.clear()
            viewModel.adapter?.notifyDataSetChanged()

            for (i in viewModel.listChatSearch.indices){
                viewModel.listChat.add(viewModel.listChatSearch[i])
                viewModel.adapter?.notifyDataSetChanged()
            }
            viewModel.cekList()
            false
        }

        searchView.setOnQueryTextListener(queryTextListener)
        searchView.setOnCloseListener(onCloseListener)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_search ->{
                return false
            }
        }
        searchView.setOnQueryTextListener(queryTextListener)
        searchView.setOnCloseListener(onCloseListener)
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        FirebaseUtils.stopRefreshListChat2()
        FirebaseUtils.stopRefreshListChat3()
        FirebaseUtils.stopRefreshListChat4()
    }
}