package com.exomatik.ballighadmin.ui.main.listLD

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.request.CachePolicy
import com.exomatik.ballighadmin.R
import com.exomatik.ballighadmin.base.BaseViewModel
import com.exomatik.ballighadmin.model.ModelDataLembaga
import com.exomatik.ballighadmin.model.ModelUser
import com.exomatik.ballighadmin.ui.main.profile.ld.AdminLihatProfileLDFragment
import com.exomatik.ballighadmin.utils.Constant
import com.exomatik.ballighadmin.utils.FirebaseUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.item_cari_afiliasi.view.*

class ListLDViewModel(private val rcChat: RecyclerView,
                      private val context: Context?,
                      private val navController: NavController) : BaseViewModel() {
    val listRequestLD = ArrayList<ModelUser?>()
    lateinit var adapter: AdapterListLD

    fun cekList() {
        if (listRequestLD.size == 0) status.value = Constant.noRequest
        else status.value = ""
    }

    fun initAdapter(){
        adapter = AdapterListLD(listRequestLD,
            { afl: ModelUser -> onClickItem(afl)} ,
            {idLembaga: String, view: View -> getDataLembaga(idLembaga, view)})
        rcChat.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rcChat.adapter = adapter
    }

    private fun onClickItem(dataUser: ModelUser) {
        val bundle = Bundle()
        val cariFragment = AdminLihatProfileLDFragment()
        bundle.putParcelable("dataUser", dataUser)
        cariFragment.arguments = bundle
        navController.navigate(R.id.adminLihatProfileLDFragment, bundle)
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
                        val data = snapshot.getValue(ModelUser::class.java)

                        listRequestLD.add(data)
                        adapter.notifyDataSetChanged()
                    }
                }
                cekList()
            }
        }

        FirebaseUtils.refreshAfiliasi2DataWith2ChildObject(
            Constant.referenceUser,
            "jenisAkun",
            Constant.akunLD,
            valueEventListener
        )
    }

    private fun getDataLembaga(idLembaga: String, view: View) {
        isShowLoading.value = true

        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(result: DatabaseError) {
                isShowLoading.value = false
            }

            override fun onDataChange(result: DataSnapshot) {
                isShowLoading.value = false
                if (result.exists()) {
                    val data = result.getValue(ModelDataLembaga::class.java)

                    view.imgLd.setBackgroundResource(android.R.color.transparent)
                    view.namaLd.text = data?.namaPanjangLembaga
                    view.kabLd.text = data?.kotaLembaga
                    view.provLd.text = data?.provinsiLembaga
                    view.imgLd.load(data?.fotoLembaga) {
                        crossfade(true)
                        placeholder(R.drawable.ic_camera_white)
                        error(R.drawable.ic_camera_white)
                        fallback(R.drawable.ic_camera_white)
                        memoryCachePolicy(CachePolicy.ENABLED)
                    }
                }
            }
        }

        FirebaseUtils.getData2Child(
            Constant.akunLD
            , Constant.referenceBiodata
            , idLembaga
            , valueEventListener
        )
    }
}