package id.telkomsel.merchandise.ui.sales.addStore

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import coil.load
import coil.request.CachePolicy
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import id.telkomsel.merchandise.R
import id.telkomsel.merchandise.base.BaseFragmentBind
import id.telkomsel.merchandise.databinding.FragmentAddStoreBinding
import id.telkomsel.merchandise.utils.Constant
import id.telkomsel.merchandise.utils.adapter.dismissKeyboard
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File

class AddStoreFragment : BaseFragmentBind<FragmentAddStoreBinding>(){
    override fun getLayoutResource(): Int = R.layout.fragment_add_store
    lateinit var viewModel: AddStoreViewModel
    private var statusPickFoto = 0
    private val mapBundelKey = "MapViewBundleKey"
    private var gmap: GoogleMap? = null
    private var marker : Marker? = null
    private lateinit var place : MarkerOptions
    private lateinit var btmSheet : BottomSheetDialog
    private lateinit var mapView : MapView

    override fun myCodeHere() {
        supportActionBar?.title = "Tambah Store"
        supportActionBar?.show()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        init()
        onClick()
    }

    private fun init() {
        bind.lifecycleOwner = this

        viewModel = AddStoreViewModel(
            activity,
            findNavController(),
            bind.spinnerJenis,
            bind.spinnerProvinsi,
            bind.spinnerKabupaten,
            bind.spinnerKecamatan,
            bind.spinnerKelurahan,
            bind.etKodeToko,
            bind.etAlamat,
            bind.etTitikLokasi,
            bind.etNamaPic,
            bind.etNoHpPic,
            bind.etNoWaPic,
            bind.etNamaKasir1,
            bind.etNoHpKasir1,
            bind.etNoWaKasir1,
            bind.etNamaKasir2,
            bind.etNoHpKasir2,
            bind.etNoWaKasir2,
            bind.etNamaKasir3,
            bind.etNoHpKasir3,
            bind.etNoWaKasir3,
            bind.etNamaKasir4,
            bind.etNoHpKasir4,
            bind.etNoWaKasir4,
        )
        bind.viewModel = viewModel

        initPickMap(bind.root, savedInstanceState)
        viewModel.setAdapterJenis()
        viewModel.setAdapterProvinsi()
        viewModel.setAdapterKabupaten()
        viewModel.setAdapterKecamatan()
        viewModel.setAdapterKelurahan()
        viewModel.getDaftarProvinsi()
        getCurrentLocation()
    }

