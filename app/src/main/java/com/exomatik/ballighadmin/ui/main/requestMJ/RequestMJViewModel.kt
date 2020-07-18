package com.exomatik.ballighadmin.ui.main.requestMJ

import android.content.Context
import android.os.Bundle
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exomatik.ballighadmin.R
import com.exomatik.ballighadmin.base.BaseViewModel
import com.exomatik.ballighadmin.model.ModelDataMasjid
import com.exomatik.ballighadmin.model.ModelNotif
import com.exomatik.ballighadmin.model.ModelUser
import com.exomatik.ballighadmin.services.notification.model.Notification
import com.exomatik.ballighadmin.services.notification.model.Sender
import com.exomatik.ballighadmin.ui.main.profile.mj.AdminLihatProfileMJFragment
import com.exomatik.ballighadmin.utils.Constant
import com.exomatik.ballighadmin.utils.FirebaseUtils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class RequestMJViewModel(private val rcChat: RecyclerView,
                         private val context: Context?,
                         private val navController: NavController) : BaseViewModel() {
    val listRequestMJ = ArrayList<ModelDataMasjid?>()
    lateinit var adapter: AdapterRequestMJ

    fun cekList() {
        if (listRequestMJ.size == 0) status.value = Constant.noRequest
        else status.value = ""
    }

    fun initAdapter(){
        adapter = AdapterRequestMJ(listRequestMJ,
            { afl: ModelDataMasjid -> onClickItem(afl) },
            context, { afl: ModelDataMasjid -> requestAccept(afl) },
            { afl: ModelDataMasjid -> requestReject(afl) })
        rcChat.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rcChat.adapter = adapter
    }

    private fun onClickItem(dataMasjid: ModelDataMasjid) {
        val bundle = Bundle()
        val cariFragment = AdminLihatProfileMJFragment()
        bundle.putParcelable("dataMasjid", dataMasjid)
        cariFragment.arguments = bundle
        navController.navigate(R.id.adminLihatProfileMJFragment, bundle)
    }

    private fun requestAccept(dataMasjid: ModelDataMasjid) {
        val onCompleteListener = OnCompleteListener<Void> {
            if (it.isSuccessful){
                message.value = "Berhasil memverifikasi Masjid"
                sendNotificationMJ(dataMasjid.idMasjid, true)
            }
            else{
                message.value = "Afwan, gagal memverifikasi Masjid"
            }
        }

        val onFailureListener = OnFailureListener {
            message.value = it.message
        }

        FirebaseUtils.setValueWith3ChildString(
            Constant.akunMJ
            , Constant.referenceBiodata
            , dataMasjid.idMasjid
            , Constant.status
            , Constant.active
            , onCompleteListener
            , onFailureListener
        )
    }

    private fun sendNotificationMJ(idMasjid: String, accept: Boolean) {
        isShowLoading.value = true

        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(result: DatabaseError) {
                isShowLoading.value = false
            }

            override fun onDataChange(result: DataSnapshot) {
                isShowLoading.value = false
                if (result.exists()) {
                    for (snapshot in result.children) {
                        val data = snapshot.getValue(ModelUser::class.java)

                        val notification = if (accept)
                            Notification("Permintaan verifikasi masjid Anda telah diterima", "Verifikasi"
                                , "com.exomatik.balligh.fcm_TARGET_NOTIFICATION_BERANDA_MJ")
                        else Notification("Permintaan verifikasi masjid Anda ditolak", "Verifikasi"
                            , "com.exomatik.balligh.fcm_TARGET_NOTIFICATION_BERANDA_MJ")

                        if(data?.token.isNullOrEmpty()){
                            FirebaseUtils.simpanNotif(ModelNotif(notification, data?.idMasjid, ""))
                        }
                        else{
                            val sender = Sender(notification, data?.token)
                            FirebaseUtils.sendNotif(sender)
                        }
                    }
                }
            }
        }

        FirebaseUtils.searchDataWith1ChildObject(
            Constant.referenceUser, Constant.referenceIdMasjid
            , idMasjid
            , valueEventListener
        )
    }

    private fun requestReject(dataMasjid: ModelDataMasjid) {
        val onCompleteListener = OnCompleteListener<Void> {
            if (it.isSuccessful){
                message.value = "Berhasil menolak Masjid"
                sendNotificationMJ(dataMasjid.idMasjid, false)
            }
            else{
                message.value = "Afwan, gagal menolak Masjid"
            }
        }

        val onFailureListener = OnFailureListener {
            message.value = it.message
        }

        FirebaseUtils.setValueWith3ChildString(
            Constant.akunMJ
            , Constant.referenceBiodata
            , dataMasjid.idMasjid
            , Constant.status
            , Constant.rejected
            , onCompleteListener
            , onFailureListener
        )
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
            Constant.request,
            valueEventListener
        )
    }
}