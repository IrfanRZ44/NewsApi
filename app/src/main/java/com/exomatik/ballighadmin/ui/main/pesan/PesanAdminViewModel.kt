package com.exomatik.ballighadmin.ui.main.pesan

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exomatik.ballighadmin.R
import com.exomatik.ballighadmin.base.BaseViewModel
import com.exomatik.ballighadmin.model.*
import com.exomatik.ballighadmin.ui.main.pesan.adminToLD.PesanAdmintoLDFragment
import com.exomatik.ballighadmin.ui.main.pesan.adminToMB.PesanAdmintoMBFragment
import com.exomatik.ballighadmin.ui.main.pesan.adminToMJ.PesanAdmintoMJFragment
import com.exomatik.ballighadmin.utils.Constant
import com.exomatik.ballighadmin.utils.Constant.active
import com.exomatik.ballighadmin.utils.Constant.admin
import com.exomatik.ballighadmin.utils.Constant.akunLD
import com.exomatik.ballighadmin.utils.Constant.akunMB
import com.exomatik.ballighadmin.utils.Constant.akunMJ
import com.exomatik.ballighadmin.utils.Constant.chatLDtoAdmin
import com.exomatik.ballighadmin.utils.Constant.chatMBtoAdmin
import com.exomatik.ballighadmin.utils.Constant.chatMJtoAdmin
import com.exomatik.ballighadmin.utils.Constant.noMessage
import com.exomatik.ballighadmin.utils.Constant.referenceBiodata
import com.exomatik.ballighadmin.utils.Constant.referenceChat
import com.exomatik.ballighadmin.utils.Constant.referenceIdTujuan
import com.exomatik.ballighadmin.utils.Constant.referenceRuangChat
import com.exomatik.ballighadmin.utils.Constant.referenceUser
import com.exomatik.ballighadmin.utils.Constant.referenceUsername
import com.exomatik.ballighadmin.utils.FirebaseUtils
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.item_fragment_chat.view.*
import java.util.*
import kotlin.collections.ArrayList

