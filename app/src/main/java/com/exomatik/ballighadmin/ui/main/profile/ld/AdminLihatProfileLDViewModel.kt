package com.exomatik.ballighadmin.ui.main.profile.ld

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
import com.exomatik.ballighadmin.R
import com.exomatik.ballighadmin.base.BaseViewModel
import com.exomatik.ballighadmin.model.ModelDataLembaga
import com.exomatik.ballighadmin.model.ModelUser
import com.exomatik.ballighadmin.ui.main.pesan.adminToLD.PesanAdmintoLDFragment
import com.exomatik.ballighadmin.utils.Constant
import com.exomatik.ballighadmin.utils.Constant.akunLD
import com.exomatik.ballighadmin.utils.FirebaseUtils
import com.exomatik.ballighadmin.ui.general.adapter.ImageSampulAdapter
import com.exomatik.ballighadmin.utils.onClickFoto
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class AdminLihatProfileLDViewModel(private val context: Context?,
                                   private val activity: Activity?,
                                   private val navController: NavController) : BaseViewModel() {
    val namaLembaga = MutableLiveData<String>()
    val alamatLembaga = MutableLiveData<String>()
    val provinsiLembaga = MutableLiveData<String>()
    val kotaLembaga = MutableLiveData<String>()
    val kecamatanLembaga = MutableLiveData<String>()
    val namaPengurus = MutableLiveData<String>()
    val fotoPengurus = MutableLiveData<String>()
    val fotoLembaga = MutableLiveData<String>()
    val provinsiPengurus = MutableLiveData<String>()
    val kotaPengurus = MutableLiveData<String>()
    val kecamatanPengurus = MutableLiveData<String>()
    val dataLembaga = MutableLiveData<ModelDataLembaga>()
    val dataUser = MutableLiveData<ModelUser>()
    var adapterSampul: ImageSampulAdapter? = null
    private var listFoto = ArrayList<String>()

    fun initAdapter() {
        adapterSampul = context?.let {
            ImageSampulAdapter(
                it,
                listFoto,
                navController
            )
        }
    }

    fun setDataLembaga() {
        namaLembaga.value = if (dataLembaga.value?.namaPanjangLembaga?.isEmpty() == true) "Nama Lembaga" else dataLembaga.value?.namaPanjangLembaga
        alamatLembaga.value =
            if (dataLembaga.value?.alamatLembaga?.isEmpty() == true) "Alamat Lembaga" else dataLembaga.value?.alamatLembaga
        provinsiLembaga.value =
            if (dataLembaga.value?.provinsiLembaga?.isEmpty() == true) "Provinsi" else dataLembaga.value?.provinsiLembaga
        kotaLembaga.value =
            if (dataLembaga.value?.kotaLembaga?.isEmpty() == true) "Kota" else dataLembaga.value?.kotaLembaga
        kecamatanLembaga.value =
            if (dataLembaga.value?.kecamatanLembaga?.isEmpty() == true) "Kecamatan" else dataLembaga.value?.kecamatanLembaga?.trim()
        kecamatanLembaga.value = "${kecamatanLembaga.value?.trim()}, "
        fotoLembaga.value =
            if (dataLembaga.value?.fotoLembaga?.isEmpty() == true) "" else dataLembaga.value?.fotoLembaga
        if (dataLembaga.value?.sampul.isNullOrEmpty()) setEmptyFoto() else setDataFoto()
    }

    fun setDataUser() {
        namaPengurus.value =
            if (dataUser.value?.nama?.isEmpty() == true) dataUser.value?.username else dataUser.value?.nama
        fotoPengurus.value =
            if (dataUser.value?.foto?.isEmpty() == true) "" else dataUser.value?.foto
        provinsiPengurus.value =
            if (dataUser.value?.provinsi?.isEmpty() == true) "Provinsi" else dataUser.value?.provinsi
        kotaPengurus.value =
            if (dataUser.value?.kota?.isEmpty() == true) "Kota" else dataUser.value?.kota
        kecamatanPengurus.value =
            if (dataUser.value?.kecamatan?.isEmpty() == true) "Kecamatan" else dataUser.value?.kecamatan?.trim()
        kecamatanPengurus.value = "${kecamatanPengurus.value?.trim()}, "
    }

    private fun setDataFoto() {
        var i = 0
        while (i < dataLembaga.value?.sampul?.size ?: 0) {
            dataLembaga.value?.sampul?.get(i)?.let { listFoto.add(it) }
            adapterSampul?.notifyDataSetChanged()
            i++
        }
    }

    private fun setEmptyFoto() {
        var i = 0
        while (i < 3) {
            listFoto.add("")
            adapterSampul?.notifyDataSetChanged()
            i++
        }
    }

    fun getDataPengurus(idLembaga: String) {
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
                        val id = data?.idLembaga

                        if (!id.isNullOrEmpty() && id == idLembaga){
                            dataUser.value = data
                            setDataUser()
                        }
                        else if (idLembaga.isEmpty()){
                            setDataUser()
                        }
                    }
                } else {
                    message.value = "Afwan, terjadi gangguan database"
                }
            }
        }

        FirebaseUtils.searchDataWith1ChildObject(
            Constant.referenceUser, Constant.referenceIdLembaga
            , idLembaga
            , valueEventListener
        )
    }

    fun getDataLembaga(idLembaga: String) {
        isShowLoading.value = true

        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(result: DatabaseError) {
                isShowLoading.value = false
                message.value = "Error, ${result.message}"
            }

            override fun onDataChange(result: DataSnapshot) {
                isShowLoading.value = false
                if (result.exists()) {
                    val data = result.getValue(ModelDataLembaga::class.java)
                    dataLembaga.value = data
                    setDataLembaga()
                    try {
                        getDataPengurus(
                            dataLembaga.value?.idLembaga
                                ?: throw Exception("Error, mohon login ulang")
                        )
                    } catch (e: java.lang.Exception) {
                        message.value = e.message
                    }
                } else {
                    message.value = "Afwan, terjadi gangguan database"
                }
            }
        }

        FirebaseUtils.getData2Child(
            akunLD
            , Constant.referenceBiodata
            , idLembaga
            , valueEventListener
        )
    }

    fun onClickPhone(){
        isShowLoading.value = true
        callingPhone()
    }

    fun onClickNavigasi(){
        val locationPoint = dataLembaga.value?.titikAlamatLembaga?:"${Constant.defaultLatitude}___${Constant.defaultLongitude}"

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
        val fragmentTujuan = PesanAdmintoLDFragment()
        bundle.putParcelable("dataLembaga", dataLembaga.value)
        bundle.putParcelable("dataPengurus", dataUser.value)
        fragmentTujuan.arguments = bundle
        navController.navigate(R.id.pesanAdmintoLDFragment, bundle)
    }

    fun clickFoto(foto: String){
        onClickFoto(foto, navController)
    }

    private fun callingPhone() {
        val uri = "tel:" + dataUser.value?.noHp
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse(uri)
        try {
            if (ActivityCompat.checkSelfPermission(context?:throw Exception("Error, mohon keluar dan masuk lagi ke aplikasi"), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity?:throw Exception("Error, mohon keluar dan masuk lagi ke aplikasi"), arrayOf(
                    Manifest.permission.CALL_PHONE),
                    Constant.codeRequestPhone
                )
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