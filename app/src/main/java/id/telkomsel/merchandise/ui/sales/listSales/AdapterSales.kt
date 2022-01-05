package id.telkomsel.merchandise.ui.sales.listSales

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import id.telkomsel.merchandise.R
import id.telkomsel.merchandise.model.ModelSales
import id.telkomsel.merchandise.utils.Constant
import id.telkomsel.merchandise.utils.adapter.onClickFoto
import kotlinx.android.synthetic.main.item_sales.view.*

class AdapterSales(
    private val listKelas: ArrayList<ModelSales>,
    private val navController: NavController,
    private val isShowConfirm: Boolean,
    private val onClick: (ModelSales) -> Unit
) : RecyclerView.Adapter<AdapterSales.AfiliasiHolder>(){

    inner class AfiliasiHolder(private val viewItem : View) : RecyclerView.ViewHolder(viewItem){
        @SuppressLint("SetTextI18n")
        fun bindAfiliasi(item: ModelSales){
            viewItem.textNama.text = item.nama_lengkap
            viewItem.textWilayah.text = "${item.kelurahan}, ${item.kecamatan}, ${item.kabupaten}"
            viewItem.textAlamat.text = item.alamat

            viewItem.btnKonfirmasi.setOnClickListener {
                onClick(item)
            }

            if (isShowConfirm){
                viewItem.textValidasi.visibility = View.VISIBLE
            }
            else{
                viewItem.textValidasi.visibility = View.GONE
            }

            viewItem.imgFoto.load("${Constant.reffURL}${item.foto_profil}") {
                crossfade(true)
                transformations(CircleCropTransformation())
                placeholder(R.drawable.ic_image_gray)
                error(R.drawable.ic_image_gray)
                fallback(R.drawable.ic_image_gray)
                memoryCachePolicy(CachePolicy.ENABLED)
            }

            viewItem.imgFoto.setOnClickListener {
                onClickFoto(item.foto_wajah, navController)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AfiliasiHolder {
        return AfiliasiHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_sales, parent, false))
    }
    override fun getItemCount(): Int = listKelas.size
    override fun onBindViewHolder(holder: AfiliasiHolder, position: Int) {
        holder.bindAfiliasi(listKelas[position])
    }
}
