package id.telkomsel.merchant.ui.merchant.addProduk

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import id.telkomsel.merchant.R
import id.telkomsel.merchant.databinding.FragmentAddProdukBinding
import id.telkomsel.merchant.base.BaseFragmentBind
import id.telkomsel.merchant.utils.adapter.dismissKeyboard

class AddProdukFragment : BaseFragmentBind<FragmentAddProdukBinding>(){
    override fun getLayoutResource(): Int = R.layout.fragment_add_produk
    lateinit var viewModel: AddProdukViewModel
    private lateinit var btmSheet : BottomSheetDialog

    override fun myCodeHere() {
        supportActionBar?.title = "Tambah Merchant"
        supportActionBar?.show()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        init()
        onClick()
    }

    private fun init() {
        bind.lifecycleOwner = this
//        bind.etTglKadaluarsa.editText?.keyListener = null
//        bind.etNamaMerchant.editText?.keyListener = null

        viewModel = AddProdukViewModel(activity, context, findNavController(), bind.spinnerKategori,
            bind.etNamaMerchant, bind.etNamaProduk,
            bind.etTglKadaluarsa, bind.etStok, bind.etDesc, bind.etHarga
            )
        bind.viewModel = viewModel
        initPickMerchant(bind.root)
        viewModel.setAdapterKategori()

        viewModel.getDaftarKategori()
    }

    private fun onClick(){
        bind.spinnerKategori.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                activity?.let { dismissKeyboard(it) }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        bind.btnNamaMerchant.setOnClickListener {
            btmSheet.show()
        }

        bind.cardFotoProduk.setOnClickListener {
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

    @SuppressLint("InflateParams")
    private fun initPickMerchant(root: View) {
        btmSheet = BottomSheetDialog(root.context)
        val bottomView = layoutInflater.inflate(R.layout.behavior_pick_merchant, null)

        btmSheet.setContentView(bottomView)
        btmSheet.setCanceledOnTouchOutside(false)
        btmSheet.setCancelable(false)
        btmSheet.behavior.isDraggable = false
        btmSheet.behavior.state = BottomSheetBehavior.STATE_EXPANDED

        val textStatus = btmSheet.findViewById<AppCompatTextView>(R.id.textStatus)
        val etSearch = btmSheet.findViewById<TextInputLayout>(R.id.etSearch)
        val rcRequest = btmSheet.findViewById<RecyclerView>(R.id.rcRequest)

        etSearch?.editText?.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                return@OnEditorActionListener false
            }
            false
        })
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)

            if (resultCode == Activity.RESULT_OK){
                val imageUri = result.uri

                viewModel.etFotoProduk.value = imageUri
            }
        }
    }
}