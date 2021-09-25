package id.telkomsel.merchant.ui.pelanggan.beranda.voucher.daftarVoucher

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseViewModel
import id.telkomsel.merchant.model.ModelVoucher
import id.telkomsel.merchant.model.response.ModelResponse
import id.telkomsel.merchant.model.response.ModelResponseVoucher
import id.telkomsel.merchant.services.loadingIndicatorView.LoadingIndicatorView
import id.telkomsel.merchant.utils.Constant
import id.telkomsel.merchant.utils.DataSave
import id.telkomsel.merchant.utils.RetrofitUtils
import id.telkomsel.merchant.utils.adapter.convertRupiah
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
    private lateinit var textMessage: AppCompatTextView
    private lateinit var textKode: AppCompatTextView
    private lateinit var textNamaMerchant: AppCompatTextView
    private lateinit var textNamaProduk: AppCompatTextView
    private lateinit var textAlamat: AppCompatTextView
    private lateinit var textKecamatan: AppCompatTextView
    private lateinit var textHarga: AppCompatTextView
    private lateinit var textPromo: AppCompatTextView
    private lateinit var textJumlah: AppCompatTextView
    private lateinit var btnMaps: AppCompatButton
    private lateinit var btnUsed: AppCompatButton
    private lateinit var showProgress: LoadingIndicatorView

    fun initAdapter() {
        rcRequest.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        adapter = AdapterVoucher(listData, statusVoucher)
        { item: ModelVoucher, position: Int -> onClickVoucher(item, position) }
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
        textMessage = bottomView.findViewById(R.id.textMessage)
        textKode = bottomView.findViewById(R.id.textKode)
        textNamaMerchant = bottomView.findViewById(R.id.textNamaMerchant)
        textNamaProduk = bottomView.findViewById(R.id.textNamaProduk)
        textAlamat = bottomView.findViewById(R.id.textAlamat)
        textHarga = bottomView.findViewById(R.id.textHarga)
        textKecamatan = bottomView.findViewById(R.id.textKecamatan)
        textPromo = bottomView.findViewById(R.id.textPromo)
        textJumlah = bottomView.findViewById(R.id.textJumlah)
        btnMaps = bottomView.findViewById(R.id.btnMaps)
        btnUsed = bottomView.findViewById(R.id.btnUsed)
        showProgress = bottomView.findViewById(R.id.showProgress)
        val btnClose = bottomView.findViewById<AppCompatImageButton>(R.id.btnClose)

        btnClose.setOnClickListener {
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

                        if (result.data.isEmpty()){
                            if (startPage == 0) {
                                when (statusVoucher) {
                                    Constant.active -> {
                                        message.value = "Maaf, Anda belum memiliki voucher yang terjual"
                                    }
                                    Constant.expired -> {
                                        message.value =
                                            "Maaf, Anda tidak memiliki voucher yang telah digunakan"
                                    }
                                    else -> {
                                        message.value =
                                            "Maaf, Anda tidak memiliki voucher yang telah kadaluarsa"
                                    }
                                }
                            } else {
                                status.value = "Maaf, sudah tidak ada lagi data"
                            }
                        }
                        else{
                            message.value = ""
                        }
                        startPage += 25
                    } else {
                        if (startPage == 0) {
                            when (statusVoucher) {
                                Constant.active -> {
                                    message.value = "Maaf, Anda belum memiliki voucher"
                                }
                                Constant.expired -> {
                                    message.value =
                                        "Maaf, Anda tidak memiliki voucher yang telah digunakan"
                                }
                                else -> {
                                    message.value =
                                        "Maaf, Anda tidak memiliki voucher yang telah kadaluarsa"
                                }
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

    private fun updateStatusVoucher(kodeVoucher: String, position: Int) {
        showProgress.visibility = View.VISIBLE

        RetrofitUtils.updateStatusVoucher(kodeVoucher,
            object : Callback<ModelResponse> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(
                    call: Call<ModelResponse>,
                    response: Response<ModelResponse>
                ) {
                    showProgress.visibility = View.GONE
                    val result = response.body()

                    if (result?.message == Constant.reffSuccess) {
                        status.value = "Berhasil update status voucher"
                        listData.removeAt(position)
                        adapter.notifyDataSetChanged()
                        btmSheet.dismiss()
                    } else {
                        textMessage.text = "Gagal update status voucher"
                    }
                }

                override fun onFailure(
                    call: Call<ModelResponse>,
                    t: Throwable
                ) {
                    showProgress.visibility = View.GONE
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

    private fun dialogUseVoucher(kodeVoucher: String, position: Int){
        if (activity != null){
            val alertUseVoucher = AlertDialog.Builder(activity)
            alertUseVoucher.setMessage("Apakah Anda yakin ingin mengubah status voucher menjadi telah terpakai?")
            alertUseVoucher.setPositiveButton(
                Constant.iya
            ) { dialog, _ ->
                updateStatusVoucher(kodeVoucher, position)
                dialog.dismiss()
            }

            alertUseVoucher.setNegativeButton(
                Constant.batal
            ) { dialog, _ ->
                dialog.dismiss()
            }
            alertUseVoucher.show()
        }
        else{
            message.value = "Mohon mulai ulang aplikasi"
        }
    }

    @SuppressLint("SetTextI18n")
    private fun onClickVoucher(item: ModelVoucher, position: Int){
        generateQRCode(item.kode_voucher)
        textKode.text = ": ${item.kode_voucher}"
        textNamaMerchant.text = ": ${item.dataMerchant?.nama_merchant}"
        textNamaProduk.text = ": ${item.dataProduk?.nama}"
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

        btnUsed.setOnClickListener {
            dialogUseVoucher(item.kode_voucher, position)
        }

        btmSheet.show()
    }
}