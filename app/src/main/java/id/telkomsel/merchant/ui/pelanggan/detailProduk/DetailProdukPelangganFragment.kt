package id.telkomsel.merchant.ui.pelanggan.detailProduk

import androidx.navigation.fragment.findNavController
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseFragmentBind
import id.telkomsel.merchant.databinding.FragmentDetailProdukPelangganBinding
import id.telkomsel.merchant.model.ModelFotoProduk
import id.telkomsel.merchant.utils.Constant
import id.telkomsel.merchant.utils.adapter.onClickFoto
import id.telkomsel.merchant.listener.ListenerFotoProduk

class DetailProdukPelangganFragment : BaseFragmentBind<FragmentDetailProdukPelangganBinding>(),
    ListenerFotoProduk {
    override fun getLayoutResource(): Int = R.layout.fragment_detail_produk_pelanggan
    lateinit var viewModel: DetailProdukPelangganViewModel

    override fun myCodeHere() {
        supportActionBar?.title = "Detail Produk"
        supportActionBar?.show()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        init()
    }

    private fun init() {
        bind.lifecycleOwner = this
        viewModel = DetailProdukPelangganViewModel(
            context, findNavController(), bind.viewPager, bind.dotsIndicator,
            savedData, this
        )
        bind.viewModel = viewModel
        try {
            viewModel.dataProduk.value = this.arguments?.getParcelable(Constant.reffProduk)?:throw Exception("Error, terjadi kesalahan database")
            viewModel.setData()
            viewModel.getDataMerchant(viewModel.dataProduk.value?.merchant_id.toString())
            viewModel.initDialogJumlah(bind.root, layoutInflater)
            val produkId = viewModel.dataProduk.value?.id
            if (produkId != null){
                viewModel.getDaftarFotoProduk(produkId)
            }
        }catch (e: Exception){
            viewModel.message.value = e.message
        }
    }

    override fun clickFotoProduk(rows: ModelFotoProduk) {
        super.clickFotoProduk(rows)

        onClickFoto(rows.url_foto, findNavController())
    }
}