package com.exomatik.ballighadmin.ui.main.listMB

import com.exomatik.ballighadmin.base.BaseFragmentBind
import androidx.navigation.fragment.findNavController
import com.exomatik.ballighadmin.R
import com.exomatik.ballighadmin.databinding.FragmentListMbBinding
import com.exomatik.ballighadmin.utils.FirebaseUtils

class ListMBFragment : BaseFragmentBind<FragmentListMbBinding>() {
    override fun getLayoutResource(): Int = R.layout.fragment_list_mb
    private lateinit var viewModel: ListMBViewModel

    override fun myCodeHere() {
        init()
        onCLick()
    }

    private fun init(){
        bind.lifecycleOwner = this
        viewModel = ListMBViewModel(bind.rcChat, context, findNavController())
        bind.viewModel = viewModel
        viewModel.initAdapter()
        viewModel.getListMuballigh()

        bind.swipeRefresh.isRefreshing = false
    }

    private fun onCLick() {
        bind.swipeRefresh.setOnRefreshListener {
            viewModel.listRequestMB.clear()
            viewModel.adapter.notifyDataSetChanged()
            FirebaseUtils.stopRefreshAfiliasi2()
            viewModel.getListMuballigh()
            bind.swipeRefresh.isRefreshing = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        FirebaseUtils.stopRefreshAfiliasi2()
    }
}
