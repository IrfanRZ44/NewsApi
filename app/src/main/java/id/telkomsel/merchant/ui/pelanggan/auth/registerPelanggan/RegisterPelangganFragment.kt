package id.telkomsel.merchant.ui.pelanggan.auth.registerPelanggan

import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.TextView
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseFragmentBind
import androidx.navigation.fragment.findNavController
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import id.telkomsel.merchant.databinding.FragmentRegisterPelangganBinding
import id.telkomsel.merchant.utils.Constant
import id.telkomsel.merchant.utils.adapter.dismissKeyboard

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
            bind.spinnerProvinsi, bind.spinnerKabupaten, bind.spinnerKecamatan, bind.spinnerKelurahan,
            bind.etNama, bind.etNomorMkios, bind.etAlamat, bind.etNoHp, bind.etNoWa,
            bind.etUsername, bind.etPassword, bind.etConfirmPassword, bind.etTglLahir,
            savedData, bind.cekKebijakan)
        bind.viewModel = viewModel

        viewModel.setAdapterProvinsi()
        viewModel.setAdapterKabupaten()
        viewModel.setAdapterKecamatan()
        viewModel.setAdapterKelurahan()

        viewModel.dataPelanggan = this.arguments?.getParcelable(Constant.reffPelanggan)
        if (viewModel.dataPelanggan != null){
            viewModel.setDataPelanggan(
                this.arguments?.getParcelable(Constant.dataModelFotoProfil)
            )
        }

        viewModel.getDaftarProvinsi()
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

                viewModel.etFotoProfil.value = imageUri
            }
        }
    }
}