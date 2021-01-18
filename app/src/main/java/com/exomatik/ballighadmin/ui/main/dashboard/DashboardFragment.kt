package com.exomatik.ballighadmin.ui.main.dashboard

import com.exomatik.ballighadmin.R
import com.exomatik.ballighadmin.base.BaseFragmentBind
import androidx.navigation.fragment.findNavController
import com.exomatik.ballighadmin.databinding.FragmentDashboardBinding
import com.exomatik.ballighadmin.utils.FirebaseUtils

class DashboardFragment : BaseFragmentBind<FragmentDashboardBinding>() {
    override fun getLayoutResource(): Int = R.layout.fragment_dashboard
    private lateinit var viewModel: DashboardViewModel

    override fun myCodeHere() {
        init()
        onCLick()
    }

    private fun init(){
        bind.lifecycleOwner = this
        viewModel = DashboardViewModel(bind.rcMingguan, context, findNavController())
        bind.viewModel = viewModel
        viewModel.initAdapter()
        viewModel.getListUser()
        bind.swipeRefresh.isRefreshing = false
    }

    private fun onCLick() {
        bind.swipeRefresh.setOnRefreshListener {
            viewModel.getListUser()
            bind.swipeRefresh.isRefreshing = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        FirebaseUtils.stopRefreshAfiliasi2()
    }
}
