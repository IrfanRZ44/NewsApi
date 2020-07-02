package com.exomatik.ballighadmin.ui.main.pesan

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.exomatik.ballighadmin.R
import com.exomatik.ballighadmin.model.ModelListChat
import com.exomatik.ballighadmin.utils.Constant
import com.exomatik.ballighadmin.utils.Constant.akunLD
import com.exomatik.ballighadmin.utils.Constant.akunMB
import com.exomatik.ballighadmin.utils.Constant.akunMJ
import com.exomatik.ballighadmin.utils.onClickFoto
import kotlinx.android.synthetic.main.item_fragment_chat.view.*

class AdapterChatAdminFragment(private val listChat : ArrayList<ModelListChat>,
                               private val getUnreadChat: (ModelListChat, View, String) -> Unit,
                               private val setTime: (AppCompatTextView, ModelListChat) -> Unit,
                               private val onClickItem: (ModelListChat) -> Unit,
                               private val navController: NavController
) :
    RecyclerView.Adapter<AdapterChatAdminFragment.ChatHolder>() {

    inner class ChatHolder(private val itemChat: View) : RecyclerView.ViewHolder(itemChat) {
        @SuppressLint("SetTextI18n")
        fun bindChatPerson(dataChat: ModelListChat,
                           getUnreadChat: (ModelListChat, View, String) -> Unit,
                           setTime: (AppCompatTextView, ModelListChat) -> Unit,
                           onClickItem: (ModelListChat) -> Unit
                           ) {
            when {
                dataChat.jenisChat == akunMJ -> {
                    itemChat.rlActive.visibility = View.VISIBLE
                    itemChat.imgJenisAkun.setImageResource(R.drawable.ic_message_mj)
                    getUnreadChat(dataChat, itemChat, Constant.chatMJtoAdmin)
                }
                dataChat.jenisChat == akunLD -> {
                    itemChat.rlActive.visibility = View.VISIBLE
                    itemChat.imgJenisAkun.setImageResource(R.drawable.ic_message_ld)
                    getUnreadChat(dataChat, itemChat, Constant.chatLDtoAdmin)
                }
                dataChat.jenisChat == akunMB -> {
                    itemChat.rlActive.visibility = View.VISIBLE
                    itemChat.imgJenisAkun.setImageResource(R.drawable.ic_message_mb)
                    getUnreadChat(dataChat, itemChat, Constant.chatMBtoAdmin)
                }
            }

            itemChat.imgFoto?.load(dataChat.foto) {
                crossfade(true)
                placeholder(R.drawable.ic_logo)
                transformations(CircleCropTransformation())
                error(R.drawable.ic_logo)
                fallback(R.drawable.ic_logo)
                memoryCachePolicy(CachePolicy.ENABLED)
            }
            itemChat.imgFoto?.setOnClickListener {
                onClickFoto(dataChat.foto, navController)
            }

            itemChat.textName.text = dataChat.nama
            itemChat.textMessage.text = dataChat.msg
            if (dataChat.status == Constant.online) itemChat.rlActive.setBackgroundResource(R.drawable.circle_green1)
            else itemChat.rlActive.setBackgroundResource(R.drawable.circle_gray5)
            setTime(itemChat.textWaktu, dataChat)

            itemChat.setOnClickListener {
                onClickItem(dataChat)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {
        return ChatHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_fragment_chat,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = listChat.size

    override fun onBindViewHolder(holder: ChatHolder, position: Int) {
        holder.bindChatPerson(listChat[position], getUnreadChat, setTime, onClickItem)
    }
}