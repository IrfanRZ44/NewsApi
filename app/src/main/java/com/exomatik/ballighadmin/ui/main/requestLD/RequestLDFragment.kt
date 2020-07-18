package com.exomatik.ballighadmin.ui.main.requestLD

import androidx.navigation.fragment.findNavController
import com.exomatik.ballighadmin.R
import com.exomatik.ballighadmin.base.BaseFragmentBind
import com.exomatik.ballighadmin.databinding.FragmentLdRequestBinding
import com.exomatik.ballighadmin.utils.FirebaseUtils

class RequestLDFragment : BaseFragmentBind<FragmentLdRequestBinding>() {
    override fun getLayoutResource(): Int = R.layout.fragment_ld_request
    private lateinit var viewModel: RequestLDViewModel

    override fun myCodeHere() {
        init()
        onCLick()
    }

    private fun init(){
        bind.lifecycleOwner = this
        viewModel = RequestLDViewModel(bind.rcChat, context, findNavController())
        bind.viewModel = viewModel
        viewModel.initAdapter()
        viewModel.getListLembaga()

        bind.swipeRefresh.isRefreshing = false
    }

    private fun onCLick() {
        bind.swipeRefresh.setOnRefreshListener {
            viewModel.listRequestLD.clear()
            viewModel.adapter.notifyDataSetChanged()
            FirebaseUtils.stopRefreshAfiliasi1()
            viewModel.getListLembaga()
            bind.swipeRefresh.isRefreshing = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        FirebaseUtils.stopRefreshAfiliasi1()
    }
}
