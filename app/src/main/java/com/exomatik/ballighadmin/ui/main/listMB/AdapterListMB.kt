package com.exomatik.ballighadmin.ui.main.listMB

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

class AdapterListMB (private val listAfiliasi : ArrayList<ModelUser?>,
                     private val onClik : (ModelUser) -> Unit
) : RecyclerView.Adapter<AdapterListMB.AfiliasiHolder>(){

    inner class AfiliasiHolder(private val itemAfiliasi : View) : RecyclerView.ViewHolder(itemAfiliasi){
        @SuppressLint("SetTextI18n")
        @Suppress("DEPRECATION")
        fun bindAfiliasi(data: ModelUser,
                         onClik: (ModelUser) -> Unit){
            if (data.nama.isEmpty()){
                itemAfiliasi.namaLd.text = data.username
                itemAfiliasi.kabLd.text = "Kabupaten/Kota"
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
                itemAfiliasi.namaLd.text = data.nama
                itemAfiliasi.kabLd.text = data.kota
                itemAfiliasi.provLd.text = data.provinsi
                itemAfiliasi.imgLd.setBackgroundResource(android.R.color.transparent)

                itemAfiliasi.imgLd.load(data.foto) {
                    crossfade(true)
                    placeholder(R.drawable.ic_camera_white)
                    error(R.drawable.ic_camera_white)
                    fallback(R.drawable.ic_camera_white)
                    memoryCachePolicy(CachePolicy.ENABLED)
                }
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
        listAfiliasi[position]?.let { holder.bindAfiliasi(it, onClik) }
    }
}
