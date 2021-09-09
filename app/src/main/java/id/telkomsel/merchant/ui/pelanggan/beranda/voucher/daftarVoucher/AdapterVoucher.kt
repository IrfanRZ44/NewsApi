package id.telkomsel.merchant.ui.pelanggan.beranda.voucher.daftarVoucher

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import id.telkomsel.merchant.R
import id.telkomsel.merchant.model.ModelProduk
import id.telkomsel.merchant.model.ModelVoucher
import id.telkomsel.merchant.utils.Constant
import id.telkomsel.merchant.utils.adapter.convertRupiah
import kotlinx.android.synthetic.main.item_voucher.view.*

class AdapterVoucher(private val listVoucher: ArrayList<ModelVoucher>,
                     private val statusRequest: String,
                     private val onClickItem : (ModelVoucher) -> Unit
                     ) : RecyclerView.Adapter<AdapterVoucher.AfiliasiHolder>(){

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
            viewItem.textAlamat.text = item.dataMerchant?.alamat_merchant.toString()
            viewItem.textNamaMerchant.text = item.dataMerchant?.nama_merchant.toString()
            viewItem.textNamaProduk.text = item.dataProduk?.nama.toString()
            val harga = item.dataProduk?.harga?:0
            viewItem.textHargaProduk.text = convertRupiah(harga.toDouble())
            viewItem.textPromo.text = "Promo ${item.dataProduk?.promo}"
            viewItem.textTanggal.text = "Berlaku hingga ${item.dataProduk?.tgl_kadaluarsa}"

            if (statusRequest == Constant.active){
                viewItem.btnVoucher.visibility = View.VISIBLE
                viewItem.btnVoucher.setOnClickListener {
                    onClickItem(item)
                }
            }
            else{
                viewItem.btnVoucher.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AfiliasiHolder {
        return AfiliasiHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_voucher,
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
