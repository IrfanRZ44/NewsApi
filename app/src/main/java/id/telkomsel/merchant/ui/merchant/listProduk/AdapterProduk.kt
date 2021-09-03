package id.telkomsel.merchant.ui.merchant.listProduk

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import id.telkomsel.merchant.R
import id.telkomsel.merchant.model.ModelProduk
import id.telkomsel.merchant.utils.adapter.convertNumberWithoutRupiah
import id.telkomsel.merchant.utils.adapter.convertRupiah
import kotlinx.android.synthetic.main.item_produk.view.*

class AdapterProduk(private val listItem: ArrayList<ModelProduk>,
                    private val onClickItem : (ModelProduk) -> Unit
) : RecyclerView.Adapter<AdapterProduk.AfiliasiHolder>(){
    inner class AfiliasiHolder(private val viewItem : View) : RecyclerView.ViewHolder(viewItem){
        @SuppressLint("SetTextI18n")
        fun bindAfiliasi(item: ModelProduk) {
            viewItem.textNama.text = item.nama
            viewItem.textHarga.text = convertRupiah(item.harga.toDouble())
            viewItem.textStok.text = "${item.stok} stok"
            viewItem.textPromo.text = "${item.promo} \nTukar ${convertNumberWithoutRupiah(item.jumlah_poin.toDouble())} Poin"
            viewItem.btnFavorit.visibility = View.GONE
            viewItem.textKota.text = item.dataMerchant?.kabupaten
            viewItem.textTerjual.text = "${item.rating}\t\t Terjual ${item.terjual}"

            viewItem.imgFoto.load(item.url_foto) {
                crossfade(true)
                placeholder(R.drawable.ic_camera_white)
                error(R.drawable.ic_camera_white)
                fallback(R.drawable.ic_camera_white)
                memoryCachePolicy(CachePolicy.ENABLED)
            }

            viewItem.setOnClickListener {
                onClickItem(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AfiliasiHolder {
        return AfiliasiHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_produk, parent, false))
    }

    override fun getItemCount(): Int = listItem.size
    override fun onBindViewHolder(holder: AfiliasiHolder, position: Int) {
        holder.bindAfiliasi(listItem[position])
    }
}
