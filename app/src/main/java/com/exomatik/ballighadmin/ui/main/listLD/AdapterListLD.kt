package com.exomatik.ballighadmin.ui.main.listLD

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.request.CachePolicy
import com.exomatik.ballighadmin.R
import com.exomatik.ballighadmin.model.ModelUser
import kotlinx.android.synthetic.main.item_cari_afiliasi.view.*

class AdapterListLD (private val listAfiliasi : ArrayList<ModelUser?>,
                     private val onClik : (ModelUser) -> Unit,
                     private val getDetailLD : (String, View) -> Unit
) : RecyclerView.Adapter<AdapterListLD.AfiliasiHolder>(){

    inner class AfiliasiHolder(
        private val itemAfiliasi : View
    ) : RecyclerView.ViewHolder(itemAfiliasi){
        @SuppressLint("SetTextI18n")
        @Suppress("DEPRECATION")
        fun bindAfiliasi(data: ModelUser,
                         onClik: (ModelUser) -> Unit,
                         getDetailLD: (String, View) -> Unit){
            if (data.idLembaga.isEmpty()){
                itemAfiliasi.namaLd.text = data.username
                itemAfiliasi.kabLd.text = "Kota"
                itemAfiliasi.provLd.text = "Provinsi"

                itemAfiliasi.imgLd.load(R.drawable.ic_camera_white) {
                    crossfade(true)
                    placeholder(R.drawable.ic_camera_white)
                    error(R.drawable.ic_camera_white)
                    fallback(R.drawable.ic_camera_white)
                    memoryCachePolicy(CachePolicy.ENABLED)
                }
            }
            else{
                getDetailLD(data.idLembaga, itemAfiliasi)
            }

            itemAfiliasi.setOnClickListener {
                onClik(data)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AfiliasiHolder {
        return AfiliasiHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_cari_afiliasi, parent, false))
    }
    override fun getItemCount(): Int = listAfiliasi.size
    override fun onBindViewHolder(holder: AfiliasiHolder, position: Int) {
        listAfiliasi[position]?.let { holder.bindAfiliasi(it, onClik, getDetailLD) }
    }
}
