package com.exomatik.ballighadmin.ui.main.profile.mj

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
import com.exomatik.balligh.R
import com.exomatik.balligh.base.BaseViewModel
import com.exomatik.balligh.model.ModelDataMasjid
import com.exomatik.balligh.model.ModelUser
import com.exomatik.balligh.ui.admin.pesan.adminToMJ.PesanAdmintoMJFragment
import com.exomatik.balligh.utils.Constant
import com.exomatik.balligh.utils.Constant.akunMJ
import com.exomatik.balligh.utils.FirebaseUtils
import com.exomatik.balligh.ui.general.adapter.ImageSampulAdapter
import com.exomatik.balligh.utils.onClickFoto
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class AdminLihatProfileMJViewModel(private val context: Context?,
                                   private val activity: Activity?,
                                   private val navController: NavController) : BaseViewModel() {
    val namaMasjid = MutableLiveData<String>()
    val alamatMasjid = MutableLiveData<String>()
    val provinsiMasjid = MutableLiveData<String>()
    val kotaMasjid = MutableLiveData<String>()
    val kecamatanMasjid = MutableLiveData<String>()
    val namaPengurus = MutableLiveData<String>()
    val fotoMasjid = MutableLiveData<String>()
    val fotoPengurus = MutableLiveData<String>()
    val noHpPengurus = MutableLiveData<String>()
    val dataUser = MutableLiveData<ModelUser>()
    val dataMasjid = MutableLiveData<ModelDataMasjid>()
    var adapterSampul: ImageSampulAdapter? = null
    private var listFoto = ArrayList<String>()

    fun setDataMasjid() {
        namaMasjid.value =
            if (dataMasjid.value?.namaMasjid?.isEmpty() == true) "Nama Masjid" else dataMasjid.value?.namaMasjid
        alamatMasjid.value =
            if (dataMasjid.value?.alamatMasjid?.isEmpty() == true) "Alamat Masjid" else dataMasjid.value?.alamatMasjid
        provinsiMasjid.value =
            if (dataMasjid.value?.provinsiMasjid?.isEmpty() == true) "Provinsi" else dataMasjid.value?.provinsiMasjid
        kotaMasjid.value =
            if (dataMasjid.value?.kotaMasjid?.isEmpty() == true) "Kota" else dataMasjid.value?.kotaMasjid
        kecamatanMasjid.value =
            if (dataMasjid.value?.kecamatanMasjid?.isEmpty() == true) "Kecamatan" else dataMasjid.value?.kecamatanMasjid?.trim()
        kecamatanMasjid.value = "${kecamatanMasjid.value}, "
        fotoMasjid.value =
            if (dataMasjid.value?.fotoMasjid?.isEmpty() == true) "" else dataMasjid.value?.fotoMasjid
        if (dataMasjid.value?.sampul.isNullOrEmpty()) setEmptyFoto() else setDataFoto()
    }

    fun setDataUser() {
        namaPengurus.value = if (dataUser.value?.nama?.isEmpty() == true) "Nama Pengurus" else dataUser.value?.nama
        fotoPengurus.value = if (dataUser.value?.foto?.isEmpty() == true) "" else dataUser.value?.foto
        noHpPengurus.value = if (dataUser.value?.noHp?.isEmpty() == true) "Kontak Pengurus" else dataUser.value?.noHp
    }

    fun initAdapter() {
        adapterSampul = context?.let {
            ImageSampulAdapter(
                it,
                listFoto,
                navController
            )
        }
    }

    private fun setDataFoto() {
        var i = 0
        while (i < dataMasjid.value?.sampul?.size ?: 0) {
            dataMasjid.value?.sampul?.get(i)?.let { listFoto.add(it) }
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

    fun getDataPengurus(idMasjid: String) {
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
            Constant.referenceUser, Constant.referenceIdMasjid
            , idMasjid
            , valueEventListener
        )
    }

    fun getDataMasjid(idMasjid: String) {
        isShowLoading.value = true

        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(result: DatabaseError) {
                isShowLoading.value = false
                message.value = "Error, ${result.message}"
            }

            override fun onDataChange(result: DataSnapshot) {
                isShowLoading.value = false
                if (result.exists()) {
                    val data = result.getValue(ModelDataMasjid::class.java)
                    dataMasjid.value = data
                    setDataMasjid()
                    try {
                        getDataPengurus(
                            dataMasjid.value?.idMasjid
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
            akunMJ
            , Constant.referenceBiodata
            , idMasjid
            , valueEventListener
        )
    }

    fun onClickPhone(){
        isShowLoading.value = true
        callingPhone()
    }

    fun onClickNavigasi(){
        val locationPoint = dataMasjid.value?.titikAlamatMasjid?:"${Constant.defaultLatitude}___${Constant.defaultLongitude}"

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
        val fragmentTujuan = PesanAdmintoMJFragment()
        bundle.putParcelable("dataMasjid", dataMasjid.value)
        bundle.putParcelable("dataPengurus", dataUser.value)
        fragmentTujuan.arguments = bundle
        navController.navigate(R.id.pesanAdmintoMJFragment, bundle)
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