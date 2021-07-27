package id.telkomsel.merchant.ui.pelanggan.detailProduk

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.santalu.autoviewpager.AutoViewPager
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseViewModel
import id.telkomsel.merchant.model.ModelFotoProduk
import id.telkomsel.merchant.model.ModelMerchant
import id.telkomsel.merchant.model.ModelProduk
import id.telkomsel.merchant.model.response.ModelResponse
import id.telkomsel.merchant.model.response.ModelResponseDaftarFotoProduk
import id.telkomsel.merchant.model.response.ModelResponseMerchant
import id.telkomsel.merchant.utils.Constant
import id.telkomsel.merchant.utils.DataSave
import id.telkomsel.merchant.utils.RetrofitUtils
import id.telkomsel.merchant.utils.adapter.convertNumberWithoutRupiah
import id.telkomsel.merchant.utils.adapter.convertRupiah
import id.telkomsel.merchant.utils.adapter.onClickFoto
import id.telkomsel.merchant.utils.listener.ListenerFotoProduk
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("StaticFieldLeak")
class DetailProdukPelangganViewModel(
    private val context: Context?,
    private val navController: NavController,
    private val viewPager: AutoViewPager,
    private val dotsIndicator: DotsIndicator,
    private val savedData: DataSave,
    private val listener: ListenerFotoProduk
) : BaseViewModel() {
    val dataProduk = MutableLiveData<ModelProduk>()
    val dataMerchant = MutableLiveData<ModelMerchant>()
    val promoProduk = MutableLiveData<String>()
    val poinVoucher = MutableLiveData<String>()
    val poinProduk = MutableLiveData<String>()
    val viewProduk = MutableLiveData<String>()
    val stokProduk = MutableLiveData<String>()
    val alamatProduk = MutableLiveData<String>()
    val tanggalProduk = MutableLiveData<String>()
    val hargaProduk = MutableLiveData<String>()
    private val listGambar = ArrayList<ModelFotoProduk>()
    private var adapterFotoProduk: AdapterFotoProduk? = null

    fun setAdapterFoto(gambar: List<ModelFotoProduk>?) {
        val ctx = context

        listGambar.clear()
        listGambar.add(ModelFotoProduk(0, 0, ""))
        listGambar.add(ModelFotoProduk(0, 0, ""))
        listGambar.add(ModelFotoProduk(0, 0, ""))
        listGambar.add(ModelFotoProduk(0, 0, ""))
        listGambar.add(ModelFotoProduk(0, 0, ""))

        if (gambar != null){
            for (i in gambar.indices){
                if (i < 5){
                    listGambar[i] = gambar[i]
                }
            }
        }

        if (ctx != null){
            adapterFotoProduk = AdapterFotoProduk(
                ctx, listGambar, listener
            )
            viewPager.offscreenPageLimit = 0
            viewPager.adapter = adapterFotoProduk
            dotsIndicator.setViewPager(viewPager)
        }
    }

    fun clickFotoProfil(){
        dataMerchant.value?.foto_profil?.let { onClickFoto(it, navController) }
    }

    fun onClickTukarVoucher(){
        val dataPelanggan = savedData.getDataPelanggan()
        if (dataPelanggan?.username.isNullOrEmpty()){
            message.value = "Maaf, Anda harus login terlebih dahulu"
            val navOption = NavOptions.Builder().setPopUpTo(R.id.pelangganFragment, true).build()
            navController.navigate(R.id.pelangganFragment, null, navOption)
            savedData.setDataBoolean(true, Constant.login)
        }
        else{
            message.value = "Maaf, Anda tidak memiliki poin"
        }
    }

    fun onClickTukarProduk(){
        val dataPelanggan = savedData.getDataPelanggan()
        if (dataPelanggan?.username.isNullOrEmpty()){
            message.value = "Maaf, Anda harus login terlebih dahulu"
            val navOption = NavOptions.Builder().setPopUpTo(R.id.pelangganFragment, true).build()
            navController.navigate(R.id.pelangganFragment, null, navOption)
            savedData.setDataBoolean(true, Constant.login)
        }
        else{
            message.value = "Maaf, Anda tidak memiliki poin"
        }
    }

    fun setData(){
        val hargaVoucher = dataProduk.value?.jumlah_poin?:0
        val hrgProduk = dataProduk.value?.harga?:0
        promoProduk.value = "Promo ${dataProduk.value?.promo}"
        poinProduk.value = "Beli sekarang ${convertNumberWithoutRupiah(hrgProduk.toDouble())} Poin"
        poinVoucher.value = "Tukar ${convertNumberWithoutRupiah(hargaVoucher.toDouble())} Poin"
        viewProduk.value = "${dataProduk.value?.view} Views"
        stokProduk.value = "${dataProduk.value?.stok} Stok"
        tanggalProduk.value = "Masa berlaku hingga ${dataProduk.value?.tgl_kadaluarsa}"
        val harga = dataProduk.value?.harga?:0
        hargaProduk.value = convertRupiah(harga.toDouble())

        pelangganViewProduk(dataProduk.value?.id.toString())
    }

    fun getDataMerchant(merchantId: String){
        isShowLoading.value = true

        RetrofitUtils.getDataMerchantById(merchantId, object : Callback<ModelResponseMerchant> {
            override fun onResponse(
                call: Call<ModelResponseMerchant>,
                response: Response<ModelResponseMerchant>
            ) {
                isShowLoading.value = false
                val result = response.body()

                if (result?.message == Constant.reffSuccess){
                    dataMerchant.value = result.dataMerchant
                    alamatProduk.value = "${dataMerchant.value?.alamat_merchant}, " +
                            "${dataMerchant.value?.kelurahan}, " +
                            "${dataMerchant.value?.kecamatan}, " +
                            "${dataMerchant.value?.kabupaten}"
                }
                else{
                    message.value = "Error, gagal mendapatkan data merchant"
                }
            }

            override fun onFailure(
                call: Call<ModelResponseMerchant>,
                t: Throwable
            ) {
                isShowLoading.value = false
                message.value = "Error, gagal mendapatkan data merchant"
            }
        })
    }

    private fun pelangganViewProduk(produkId: String){
        RetrofitUtils.pelangganViewProduk(produkId,
            object : Callback<ModelResponse> {
                override fun onResponse(call: Call<ModelResponse>, response: Response<ModelResponse>) {}
                override fun onFailure(call: Call<ModelResponse>, t: Throwable) {}
            })
    }

    fun getDaftarFotoProduk(id: Int){
        isShowLoading.value = true

        RetrofitUtils.getDaftarFotoProduk(id,
            object : Callback<ModelResponseDaftarFotoProduk> {
                override fun onResponse(
                    call: Call<ModelResponseDaftarFotoProduk>,
                    response: Response<ModelResponseDaftarFotoProduk>
                ) {
                    isShowLoading.value = false
                    val result = response.body()

                    if (result?.message == Constant.reffSuccess){
                        setAdapterFoto(result.data)
                    }
                    else{
                        setAdapterFoto(null)
                    }
                }

                override fun onFailure(
                    call: Call<ModelResponseDaftarFotoProduk>,
                    t: Throwable
                ) {
                    isShowLoading.value = false
                    setAdapterFoto(null)
                }
            })
    }
}