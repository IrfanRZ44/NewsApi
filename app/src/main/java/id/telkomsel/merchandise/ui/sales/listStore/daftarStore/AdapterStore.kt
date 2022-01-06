package id.telkomsel.merchandise.ui.sales.listStore.daftarStore

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import id.telkomsel.merchandise.R
import id.telkomsel.merchandise.model.ModelStore
import id.telkomsel.merchandise.utils.Constant
import kotlinx.android.synthetic.main.item_store.view.*

class AdapterStore(private val listItem: ArrayList<ModelStore>,
                    private val onClickItem : (ModelStore) -> Unit
) : RecyclerView.Adapter<AdapterStore.AfiliasiHolder>(){
    inner class AfiliasiHolder(private val viewItem : View) : RecyclerView.ViewHolder(viewItem){
        @SuppressLint("SetTextI18n")
        fun bindAfiliasi(item: ModelStore) {
            viewItem.textKategori.text = item.jenis_store
            viewItem.textKode.text = item.kode_toko
            viewItem.textAlamat.text = item.alamat
            viewItem.textWilayah.text = "${item.kelurahan}, ${item.kecamatan}, ${item.kabupaten}"

            viewItem.imgFoto.load("${Constant.reffURL}${item.foto_depan_atas}") {
                crossfade(true)
                placeholder(R.drawable.ic_camera_white)
                transformations(CircleCropTransformation())
                error(R.drawable.ic_camera_white)
                fallback(R.drawable.ic_camera_white)
                memoryCachePolicy(CachePolicy.ENABLED)
            }

            viewItem.btnKonfirmasi.setOnClickListener {
                onClickItem(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AfiliasiHolder {
        return AfiliasiHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_store, parent, false))
    }

    override fun getItemCount(): Int = listItem.size
    override fun onBindViewHolder(holder: AfiliasiHolder, position: Int) {
        holder.bindAfiliasi(listItem[position])
    }
}