    private fun onClick(){
        bind.spinnerJenis.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                activity?.let { dismissKeyboard(it) }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        bind.spinnerProvinsi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                activity?.let { dismissKeyboard(it) }
                viewModel.listKabupaten.clear()
                viewModel.adapterKabupaten.notifyDataSetChanged()
                viewModel.getDaftarKabupaten(viewModel.listProvinsi[position].id)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        bind.spinnerKabupaten.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                activity?.let { dismissKeyboard(it) }
                viewModel.listKecamatan.clear()
                viewModel.adapterKecamatan.notifyDataSetChanged()
                viewModel.getDaftarKecamatan(viewModel.listKabupaten[position].id)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        bind.spinnerKecamatan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                activity?.let { dismissKeyboard(it) }
                viewModel.listKelurahan.clear()
                viewModel.adapterKelurahan.notifyDataSetChanged()
                viewModel.getDaftarKelurahan(viewModel.listKecamatan[position].id)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        bind.spinnerKelurahan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                activity?.let { dismissKeyboard(it) }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        bind.cardFotoDepanAtas.setOnClickListener {
            statusPickFoto = 1
            context?.let {
                CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAllowFlipping(true)
                    .setAllowRotation(true)
                    .setAspectRatio(1, 1)
                    .start(it, this)
            }
        }

        bind.cardFotoDepanBawah.setOnClickListener {
            statusPickFoto = 2
            context?.let {
                CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAllowFlipping(true)
                    .setAllowRotation(true)
                    .setAspectRatio(1, 1)
                    .start(it, this)
            }
        }

        bind.cardFotoEtalasePerdana.setOnClickListener {
            statusPickFoto = 3
            context?.let {
                CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAllowFlipping(true)
                    .setAllowRotation(true)
                    .setAspectRatio(1, 1)
                    .start(it, this)
            }
        }

        bind.cardFotoMejaPembayaran.setOnClickListener {
            statusPickFoto = 4
            context?.let {
                CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAllowFlipping(true)
                    .setAllowRotation(true)
                    .setAspectRatio(1, 1)
                    .start(it, this)
            }
        }

        bind.cardFotoBelakangKasir.setOnClickListener {
            statusPickFoto = 5
            context?.let {
                CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAllowFlipping(true)
                    .setAllowRotation(true)
                    .setAspectRatio(1, 1)
                    .start(it, this)
            }
        }

        bind.btnLocation.setOnClickListener {
            btmSheet.show()
            mapView.getMapAsync{
                if(marker != null) marker?.remove()
                gmap = it
                when {
                    !viewModel.etLatLng.value.isNullOrEmpty() -> {
                        setMarker(viewModel.etLatLng.value)
                    }
                    else -> {
                        val myLocation = LatLng(Constant.defaultLatitude, Constant.defaultLongitude)
                        gmap?.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15f))

                        checkPermission()
                    }
                }

                gmap?.setOnMapClickListener { latLng ->
                    val locationPoint = latLng.latitude.toString() + " : " + latLng.longitude.toString()

                    viewModel.etLatLng.value = locationPoint
                    viewModel.latitude.value = latLng.latitude.toString()
                    viewModel.longitude.value = latLng.longitude.toString()
                    if(marker != null) marker?.remove()
                    place = MarkerOptions().position(latLng).title("Titik Lokasi")
                    try {
                        marker = gmap?.addMarker(place)?:throw Exception("Gagal mendapatkan titik lokasi")
                    }catch (e: Exception){
                        viewModel.message.value = e.message
                    }
                }

                val pick = btmSheet.findViewById<FloatingActionButton>(R.id.btnMap)
                pick?.setOnClickListener {
                    if (marker != null) {
                        btmSheet.dismiss()
                        viewModel.message.value = "Berhasil memilih lokasi"
                    } else Toast.makeText(view?.context, "Afwan, Anda belum memilih lokasi", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && data != null) {
            val result = CropImage.getActivityResult(data)
            val act = activity

            if (resultCode == Activity.RESULT_OK && act != null){
                val imageUri = result.uri

                imageUri?.path?.let { it1 ->
                    compressImage(act,
                        it1, statusPickFoto
                    )
                }
            }
        }
    }

    private fun compressImage(act: Activity, path: String, statusFoto: Int){
        val job = Job()
        val uiScope = CoroutineScope(Dispatchers.IO + job)
        uiScope.launch {
            val compressedImageFile = Compressor.compress(act, File(path)) {
                resolution(256, 256)
                quality(70)
                format(Bitmap.CompressFormat.JPEG)
                size(124_000) // 124 KB
            }
            val resultUri = Uri.fromFile(compressedImageFile)

            act.runOnUiThread {
                resultUri?.let { it ->
                    val tempPath = it.path

                    if(!tempPath.isNullOrEmpty()){
                        when (statusFoto) {
                            1 -> {
                                viewModel.etFotoDepanAtas.value = Uri.parse(tempPath)
                                setPhoto(it, bind.imgFotoDepanAtas)
                            }
                            2 -> {
                                viewModel.etFotoDepanBawah.value = Uri.parse(tempPath)
                                setPhoto(it, bind.imgFotoDepanBawah)
                            }
                            3 -> {
                                viewModel.etFotoEtalasePerdana.value = Uri.parse(tempPath)
                                setPhoto(it, bind.imgFotoEtalasePerdana)
                            }
                            4 -> {
                                viewModel.etFotoMejaPembayaran.value = Uri.parse(tempPath)
                                setPhoto(it, bind.imgFotoMejaPembayaran)
                            }
                            5 -> {
                                viewModel.etFotoBelakangKasir.value = Uri.parse(tempPath)
                                setPhoto(it, bind.imgFotoBelakangKasir)
                            }
                        }
                    }
                    else{
                        when (statusFoto) {
                            1 -> {
                                viewModel.etFotoDepanAtas.value = Uri.parse(path)
                                setPhoto(Uri.parse(path), bind.imgFotoDepanAtas)
                            }
                            2 -> {
                                viewModel.etFotoDepanBawah.value = Uri.parse(path)
                                setPhoto(Uri.parse(path), bind.imgFotoDepanBawah)
                            }
                            3 -> {
                                viewModel.etFotoEtalasePerdana.value = Uri.parse(path)
                                setPhoto(Uri.parse(path), bind.imgFotoEtalasePerdana)
                            }
                            4 -> {
                                viewModel.etFotoMejaPembayaran.value = Uri.parse(path)
                                setPhoto(Uri.parse(path), bind.imgFotoMejaPembayaran)
                            }
                            5 -> {
                                viewModel.etFotoBelakangKasir.value = Uri.parse(path)
                                setPhoto(Uri.parse(path), bind.imgFotoBelakangKasir)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setPhoto(uri: Uri, imageView: AppCompatImageView){
//        when (statusPickFoto) {
//            1 -> {
//                viewModel.etFotoDepanAtas.value = Uri.parse(path)
//            }
//            2 -> {
//                viewModel.etFotoDepanBawah.value = Uri.parse(path)
//            }
//            3 -> {
//                viewModel.etFotoEtalasePerdana.value = Uri.parse(path)
//            }
//            4 -> {
//                viewModel.etFotoMejaPembayaran.value = Uri.parse(path)
//            }
//            5 -> {
//                viewModel.etFotoBelakangKasir.value = Uri.parse(path)
//            }
//        }
        imageView.load(uri) {
            crossfade(true)
            placeholder(R.drawable.ic_camera_white)
            error(R.drawable.ic_camera_white)
            fallback(R.drawable.ic_camera_white)
            memoryCachePolicy(CachePolicy.ENABLED)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        var mapViewBundle = outState.getBundle(mapBundelKey)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(mapBundelKey, mapViewBundle)
        }
        mapView.onSaveInstanceState(mapViewBundle)
    }

    override fun onResume() {
        super.onResume()
        if (this::mapView.isInitialized) mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        if (this::mapView.isInitialized) mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        if (this::mapView.isInitialized) mapView.onStop()
    }

    override fun onPause() {
        if (this::mapView.isInitialized) mapView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        if (this::mapView.isInitialized) mapView.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        if (this::mapView.isInitialized) mapView.onLowMemory()
    }

    private fun getCurrentLocation() {
        context?.let {
            if (ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    it, Manifest.permission.ACCESS_COARSE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                val fusedLocationClient = LocationServices.getFusedLocationProviderClient(it)

                fusedLocationClient?.lastLocation?.addOnSuccessListener { location: Location? ->
                    viewModel.latitude.value = location?.latitude.toString()
                    viewModel.longitude.value = location?.longitude.toString()
                }

            }
            else {
                viewModel.message.value = "Anda belum mengizinkan akses lokasi aplikasi ini"

                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    Constant.codeRequestLocation
                )
            }
        }
    }

    private fun checkPermission() {
        context?.let {
            if (ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    it, Manifest.permission.ACCESS_COARSE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                val fusedLocationClient = LocationServices.getFusedLocationProviderClient(it)

                fusedLocationClient?.lastLocation?.addOnSuccessListener { location: Location? ->
                    val myLocation = LatLng(location?.latitude?:Constant.defaultLatitude, location?.longitude?:Constant.defaultLongitude)
                    gmap?.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15f))

                    val place = MarkerOptions().position(myLocation).title("Lokasi Anda")
                    val locationPoint = myLocation.latitude.toString() + " : " + myLocation.longitude.toString()

                    viewModel.etLatLng.value = locationPoint
                    viewModel.latitude.value = location?.latitude.toString()
                    viewModel.longitude.value = location?.longitude.toString()

                    if (marker != null) marker?.remove()
                    marker = gmap?.addMarker(place)
                }

            }
            else {
                viewModel.message.value = "Anda belum mengizinkan akses lokasi aplikasi ini"

                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    Constant.codeRequestLocation
                )
            }
        }
    }

    @SuppressLint("InflateParams")
    private fun initPickMap(root: View, bundle: Bundle?) {
        btmSheet = BottomSheetDialog(root.context)
        val bottomView = layoutInflater.inflate(R.layout.behavior_pick_maps,null)

        var mapViewBundle: Bundle? = null
        if (bundle != null) {
            mapViewBundle = bundle.getBundle(mapBundelKey)
        }

        btmSheet.setContentView(bottomView)
        btmSheet.setCanceledOnTouchOutside(false)
        btmSheet.setCancelable(false)
        btmSheet.behavior.isDraggable = false
        btmSheet.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        mapView = bottomView.findViewById(R.id.mapViewPick)
        mapView.onCreate(mapViewBundle)
    }

    private fun setMarker(titikLokasi : String?){
        val locationPoint = titikLokasi?:"${Constant.defaultLatitude} : ${Constant.defaultLongitude}"

        val data = locationPoint.split(" : ")
        val latitude = data[0].toDouble()
        val longitude = data[1].toDouble()
        val myLocation = LatLng(latitude, longitude)
        gmap?.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15f))
        val place = MarkerOptions().position(myLocation).title("Titik Lokasi")

        marker = gmap?.addMarker(place)
        viewModel.message.value = "Berhasil memilih lokasi"
    }
}