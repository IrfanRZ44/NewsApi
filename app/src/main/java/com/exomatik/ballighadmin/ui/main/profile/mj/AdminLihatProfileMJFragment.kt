package com.exomatik.ballighadmin.ui.main.profile.mj

import androidx.navigation.fragment.findNavController
import com.exomatik.balligh.R
import com.exomatik.balligh.base.BaseFragmentBind
import com.exomatik.balligh.databinding.FragmentAdminLihatProfileMjBinding
import com.exomatik.balligh.model.ModelDataMasjid

class AdminLihatProfileMJFragment : BaseFragmentBind<FragmentAdminLihatProfileMjBinding>(){
    override fun getLayoutResource(): Int = R.layout.fragment_admin_lihat_profile_mj
    private lateinit var viewModel: AdminLihatProfileMJViewModel

    override fun myCodeHere() {
        setUpDataBinding()
        setAdapterFoto()
    }

    private fun setUpDataBinding() {
        bind.lifecycleOwner = this
        val idMasjid = this.arguments?.getString("idMasjid")
        val dataMasjid = this.arguments?.getParcelable<ModelDataMasjid>("dataMasjid")
        viewModel = AdminLihatProfileMJViewModel(context, activity, findNavController())
        bind.viewModel = viewModel
        bind.lifecycleOwner = this
        viewModel.isShowLoading.value = true
        try {
            if (dataMasjid != null) {
                viewModel.dataMasjid.value = dataMasjid
                viewModel.setDataMasjid()
                viewModel.getDataPengurus(
                    viewModel.dataMasjid.value?.idMasjid?: throw Exception("Error, mohon login ulang")
                )

            } else {
                viewModel.getDataMasjid(idMasjid ?: throw Exception("Error, mohon login ulang"))
            }
        } catch (e: Exception) {
            viewModel.isShowLoading.value = false
            viewModel.message.value = e.message
        }
    }

    private fun setAdapterFoto() {
        viewModel.initAdapter()
        bind.viewPagerPj.offscreenPageLimit = 0
        bind.viewPagerPj.adapter = viewModel.adapterSampul
        bind.viewPagerPj.currentItem = 0
        bind.dotsIndicatorPj.setViewPager(bind.viewPagerPj)
    }
}
