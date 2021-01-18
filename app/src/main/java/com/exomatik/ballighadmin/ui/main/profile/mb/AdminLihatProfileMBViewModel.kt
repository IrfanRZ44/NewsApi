package com.exomatik.ballighadmin.ui.main.profile.mb

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.exomatik.balligh.R
import com.exomatik.balligh.base.BaseViewModel
import com.exomatik.balligh.model.ModelDataMuballigh
import com.exomatik.balligh.model.ModelUser
import com.exomatik.balligh.ui.admin.pesan.adminToMB.PesanAdmintoMBFragment
import com.exomatik.balligh.ui.general.adapter.ImageSampulAdapter
import com.exomatik.balligh.ui.general.adapter.KualifikasiAdapter
import com.exomatik.balligh.utils.*
import com.exomatik.balligh.utils.Constant.akunMB
import com.exomatik.balligh.utils.Constant.codeRequestPhone
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class AdminLihatProfileMBViewModel(private val context: Context?,
                                   private val activity: Activity?,
                                   private val navController: NavController
                                ) : BaseViewModel() {
    val nama = MutableLiveData<String>()
    val gelar = MutableLiveData<String>()
    val alamat = MutableLiveData<String>()
    val provinsi = MutableLiveData<String>()
    val kota = MutableLiveData<String>()
    val kecamatan = MutableLiveData<String>()
    val dataUser = MutableLiveData<ModelUser>()
    val isShowingKualifikasi = MutableLiveData<Boolean>()
    val isShowingTabligh = MutableLiveData<Boolean>()
    val dataMuballigh = MutableLiveData<ModelDataMuballigh>()
    var layoutManagerKualifikasi: LinearLayoutManager? = null
    var layoutManagerTabligh: LinearLayoutManager? = null
    var adapterKualifikasi: KualifikasiAdapter? = null
    var adapterTabligh: KualifikasiAdapter? = null
    var adapterSampul: ImageSampulAdapter? = null
    private var listKualifikasi = ArrayList<String>()
    private var listTabligh = ArrayList<String>()
    private var listFoto = ArrayList<String>()

    fun initAdapter(){
        layoutManagerKualifikasi = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        layoutManagerTabligh = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapterKualifikasi =
            KualifikasiAdapter(listKualifikasi)
        adapterTabligh = KualifikasiAdapter(listTabligh)
        adapterSampul = context?.let {
            ImageSampulAdapter(
                it,
                listFoto,
                navController
            )
        }
    }

    fun setDataUser(){
        nama.value = if (dataUser.value?.nama.isNullOrEmpty()) "Nama Muballigh" else dataUser.value?.nama
        alamat.value = if (dataUser.value?.alamat.isNullOrEmpty()) "Alamat" else dataUser.value?.alamat
        kecamatan.value = if (dataUser.value?.kecamatan.isNullOrEmpty()) "Kecamatan" else dataUser.value?.kecamatan
        kota.value = if (dataUser.value?.kota.isNullOrEmpty()) "Kabupaten / Kota" else dataUser.value?.kota
        kota.value = "${kota.value}, "
        provinsi.value = if (dataUser.value?.provinsi.isNullOrEmpty()) "Provinsi" else dataUser.value?.provinsi
    }

    fun setDataMuballigh() {
        if (dataMuballigh.value?.sampul.isNullOrEmpty()) setEmptyFoto() else setDataFoto()
        if (dataMuballigh.value?.listKualifikasi.isNullOrEmpty()) isShowingKualifikasi.value = false else {
            isShowingKualifikasi.value = true
            setDataKualifikasi()
        }
        if (dataMuballigh.value?.listTabligh.isNullOrEmpty()) isShowingTabligh.value = false else {
            isShowingTabligh.value = true
            setDataTabligh()
        }

        if ((dataMuballigh.value?.listKualifikasi.isNullOrEmpty()) and (dataMuballigh.value?.listTabligh.isNullOrEmpty())){
            dataMuballigh.value?.showingKualifikasi = false
        }
        setGelar()
    }

    fun getDataMuballigh(idMuballigh: String?, searchPengurus: Boolean) {
        try {
            val id = idMuballigh?:throw Exception("Error, data muballigh tidak tersedia")
            isShowLoading.value = true
            val valueEventListener = object : ValueEventListener {
                override fun onCancelled(result: DatabaseError) {
                    isShowLoading.value = false
                    message.value = result.message
                }

                override fun onDataChange(result: DataSnapshot) {
                    isShowLoading.value = false
                    if (result.exists()) {
                        val data = result.getValue(ModelDataMuballigh::class.java)

                        dataMuballigh.value = data
                    }

                    setDataMuballigh()
                    if (searchPengurus) getDataPengurus(id)
                }
            }
            FirebaseUtils.getData2Child(
                akunMB,
                Constant.referenceBiodata, id, valueEventListener)
        }catch (e: java.lang.Exception){
            message.value = e.message
            isShowLoading.value = false
        }
    }

    private fun setGelar() {
        val gelar1  = dataUser.value?.dataPendidikan?.s1Gelar?:""
        val gelar2  = dataUser.value?.dataPendidikan?.s2Gelar?:""
        val gelar3  = dataUser.value?.dataPendidikan?.s3Gelar?:""
        val data= gelar1 + gelar2 + gelar3

        if (data.isEmpty()) gelar.value = ""
        else gelar.value = "($data)"
    }

    private fun setDataTabligh() {
        var j = 0
        while (j < dataMuballigh.value?.listTabligh?.size?:0){
            dataMuballigh.value?.listTabligh?.get(j)?.let { listTabligh.add(it) }
            adapterTabligh?.notifyDataSetChanged()
            j++
        }
    }

    private fun setDataKualifikasi() {
        var i = 0
        while (i < dataMuballigh.value?.listKualifikasi?.size?:0){
            dataMuballigh.value?.listKualifikasi?.get(i)?.let { listKualifikasi.add(it) }
            adapterKualifikasi?.notifyDataSetChanged()
            i++
        }
    }

    private fun setDataFoto() {
        var i = 0
        while (i < dataMuballigh.value?.sampul?.size?:0){
            dataMuballigh.value?.sampul?.get(i)?.let { listFoto.add(it) }
            adapterSampul?.notifyDataSetChanged()
            i++
        }
    }

    private fun setEmptyFoto() {
        var i = 0
        while (i < 3){
            listFoto.add("")
            adapterSampul?.notifyDataSetChanged()
            i++
        }
    }

    fun getDataPengurus(id: String) {
        isShowLoading.value = true

        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(result: DatabaseError) {
                isShowLoading.value = false
                message.value = "Error, ${result.message}"
            }

            override fun onDataChange(result: DataSnapshot) {
                isShowLoading.value = false
                if (result.exists()) {
                    for (snapshot in result.children) {
                        val data = snapshot.getValue(ModelUser::class.java)
                        dataUser.value = data
                        setDataUser()
                    }
                } else {
                    message.value = "Afwan, terjadi gangguan database"
                }
            }
        }

        FirebaseUtils.searchDataWith1ChildObject(
            Constant.referenceUser, Constant.referenceIdMuballigh
            , id
            , valueEventListener
        )
    }

    fun onClickPhone(){
        isShowLoading.value = true
        callingPhone()
    }

    fun onClickNavigasi(){
        val locationPoint = dataUser.value?.titikAlamat?:"${Constant.defaultLatitude}___${Constant.defaultLongitude}"

        val data = locationPoint.split("___")
        val latitude = data[0].toDouble()
        val longitude = data[1].toDouble()
        val intent = Intent(Intent.ACTION_VIEW,
            Uri.parse("http://maps.google.com/maps?daddr=$latitude,$longitude")
        )
        activity?.startActivity(intent)
    }

    fun onClickChat(){
        val bundle = Bundle()
        val fragmentTujuan = PesanAdmintoMBFragment()
        bundle.putParcelable("dataUserMuballigh", dataUser.value)
        fragmentTujuan.arguments = bundle
        navController.navigate(R.id.pesanAdmintoMBFragment, bundle)
    }

    private fun callingPhone() {
        val uri = "tel:" + dataUser.value?.noHp
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse(uri)
        try {
            if (ActivityCompat.checkSelfPermission(context?:throw Exception("Error, mohon keluar dan masuk lagi ke aplikasi"), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity?:throw Exception("Error, mohon keluar dan masuk lagi ke aplikasi"), arrayOf(Manifest.permission.CALL_PHONE), codeRequestPhone)
                isShowLoading.value = false
                message.value = "Mohon klik lagi tombol setelah memberikan izin"
            } else {
                activity?.startActivity(intent)
                isShowLoading.value = false
            }
        }catch (e: Exception){
            isShowLoading.value = false
            message.value = e.message
        }
    }
}