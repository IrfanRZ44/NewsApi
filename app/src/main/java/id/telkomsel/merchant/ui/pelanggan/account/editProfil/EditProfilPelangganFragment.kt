package id.telkomsel.merchant.ui.pelanggan.account.editProfil

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseFragmentBind
import id.telkomsel.merchant.databinding.FragmentEditProfilPelangganBinding
import id.telkomsel.merchant.utils.adapter.dismissKeyboard
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class EditProfilPelangganFragment : BaseFragmentBind<FragmentEditProfilPelangganBinding>(){
    override fun getLayoutResource(): Int = R.layout.fragment_edit_profil_pelanggan
    lateinit var viewModel: EditProfilPelangganViewModel

    override fun myCodeHere() {
        supportActionBar?.show()
        supportActionBar?.title = "Edit Profil"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        init()
        onClick()
    }

    private fun init() {
        bind.lifecycleOwner = this
        bind.etTglLahir.editText?.keyListener = null

        viewModel = EditProfilPelangganViewModel(activity, findNavController(),
            bind.spinnerProvinsi, bind.spinnerKabupaten, bind.spinnerKecamatan, bind.spinnerKelurahan,
            bind.etNama, bind.etAlamat, bind.etNoHp, bind.etNoWa, bind.etTglLahir, savedData)
        bind.viewModel = viewModel

        viewModel.setAdapterProvinsi()
        viewModel.setAdapterKabupaten()
        viewModel.setAdapterKecamatan()
        viewModel.setAdapterKelurahan()

        viewModel.dataPelanggan = savedData.getDataPelanggan()
        if (viewModel.dataPelanggan != null){
            viewModel.setDataPelanggan()
        }

        viewModel.getDaftarProvinsi()
    }

    private fun onClick(){
        bind.etNoWa.editText?.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.onClickEditProfil()
                return@OnEditorActionListener false
            }
            false
        })

        bind.cardFotoProfil.setOnClickListener {
            context?.let {
                CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAllowFlipping(true)
                    .setAllowRotation(true)
                    .setAspectRatio(1, 1)
                    .start(it, this)
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
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)

            if (resultCode == Activity.RESULT_OK){
                val imageUri = result.uri
                val users = savedData.getDataPelanggan()?.username
                val act = activity
                val realFoto = imageUri.path
                viewModel.etFotoProfil.value = imageUri

                if (!realFoto.isNullOrEmpty() && act != null && !users.isNullOrEmpty()){
                    compressImage(act, realFoto, users)
                }
                else{
                    viewModel.message.value = "Error, gagal mengupload foto"
                }
            }
        }
    }

    private fun compressImage(act: Activity, realFoto: String, users: String){
        val job = Job()
        val uiScope = CoroutineScope(Dispatchers.IO + job)
        uiScope.launch {
            val compressedImageFile = Compressor.compress(act, File(realFoto)) {
                resolution(256, 256)
                quality(70)
                format(Bitmap.CompressFormat.JPEG)
                size(124_000) // 124 KB
            }
            val resultUri = Uri.fromFile(compressedImageFile)

            act.runOnUiThread {
                resultUri?.let {
                    val tempPath = it.path

                    if(!tempPath.isNullOrEmpty()){
                        val fileProduk = File(tempPath)
                        val urlFoto = MultipartBody.Part.createFormData("url_foto", fileProduk.name, RequestBody.create(
                            MediaType.get("image/*"), fileProduk))
                        val username = RequestBody.create(MediaType.get("text/plain"), users)
                        viewModel.updateFotoPelanggan(username, urlFoto)
                    }
                    else{
                        val fileProduk = File(realFoto)
                        val urlFoto = MultipartBody.Part.createFormData("url_foto", fileProduk.name, RequestBody.create(
                            MediaType.get("image/*"), fileProduk))
                        val username = RequestBody.create(MediaType.get("text/plain"), users)
                        viewModel.updateFotoPelanggan(username, urlFoto)
                    }
                }
            }
        }
    }
}