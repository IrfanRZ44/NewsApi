package id.telkomsel.merchant.ui.admin.detailMerchant

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseFragmentBind
import id.telkomsel.merchant.databinding.FragmentDetailMerchantAdminBinding
import id.telkomsel.merchant.utils.Constant

class DetailMerchantAdminFragment : BaseFragmentBind<FragmentDetailMerchantAdminBinding>(), OnMapReadyCallback {
    override fun getLayoutResource(): Int = R.layout.fragment_detail_merchant_admin
    lateinit var viewModel: DetailMerchantAdminViewModel
    private val mapBundelKey = "MapViewBundleKey"
    private var gmap: GoogleMap? = null
    private var marker: Marker? = null

    override fun myCodeHere() {
        supportActionBar?.title = "Detail Merchant"
        supportActionBar?.show()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        init()
        setMap(savedInstanceState)

        if (savedData.getDataMerchant()?.level == Constant.levelCSO){
            bind.rfaLayout.visibility = View.VISIBLE
            floatingAction()
        }
        else{
            bind.rfaLayout.visibility = View.GONE
        }
    }

    private fun init() {
        bind.lifecycleOwner = this
        viewModel = DetailMerchantAdminViewModel(context, findNavController())
        bind.viewModel = viewModel
        try {
            viewModel.dataMerchant.value = this.arguments?.getParcelable(Constant.reffMerchant)?:throw Exception("Error, terjadi kesalahan database")
        }catch (e: Exception){
            viewModel.message.value = e.message
        }

        if (viewModel.dataMerchant.value?.status_merchant == Constant.statusRequest){
            bind.rfaLayout.visibility = View.VISIBLE
        }
        else{
            bind.rfaLayout.visibility = View.GONE
        }
    }

    private fun setMap(savedInstanceState: Bundle?) {
        viewModel.isShowLoading.value = true
        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(mapBundelKey)
        }
        bind.mapView.onCreate(mapViewBundle)
        bind.mapView.getMapAsync(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        var mapViewBundle = outState.getBundle(mapBundelKey)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(mapBundelKey, mapViewBundle)
        }
        bind.mapView.onSaveInstanceState(mapViewBundle)
    }

    override fun onResume() {
        super.onResume()
        bind.mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        bind.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        bind.mapView.onStop()
    }

    override fun onPause() {
        bind.mapView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        bind.mapView.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        bind.mapView.onLowMemory()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gmap = googleMap
        gmap?.setMinZoomPreference(15f)
        viewModel.isShowLoading.value = false

        val latitude = viewModel.dataMerchant.value?.latitude
        val longitude = viewModel.dataMerchant.value?.longitude

        if (!latitude.isNullOrEmpty() && !longitude.isNullOrEmpty()){
            val lat = latitude.toDouble()
            val longit = longitude.toDouble()
            val myLocation = LatLng(lat, longit)
            gmap?.moveCamera(CameraUpdateFactory.newLatLng(myLocation))
            gmap?.setMinZoomPreference(15f)
            val place = MarkerOptions().position(myLocation).title("Lokasi Merchant")
            marker = gmap?.addMarker(place)
        }
        else{
            viewModel.message.value = "Error, gagal mengambil lokasi merchant"
        }

        gmap?.setOnMapClickListener {
            viewModel.onClickMaps()
        }
    }

    private fun floatingAction() {
        val rfaContent = RapidFloatingActionContentLabelList(context)
        val item = listOf(
            RFACLabelItem<Int>()
                .setLabel("Terima")
                .setResId(R.drawable.ic_true_white)
                .setIconNormalColor(0xff52af44.toInt())
                .setIconPressedColor(0xff3E8534.toInt())
                .setWrapper(0),
            RFACLabelItem<Int>()
                .setLabel("Tolak")
                .setResId(R.drawable.ic_close_white)
                .setIconNormalColor(0xffd32f2f.toInt())
                .setIconPressedColor(0xffB00020.toInt())
                .setWrapper(0)

        )

        rfaContent.setItems(item).setIconShadowColor(0xff888888.toInt())

        val rfabHelper = RapidFloatingActionHelper(
            context,
            bind.rfaLayout,
            bind.rfaBtn,
            rfaContent
        ).build()

        rfaContent.setOnRapidFloatingActionContentLabelListListener(object :
            RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener<Any> {
            override fun onRFACItemLabelClick(position: Int, item: RFACLabelItem<Any>?) {
                when(position) {
                    0 -> {
                        viewModel.onClickAccept()
                    }
                    1 -> {
                        viewModel.onClickDecline()
                    }
                }
                rfabHelper.toggleContent()
            }

            override fun onRFACItemIconClick(position: Int, item: RFACLabelItem<Any>?) {
                when(position) {
                    0 -> {
                        viewModel.onClickAccept()
                    }
                    1 -> {
                        viewModel.onClickDecline()
                    }
                }
                rfabHelper.toggleContent()
            }
        })
    }
}