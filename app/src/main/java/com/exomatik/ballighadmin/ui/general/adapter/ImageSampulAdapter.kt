package com.exomatik.ballighadmin.ui.general.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.navigation.NavController
import androidx.viewpager.widget.PagerAdapter
import coil.api.load
import coil.request.CachePolicy
import com.exomatik.ballighadmin.R
import com.exomatik.ballighadmin.utils.onClickFoto
import kotlinx.android.synthetic.main.item_image_profile.view.*

class ImageSampulAdapter(private val ctx: Context,
                         private val data: ArrayList<String>,
                         private val navController: NavController) :
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
        return view === `object` as LinearLayoutCompat
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        view = layoutInflater.inflate(R.layout.item_image_profile, container, false)

        view.imgBoard.load(data[position]) {
            crossfade(true)
            placeholder(R.drawable.ic_camera_white_big)
            error(R.drawable.ic_camera_white_big)
            fallback(R.drawable.ic_camera_white_big)

            memoryCachePolicy(CachePolicy.ENABLED)
        }

        if (data[position].isNotEmpty()){
            view.imgBoard.setOnClickListener {
                onClickFoto(data[position], navController)
            }
        }

        container.addView(view)
        return view
    }

    override fun destroyItem(
        container: ViewGroup,
        position: Int,
        `object`: Any
    ) {
        container.removeView(`object` as LinearLayoutCompat)
    }

}