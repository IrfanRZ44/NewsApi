package id.telkomsel.merchant.ui.pelanggan.detailProduk

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import coil.load
import coil.request.CachePolicy
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
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
import id.telkomsel.merchant.services.numberPicker.NumberPicker
import id.telkomsel.merchant.services.numberPicker.enums.ActionEnum
import id.telkomsel.merchant.services.numberPicker.interfac.ValueChangedListener
import id.telkomsel.merchant.utils.Constant
import id.telkomsel.merchant.utils.DataSave
import id.telkomsel.merchant.utils.RetrofitUtils
import id.telkomsel.merchant.utils.adapter.*
import id.telkomsel.merchant.listener.ListenerFotoProduk
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
    val viewProduk = MutableLiveData<String>()
    val stokProduk = MutableLiveData<String>()
    val alamatProduk = MutableLiveData<String>()
    val tanggalProduk = MutableLiveData<String>()
    val hargaProduk = MutableLiveData<String>()
    private val listGambar = ArrayList<ModelFotoProduk>()
    private var adapterFotoProduk: AdapterFotoProduk? = null
    lateinit var btmSheet : BottomSheetDialog
    lateinit var imgFoto : AppCompatImageView
    private lateinit var btnClose : AppCompatImageButton
    private lateinit var etJumlah : NumberPicker
    lateinit var textStatus : AppCompatTextView
    lateinit var textStok : AppCompatTextView
    lateinit var textHarga : AppCompatTextView
    lateinit var btnBeli : AppCompatButton

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
        val ctx = context

        if (!dataPelanggan?.username.isNullOrEmpty()){
            if (dataPelanggan?.poin?:0 > dataProduk.value?.jumlah_poin?:0 && ctx != null){
                btmSheet.show()
            }
            else{
                message.value = "Maaf, Anda tidak memiliki poin yang cukup"
            }
        }
        else{
            message.value = "Maaf, Anda harus login terlebih dahulu"
            val navOption = NavOptions.Builder().setPopUpTo(R.id.pelangganFragment, true).build()
            navController.navigate(R.id.pelangganFragment, null, navOption)
            savedData.setDataBoolean(true, Constant.login)
        }
    }

    private fun onClickCreateVoucher(produkId: Int, username: String, jumlah: Int, totalHarga: Long){
        isShowLoading.value = true

        RetrofitUtils.createVoucher(produkId, username, jumlah, totalHarga,
            object : Callback<ModelResponse> {
                override fun onResponse(
                    call: Call<ModelResponse>,
                    response: Response<ModelResponse>
                ) {
                    isShowLoading.value = false
                    val result = response.body()

                    if (result?.message == Constant.reffSuccess){
                        message.value = "Berhasil membeli voucher"
                        val navOption = NavOptions.Builder().setPopUpTo(R.id.pelangganFragment, true).build()
                        navController.navigate(R.id.pelangganFragment, null, navOption)
                    }
                    else{
                        message.value = result?.message
                    }
                }

                override fun onFailure(
                    call: Call<ModelResponse>,
                    t: Throwable
                ) {
                    isShowLoading.value = false
                    message.value = "Error, gagal membeli voucher " + t.message
                }
            })
    }

    @SuppressLint("InflateParams", "SetTextI18n")
    fun initDialogJumlah(root: View, layoutInflater: LayoutInflater){
        btmSheet = BottomSheetDialog(root.context)
        val bottomView = layoutInflater.inflate(R.layout.behavior_input_jumlah,null)

        btmSheet.setContentView(bottomView)
        btmSheet.setCanceledOnTouchOutside(true)
        btmSheet.setCancelable(true)
        btmSheet.behavior.isDraggable = false
        btmSheet.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        imgFoto = bottomView.findViewById(R.id.imgFoto)
        btnClose = bottomView.findViewById(R.id.btnClose)
        etJumlah = bottomView.findViewById(R.id.etJumlah)
        textStatus = bottomView.findViewById(R.id.textStatus)
        textStok = bottomView.findViewById(R.id.textStok)
        textHarga = bottomView.findViewById(R.id.textHarga)
        btnBeli = bottomView.findViewById(R.id.btnBeli)

        textHarga.text = dataProduk.value?.harga.toString()
        textStok.text = dataProduk.value?.stok.toString()
        etJumlah.min = 1
        btnBeli.text = "Beli Sekarang ${dataProduk.value?.jumlah_poin} Poin"
        etJumlah.max = dataProduk.value?.stok?:1
        imgFoto.load(dataProduk.value?.url_foto) {
            crossfade(true)
            placeholder(R.drawable.ic_camera_white)
            error(R.drawable.ic_camera_white)
            fallback(R.drawable.ic_camera_white)
            memoryCachePolicy(CachePolicy.ENABLED)
        }

        textStatus.visibility = View.GONE
        etJumlah.setActionEnabled(ActionEnum.INCREMENT, true)
        etJumlah.setActionEnabled(ActionEnum.DECREMENT, true)
        etJumlah.setActionEnabled(ActionEnum.MANUAL, true)

        var defaultValue = 1
        val defaultHarga = dataProduk.value?.jumlah_poin?:0
        var hargaNow = dataProduk.value?.jumlah_poin?:0

        etJumlah.valueChangedListener = object : ValueChangedListener {
            override fun valueChanged(value: Int, action: ActionEnum?) {
                hargaNow = if (value > defaultValue){
                    hargaNow + defaultHarga
                }
                else{
                    hargaNow - defaultHarga
                }

                btnBeli.text = "Beli Sekarang $hargaNow Poin"
                defaultValue = value
            }
        }

        btnBeli.setOnClickListener {
            btmSheet.dismiss()
            val produkId = dataProduk.value?.id
            val username = savedData.getDataPelanggan()?.username
            val jumlah = etJumlah.value
            val totalHarga = hargaNow

            if (produkId != null && !username.isNullOrEmpty()){
                onClickCreateVoucher(produkId, username, jumlah, totalHarga)
            }
            else{
                message.value = "Error, terjadi kesalahan database"
            }
        }

        btnClose.setOnClickListener {
            btmSheet.dismiss()
        }
    }

    fun setData(){
        val hargaVoucher = dataProduk.value?.jumlah_poin?:0
        promoProduk.value = "Promo ${dataProduk.value?.promo}"
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