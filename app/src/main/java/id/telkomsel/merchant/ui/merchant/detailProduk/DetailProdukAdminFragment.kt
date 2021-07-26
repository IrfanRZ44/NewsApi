package id.telkomsel.merchant.ui.merchant.detailProduk

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList
import id.telkomsel.merchant.R
import id.telkomsel.merchant.databinding.FragmentDetailProdukAdminBinding
import id.telkomsel.merchant.base.BaseFragmentBind
import id.telkomsel.merchant.model.ModelFotoProduk
import id.telkomsel.merchant.ui.merchant.editProduk.EditProdukFragment
import id.telkomsel.merchant.utils.Constant
import id.telkomsel.merchant.utils.adapter.onClickFoto
import id.telkomsel.merchant.utils.listener.ListenerFotoProduk
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class DetailProdukAdminFragment : BaseFragmentBind<FragmentDetailProdukAdminBinding>(),
    ListenerFotoProduk {
    override fun getLayoutResource(): Int = R.layout.fragment_detail_produk_admin
    lateinit var viewModel: DetailProdukAdminViewModel
    private var dataFotoProduk: ModelFotoProduk? = null

    override fun myCodeHere() {
        supportActionBar?.title = "Detail Produk"
        supportActionBar?.show()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        init()
        if (savedData.getDataMerchant()?.level == Constant.levelCSO && viewModel.dataProduk.value?.status == Constant.statusRequest){
            bind.rfaLayout.visibility = View.VISIBLE
            floatingAction()
        }
        else{
            bind.rfaLayout.visibility = View.GONE
        }
    }

    private fun init() {
        bind.lifecycleOwner = this
        viewModel = DetailProdukAdminViewModel(context, findNavController(), bind.viewPager,
            bind.dotsIndicator, savedData, this)
        bind.viewModel = viewModel
        try {
            viewModel.dataProduk.value = this.arguments?.getParcelable(Constant.reffProduk)?:throw Exception("Error, terjadi kesalahan database")
            viewModel.setData()
            viewModel.getDataMerchant(viewModel.dataProduk.value?.merchant_id.toString())
            val produkId = viewModel.dataProduk.value?.id
            if (produkId != null){
                viewModel.getDaftarFotoProduk(produkId)
            }
        }catch (e: Exception){
            viewModel.message.value = e.message
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

    override fun clickEditProduk() {
        super.clickEditProduk()

        val bundle = Bundle()
        val fragmentTujuan = EditProdukFragment()
        bundle.putParcelable(Constant.reffProduk, viewModel.dataProduk.value)
        bundle.putParcelable(Constant.reffMerchant, viewModel.dataMerchant.value)
        fragmentTujuan.arguments = bundle
        findNavController().navigate(R.id.editProdukFragment, bundle)
    }

    override fun clickUploadProduk(position: Int, rows: ModelFotoProduk) {
        super.clickUploadProduk(position, rows)

        dataFotoProduk = rows
        context?.let {
            CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAllowFlipping(true)
                .setAllowRotation(true)
                .setAspectRatio(2, 1)
                .start(it, this)
        }
    }

    override fun clickFotoProduk(rows: ModelFotoProduk) {
        super.clickFotoProduk(rows)

        onClickFoto(rows.url_foto, findNavController())
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            val imagePath = result.uri.path
            val lvl = savedData.getDataMerchant()?.level

            if (resultCode == Activity.RESULT_OK && !imagePath.isNullOrEmpty() && !lvl.isNullOrEmpty()){
                val fileProduk = File(imagePath)
                val urlFoto = MultipartBody.Part.createFormData("url_foto", fileProduk.name, RequestBody.create(
                    MediaType.get("image/*"), fileProduk))
                val produkId = RequestBody.create(MediaType.get("text/plain"), viewModel.dataProduk.value?.id.toString())
                val level = RequestBody.create(MediaType.get("text/plain"), lvl)

                val idFoto = dataFotoProduk?.id
                if (idFoto != 0){
                    val id = RequestBody.create(MediaType.get("text/plain"), idFoto.toString())
                    viewModel.updateFotoProduk(id, level, urlFoto)
                }
                else{
                    viewModel.createFotoProduk(produkId, level, urlFoto)
                }
                dataFotoProduk = null
            }
        }
    }
}