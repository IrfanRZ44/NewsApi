package id.telkomsel.merchant.ui.pelanggan.beranda.riwayatPoin

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.telkomsel.merchant.R
import id.telkomsel.merchant.model.ModelRiwayatPoin
import kotlinx.android.synthetic.main.item_riwayat_poin.view.*
import java.text.NumberFormat

class AdapterRiwayatPoin(private val listKelas: ArrayList<ModelRiwayatPoin>) : RecyclerView.Adapter<AdapterRiwayatPoin.AfiliasiHolder>(){

    inner class AfiliasiHolder(private val viewItem: View) : RecyclerView.ViewHolder(viewItem){
        @SuppressLint("SetTextI18n")
        fun bindAfiliasi(item: ModelRiwayatPoin){
            viewItem.textTransaksi.text = item.transaksi

            val format = NumberFormat.getCurrencyInstance()
            viewItem.textPoin.text = item.status + format.format(item.poin)
            if (item.status == "-"){
                viewItem.textPoin.setTextColor(Color.RED)
            }
            else{
                viewItem.textPoin.setTextColor(Color.GREEN)
            }
            
            viewItem.textTanggal.text = item.tanggal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AfiliasiHolder {
        return AfiliasiHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_riwayat_poin,
                parent,
                false
            )
        )
    }
    override fun getItemCount(): Int = listKelas.size
    override fun onBindViewHolder(holder: AfiliasiHolder, position: Int) {
        holder.bindAfiliasi(listKelas[position])
    }
}
