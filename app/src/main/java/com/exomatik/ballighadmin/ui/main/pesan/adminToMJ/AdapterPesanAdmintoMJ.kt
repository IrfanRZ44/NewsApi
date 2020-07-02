package com.exomatik.ballighadmin.ui.main.pesan.adminToMJ

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.request.CachePolicy
import com.exomatik.ballighadmin.R
import com.exomatik.ballighadmin.model.ModelChat
import com.exomatik.ballighadmin.utils.Constant
import com.exomatik.ballighadmin.utils.Constant.pending
import com.exomatik.ballighadmin.utils.Constant.read
import com.exomatik.ballighadmin.utils.Constant.sended
import com.exomatik.ballighadmin.utils.Constant.unread
import java.util.*
import kotlin.collections.ArrayList

class AdapterPesanAdmintoMJ(private val listChat: ArrayList<ModelChat?>,
                            private val alertDelete: (ModelChat, RelativeLayout) -> Unit,
                            private val context: Context?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return if (viewType == VIEW_TYPE_ME) {
                val viewChatMine = layoutInflater.inflate(R.layout.item_chat_right, parent, false)
                MyChatViewHolder(viewChatMine)
            }
            else {
                val viewChatOther = layoutInflater.inflate(R.layout.item_chat_left, parent, false)
                OtherChatViewHolder(viewChatOther)
            }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (listChat[position]?.senderId == listChat[position]?.idTujuan) {
            configureMyChatViewHolder(holder as MyChatViewHolder, position)
        } else {
            configureOtherChatViewHolder(holder as OtherChatViewHolder, position)
        }
    }

    @Suppress("DEPRECATION")
    @SuppressLint("SetTextI18n")
    private fun configureMyChatViewHolder(myChatViewHolder: MyChatViewHolder, position: Int) {
        val tanggal = DateFormat.format(Constant.dateFormat3, listChat[position]?.timeStamp?:0).toString()
        var showingDate = true
        if (position != 0){
            val positionBefore = position - 1
            val tanggalBefore = DateFormat.format(Constant.dateFormat3, listChat[positionBefore]?.timeStamp?:0).toString()

            if (tanggal == tanggalBefore){
                showingDate = false
            }
        }

        if (showingDate){
            myChatViewHolder.cardDate.visibility = View.VISIBLE
            context?.resources?.getColor(R.color.colorPrimary)?.let {
                myChatViewHolder.cardDate.setCardBackgroundColor(
                    it
                )
            }
            listChat[position]?.timeStamp?.let { setTime(myChatViewHolder.textDate, it, tanggal) }
        }
        else{
            myChatViewHolder.cardDate.visibility = View.GONE
        }

        myChatViewHolder.backgroundChat.setBackgroundResource(R.drawable.bg_item_chat_admin_right)
        context?.resources?.getColor(R.color.white)?.let { myChatViewHolder.textMessage.setTextColor(it) }

        myChatViewHolder.textMessage.text = listChat[position]?.message
        val time = DateFormat.format(Constant.timeFormat,
            listChat[position]?.timeStamp?:0).toString()
        myChatViewHolder.textTime.text = time

        when {
            listChat[position]?.status == pending -> myChatViewHolder.imgStatus.setImageResource(R.drawable.ic_pesan_pending)
            listChat[position]?.status == sended -> myChatViewHolder.imgStatus.setImageResource(R.drawable.ic_pesan_sended)
            listChat[position]?.status == unread -> myChatViewHolder.imgStatus.setImageResource(R.drawable.ic_pesan_unread)
            listChat[position]?.status == read -> myChatViewHolder.imgStatus.setImageResource(R.drawable.ic_pesan_read)
        }

        val foto = listChat[position]?.urlFoto
        if (!foto.isNullOrEmpty()){
            myChatViewHolder.imgFoto.visibility = View.VISIBLE
            myChatViewHolder.imgFoto.load(foto) {
                crossfade(true)
                placeholder(R.drawable.ic_logo)
                error(R.drawable.ic_logo)
                fallback(R.drawable.ic_logo)
                memoryCachePolicy(CachePolicy.ENABLED)
            }
        }
        else{
            myChatViewHolder.imgFoto.visibility = View.GONE
        }

        myChatViewHolder.backgroundChat.setOnLongClickListener {
            myChatViewHolder.rlItem.setBackgroundResource(R.color.gray5)
            val dataChat = listChat[position]
            if (dataChat != null){
                if (dataChat.senderId == dataChat.idTujuan) alertDelete(dataChat, myChatViewHolder.rlItem)
            }
            true
        }
    }

    @Suppress("DEPRECATION")
    @SuppressLint("SetTextI18n")
    private fun configureOtherChatViewHolder(otherChatViewHolder: OtherChatViewHolder, position: Int) {
        val tanggal = DateFormat.format(Constant.dateFormat3, listChat[position]?.timeStamp?:0).toString()
        var showingDate = true
        if (position != 0){
            val positionBefore = position - 1
            val tanggalBefore = DateFormat.format(Constant.dateFormat3, listChat[positionBefore]?.timeStamp?:0).toString()

            if (tanggal == tanggalBefore){
                showingDate = false
            }
        }

        if (showingDate){
            otherChatViewHolder.cardDate.visibility = View.VISIBLE
            context?.resources?.getColor(R.color.colorPrimary)?.let {
                otherChatViewHolder.cardDate.setCardBackgroundColor(
                    it
                )
            }
            listChat[position]?.timeStamp?.let { setTime(otherChatViewHolder.textDate, it, tanggal) }
        }
        else{
            otherChatViewHolder.cardDate.visibility = View.GONE
        }

        otherChatViewHolder.textMessage.text = listChat[position]?.message
        val time = DateFormat.format(Constant.timeFormat,
            listChat[position]?.timeStamp?:0).toString()
        otherChatViewHolder.textTime.text = time

        val foto = listChat[position]?.urlFoto
        if (!foto.isNullOrEmpty()){
            otherChatViewHolder.imgFoto.visibility = View.VISIBLE
            otherChatViewHolder.imgFoto.load(foto) {
                crossfade(true)
                placeholder(R.drawable.ic_logo)
                error(R.drawable.ic_logo)
                fallback(R.drawable.ic_logo)
                memoryCachePolicy(CachePolicy.ENABLED)
            }
        }
        else{
            otherChatViewHolder.imgFoto.visibility = View.GONE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setTime(textTime: AppCompatTextView, timeStamp: Long, setTanggal: String) {
        val strDate1 = Date(timeStamp)
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
            textTime.text = setTanggal
        } else {
            if (bulan1 < bulan2) {
                textTime.text = setTanggal
            } else {
                if (tanggal1 < tanggal2) {
                    val hasil = tanggal2 - tanggal1
                    if (hasil == 1) {
                        textTime.text = "Kemarin"
                    } else {
                        textTime.text = setTanggal
                    }
                } else {
                    textTime.text = "Hari ini"
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return listChat.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (listChat[position]?.senderId == listChat[position]?.idTujuan) {
            VIEW_TYPE_ME
        } else {
            VIEW_TYPE_OTHER
        }
    }

    private class MyChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textMessage: AppCompatTextView = itemView.findViewById(R.id.textMessageMe)
        val textTime: AppCompatTextView = itemView.findViewById(R.id.textTimeMe)
        val imgStatus: AppCompatImageView = itemView.findViewById(R.id.imgStatus)
        val imgFoto: AppCompatImageView = itemView.findViewById(R.id.imgFoto)
        val backgroundChat: LinearLayoutCompat = itemView.findViewById(R.id.backgroundChat)
        val rlItem: RelativeLayout = itemView.findViewById(R.id.rlItem)
        val cardDate: CardView = itemView.findViewById(R.id.cardDate)
        val textDate: AppCompatTextView = itemView.findViewById(R.id.textDate)
    }

    private class OtherChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textMessage: AppCompatTextView = itemView.findViewById(R.id.textMessageOther)
        val textTime: AppCompatTextView = itemView.findViewById(R.id.textTimeOther)
        val imgFoto: AppCompatImageView = itemView.findViewById(R.id.imgFoto)
        val cardDate: CardView = itemView.findViewById(R.id.cardDate)
        val textDate: AppCompatTextView = itemView.findViewById(R.id.textDate)
    }

    companion object {
        private const val VIEW_TYPE_ME = 1
        private const val VIEW_TYPE_OTHER = 2
    }
}