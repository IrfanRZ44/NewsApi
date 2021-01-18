package com.exomatik.ballighadmin.ui.main.dashboard

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exomatik.ballighadmin.base.BaseViewModel
import com.exomatik.ballighadmin.model.ModelRiwayat
import com.exomatik.ballighadmin.model.ModelUser
import com.exomatik.ballighadmin.utils.Constant
import com.exomatik.ballighadmin.utils.FirebaseUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class DashboardViewModel(private val rcRiwayat: RecyclerView,
                         private val context: Context?,
                         private val navController: NavController) : BaseViewModel() {
    val listUser = ArrayList<ModelUser?>()
    val listLD = ArrayList<ModelUser?>()
    val listMJ = ArrayList<ModelUser?>()
    val listMB = ArrayList<ModelUser?>()
    val listNewUserLD = ArrayList<ModelUser?>()
    val listNewUserMJ = ArrayList<ModelUser?>()
    val listNewUserMB = ArrayList<ModelUser?>()
    val totalUser = MutableLiveData<String>()
    val totalLD = MutableLiveData<String>()
    val totalMJ = MutableLiveData<String>()
    val totalMB = MutableLiveData<String>()
    val newUserLD = MutableLiveData<String>()
    val newUserMJ = MutableLiveData<String>()
    val newUserMB = MutableLiveData<String>()
    private val listRiwayat = ArrayList<ModelRiwayat?>()
    private lateinit var adapter: AdapterRiwayat

    fun initAdapter(){
        adapter = AdapterRiwayat(listRiwayat)
        rcRiwayat.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rcRiwayat.adapter = adapter
        listRiwayat.add(ModelRiwayat(true, ""))
        listRiwayat.add(ModelRiwayat(false, "22-03-1997"))
        listRiwayat.add(ModelRiwayat(false, "22-04-2002"))
        listRiwayat.add(ModelRiwayat(false, "22-05-2000"))
        adapter.notifyDataSetChanged()
    }

    fun getListUser(){
        listUser.clear()
        listLD.clear()
        listMJ.clear()
        listMB.clear()
        listNewUserLD.clear()
        listNewUserMJ.clear()
        listNewUserMB.clear()

        isShowLoading.value = true
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(result: DatabaseError) {
                isShowLoading.value = false
                message.value = result.message

                totalUser.value = listUser.size.toString()

                totalLD.value = listLD.size.toString()
                totalMJ.value = listMJ.size.toString()
                totalMB.value = listMB.size.toString()

                newUserLD.value = listNewUserLD.size.toString()
                newUserMJ.value = listNewUserMJ.size.toString()
                newUserMB.value = listNewUserMB.size.toString()
            }

            override fun onDataChange(result: DataSnapshot) {
                listUser.clear()
                isShowLoading.value = false
                if (result.exists()) {
                    for (snapshot in result.children) {
                        val data = snapshot.getValue(ModelUser::class.java)
                        listUser.add(data)
                        totalUser.value = listUser.size.toString()

                        val idLembaga = data?.idLembaga
                        if (!idLembaga.isNullOrEmpty()){
                            listLD.add(data)

                            val idSplit = idLembaga.split("__")
                            val timeStamp = idSplit[1]
                            if (setTime(timeStamp.toLong())){
                                listNewUserLD.add(data)
                            }
                        }

                        val idMasjid = data?.idMasjid
                        if (!idMasjid.isNullOrEmpty()){
                            listMJ.add(data)

                            val idSplit = idMasjid.split("__")
                            val timeStamp = idSplit[1]
                            if (setTime(timeStamp.toLong())){
                                listNewUserMJ.add(data)
                            }
                        }

                        val idMuballigh = data?.idMuballigh
                        if (!idMuballigh.isNullOrEmpty()){
                            listMB.add(data)

                            val idSplit = idMuballigh.split("__")
                            val timeStamp = idSplit[1]
                            if (setTime(timeStamp.toLong())){
                                listNewUserMB.add(data)
                            }
                        }

                        totalUser.value = listUser.size.toString()

                        totalLD.value = listLD.size.toString()
                        totalMJ.value = listMJ.size.toString()
                        totalMB.value = listMB.size.toString()

                        newUserLD.value = listNewUserLD.size.toString()
                        newUserMJ.value = listNewUserMJ.size.toString()
                        newUserMB.value = listNewUserMB.size.toString()
                    }
                }

            }
        }

        FirebaseUtils.getDataObject(
            Constant.referenceUser,
            valueEventListener
        )
    }

    @Suppress("DEPRECATION")
    private fun setTime(timeStamp: Long) : Boolean {
        val strDate1 = Date(timeStamp)
        val strDate2 = Date()
        val cal1 = Calendar.getInstance()
        cal1.time = strDate1
        val cal2 = Calendar.getInstance()
        cal2.time = strDate2
        val tanggal1 = cal1[Calendar.DAY_OF_MONTH]
        val tanggal2 = cal2[Calendar.DAY_OF_MONTH]
        val bulan1 = cal1[Calendar.MONTH]
        val bulan2 = cal2[Calendar.MONTH]
        val tahun1 = cal1[Calendar.YEAR]
        val tahun2 = cal2[Calendar.YEAR]
        return if (tahun1 < tahun2) {
            false
        } else {
            if (bulan1 < bulan2) {
                false
            } else {
                tanggal1 >= tanggal2
            }
        }
    }
}