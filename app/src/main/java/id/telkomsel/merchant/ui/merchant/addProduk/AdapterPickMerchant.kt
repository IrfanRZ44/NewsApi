package id.telkomsel.merchant.ui.merchant.addProduk

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import id.telkomsel.merchant.R
import id.telkomsel.merchant.model.ModelMerchant
import kotlinx.android.synthetic.main.item_merchant.view.*

class AdapterPickMerchant (private val listData : ArrayList<ModelMerchant>,
                           private val onClick : (ModelMerchant) -> Unit
) : RecyclerView.Adapter<AdapterPickMerchant.AfiliasiHolder>(){

    inner class AfiliasiHolder(private val viewItem : View) : RecyclerView.ViewHolder(viewItem){
        @SuppressLint("SetTextI18n")
        fun bindAfiliasi(item: ModelMerchant){
            viewItem.textNama.text = item.nama_merchant
            viewItem.textWilayah.text = "${item.kelurahan}, ${item.kecamatan}, ${item.kabupaten}"
            viewItem.textAlamat.text = item.alamat_merchant
            viewItem.textKategori.text = "Kategori"
            viewItem.textValidasi.visibility = View.GONE
            viewItem.btnKonfirmasi.text = "Pilih"

            viewItem.imgFoto.load(item.foto_profil) {
                crossfade(true)
                transformations(CircleCropTransformation())
                placeholder(R.drawable.ic_image_gray)
                error(R.drawable.ic_image_gray)
                fallback(R.drawable.ic_image_gray)
                memoryCachePolicy(CachePolicy.ENABLED)
            }

            viewItem.btnKonfirmasi.setOnClickListener {
                onClick(item)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AfiliasiHolder {
        return AfiliasiHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_merchant, parent, false))
    }
    override fun getItemCount(): Int = listData.size
    override fun onBindViewHolder(holder: AfiliasiHolder, position: Int) {
        holder.bindAfiliasi(listData[position])
    }
}
