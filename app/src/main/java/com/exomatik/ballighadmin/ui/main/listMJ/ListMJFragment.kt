package com.exomatik.ballighadmin.ui.main.listMJ

import com.exomatik.ballighadmin.R
import com.exomatik.ballighadmin.base.BaseFragmentBind
import com.exomatik.ballighadmin.databinding.FragmentListMjBinding
import androidx.navigation.fragment.findNavController
import com.exomatik.ballighadmin.utils.FirebaseUtils

class ListMJFragment : BaseFragmentBind<FragmentListMjBinding>() {
    override fun getLayoutResource(): Int = R.layout.fragment_list_mj
    private lateinit var viewModel: ListMJViewModel

    override fun myCodeHere() {
        init()
        onCLick()
    }

    private fun init(){
        bind.lifecycleOwner = this
        viewModel = ListMJViewModel(bind.rcChat, context, findNavController())
        bind.viewModel = viewModel
        viewModel.initAdapter()
        viewModel.getListMasjid()

        bind.swipeRefresh.isRefreshing = false
    }

    private fun onCLick() {
        bind.swipeRefresh.setOnRefreshListener {
            viewModel.listRequestMJ.clear()
            viewModel.adapter.notifyDataSetChanged()
            FirebaseUtils.stopRefreshAfiliasi1()
            viewModel.getListMasjid()
            bind.swipeRefresh.isRefreshing = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        FirebaseUtils.stopRefreshAfiliasi1()
    }
}
