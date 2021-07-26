package id.telkomsel.merchant.ui.pelanggan.account.editProfil

import android.app.Activity
import android.content.Intent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseFragmentBind
import androidx.navigation.fragment.findNavController
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import id.telkomsel.merchant.databinding.FragmentEditProfilPelangganBinding
import id.telkomsel.merchant.utils.Constant
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class EditProfilPelangganFragment : BaseFragmentBind<FragmentEditProfilPelangganBinding>(){
    override fun getLayoutResource(): Int = R.layout.fragment_edit_profil_pelanggan
    lateinit var viewModel: EditProfilPelangganViewModel

    override fun myCodeHere() {
        supportActionBar?.show()
        supportActionBar?.title = "Daftar Pelanggan"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        init()
        onClick()
    }

    private fun init() {
        bind.lifecycleOwner = this
        bind.etTglLahir.editText?.keyListener = null

        viewModel = EditProfilPelangganViewModel(activity, findNavController(),
            bind.etNama, bind.etAlamat, bind.etNoHp, bind.etNoWa, bind.etTglLahir, savedData)
        bind.viewModel = viewModel

        viewModel.dataPelanggan = savedData.getDataPelanggan()
        if (viewModel.dataPelanggan != null){
            viewModel.setDataPelanggan()
        }
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
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            val imagePath = result.uri.path
            val users = savedData.getDataPelanggan()?.username

            if (resultCode == Activity.RESULT_OK && !imagePath.isNullOrEmpty() && !users.isNullOrEmpty()){
                val imageUri = result.uri

                viewModel.etFotoProfil.value = imageUri
                val fileProduk = File(imagePath)
                val urlFoto = MultipartBody.Part.createFormData("url_foto", fileProduk.name, RequestBody.create(
                    MediaType.get("image/*"), fileProduk))
                val username = RequestBody.create(MediaType.get("text/plain"), users)
                viewModel.updateFotoPelanggan(username, urlFoto)
            }
        }
    }
}