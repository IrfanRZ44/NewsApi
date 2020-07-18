package com.exomatik.ballighadmin.ui.main.requestLD

import android.content.Context
import android.os.Bundle
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exomatik.ballighadmin.R
import com.exomatik.ballighadmin.base.BaseViewModel
import com.exomatik.ballighadmin.model.*
import com.exomatik.ballighadmin.services.notification.model.Notification
import com.exomatik.ballighadmin.services.notification.model.Sender
import com.exomatik.ballighadmin.ui.main.profile.ld.AdminLihatProfileLDFragment
import com.exomatik.ballighadmin.utils.Constant
import com.exomatik.ballighadmin.utils.Constant.grupLDtoMB
import com.exomatik.ballighadmin.utils.Constant.grupLDtoMJ
import com.exomatik.ballighadmin.utils.Constant.referenceRuangChat
import com.exomatik.ballighadmin.utils.FirebaseUtils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class RequestLDViewModel(private val rcChat: RecyclerView,
                         private val context: Context?,
                         private val navController: NavController) : BaseViewModel() {
    val listRequestLD = ArrayList<ModelDataLembaga?>()
    lateinit var adapter: AdapterRequestLD

    fun cekList() {
        if (listRequestLD.size == 0) status.value = Constant.noRequest
        else status.value = ""
    }

    fun initAdapter(){
        adapter = AdapterRequestLD(listRequestLD,
            { afl: ModelDataLembaga -> onClickItem(afl) },
            context, { afl: ModelDataLembaga -> requestAccept(afl) },
            { afl: ModelDataLembaga -> requestReject(afl) })
        rcChat.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rcChat.adapter = adapter
    }

    private fun onClickItem(dataLembaga: ModelDataLembaga) {
        val bundle = Bundle()
        val cariFragment = AdminLihatProfileLDFragment()
        bundle.putParcelable("dataLembaga", dataLembaga)
        cariFragment.arguments = bundle
        navController.navigate(R.id.adminLihatProfileLDFragment, bundle)
    }

    private fun requestAccept(dataLembaga: ModelDataLembaga) {
        val onCompleteListener = OnCompleteListener<Void> {
            if (it.isSuccessful){
                sendChatMB(dataLembaga)
            }
            else{
                message.value = "Afwan, gagal memverifikasi Lembaga Dakwah"
            }
        }

        val onFailureListener = OnFailureListener {
            message.value = it.message
        }

        FirebaseUtils.setValueWith3ChildString(
            Constant.akunLD
            , Constant.referenceBiodata
            , dataLembaga.idLembaga
            , Constant.status
            , Constant.active
            , onCompleteListener
            , onFailureListener
        )
    }

    private fun sendChatMB(dataLembaga: ModelDataLembaga) {
        val dataChat = ModelDataChat(
            dataLembaga.idLembaga,
            "", dataLembaga.idLembaga,
            "", System.currentTimeMillis())
        val onCompleteListener = OnCompleteListener<Void> {
            sendChatMJ(dataLembaga)
        }

        val onFailureListener = OnFailureListener {}

        FirebaseUtils.setValueWith2ChildObject(
            referenceRuangChat, grupLDtoMB, dataChat.idChat, dataChat,
            onCompleteListener, onFailureListener
        )
    }

    private fun sendChatMJ(dataLembaga: ModelDataLembaga) {
        val dataChat = ModelDataChat(
            dataLembaga.idLembaga,
            "", dataLembaga.idLembaga,
            "", System.currentTimeMillis())

        val onCompleteListener = OnCompleteListener<Void> {
            message.value = "Berhasil memverifikasi Lembaga Dakwah"
            sendNotificationLD(dataLembaga.idLembaga, true)
        }

        val onFailureListener = OnFailureListener {}

        FirebaseUtils.setValueWith2ChildObject(
            referenceRuangChat, grupLDtoMJ, dataChat.idChat,
            dataChat, onCompleteListener, onFailureListener
        )
    }

    private fun sendNotificationLD(idLembaga: String, accept: Boolean) {
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
                            Notification("Permintaan verifikasi lembaga Anda telah diterima", "Verifikasi"
                            , "com.exomatik.balligh.fcm_TARGET_NOTIFICATION_BERANDA_LD")
                        else Notification("Permintaan verifikasi lembaga Anda ditolak", "Verifikasi"
                            , "com.exomatik.balligh.fcm_TARGET_NOTIFICATION_BERANDA_LD")

                        if(data?.token.isNullOrEmpty()){
                            FirebaseUtils.simpanNotif(ModelNotif(notification, data?.idLembaga, ""))
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
            Constant.referenceUser, Constant.referenceIdLembaga
            , idLembaga
            , valueEventListener
        )
    }

    private fun requestReject(dataLembaga: ModelDataLembaga) {
        val onCompleteListener = OnCompleteListener<Void> {
            if (it.isSuccessful){
                message.value = "Berhasil menolak Lembaga Dakwah"
                sendNotificationLD(dataLembaga.idLembaga, false)
            }
            else{
                message.value = "Afwan, gagal menolak Lembaga Dakwah"
            }
        }

        val onFailureListener = OnFailureListener {
            message.value = it.message
        }

        FirebaseUtils.setValueWith3ChildString(
            Constant.akunLD
            , Constant.referenceBiodata
            , dataLembaga.idLembaga
            , Constant.status
            , Constant.rejected
            , onCompleteListener
            , onFailureListener
        )
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
            Constant.request,
            valueEventListener
        )
    }
}