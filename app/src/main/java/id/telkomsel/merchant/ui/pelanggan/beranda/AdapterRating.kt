package id.telkomsel.merchant.ui.pelanggan.beranda

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar.OnRatingBarChangeListener
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import id.telkomsel.merchant.R
import id.telkomsel.merchant.model.ModelVoucher
import id.telkomsel.merchant.utils.adapter.convertRupiah
import kotlinx.android.synthetic.main.item_voucher_rating.view.*

class AdapterRating(
    private val listVoucher: ArrayList<ModelVoucher>,
    private val onClickItem: (ModelVoucher, Int) -> Unit
) : RecyclerView.Adapter<AdapterRating.AfiliasiHolder>(){
    var kodeVoucher = ""

    inner class AfiliasiHolder(private val viewItem: View) : RecyclerView.ViewHolder(viewItem){
        fun bindAfiliasi(item: ModelVoucher){
            viewItem.imgFoto.load(item.dataProduk?.url_foto) {
                crossfade(true)
                placeholder(R.drawable.ic_camera_white)
                error(R.drawable.ic_camera_white)
                fallback(R.drawable.ic_camera_white)
                memoryCachePolicy(CachePolicy.ENABLED)
            }

            viewItem.btnRating.rating = 0.0F
            viewItem.textNamaMerchant.text = item.dataMerchant?.nama_merchant.toString()
            viewItem.textNamaProduk.text = item.dataProduk?.nama.toString()
            val harga = item.dataProduk?.harga?:0
            viewItem.textHargaProduk.text = convertRupiah(harga.toDouble())

            viewItem.btnClose.setOnClickListener {
                if (kodeVoucher.isEmpty() || kodeVoucher != item.kode_voucher){
                    onClickItem(item, 0)
                }
            }

            viewItem.btnRating.onRatingBarChangeListener =
                OnRatingBarChangeListener { _, rating, _ ->
                    if (kodeVoucher.isEmpty() || kodeVoucher != item.kode_voucher){
                        onClickItem(item, rating.toInt())
                    }
                    kodeVoucher = item.kode_voucher
                }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AfiliasiHolder {
        return AfiliasiHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_voucher_rating,
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
