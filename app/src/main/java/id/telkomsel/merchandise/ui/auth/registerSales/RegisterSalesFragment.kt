package id.telkomsel.merchandise.ui.auth.registerSales

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
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
import id.telkomsel.merchandise.databinding.FragmentRegisterSalesBinding
import id.telkomsel.merchandise.utils.Constant
import id.telkomsel.merchandise.utils.adapter.dismissKeyboard

class RegisterSalesFragment : BaseFragmentBind<FragmentRegisterSalesBinding>(){
    override fun getLayoutResource(): Int = R.layout.fragment_register_sales
    lateinit var viewModel: RegisterSalesViewModel
    private val mapBundelKey = "MapViewBundleKey"
    private var gmap: GoogleMap? = null
    private var marker : Marker? = null
    private lateinit var place : MarkerOptions
    private lateinit var btmSheet : BottomSheetDialog
    private lateinit var mapView : MapView
    private var statusPickFoto = 0

    override fun myCodeHere() {
        supportActionBar?.hide()
        init(savedInstanceState)
        onClick()
    }

    private fun init(savedInstanceState: Bundle?){
        bind.lifecycleOwner = this
        bind.etTitikLokasi.editText?.keyListener = null
        bind.etTglLahir.editText?.keyListener = null

        viewModel = RegisterSalesViewModel(activity, findNavController(), bind.spinnerPernikahan,
            bind.spinnerJenisKelamin, bind.spinnerSIM,
            bind.spinnerProvinsi, bind.spinnerKabupaten, bind.spinnerKecamatan, bind.spinnerKelurahan,
            bind.spinnerMotor, bind.etNamaLengkap, bind.etNamaPanggilan, bind.etJumlahAnak, bind.etTempatLahir,
            bind.etTglLahir, bind.etAlamat, bind.etTitikLokasi, bind.etNoHpSales, bind.etNoWaSales,
            bind.etEmail, bind.etPengalamanKerja, bind.etNomorPolisi, bind.etUsername, bind.etPassword, bind.etConfirmPassword
            )

        bind.viewModel = viewModel
        initPickMap(bind.root, savedInstanceState)
        viewModel.setAdapterProvinsi()
        viewModel.setAdapterKabupaten()
        viewModel.setAdapterKecamatan()
        viewModel.setAdapterKelurahan()
        getCurrentLocation()

        viewModel.dataSales = this.arguments?.getParcelable(Constant.reffSales)
        if (viewModel.dataSales != null){
            viewModel.setDataSales(
                this.arguments?.getParcelable(Constant.dataModelFotoProfil),
                this.arguments?.getParcelable(Constant.dataModelFotoDepanKendaraan),
                this.arguments?.getParcelable(Constant.dataModelFotoBelakangKendaraan),
                this.arguments?.getParcelable(Constant.dataModelFotoWajah),
                this.arguments?.getParcelable(Constant.dataModelFotoSeluruhBadan)
            )
        }

        viewModel.setAdapterPernikahan()
        viewModel.setAdapterJenisKelamin()
        viewModel.setAdapterSIM()
        viewModel.setAdapterMotor()
        viewModel.getDaftarProvinsi()
    }

    private fun onClick(){
        bind.spinnerPernikahan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                activity?.let { dismissKeyboard(it) }
                if (position > 1){
                    bind.etJumlahAnak.visibility = View.VISIBLE
                }
                else{
                    bind.etJumlahAnak.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        bind.spinnerJenisKelamin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                activity?.let { dismissKeyboard(it) }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        bind.spinnerSIM.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

        bind.cardFotoProfil.setOnClickListener {
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

        bind.cardFotoDepanKendaraan.setOnClickListener {
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

        bind.cardFotoBelakangKendaraan.setOnClickListener {
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

        bind.cardFotoWajah.setOnClickListener {
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

        bind.cardFotoSeluruhBadan.setOnClickListener {
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
                    viewModel.currentLatitude.value = location?.latitude.toString()
                    viewModel.currentLongitude.value = location?.longitude.toString()
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

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)

            if (resultCode == Activity.RESULT_OK){
                val imageUri = result.uri

                when (statusPickFoto) {
                    1 -> viewModel.etFotoDepanKendaraan.value = imageUri
                    2 -> viewModel.etFotoBelakangKendaraan.value = imageUri
                    3 -> viewModel.etFotoWajah.value = imageUri
                    4 -> viewModel.etFotoSeluruhBadan.value = imageUri
                    5 -> viewModel.etFotoProfil.value = imageUri
                }
            }
        }
    }
}