package com.exomatik.ballighadmin.ui.main.listLD

import android.content.Context
import android.os.Bundle
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exomatik.ballighadmin.R
import com.exomatik.ballighadmin.base.BaseViewModel
import com.exomatik.ballighadmin.model.ModelDataLembaga
import com.exomatik.ballighadmin.utils.Constant
import com.exomatik.ballighadmin.utils.FirebaseUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ListLDViewModel(private val rcChat: RecyclerView,
                      private val context: Context?,
                      private val navController: NavController) : BaseViewModel() {
    val listRequestLD = ArrayList<ModelDataLembaga?>()
    lateinit var adapter: AdapterListLD

    fun cekList() {
        if (listRequestLD.size == 0) status.value = Constant.noRequest
        else status.value = ""
    }

    fun initAdapter(){
        adapter = AdapterListLD(listRequestLD) { afl: ModelDataLembaga -> onClickItem(afl) }
        rcChat.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rcChat.adapter = adapter
    }

    private fun onClickItem(dataLembaga: ModelDataLembaga) {
//        val bundle = Bundle()
//        val cariFragment = AdminLihatProfileLDFragment()
//        bundle.putParcelable("dataLembaga", dataLembaga)
//        cariFragment.arguments = bundle
//        navController.navigate(R.id.adminLihatProfileLDFragment, bundle)
    }

    fun getListLembaga(){
        isShowLoading.value = true
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(result: DatabaseError) {
                isShowLoading.value = false
                message.value = result.message
                cekList()
            }

            override fun onDataChange(result: DataSnapshot) {
                listRequestLD.clear()
                adapter.notifyDataSetChanged()

                isShowLoading.value = false
                if (result.exists()) {
                    for (snapshot in result.children) {
                        val data = snapshot.getValue(ModelDataLembaga::class.java)
                        listRequestLD.add(data)
                        adapter.notifyDataSetChanged()
                    }
                }
                cekList()
            }
        }

        FirebaseUtils.refreshAfiliasi1DataWith2ChildObject(
            Constant.akunLD,
            Constant.referenceBiodata,
            Constant.status,
            Constant.active,
            valueEventListener
        )
    }
}