package id.telkomsel.merchant.ui.merchant.listProduk.daftarProduk

import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import id.telkomsel.merchant.R
import id.telkomsel.merchant.model.ModelKategori
import kotlinx.android.synthetic.main.item_kategori.*

class AdapterAllKategori(private val item: ModelKategori,
                         private val onClickItem : (ModelKategori) -> Unit
                         ) : Item(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.textNama.text = item.nama

        viewHolder.cardView.setOnClickListener {
            onClickItem(item)
        }
    }



    override fun getLayout() = R.layout.item_kategori

    override fun getSpanSize(spanCount: Int, position: Int) = spanCount / 3
}