package id.telkomsel.merchant.ui.merchant.listProduk

import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import id.telkomsel.merchant.R
import kotlinx.android.synthetic.main.item_kategori_header.*

class AdapterHeaderKategori(private val title: String) : Item(), ExpandableItem {
    private lateinit var expandableGroup: ExpandableGroup

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.textFilter.text = title
    }

    override fun getLayout() = R.layout.item_kategori_header

    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
        expandableGroup = onToggleListener
    }
}