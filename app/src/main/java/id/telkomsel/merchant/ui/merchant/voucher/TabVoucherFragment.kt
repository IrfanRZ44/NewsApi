package id.telkomsel.merchant.ui.merchant.voucher

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.viewpager.widget.ViewPager
import com.google.zxing.integration.android.IntentIntegrator
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseFragmentBind
import id.telkomsel.merchant.databinding.FragmentTabVoucherBinding
import id.telkomsel.merchant.model.response.ModelResponse
import id.telkomsel.merchant.ui.merchant.voucher.daftarVoucher.DaftarVoucherFragment
import id.telkomsel.merchant.ui.merchant.voucher.scanVoucher.CustomScanActivity
import id.telkomsel.merchant.utils.Constant
import id.telkomsel.merchant.utils.RetrofitUtils
import id.telkomsel.merchant.utils.adapter.SectionsPagerAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TabVoucherFragment : BaseFragmentBind<FragmentTabVoucherBinding>() {
    private lateinit var viewModel: TabVoucherViewModel
    override fun getLayoutResource(): Int = R.layout.fragment_tab_voucher
    private val fragmentTerjual = DaftarVoucherFragment(Constant.active)
    private val fragmentExpired = DaftarVoucherFragment(Constant.expired)
    private val fragmentNotUsed = DaftarVoucherFragment(Constant.notused)

    override fun myCodeHere() {
        supportActionBar?.show()
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.title = Constant.voucher
        setHasOptionsMenu(false)

        init()
        onClick()
    }

    private fun init() {
        bind.lifecycleOwner = this
        viewModel = TabVoucherViewModel()
        bind.viewModel = viewModel

        if (savedData.getDataMerchant()?.level == Constant.levelMerchant){
            bind.btnAction.visibility = View.VISIBLE
        }
        else{
            bind.btnAction.visibility = View.GONE
        }
    }

    private fun onClick() {
        bind.btnKode.setOnClickListener {
            context?.let { it1 -> showDialogInputKode(it1) }
        }

        bind.btnScan.setOnClickListener {
            showScanner()
        }
    }

    @Suppress("DEPRECATION")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewPager(bind.viewPager)
        bind.tabs.setupWithViewPager(bind.viewPager)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Suppress("DEPRECATION")
    private fun setupViewPager(pager: ViewPager) {
        val adapter = SectionsPagerAdapter(childFragmentManager)

        adapter.addFragment(fragmentTerjual, "Terjual")
        adapter.addFragment(fragmentExpired, "Terpakai")
        adapter.addFragment(fragmentNotUsed, "Kadaluarsa")

        pager.adapter = adapter
    }

    private fun showDialogInputKode(ctx: Context){
        val alert = AlertDialog.Builder(ctx)
        alert.setMessage("Mohon masukkan kode voucher yang telah digunakan pelanggan :")

        val editText = EditText(ctx)
        val linearLayout = LinearLayout(ctx)
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
            "Kirim"
        ) { dialog, _ ->
            val kodeVoucher = editText.text.toString()
            val merchantId = savedData.getDataMerchant()?.id
            dialog.dismiss()
            if (kodeVoucher.isNotEmpty() && merchantId != null){
                updateStatusVoucherByMerchant(kodeVoucher, merchantId)
            }
            else{
                viewModel.message.value = "Error, mohon masukkan kode voucher"
            }
        }
        alert.setNegativeButton(
            Constant.batal
        ) { dialog, _ ->
            dialog.dismiss()
        }

        alert.show()
    }

    private fun showScanner(){
        val integrator = IntentIntegrator.forSupportFragment(this)

        integrator.setOrientationLocked(true)
        integrator.setPrompt("Scan QR code")
        integrator.setBeepEnabled(true)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)

        integrator.captureActivity = CustomScanActivity::class.java
        integrator.initiateScan()
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                setStatus("Dibatalkan")
            } else {
                val kodeVoucher = result.contents
                val merchantId = savedData.getDataMerchant()?.id
                if (kodeVoucher.isNotEmpty() && merchantId != null){
                    updateStatusVoucherByMerchant(kodeVoucher, merchantId)
                }
                else{
                    viewModel.message.value = "Error, QR Code tidak valid"
                }
            }
        }
    }

    private fun updateStatusVoucherByMerchant(kodeVoucher: String, merchantId: Int) {
        setLoading(true)

        RetrofitUtils.updateStatusVoucherByMerchant(kodeVoucher, merchantId,
            object : Callback<ModelResponse> {
                override fun onResponse(
                    call: Call<ModelResponse>,
                    response: Response<ModelResponse>
                ) {
                    setLoading(false)
                    val result = response.body()

                    if (result?.message == Constant.reffSuccess) {
                        setCustom()
                    } else {
                        setStatus(result?.message?:"Error, kode voucher tidak ditemukan")
                    }
                }

                override fun onFailure(
                    call: Call<ModelResponse>,
                    t: Throwable
                ) {
                    setLoading(false)
                    setStatus(t.message?:"Error, koneksi tidak stabil")
                }
            })
    }

    private fun setMessage(msg: String){
        when (bind.tabs.selectedTabPosition) {
            0 -> {
                fragmentTerjual.viewModel.message.value = msg
            }
            1 -> {
                fragmentExpired.viewModel.message.value = msg
            }
            else -> {
                fragmentNotUsed.viewModel.message.value = msg
            }
        }
    }

    private fun setStatus(msg: String){
        when (bind.tabs.selectedTabPosition) {
            0 -> {
                fragmentTerjual.viewModel.status.value = msg
            }
            1 -> {
                fragmentExpired.viewModel.status.value = msg
            }
            else -> {
                fragmentNotUsed.viewModel.status.value = msg
            }
        }
    }

    private fun setLoading(load: Boolean){
        when (bind.tabs.selectedTabPosition) {
            0 -> {
                fragmentTerjual.viewModel.isShowLoading.value = load
            }
            1 -> {
                fragmentExpired.viewModel.isShowLoading.value = load
            }
            else -> {
                fragmentNotUsed.viewModel.isShowLoading.value = load
            }
        }
    }

    private fun setCustom(){
        when (bind.tabs.selectedTabPosition) {
            0 -> {
                fragmentTerjual.viewModel.status.value = "Berhasil update status voucher"
                fragmentTerjual.viewModel.startPage = 0
                fragmentTerjual.viewModel.listData.clear()
                fragmentTerjual.viewModel.adapter.notifyDataSetChanged()
                fragmentTerjual.viewModel.checkUsername()
            }
            1 -> {
                fragmentExpired.viewModel.status.value = "Berhasil update status voucher"
                fragmentExpired.viewModel.startPage = 0
                fragmentExpired.viewModel.listData.clear()
                fragmentExpired.viewModel.adapter.notifyDataSetChanged()
                fragmentExpired.viewModel.checkUsername()
            }
            else -> {
                fragmentNotUsed.viewModel.status.value = "Berhasil update status voucher"
                fragmentNotUsed.viewModel.startPage = 0
                fragmentNotUsed.viewModel.listData.clear()
                fragmentNotUsed.viewModel.adapter.notifyDataSetChanged()
                fragmentNotUsed.viewModel.checkUsername()
            }
        }
    }
}