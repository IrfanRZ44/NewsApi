package com.exomatik.ballighadmin.ui.main.listMJ

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
import com.exomatik.ballighadmin.model.ModelDataMasjid
import com.exomatik.ballighadmin.model.ModelUser
import com.exomatik.ballighadmin.ui.main.profile.mj.AdminLihatProfileMJFragment
import com.exomatik.ballighadmin.utils.Constant
import com.exomatik.ballighadmin.utils.FirebaseUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.item_cari_afiliasi.view.*

class ListMJViewModel(private val rcChat: RecyclerView,
                      private val context: Context?,
                      private val navController: NavController) : BaseViewModel() {
    val listRequestMJ = ArrayList<ModelUser?>()
    lateinit var adapter: AdapterListMJ

    fun cekList() {
        if (listRequestMJ.size == 0) status.value = Constant.noRequest
        else status.value = ""
    }

    fun initAdapter(){
        adapter = AdapterListMJ(listRequestMJ,
            { afl: ModelUser -> onClickItem(afl) },
            { idMasjid: String, view: View -> getDetailMasjid(idMasjid, view) }
        )
        rcChat.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rcChat.adapter = adapter
    }

    private fun onClickItem(dataUser: ModelUser) {
        val bundle = Bundle()
        val cariFragment = AdminLihatProfileMJFragment()
        bundle.putParcelable("dataUser", dataUser)
        cariFragment.arguments = bundle
        navController.navigate(R.id.adminLihatProfileMJFragment, bundle)
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
                        val data = snapshot.getValue(ModelUser::class.java)

                        listRequestMJ.add(data)
                        adapter.notifyDataSetChanged()
                    }
                }
                cekList()
            }
        }

        FirebaseUtils.refreshAfiliasi2DataWith2ChildObject(
            Constant.referenceUser,
            "jenisAkun",
            Constant.akunMJ,
            valueEventListener
        )
    }

    private fun getDetailMasjid(idMasjid: String, view: View) {
        isShowLoading.value = true

        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(result: DatabaseError) {
                isShowLoading.value = false
            }

            override fun onDataChange(result: DataSnapshot) {
                isShowLoading.value = false
                if (result.exists()) {
                    val data = result.getValue(ModelDataMasjid::class.java)

                    view.imgLd.setBackgroundResource(android.R.color.transparent)
                    view.namaLd.text = data?.namaMasjid
                    view.kabLd.text = data?.kotaMasjid
                    view.provLd.text = data?.provinsiMasjid
                    view.imgLd.load(data?.fotoMasjid) {
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
            Constant.akunMJ
            , Constant.referenceBiodata
            , idMasjid
            , valueEventListener
        )
    }
}