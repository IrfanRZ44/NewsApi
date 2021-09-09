package id.telkomsel.merchant.ui.pelanggan.beranda.voucher.daftarVoucher

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseViewModel
import id.telkomsel.merchant.model.ModelVoucher
import id.telkomsel.merchant.model.response.ModelResponseVoucher
import id.telkomsel.merchant.utils.Constant
import id.telkomsel.merchant.utils.DataSave
import id.telkomsel.merchant.utils.RetrofitUtils
import id.telkomsel.merchant.utils.adapter.convertRupiah
import kotlinx.android.synthetic.main.item_voucher.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("StaticFieldLeak")
class DaftarVoucherViewModel(
    private val activity: Activity?,
    private val rcRequest: RecyclerView,
    private val savedData: DataSave,
    private val statusVoucher: String
) : BaseViewModel() {
    val listData = ArrayList<ModelVoucher>()
    lateinit var adapter: AdapterVoucher
    var isSearching = false
    var startPage = 0
    lateinit var btmSheet: BottomSheetDialog
    private lateinit var imgCode: AppCompatImageView
    private lateinit var textKode: AppCompatTextView
    private lateinit var textNamaMerchant: AppCompatTextView
    private lateinit var textAlamat: AppCompatTextView
    private lateinit var textKecamatan: AppCompatTextView
    private lateinit var textHarga: AppCompatTextView
    private lateinit var textPromo: AppCompatTextView
    private lateinit var textJumlah: AppCompatTextView
    private lateinit var btnMaps: FloatingActionButton

    fun initAdapter() {
        rcRequest.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        adapter = AdapterVoucher(listData, statusVoucher) { item: ModelVoucher -> onClickVoucher(
            item
        ) }
        rcRequest.adapter = adapter
    }

    @SuppressLint("InflateParams", "SetTextI18n")
    fun initBehaviorVoucher(root: View, layoutInflater: LayoutInflater){
        btmSheet = BottomSheetDialog(root.context)
        val bottomView = layoutInflater.inflate(R.layout.behavior_generator_voucher, null)

        btmSheet.setContentView(bottomView)
        btmSheet.setCanceledOnTouchOutside(true)
        btmSheet.setCancelable(true)
        btmSheet.behavior.isDraggable = false
        btmSheet.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        imgCode = bottomView.findViewById(R.id.imgCode)
        textKode = bottomView.findViewById(R.id.textKode)
        textNamaMerchant = bottomView.findViewById(R.id.textNamaMerchant)
        textAlamat = bottomView.findViewById(R.id.textAlamat)
        textHarga = bottomView.findViewById(R.id.textHarga)
        textKecamatan = bottomView.findViewById(R.id.textKecamatan)
        textPromo = bottomView.findViewById(R.id.textPromo)
        textJumlah = bottomView.findViewById(R.id.textJumlah)
        btnMaps = bottomView.findViewById(R.id.btnMaps)
        val btnDone = bottomView.findViewById<FloatingActionButton>(R.id.btnDone)

        btnDone.setOnClickListener {
            btmSheet.dismiss()
        }
    }

    fun checkUsername(){
        val username = savedData.getDataPelanggan()?.username
        if (!username.isNullOrEmpty()){
            getDaftarVoucher(username)
        }
        else{
            status.value = "Error, terjadi kesalahan database"
        }
    }

    private fun getDaftarVoucher(username: String) {
        isShowLoading.value = true
        adapter.notifyDataSetChanged()

        RetrofitUtils.getDaftarVoucher(startPage, username, statusVoucher,
            object : Callback<ModelResponseVoucher> {
                override fun onResponse(
                    call: Call<ModelResponseVoucher>,
                    response: Response<ModelResponseVoucher>
                ) {
                    isShowLoading.value = false
                    isSearching = false
                    val result = response.body()

                    if (result?.message == Constant.reffSuccess) {
                        for (i in result.data.indices) {
                            listData.add(result.data[i])
                            adapter.notifyDataSetChanged()
                        }

                        startPage += 25
                    } else {
                        if (startPage == 0) {
                            if (statusVoucher == Constant.active) {
                                status.value = "Maaf, Anda belum memiliki voucher"
                            } else {
                                status.value =
                                    "Maaf, Anda tidak memiliki voucher yang telah kadaluarsa"
                            }
                        } else {
                            status.value = result?.message
                        }
                    }
                }

                override fun onFailure(
                    call: Call<ModelResponseVoucher>,
                    t: Throwable
                ) {
                    isSearching = false
                    isShowLoading.value = false
                    message.value = t.message
                }
            })
    }

    private fun generateQRCode(kodeVoucher: String){
        val localMultiFormatWriter = MultiFormatWriter()

        try {
            val localBitMatrix = localMultiFormatWriter.encode(kodeVoucher, BarcodeFormat.QR_CODE, 1000, 1000)
            val localBitmap = BarcodeEncoder().createBitmap(localBitMatrix)
            imgCode.setImageBitmap(localBitmap)
            return
        } catch (localException: Exception) {
            message.value = localException.message.toString()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun onClickVoucher(item: ModelVoucher){
        generateQRCode(item.kode_voucher)
        textKode.text = ": ${item.kode_voucher}"
        textNamaMerchant.text = ": ${item.dataMerchant?.nama_merchant}"
        textAlamat.text = ": ${item.dataMerchant?.alamat_merchant}"
        textKecamatan.text = ": ${item.dataMerchant?.kecamatan}, ${item.dataMerchant?.kabupaten}"
        val harga = item.dataProduk?.harga?:0
        textHarga.text = ": ${convertRupiah(harga.toDouble())}"
        textPromo.text = ": ${item.dataProduk?.promo}"
        textJumlah.text = ": ${item.jumlah}"

        btnMaps.setOnClickListener {
            val gmmIntentUri = Uri.parse("google.navigation:q=${item.dataMerchant?.latitude},${item.dataMerchant?.longitude}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            activity?.startActivity(mapIntent)
        }

        btmSheet.show()
    }
}