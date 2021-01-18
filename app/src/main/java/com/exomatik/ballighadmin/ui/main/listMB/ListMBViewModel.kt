package com.exomatik.ballighadmin.ui.main.listMB

import android.content.Context
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exomatik.ballighadmin.base.BaseViewModel
import com.exomatik.ballighadmin.model.ModelUser
import com.exomatik.ballighadmin.utils.Constant
import com.exomatik.ballighadmin.utils.FirebaseUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ListMBViewModel(private val rcChat: RecyclerView,
                      private val context: Context?,
                      private val navController: NavController) : BaseViewModel() {
    val listRequestMB = ArrayList<ModelUser?>()
    lateinit var adapter: AdapterListMB

    fun cekList() {
        if (listRequestMB.size == 0) status.value = Constant.noRequest
        else status.value = ""
    }

    fun initAdapter(){
        adapter = AdapterListMB(listRequestMB) { afl: ModelUser -> onClickItem(afl) }
        rcChat.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rcChat.adapter = adapter
    }

    private fun onClickItem(dataMasjid: ModelUser) {
//        val bundle = Bundle()
//        val cariFragment = AdminLihatProfileMJFragment()
//        bundle.putParcelable("dataMasjid", dataMasjid)
//        cariFragment.arguments = bundle
//        navController.navigate(R.id.adminLihatProfileMJFragment, bundle)
    }

    fun getListMuballigh(){
        isShowLoading.value = true
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(result: DatabaseError) {
                isShowLoading.value = false
                message.value = result.message
                cekList()
            }

            override fun onDataChange(result: DataSnapshot) {
                listRequestMB.clear()
                adapter.notifyDataSetChanged()

                isShowLoading.value = false
                if (result.exists()) {
                    for (snapshot in result.children) {
                        val data = snapshot.getValue(ModelUser::class.java)
                        listRequestMB.add(data)
                        adapter.notifyDataSetChanged()
                    }
                }
                cekList()
            }
        }

        FirebaseUtils.refreshAfiliasi2DataWith2ChildObject(
            Constant.referenceUser,
            "jenisAkun",
            Constant.akunMB,
            valueEventListener
        )
    }
}