class PesanAdminViewModel(private val rcChat: RecyclerView,
                          private val context: Context?,
                          private val navController: NavController) : BaseViewModel() {
    var adapter: AdapterChatAdminFragment? = null
    val listChat = ArrayList<ModelListChat>()
    val listChatSearch = ArrayList<ModelListChat>()
    val listNama = ArrayList<ModelSearchChat>()

    fun cekList() {
        isShowLoading.value = false
        if (listChat.size == 0) status.value = noMessage
        else status.value = ""
    }

    fun initAdapter() {
        adapter = AdapterChatAdminFragment(listChat,
            { item: ModelListChat, view: View, ruangChat ->
                getUnreadChat(
                    item,
                    view,
                    ruangChat
                )
            },
            { textWaktu: AppCompatTextView, item: ModelListChat ->
                setTime(
                    textWaktu,
                    item
                )
            },
            { item: ModelListChat ->
                onClickItem(
                    item
                )
            },
            navController
        )
        val layoutMgr = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rcChat.layoutManager = layoutMgr
        rcChat.adapter = adapter
    }

    fun getChatMuballigh(){
        isShowLoading.value = true

        val childEvent = object : ChildEventListener {
            override fun onChildAdded(result: DataSnapshot, key: String?) {
                status.value = ""

                if (result.exists()) {
                    val data = result.getValue(ModelDataChat::class.java)

                    if (data != null){
                        if (data.idTujuan == admin){
                            val waktu = DateFormat.format(Constant.timeFormat, data.timeStamp).toString()
                            val tanggal = DateFormat.format("dd", data.timeStamp).toString()
                            val bulan = DateFormat.format("M", data.timeStamp).toString()
                            val tahun = DateFormat.format("yyyy", data.timeStamp).toString()

                            val dataChat = ModelListChat(admin, data.idUser, data.idChat, akunMB,
                                waktu, tahun, bulan, tanggal, data.message, data.timeStamp
                            )

                            getDataPengurusMB(dataChat, false, 0)
                        }
                    }
                }

                cekList()
            }

            override fun onChildMoved(result: DataSnapshot, key: String?) {

            }

            override fun onChildChanged(result: DataSnapshot, key: String?) {
                status.value = ""
                if (result.exists()){
                    val data = result.getValue(ModelDataChat::class.java)
                    if (data != null){
                        for (i in listChat.indices){
                            if (listChat[i].idChat == data.idChat && listChat[i].jenisChat == akunMB){
                                val waktu = DateFormat.format(Constant.timeFormat, data.timeStamp).toString()
                                val tanggal = DateFormat.format("dd", data.timeStamp).toString()
                                val bulan = DateFormat.format("M", data.timeStamp).toString()
                                val tahun = DateFormat.format("yyyy", data.timeStamp).toString()

                                val chat = listChat[i]
                                chat.waktu = waktu
                                chat.tanggal = tanggal
                                chat.bulan = bulan
                                chat.tahun = tahun
                                chat.msg = data.message
                                chat.timeStmp = data.timeStamp
                                chat.jenisChat = akunMB

                                getDataPengurusMB(chat, true, i)
                            }
                        }
                    }
                }

                isShowLoading.value = false
            }

            override fun onChildRemoved(result: DataSnapshot) {
            }

            override fun onCancelled(result: DatabaseError) {
                cekList()
            }
        }

        cekList()
        getChatMasjid()
        FirebaseUtils.refresh2ListChatWith1Child(
            referenceRuangChat,
            chatMBtoAdmin,
            referenceIdTujuan,
            admin,
            childEvent
        )
    }

    private fun getChatMasjid(){
        isShowLoading.value = true

        val childEvent = object : ChildEventListener {
            override fun onChildAdded(result: DataSnapshot, key: String?) {
                status.value = ""

                if (result.exists()) {
                    val data = result.getValue(ModelDataChat::class.java)

                    if (data != null){
                        if (data.idTujuan == admin){
                            val waktu = DateFormat.format(Constant.timeFormat, data.timeStamp).toString()
                            val tanggal = DateFormat.format("dd", data.timeStamp).toString()
                            val bulan = DateFormat.format("M", data.timeStamp).toString()
                            val tahun = DateFormat.format("yyyy", data.timeStamp).toString()

                            val dataChat = ModelListChat(admin, data.idUser, data.idChat, akunMJ,
                                waktu, tahun, bulan, tanggal, data.message, data.timeStamp
                            )

                            getDataPengurusMJ(dataChat, false, 0)
                        }
                    }
                }

                cekList()
            }

            override fun onChildMoved(result: DataSnapshot, key: String?) {

            }

            override fun onChildChanged(result: DataSnapshot, key: String?) {
                status.value = ""
                if (result.exists()){
                    val data = result.getValue(ModelDataChat::class.java)
                    if (data != null){
                        for (i in listChat.indices){
                            if (listChat[i].idChat == data.idChat && listChat[i].jenisChat == akunMJ){
                                val waktu = DateFormat.format(Constant.timeFormat, data.timeStamp).toString()
                                val tanggal = DateFormat.format("dd", data.timeStamp).toString()
                                val bulan = DateFormat.format("M", data.timeStamp).toString()
                                val tahun = DateFormat.format("yyyy", data.timeStamp).toString()

                                val chat = listChat[i]
                                chat.waktu = waktu
                                chat.tanggal = tanggal
                                chat.bulan = bulan
                                chat.tahun = tahun
                                chat.msg = data.message
                                chat.timeStmp = data.timeStamp
                                chat.jenisChat = akunMJ

                                getDataPengurusMJ(chat, true, i)
                            }
                        }
                    }
                }

                isShowLoading.value = false
            }

            override fun onChildRemoved(result: DataSnapshot) {
            }

            override fun onCancelled(result: DatabaseError) {
                cekList()
            }
        }

        cekList()
        getChatLembaga()
        FirebaseUtils.refresh2ListChatWith1Child(
            referenceRuangChat,
            chatMJtoAdmin,
            referenceIdTujuan,
            admin,
            childEvent
        )
    }

    private fun getChatLembaga(){
        isShowLoading.value = true

        val childEvent = object : ChildEventListener {
            override fun onChildAdded(result: DataSnapshot, key: String?) {
                status.value = ""

                if (result.exists()) {
                    val data = result.getValue(ModelDataChat::class.java)

                    if (data != null){
                        if (data.idTujuan == admin){
                            val waktu = DateFormat.format(Constant.timeFormat, data.timeStamp).toString()
                            val tanggal = DateFormat.format("dd", data.timeStamp).toString()
                            val bulan = DateFormat.format("M", data.timeStamp).toString()
                            val tahun = DateFormat.format("yyyy", data.timeStamp).toString()

                            val dataChat = ModelListChat(admin, data.idUser, data.idChat, akunLD,
                                waktu, tahun, bulan, tanggal, data.message, data.timeStamp
                            )

                            getDataPengurusLD(dataChat, false, 0)

                        }
                    }
                }

                cekList()
            }

            override fun onChildMoved(result: DataSnapshot, key: String?) {

            }

            override fun onChildChanged(result: DataSnapshot, key: String?) {
                status.value = ""
                if (result.exists()){
                    val data = result.getValue(ModelDataChat::class.java)
                    if (data != null){
                        for (i in listChat.indices){
                            if (listChat[i].idChat == data.idChat && listChat[i].jenisChat == akunLD){
                                val waktu = DateFormat.format(Constant.timeFormat, data.timeStamp).toString()
                                val tanggal = DateFormat.format("dd", data.timeStamp).toString()
                                val bulan = DateFormat.format("M", data.timeStamp).toString()
                                val tahun = DateFormat.format("yyyy", data.timeStamp).toString()

                                val chat = listChat[i]
                                chat.waktu = waktu
                                chat.tanggal = tanggal
                                chat.bulan = bulan
                                chat.tahun = tahun
                                chat.msg = data.message
                                chat.timeStmp = data.timeStamp
                                chat.jenisChat = akunLD

                                getDataPengurusLD(chat, true, i)
                            }
                        }
                    }
                }

                isShowLoading.value = false
            }

            override fun onChildRemoved(result: DataSnapshot) {
            }

            override fun onCancelled(result: DatabaseError) {
                cekList()
            }
        }

        cekList()

        FirebaseUtils.refresh2ListChatWith1Child(
            referenceRuangChat,
            chatLDtoAdmin,
            referenceIdTujuan,
            admin,
            childEvent
        )
    }

    private fun getUnreadChat(item: ModelListChat, view: View, ruangChat: String) {
        isShowLoading.value = true

        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(result: DatabaseError) {
                isShowLoading.value = false
            }

            override fun onDataChange(result: DataSnapshot) {
                if (result.exists()) {
                    var unread = 0
                    for (snapshot in result.children) {
                        val data = snapshot.getValue(ModelChat::class.java)

                        if (data?.idChat == item.idChat){
                            if (data.receiverId == admin && data.status != Constant.read){
                                unread++
                            }
                        }
                    }
                    if (unread != 0) view.textChatUnread.text = unread.toString()
                }

                isShowLoading.value = false
            }
        }

        FirebaseUtils.getData2Child(
            referenceChat,
            ruangChat,
            item.idChat,
            valueEventListener
        )
    }

    @SuppressLint("SetTextI18n")
    private fun getDataMasjid(item: ModelListChat, onChange: Boolean, position: Int, idMasjid: String) {
        isShowLoading.value = true

        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(result: DatabaseError) {
                item.nama = item.idTujuan
                item.foto = ""

                isShowLoading.value = false
                if (onChange){
                    sortChangedListByDate(item, position)
                }
                else {
                    sortListByDate(item)
                }
            }

            override fun onDataChange(result: DataSnapshot) {
                if (result.exists()) {
                    val data = result.getValue(ModelDataMasjid::class.java)

                    if (data?.status.equals(active)) {
                        var isNotAdded = true
                        for (i in listNama.indices){
                            if (listNama[i].id == data?.idMasjid &&
                                    listNama[i].timeStamp == item.timeStmp &&
                                    listNama[i].jenisChat == akunMJ){
                                isNotAdded = false
                            }
                        }
                        if (isNotAdded){
                            listNama.add(ModelSearchChat(data?.namaMasjid?:item.idTujuan, item.idTujuan, akunMJ,
                                item.timeStmp))
                        }

                        item.nama = data?.namaMasjid?:item.idTujuan
                        item.foto = data?.fotoMasjid?:""

                        if (onChange){
                            sortChangedListByDate(item, position)
                        }
                        else {
                            sortListByDate(item)
                        }
                    }
                }
                else{
                    item.nama = item.idTujuan
                    item.foto = ""

                    if (onChange){
                        sortChangedListByDate(item, position)
                    }
                    else {
                        sortListByDate(item)
                    }
                }

                isShowLoading.value = false
            }
        }

        if (idMasjid.isNotEmpty()){
            FirebaseUtils.getData2Child(
                akunMJ,
                referenceBiodata,
                idMasjid,
                valueEventListener
            )
        }
        else{
            item.nama = item.idTujuan
            item.foto = ""

            if (onChange){
                sortChangedListByDate(item, position)
            }
            else {
                sortListByDate(item)
            }
        }
    }

    private fun getDataPengurusMJ(item: ModelListChat, onChange: Boolean, position: Int) {
        isShowLoading.value = true

        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(result: DatabaseError) {
                item.status = Constant.offline

                getDataMasjid(item, onChange, position, "")
                isShowLoading.value = false
            }

            override fun onDataChange(result: DataSnapshot) {
                if (result.exists()) {
                    val data = result.getValue(ModelUser::class.java)

                    item.status = data?.status?:Constant.offline
                    getDataMasjid(item, onChange, position, data?.idMasjid?:"")
                } else {
                    item.status = Constant.offline
                    getDataMasjid(item, onChange, position, "")
                }

                isShowLoading.value = false
            }
        }

        FirebaseUtils.getData1Child(
            referenceUser, item.idTujuan, valueEventListener
        )
    }

    @SuppressLint("SetTextI18n")
    private fun getDataLembaga(item: ModelListChat, onChange: Boolean, position: Int, idLembaga: String) {
        isShowLoading.value = true

        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(result: DatabaseError) {
                item.nama = item.idTujuan
                item.foto = ""

                isShowLoading.value = false
                if (onChange){
                    sortChangedListByDate(item, position)
                }
                else{
                    sortListByDate(item)
                }
            }

            override fun onDataChange(result: DataSnapshot) {
                isShowLoading.value = false
                if (result.exists()) {
                    val data = result.getValue(ModelDataLembaga::class.java)

                    if (data?.status.equals(active)) {
                        item.nama = data?.namaPanjangLembaga?:item.idTujuan
                        item.foto = data?.fotoLembaga?:""

                        var isNotAdded = true
                        for (i in listNama.indices){
                            if (listNama[i].id == data?.idLembaga &&
                                listNama[i].timeStamp == item.timeStmp &&
                                listNama[i].jenisChat == akunLD){
                                isNotAdded = false
                            }
                        }
                        if (isNotAdded){
                            listNama.add(ModelSearchChat(data?.namaPanjangLembaga?:item.idTujuan, item.idTujuan, akunLD,
                                item.timeStmp))
                        }

                        getDataPengurusLD(item, onChange, position)
                    }
                }
                else{
                    item.nama = item.idTujuan
                    item.foto = ""

                    if (onChange){
                        sortChangedListByDate(item, position)
                    }
                    else{
                        sortListByDate(item)
                    }
                }

                isShowLoading.value = false
            }
        }

        if (idLembaga.isNotEmpty()){
            FirebaseUtils.getData2Child(
                akunLD,
                referenceBiodata,
                idLembaga,
                valueEventListener
            )
        }
        else{
            item.nama = item.idTujuan
            item.foto = ""

            if (onChange){
                sortChangedListByDate(item, position)
            }
            else{
                sortListByDate(item)
            }
        }
    }

    private fun getDataPengurusLD(item: ModelListChat, onChange: Boolean, position: Int) {
        isShowLoading.value = true

        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(result: DatabaseError) {
                item.status = Constant.offline

                getDataLembaga(item, onChange, position, "")
                isShowLoading.value = false
            }

            override fun onDataChange(result: DataSnapshot) {
                if (result.exists()) {
                    val data = result.getValue(ModelUser::class.java)

                    item.status = data?.status?:Constant.offline
                    getDataLembaga(item, onChange, position, data?.idLembaga?:"")
                } else {
                    item.status = Constant.offline
                    getDataLembaga(item, onChange, position, "")
                }

                isShowLoading.value = false
            }
        }

        FirebaseUtils.getData1Child(
            referenceUser, item.idTujuan, valueEventListener
        )
    }

    @SuppressLint("SetTextI18n")
    private fun getDataPengurusMB(item: ModelListChat, onChange: Boolean, position: Int) {
        isShowLoading.value = true

        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(result: DatabaseError) {
                item.nama = item.idTujuan
                item.foto = ""
                item.status = Constant.offline

                if (onChange){
                    sortChangedListByDate(item, position)
                }
                else {
                    sortListByDate(item)
                }

                isShowLoading.value = false
            }

            override fun onDataChange(result: DataSnapshot) {
                if (result.exists()) {
                    for (snapshot in result.children) {
                        val data = snapshot.getValue(ModelUser::class.java)

                        var isNotAdded = true
                        for (i in listNama.indices){
                            if (listNama[i].id == data?.username &&
                                listNama[i].timeStamp == item.timeStmp &&
                                listNama[i].jenisChat == akunMB){
                                isNotAdded = false
                            }
                        }
                        if (isNotAdded){
                            listNama.add(ModelSearchChat(data?.nama?:item.idTujuan, item.idTujuan, akunMB,
                                item.timeStmp))
                        }

                        item.nama = data?.nama?:item.idTujuan
                        item.foto = data?.foto?:""
                        item.status = data?.status?:Constant.offline
                        if (onChange){
                            sortChangedListByDate(item, position)
                        }
                        else {
                            sortListByDate(item)
                        }
                    }
                } else {
                    item.nama = item.idTujuan
                    item.foto = ""
                    item.status = Constant.offline
                    if (onChange){
                        sortChangedListByDate(item, position)
                    }
                    else {
                        sortListByDate(item)
                    }
                }

                isShowLoading.value = false
            }
        }

        FirebaseUtils.searchDataWith1ChildObject(
            referenceUser, referenceUsername, item.idTujuan, valueEventListener
        )
    }

    private fun sortListByDate(dataChat: ModelListChat) {
        listChat.add(dataChat)
        val listSorted = listChat.sortedByDescending { it.timeStmp }
        listChat.clear()
        listChatSearch.clear()
        adapter?.notifyDataSetChanged()
        for (i: Int in listSorted.indices) {
            listChat.add(listSorted[i])
            listChatSearch.add(listSorted[i])
            adapter?.notifyDataSetChanged()
        }
        cekList()
    }

    private fun sortChangedListByDate(dataChat: ModelListChat, position: Int) {
        listChat[position] = dataChat
        val listSorted = listChat.sortedByDescending { it.timeStmp }
        listChat.clear()
        listChatSearch.clear()
        adapter?.notifyDataSetChanged()
        for (i: Int in listSorted.indices) {
            listChat.add(listSorted[i])
            listChatSearch.add(listSorted[i])
            adapter?.notifyDataSetChanged()
        }
        cekList()
    }

    @Suppress("DEPRECATION")
    @SuppressLint("SetTextI18n")
    private fun setTime(textTime: AppCompatTextView, item: ModelListChat) {
        val strDate1 = Date(item.timeStmp)
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
        if (tahun1 < tahun2) {
            textTime.text = "${item.tanggal}/${item.bulan}/${item.tahun}"
        } else {
            if (bulan1 < bulan2) {
                textTime.text = "${item.tanggal}/${item.bulan}/${item.tahun}"
            } else {
                if (tanggal1 < tanggal2) {
                    val hasil = tanggal2 - tanggal1
                    if (hasil == 1) {
                        textTime.text = "Kemarin"
                    } else {
                        textTime.text = "${item.tanggal}/${item.bulan}/${item.tahun}"
                    }
                } else {
                    textTime.text = "${item.waktu} "
                }
            }
        }
    }

    private fun onClickItem(item: ModelListChat){
        val bundle = Bundle()

        when {
            item.jenisChat == akunMJ -> {
                val fragmentTujuan = PesanAdmintoMJFragment()
                bundle.putString("username", item.idTujuan)
                fragmentTujuan.arguments = bundle
                navController.navigate(R.id.pesanAdmintoMJFragment, bundle)
            }
            item.jenisChat == akunLD -> {
                val fragmentTujuan = PesanAdmintoLDFragment()
                bundle.putString("username", item.idTujuan)
                fragmentTujuan.arguments = bundle
                navController.navigate(R.id.pesanAdmintoLDFragment, bundle)
            }
            item.jenisChat == akunMB -> {
                val fragmentTujuan = PesanAdmintoMBFragment()
                bundle.putString("username", item.idTujuan)
                bundle.putString("idChat", item.idChat)
                fragmentTujuan.arguments = bundle
                navController.navigate(R.id.pesanAdmintoMBFragment, bundle)
            }
        }
    }
}