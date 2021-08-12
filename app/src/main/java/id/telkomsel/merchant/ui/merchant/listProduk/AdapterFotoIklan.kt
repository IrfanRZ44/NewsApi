package id.telkomsel.merchant.ui.merchant.listProduk

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
import id.telkomsel.merchant.listener.ListenerFotoIklan
import id.telkomsel.merchant.model.ModelFotoIklan
import kotlinx.android.synthetic.main.item_foto_iklan.view.*

class AdapterFotoIklan(private val ctx: Context,
                       private val data: ArrayList<ModelFotoIklan>,
                       private val listener: ListenerFotoIklan
) :
    PagerAdapter() {
    private lateinit var view : View
    private lateinit var layoutInflater: LayoutInflater
    override fun getCount(): Int {
        return data.size
    }

    override fun isViewFromObject(
        view: View,
        `object`: Any
    ): Boolean {
        return view === `object` as RelativeLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        view = layoutInflater.inflate(R.layout.item_foto_iklan, container, false)

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

        view.btnUpload.visibility = View.VISIBLE

        view.imgBoard.setOnClickListener {
            listener.clickFotoIklan(data[position])
        }

        view.btnUpload.setOnClickListener {
            listener.clickUploadIklan(position, data[position])
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