package com.exomatik.ballighadmin.ui.main.profile.mj

import androidx.navigation.fragment.findNavController
import com.exomatik.ballighadmin.R
import com.exomatik.ballighadmin.base.BaseFragmentBind
import com.exomatik.ballighadmin.databinding.FragmentAdminLihatProfileMjBinding
import com.exomatik.ballighadmin.model.ModelDataMasjid
import com.exomatik.ballighadmin.model.ModelUser
import com.exomatik.ballighadmin.utils.Constant.inactive

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
        val dataUser = this.arguments?.getParcelable<ModelUser>("dataUser")
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
                if (dataUser != null){
                    viewModel.dataUser.value = dataUser
                    viewModel.setDataUser()
                    val id = dataUser.idMasjid
                    if (id == "" || id.isEmpty()){
                        viewModel.dataMasjid.value = ModelDataMasjid("", "",
                            "Nama Masjid", "", "",
                            "Provinsi", "Kota", "Kecamatan",
                            "Alamat Masjid",
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
                        viewModel.setDataMasjid()
                        viewModel.isShowLoading.value = false
                    }
                    else{
                        viewModel.getDataMasjid(id)
                    }
                }
                else{
                    viewModel.getDataMasjid(idMasjid ?: throw Exception("Error, mohon login ulang"))
                }
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
