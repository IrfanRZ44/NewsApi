package id.telkomsel.merchandise.ui.sales.addStore

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import id.telkomsel.merchandise.R
import id.telkomsel.merchandise.model.ModelSales
import kotlinx.android.synthetic.main.item_sales.view.*

class AdapterPickSales (private val listData : ArrayList<ModelSales>,
                           private val onClick : (ModelSales) -> Unit
) : RecyclerView.Adapter<AdapterPickSales.AfiliasiHolder>(){

    inner class AfiliasiHolder(private val viewItem : View) : RecyclerView.ViewHolder(viewItem){
        @SuppressLint("SetTextI18n")
        fun bindAfiliasi(item: ModelSales){
            viewItem.textNama.text = item.nama_lengkap
            viewItem.textWilayah.text = "${item.kelurahan}, ${item.kecamatan}, ${item.kabupaten}"
            viewItem.textAlamat.text = item.alamat
            viewItem.textValidasi.visibility = View.GONE
            viewItem.btnKonfirmasi.text = "Pilih"

            viewItem.imgFoto.load(item.foto_wajah) {
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
        return AfiliasiHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_sales, parent, false))
    }
    override fun getItemCount(): Int = listData.size
    override fun onBindViewHolder(holder: AfiliasiHolder, position: Int) {
        holder.bindAfiliasi(listData[position])
    }
}
