package id.telkomsel.merchant.ui.merchant.voucher.daftarVoucher

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import id.telkomsel.merchant.R
import id.telkomsel.merchant.model.ModelVoucher
import id.telkomsel.merchant.utils.adapter.convertRupiah
import kotlinx.android.synthetic.main.item_voucher_merchant.view.*

class AdapterVoucher(private val listVoucher: ArrayList<ModelVoucher>,
                     private val statusRequest: String) : RecyclerView.Adapter<AdapterVoucher.AfiliasiHolder>(){

    inner class AfiliasiHolder(private val viewItem: View) : RecyclerView.ViewHolder(viewItem){
        @SuppressLint("SetTextI18n")
        fun bindAfiliasi(item: ModelVoucher){
            viewItem.imgFoto.load(item.dataProduk?.url_foto) {
                crossfade(true)
                placeholder(R.drawable.ic_camera_white)
                error(R.drawable.ic_camera_white)
                fallback(R.drawable.ic_camera_white)
                memoryCachePolicy(CachePolicy.ENABLED)
            }

            viewItem.textJumlah.text = "Jumlah ${item.jumlah} Voucher"
            viewItem.textNamaProduk.text = item.dataProduk?.nama.toString()
            val harga = item.dataProduk?.harga?:0
            viewItem.textHargaProduk.text = convertRupiah(harga.toDouble())
            viewItem.textPromo.text = "Promo ${item.dataProduk?.promo}"
            viewItem.textTanggal.text = "Berlaku hingga ${item.dataProduk?.tgl_kadaluarsa}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AfiliasiHolder {
        return AfiliasiHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_voucher_merchant,
                parent,
                false
            )
        )
    }
    override fun getItemCount(): Int = listVoucher.size
    override fun onBindViewHolder(holder: AfiliasiHolder, position: Int) {
        holder.bindAfiliasi(listVoucher[position])
    }
}
