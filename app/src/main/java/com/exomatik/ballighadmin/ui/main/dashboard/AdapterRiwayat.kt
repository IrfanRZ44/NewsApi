package com.exomatik.ballighadmin.ui.main.dashboard

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.exomatik.ballighadmin.R
import com.exomatik.ballighadmin.model.ModelRiwayat
import kotlinx.android.synthetic.main.item_riwayat.view.*

class AdapterRiwayat (private val listAfiliasi : ArrayList<ModelRiwayat?>) : RecyclerView.Adapter<AdapterRiwayat.AfiliasiHolder>(){

    inner class AfiliasiHolder(
        private val itemAfiliasi : View
    ) : RecyclerView.ViewHolder(itemAfiliasi){
        @SuppressLint("SetTextI18n")
        @Suppress("DEPRECATION")
        fun bindAfiliasi(data: ModelRiwayat){
            if (data.isFirst){
                itemAfiliasi.card1.visibility = View.GONE
                itemAfiliasi.card2.visibility = View.VISIBLE
            }
            else{
                itemAfiliasi.card1.visibility = View.VISIBLE
                itemAfiliasi.card2.visibility = View.GONE
                itemAfiliasi.textTanggal.text = data.tanggal
                itemAfiliasi.textMB.text = "22"
                itemAfiliasi.textMJ.text = "2"
                itemAfiliasi.textLD.text = "3"
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AfiliasiHolder {
        return AfiliasiHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_riwayat, parent, false))
    }
    override fun getItemCount(): Int = listAfiliasi.size
    override fun onBindViewHolder(holder: AfiliasiHolder, position: Int) {
        listAfiliasi[position]?.let { holder.bindAfiliasi(it) }
    }
}
