package id.telkomsel.merchant.ui.pelanggan.registerPelanggan

import android.app.Activity
import android.content.Intent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseFragmentBind
import androidx.navigation.fragment.findNavController
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import id.telkomsel.merchant.databinding.FragmentRegisterPelangganBinding
import id.telkomsel.merchant.utils.Constant

class RegisterPelangganFragment : BaseFragmentBind<FragmentRegisterPelangganBinding>(){
    override fun getLayoutResource(): Int = R.layout.fragment_register_pelanggan
    lateinit var viewModel: RegisterPelangganViewModel

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

        viewModel = RegisterPelangganViewModel(activity, findNavController(),
            bind.etNama, bind.etAlamat, bind.etNoHp, bind.etNoWa,
            bind.etUsername, bind.etPassword, bind.etConfirmPassword, bind.etTglLahir)
        bind.viewModel = viewModel

        viewModel.dataPelanggan = this.arguments?.getParcelable(Constant.reffPelanggan)
        if (viewModel.dataPelanggan != null){
            viewModel.setDataMerchant(
                this.arguments?.getParcelable(Constant.dataModelFotoProfil)
            )
        }
    }

    private fun onClick(){
        bind.etConfirmPassword.editText?.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.onClickRegisterPelanggan()
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

            if (resultCode == Activity.RESULT_OK){
                val imageUri = result.uri

                viewModel.etFotoProfil.value = imageUri
            }
        }
    }
}