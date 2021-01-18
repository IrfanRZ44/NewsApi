package com.exomatik.ballighadmin.ui.main.listMJ

import android.content.Context
import android.os.Bundle
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exomatik.ballighadmin.R
import com.exomatik.ballighadmin.base.BaseViewModel
import com.exomatik.ballighadmin.model.ModelDataMasjid
//import com.exomatik.ballighadmin.ui.admin.profile.mj.AdminLihatProfileMJFragment
import com.exomatik.ballighadmin.utils.Constant
import com.exomatik.ballighadmin.utils.FirebaseUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ListMJViewModel(private val rcChat: RecyclerView,
                      private val context: Context?,
                      private val navController: NavController) : BaseViewModel() {
    val listRequestMJ = ArrayList<ModelDataMasjid?>()
    lateinit var adapter: AdapterListMJ

    fun cekList() {
        if (listRequestMJ.size == 0) status.value = Constant.noRequest
        else status.value = ""
    }

    fun initAdapter(){
        adapter = AdapterListMJ(listRequestMJ) { afl: ModelDataMasjid -> onClickItem(afl) }
        rcChat.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rcChat.adapter = adapter
    }

    private fun onClickItem(dataMasjid: ModelDataMasjid) {
//        val bundle = Bundle()
//        val cariFragment = AdminLihatProfileMJFragment()
//        bundle.putParcelable("dataMasjid", dataMasjid)
//        cariFragment.arguments = bundle
//        navController.navigate(R.id.adminLihatProfileMJFragment, bundle)
    }

    fun getListMasjid(){
        isShowLoading.value = true
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(result: DatabaseError) {
                isShowLoading.value = false
                message.value = result.message
                cekList()
            }

            override fun onDataChange(result: DataSnapshot) {
                listRequestMJ.clear()
                adapter.notifyDataSetChanged()

                isShowLoading.value = false
                if (result.exists()) {
                    for (snapshot in result.children) {
                        val data = snapshot.getValue(ModelDataMasjid::class.java)
                        listRequestMJ.add(data)
                        adapter.notifyDataSetChanged()
                    }
                }
                cekList()
            }
        }

        FirebaseUtils.refreshAfiliasi1DataWith2ChildObject(
            Constant.akunMJ,
            Constant.referenceBiodata,
            Constant.status,
            Constant.active,
            valueEventListener
        )
    }
}