package com.exomatik.ballighadmin.ui.main.requestMJ

import androidx.navigation.fragment.findNavController
import com.exomatik.ballighadmin.R
import com.exomatik.ballighadmin.base.BaseFragmentBind
import com.exomatik.ballighadmin.databinding.FragmentMjRequestBinding
import com.exomatik.ballighadmin.utils.FirebaseUtils

class RequestMJFragment : BaseFragmentBind<FragmentMjRequestBinding>() {
    override fun getLayoutResource(): Int = R.layout.fragment_mj_request
    private lateinit var viewModel: RequestMJViewModel

    override fun myCodeHere() {
        init()
        onCLick()
    }

    private fun init(){
        bind.lifecycleOwner = this
        viewModel = RequestMJViewModel(bind.rcChat, context, findNavController())
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

