package com.exomatik.ballighadmin.ui.main.dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateFormat
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exomatik.ballighadmin.base.BaseViewModel
import com.exomatik.ballighadmin.model.ModelRiwayat
import com.exomatik.ballighadmin.model.ModelUser
import com.exomatik.ballighadmin.utils.Constant
import com.exomatik.ballighadmin.utils.Constant.dateFormat1
import com.exomatik.ballighadmin.utils.FirebaseUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.item_riwayat.view.*
import java.text.SimpleDateFormat
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

    private fun initAdapter(){
        adapter = AdapterRiwayat(listRiwayat) { view: View, item: ModelRiwayat -> getRiwayatUser(view, item) }
        rcRiwayat.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rcRiwayat.adapter = adapter
        listRiwayat.clear()
        adapter.notifyDataSetChanged()
        listRiwayat.add(ModelRiwayat(true, ""))

        var i = 0
        while(i < 30){
            listRiwayat.add(ModelRiwayat(false, getDaysAgo(i)))
            adapter.notifyDataSetChanged()
            i++
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDaysAgo(daysAgo: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)
        val format = SimpleDateFormat(dateFormat1)
        val date = calendar.time
        return format.format(date)
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
                            if (compareTimeNow(timeStamp.toLong())){
                                listNewUserLD.add(data)
                            }
                        }

                        val idMasjid = data?.idMasjid
                        if (!idMasjid.isNullOrEmpty()){
                            listMJ.add(data)

                            val idSplit = idMasjid.split("__")
                            val timeStamp = idSplit[1]
                            if (compareTimeNow(timeStamp.toLong())){
                                listNewUserMJ.add(data)
                            }
                        }

                        val idMuballigh = data?.idMuballigh
                        if (!idMuballigh.isNullOrEmpty()){
                            listMB.add(data)

                            val idSplit = idMuballigh.split("__")
                            val timeStamp = idSplit[1]
                            if (compareTimeNow(timeStamp.toLong())){
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

                        initAdapter()
                    }
                }

            }
        }

        FirebaseUtils.getDataObject(
            Constant.referenceUser,
            valueEventListener
        )
    }

    private fun compareTimeNow(timeStamp: Long) : Boolean {
        val cal = Calendar.getInstance(Locale.ENGLISH)
        val strDate2 = Date()
        val cal2 = Calendar.getInstance(Locale.ENGLISH)

        cal.timeInMillis = timeStamp * 1000L
        cal2.time = strDate2

        val tgl1 = DateFormat.format(dateFormat1, timeStamp).toString()
        val tgl2 = DateFormat.format(dateFormat1, cal2).toString()

        return tgl1 == tgl2
    }

    private fun compareTime(timeStamp: Long, tgl2: String) : Boolean {
        val cal = Calendar.getInstance(Locale.ENGLISH)
        cal.timeInMillis = timeStamp * 1000L
        val tgl1 = DateFormat.format(dateFormat1, timeStamp).toString()

        return tgl1 == tgl2
    }

    private fun getRiwayatUser(view: View, item: ModelRiwayat){
        var riwayatMB = 0
        var riwayatMJ = 0
        var riwayatLD = 0
        for (i in listUser.indices){
            val idMuballigh = listUser[i]?.idMuballigh
            if (!idMuballigh.isNullOrEmpty()){
                val idSplit = idMuballigh.split("__")
                val timeStamp = idSplit[1]
                if (compareTime(timeStamp.toLong(), item.tanggal)){
                    riwayatMB++
                    view.textMB.text = riwayatMB.toString()
                }
            }

            val idMasjid = listUser[i]?.idMasjid
            if (!idMasjid.isNullOrEmpty()){
                val idSplit = idMasjid.split("__")
                val timeStamp = idSplit[1]
                if (compareTime(timeStamp.toLong(), item.tanggal)){
                    riwayatMJ++
                    view.textMJ.text = riwayatMJ.toString()
                }
            }

            val idLembaga = listUser[i]?.idLembaga
            if (!idLembaga.isNullOrEmpty()){
                val idSplit = idLembaga.split("__")
                val timeStamp = idSplit[1]
                if (compareTime(timeStamp.toLong(), item.tanggal)){
                    riwayatLD++
                    view.textLD.text = riwayatLD.toString()
                }
            }
        }
        view.textMB.text = riwayatMB.toString()
        view.textMJ.text = riwayatMJ.toString()
        view.textLD.text = riwayatLD.toString()
    }
}

