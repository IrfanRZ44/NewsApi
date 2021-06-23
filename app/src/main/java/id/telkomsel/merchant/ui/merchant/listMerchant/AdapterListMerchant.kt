package id.telkomsel.merchant.ui.merchant.listMerchant

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import id.telkomsel.merchant.R
import id.telkomsel.merchant.model.ModelMerchant
import id.telkomsel.merchant.utils.adapter.onClickFoto
import kotlinx.android.synthetic.main.item_daftar_merchant.view.*

class AdapterListMerchant (private val listKelas : ArrayList<ModelMerchant>,
                           private val navController: NavController,
                           private val isShowConfirm: Boolean,
                           private val getDataKategori : (Int, AppCompatTextView) -> Unit,
                           private val onClick : (ModelMerchant) -> Unit
) : RecyclerView.Adapter<AdapterListMerchant.AfiliasiHolder>(){

    inner class AfiliasiHolder(private val viewItem : View) : RecyclerView.ViewHolder(viewItem){
        @SuppressLint("SetTextI18n")
        fun bindAfiliasi(item: ModelMerchant){
            viewItem.textNama.text = item.nama_merchant
            viewItem.textWilayah.text = "${item.kelurahan}, ${item.kecamatan}, ${item.kabupaten}"
            viewItem.textAlamat.text = item.alamat_merchant
            getDataKategori(item.kategori_id, viewItem.textKategori)

            viewItem.btnKonfirmasi.setOnClickListener {
                onClick(item)
            }

            if (isShowConfirm){
                viewItem.textValidasi.visibility = View.VISIBLE
            }
            else{
                viewItem.textValidasi.visibility = View.GONE
            }

            viewItem.imgFoto.load(item.foto_profil) {
                crossfade(true)
                transformations(CircleCropTransformation())
                placeholder(R.drawable.ic_image_gray)
                error(R.drawable.ic_image_gray)
                fallback(R.drawable.ic_image_gray)
                memoryCachePolicy(CachePolicy.ENABLED)
            }

            viewItem.imgFoto.setOnClickListener {
                onClickFoto(item.foto_profil, navController)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AfiliasiHolder {
        return AfiliasiHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_daftar_merchant, parent, false))
    }
    override fun getItemCount(): Int = listKelas.size
    override fun onBindViewHolder(holder: AfiliasiHolder, position: Int) {
        holder.bindAfiliasi(listKelas[position])
    }
}
