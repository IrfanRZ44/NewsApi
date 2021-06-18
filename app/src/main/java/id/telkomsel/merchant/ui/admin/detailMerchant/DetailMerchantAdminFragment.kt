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
            bind.btnConfirm.visibility = View.VISIBLE
            bind.btnTolak.visibility = View.VISIBLE
        }
        else{
            bind.btnConfirm.visibility = View.GONE
            bind.btnTolak.visibility = View.GONE
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
}