package id.telkomsel.merchant.ui.pelanggan.detailProduk

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.viewpager.widget.PagerAdapter
import coil.load
import coil.request.CachePolicy
import id.telkomsel.merchant.R
import id.telkomsel.merchant.model.ModelFotoProduk
import id.telkomsel.merchant.listener.ListenerFotoProduk
import kotlinx.android.synthetic.main.item_foto_produk.view.*

class AdapterFotoProduk(private val ctx: Context,
                        private val data: ArrayList<ModelFotoProduk>,
                        private val listener: ListenerFotoProduk
) :
    PagerAdapter() {
    private lateinit var view : View
    private lateinit var layoutInflater: LayoutInflater
    override fun getCount(): Int {
        return 5
    }

    override fun isViewFromObject(
        view: View,
        `object`: Any
    ): Boolean {
        return view === `object` as RelativeLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        view = layoutInflater.inflate(R.layout.item_foto_produk, container, false)

        if (data[position].url_foto.isEmpty()){
            view.imgBoard.scaleType = ImageView.ScaleType.CENTER_INSIDE
            view.imgBoard.setImageResource(R.drawable.ic_camera_white)
        }
        else{
            view.imgBoard.scaleType = ImageView.ScaleType.FIT_XY
            view.imgBoard.load(data[position].url_foto) {
                crossfade(true)
                placeholder(R.drawable.ic_camera_white)
                error(R.drawable.ic_camera_white)
                fallback(R.drawable.ic_camera_white)
                memoryCachePolicy(CachePolicy.ENABLED)
            }
        }

        view.btnEdit.visibility = View.GONE
        view.btnUpload.visibility = View.GONE

        view.imgBoard.setOnClickListener {
            listener.clickFotoProduk(data[position])
        }

        container.addView(view)
        return view
    }

    override fun destroyItem(
        container: ViewGroup,
        position: Int,
        `object`: Any
    ) {
        container.removeView(`object` as RelativeLayout)
    }

}