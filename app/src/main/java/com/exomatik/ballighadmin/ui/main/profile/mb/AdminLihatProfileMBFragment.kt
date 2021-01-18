package com.exomatik.ballighadmin.ui.main.profile.mb

import androidx.navigation.fragment.findNavController
import com.exomatik.balligh.R
import com.exomatik.balligh.base.BaseFragmentBind
import com.exomatik.balligh.databinding.FragmentAdminLihatProfileMbBinding
import com.exomatik.balligh.model.ModelDataMuballigh
import com.exomatik.balligh.model.ModelUser

class AdminLihatProfileMBFragment : BaseFragmentBind<FragmentAdminLihatProfileMbBinding>(){
    override fun getLayoutResource(): Int = R.layout.fragment_admin_lihat_profile_mb
    private lateinit var viewModel: AdminLihatProfileMBViewModel

    override fun myCodeHere() {
        setUpDataBinding()
        setAdapterFoto()
    }

    private fun setUpDataBinding() {
        bind.lifecycleOwner = this
        val idMuballigh = this.arguments?.getString("idMuballigh")
        val dataMuballigh = this.arguments?.getParcelable<ModelDataMuballigh>("dataMuballigh")
        val dataUserMuballigh = this.arguments?.getParcelable<ModelUser>("dataUser")

        viewModel =
            AdminLihatProfileMBViewModel(
                context,
                activity,
                findNavController()
            )
        bind.viewModel = viewModel

        if (dataMuballigh != null && dataUserMuballigh != null){
            viewModel.dataUser.value = dataUserMuballigh
            viewModel.dataMuballigh.value = dataMuballigh
            viewModel.setDataMuballigh()
            viewModel.setDataUser()
        } else if (dataMuballigh != null && dataUserMuballigh == null){
            viewModel.dataMuballigh.value = dataMuballigh
            viewModel.setDataMuballigh()
            viewModel.getDataPengurus(dataMuballigh.idMuballigh)
        } else if (dataMuballigh == null && dataUserMuballigh != null){
            viewModel.dataUser.value = dataUserMuballigh
            viewModel.setDataUser()
            viewModel.getDataMuballigh(dataUserMuballigh.idMuballigh, false)
        } else{
            try {
                viewModel.getDataMuballigh(idMuballigh?:throw Exception("Error, terjadi kesalahan database"), true)
            }catch (e: Exception){
                viewModel.isShowLoading.value = false
                viewModel.message.value = e.message
            }
        }
        viewModel.initAdapter()
        setAdapterKualifikasi()
        setAdapterTabligh()
    }

    private fun setAdapterFoto() {
        bind.viewPager.offscreenPageLimit = 0
        bind.viewPager.adapter = viewModel.adapterSampul
        bind.dotsIndicator.setViewPager(bind.viewPager)
    }

    private fun setAdapterKualifikasi() {
        bind.rcKualifikasi.setHasFixedSize(true)
        bind.rcKualifikasi.layoutManager = viewModel.layoutManagerKualifikasi
        bind.rcKualifikasi.adapter = viewModel.adapterKualifikasi
    }

    private fun setAdapterTabligh() {
        bind.rcTabligh.setHasFixedSize(true)
        bind.rcTabligh.layoutManager = viewModel.layoutManagerTabligh
        bind.rcTabligh.adapter = viewModel.adapterTabligh
    }
}
