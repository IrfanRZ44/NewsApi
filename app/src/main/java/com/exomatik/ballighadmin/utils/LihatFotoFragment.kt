package com.exomatik.ballighadmin.utils

import com.exomatik.ballighadmin.R
import com.exomatik.ballighadmin.base.BaseFragment
//import com.exomatik.ballighadmin.databinding7.FragmentLihatFotoBinding

class LihatFotoFragment : BaseFragment() {
    override fun getLayoutResource(): Int = R.layout.fragment_lihat_foto

    override fun myCodeHere() {
    }

}
//class LihatFotoFragment : BaseFragmentBind<FragmentLihatFotoBinding>() {
//    override fun getLayoutResource(): Int = R.layout.fragment_lihat_foto
//
//    override fun myCodeHere() {
//        init()
//        onCLick()
//    }
//
//    private fun init(){
//        bind.lifecycleOwner = this
//        val urlFoto= this.arguments?.getString("urlFoto")
//
////        bind.imgFoto.load(urlFoto) {
////            crossfade(true)
////            placeholder(R.drawable.ic_camera_white)
////            error(R.drawable.ic_camera_white)
////            fallback(R.drawable.ic_camera_white)
////            memoryCachePolicy(CachePolicy.ENABLED)
////        }
//    }
//
//    private fun onCLick() {
//    }
//}