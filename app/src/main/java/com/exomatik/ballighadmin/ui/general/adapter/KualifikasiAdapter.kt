package com.exomatik.ballighadmin.ui.general.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.exomatik.ballighadmin.R
import kotlinx.android.synthetic.main.item_kualifikasi.view.*

class KualifikasiAdapter(private val list: ArrayList<String>) :
    RecyclerView.Adapter<KualifikasiAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_kualifikasi,
                parent,
                false
            )
        )
    }
    override fun getItemCount(): Int = list.size
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.view.textItem.text = list[position]
    }
    class Holder(val view: View) : RecyclerView.ViewHolder(view)
}