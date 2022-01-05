package id.telkomsel.merchandise.utils

import coil.load
import coil.request.CachePolicy
import id.telkomsel.merchandise.R
import id.telkomsel.merchandise.base.BaseFragmentBind
import id.telkomsel.merchandise.databinding.FragmentLihatFotoBinding

class LihatFotoFragment : BaseFragmentBind<FragmentLihatFotoBinding>() {
    override fun getLayoutResource(): Int = R.layout.fragment_lihat_foto

    override fun myCodeHere() {
        supportActionBar?.show()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        init()
        onCLick()
    }

    private fun init(){
        bind.lifecycleOwner = this
        val urlFoto= this.arguments?.getString("urlFoto")

        bind.imgFoto.load(urlFoto) {
            crossfade(true)
            placeholder(R.drawable.ic_camera_white)
            error(R.drawable.ic_camera_white)
            fallback(R.drawable.ic_camera_white)
            memoryCachePolicy(CachePolicy.ENABLED)
        }
    }

    private fun onCLick() {
    }
}