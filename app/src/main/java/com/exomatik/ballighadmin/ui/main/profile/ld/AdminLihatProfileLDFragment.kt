package com.exomatik.ballighadmin.ui.main.profile.ld

import androidx.navigation.fragment.findNavController
import com.exomatik.ballighadmin.R
import com.exomatik.ballighadmin.base.BaseFragmentBind
import com.exomatik.ballighadmin.databinding.FragmentAdminLihatProfileLdBinding
import com.exomatik.ballighadmin.model.ModelDataLembaga
import com.exomatik.ballighadmin.model.ModelUser
import com.exomatik.ballighadmin.utils.Constant.inactive
import com.exomatik.ballighadmin.utils.showLog

class AdminLihatProfileLDFragment : BaseFragmentBind<FragmentAdminLihatProfileLdBinding>() {
    override fun getLayoutResource(): Int = R.layout.fragment_admin_lihat_profile_ld
    private lateinit var viewModel: AdminLihatProfileLDViewModel

    override fun myCodeHere() {
        setUpDataBinding()
        setAdapterFoto()
    }

    private fun setUpDataBinding() {
        bind.lifecycleOwner = this
        viewModel =
            AdminLihatProfileLDViewModel(
                context,
                activity,
                findNavController()
            )
        viewModel.isShowLoading.value = true
        bind.viewModel = viewModel
        val dataLembaga = this.arguments?.getParcelable<ModelDataLembaga>("dataLembaga")
        val dataUser = this.arguments?.getParcelable<ModelUser>("dataUser")
        val idLembaga = this.arguments?.getString("idLembaga")
        try {
            if (dataLembaga != null) {
                viewModel.dataLembaga.value = dataLembaga
                viewModel.setDataLembaga()
                viewModel.getDataPengurus(
                    viewModel.dataLembaga.value?.idLembaga ?: throw Exception("Error, mohon login ulang")
                )
            } else {
                if (dataUser != null) {
                    viewModel.dataUser.value = dataUser
                    viewModel.setDataUser()
                    val id = dataUser.idLembaga
                    if (id == "" || id.isEmpty()){
                        showLog("Id kosong")
                        viewModel.dataLembaga.value = ModelDataLembaga("", "",
                            "Nama Lembaga", "", "",
                            "Provinsi", "Kota", "Kecamatan",
                            "Alamat Lembaga", "",
                            showingAlamatPengurus = false,
                            showingGelarPendidikan = false,
                            showingTTL = false,
                            fotoSKPengurus = "",
                            fotoSuratTugas = "",
                            sampul = null,
                            status = inactive,
                            indexProvinsi_Kota = "",
                            indexProvinsi_Kota_Kecamatan = ""
                        )
                        viewModel.setDataLembaga()
                        viewModel.isShowLoading.value = false
                    }
                    else{
                        viewModel.getDataLembaga(id)
                    }
                }
                else {
                    viewModel.getDataLembaga(idLembaga ?: throw Exception("Error, mohon login ulang"))
                }
            }
        } catch (e: Exception) {
            viewModel.isShowLoading.value = false
            viewModel.message.value = e.message
        }
    }

    private fun setAdapterFoto() {
        viewModel.initAdapter()
        bind.viewPagerLd.offscreenPageLimit = 0
        bind.viewPagerLd.adapter = viewModel.adapterSampul
        bind.viewPagerLd.currentItem = 0
        bind.dotsIndicatorLd.setViewPager(bind.viewPagerLd)
    }
}
