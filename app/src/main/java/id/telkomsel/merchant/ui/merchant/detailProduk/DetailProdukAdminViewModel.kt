package id.telkomsel.merchant.ui.merchant.detailProduk

import android.annotation.SuppressLint
import android.content.Context
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("StaticFieldLeak")
class DetailProdukAdminViewModel(
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
            val showEdit = savedData.getDataMerchant()?.level == Constant.levelCSO ||
                    savedData.getDataMerchant()?.level == Constant.levelSBP
            adapterFotoProduk = AdapterFotoProduk(
                ctx, listGambar, listener, showEdit
            )
            viewPager.offscreenPageLimit = 0
            viewPager.adapter = adapterFotoProduk
            dotsIndicator.setViewPager(viewPager)
        }
    }

    fun clickFotoProfil(){
        dataMerchant.value?.foto_profil?.let { onClickFoto(it, navController) }
    }

    fun onClickAccept(){
        dataProduk.value?.id?.let { updateStatus(it, Constant.statusActive, "") }
    }

    fun onClickDecline(){
        dataProduk.value?.id?.let {
            context?.let {
                    it1 -> showDialogTolak(it1, it)
            }
        }
    }

    fun onClickTukarPoin(){

    }

    fun setData(){
        val poin = dataProduk.value?.jumlah_poin?:0
        promoProduk.value = "Promo ${dataProduk.value?.promo}"
        poinProduk.value = "Tukar ${convertNumberWithoutRupiah(poin.toDouble())} Poin"
        viewProduk.value = "${dataProduk.value?.view} Views"
        stokProduk.value = "${dataProduk.value?.stok} Stok"
        tanggalProduk.value = "Masa berlaku hingga ${dataProduk.value?.tgl_kadaluarsa}"
        val harga = dataProduk.value?.harga?:0
        hargaProduk.value = convertRupiah(harga.toDouble())
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

    private fun showDialogTolak(context: Context, id: Int){
        val alert = AlertDialog.Builder(context)
        alert.setMessage("Mohon masukkan alasan produk ini ditolak :")

        val editText = EditText(context)
        val linearLayout = LinearLayout(context)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        linearLayout.setPadding(20, 0, 20, 0)
        linearLayout.layoutParams = layoutParams
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.addView(editText)
        alert.setView(linearLayout)

        alert.setPositiveButton(
            "Konfirmasi"
        ) { dialog, _ ->
            val comment = editText.text.toString()
            dialog.dismiss()
            if (comment.isEmpty()){
                message.value = "Error, mohon masukkan komentar"
            }
            else{
                updateStatus(id, Constant.statusDeclined, comment)
            }
        }
        alert.setNegativeButton(
            Constant.batal
        ) { dialog, _ ->
            dialog.dismiss()
        }

        alert.show()
    }

    private fun updateStatus(id: Int, status: String, comment: String){
        isShowLoading.value = true

        RetrofitUtils.updateStatusProduk(id, status, comment,
            object : Callback<ModelResponse> {
                override fun onResponse(
                    call: Call<ModelResponse>,
                    response: Response<ModelResponse>
                ) {
                    isShowLoading.value = false
                    val result = response.body()

                    if (result?.message == Constant.reffSuccess){
                        if (status == Constant.statusActive){
                            message.value = "Berhasil konfirmasi produk"
                            Toast.makeText(context, "Berhasil konfirmasi produk", Toast.LENGTH_LONG).show()
                        }
                        else{
                            message.value = "Berhasil menolak permintaan produk"
                            Toast.makeText(context, "Berhasil menolak permintaan produk", Toast.LENGTH_LONG).show()
                        }
                        val navOption = NavOptions.Builder().setPopUpTo(R.id.adminFragment, true).build()
                        navController.navigate(R.id.adminFragment, null, navOption)
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
                    message.value = "Error ${t.message}"
                }
            })
    }

    fun createFotoProduk(produk_id: RequestBody, level: RequestBody,
                             url_foto: MultipartBody.Part){
        isShowLoading.value = true

        RetrofitUtils.createFotoProduk(produk_id, level, url_foto,
            object : Callback<ModelResponse> {
                override fun onResponse(
                    call: Call<ModelResponse>,
                    response: Response<ModelResponse>
                ) {
                    isShowLoading.value = false
                    val result = response.body()

                    if (result?.message == Constant.reffSuccess){
                        val produkId = dataProduk.value?.id

                        if (produkId != null){
                            getDaftarFotoProduk(produkId)
                        }

                        if (savedData.getDataMerchant()?.level == Constant.levelSBP){
                            message.value = "Berhasil mengupload foto produk, mohon tunggu proses verifikasi dalam waktu 1x24 jam"
                        }
                        else{
                            message.value = "Berhasil mengupload foto produk"
                        }
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
                    message.value = t.message
                }
            })
    }

    fun deleteFotoProduk(id: Int){
        isShowLoading.value = true

        RetrofitUtils.deleteFotoProduk(id,
            object : Callback<ModelResponse> {
                override fun onResponse(
                    call: Call<ModelResponse>,
                    response: Response<ModelResponse>
                ) {
                    isShowLoading.value = false
                }

                override fun onFailure(
                    call: Call<ModelResponse>,
                    t: Throwable
                ) {
                    isShowLoading.value = false
                }
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