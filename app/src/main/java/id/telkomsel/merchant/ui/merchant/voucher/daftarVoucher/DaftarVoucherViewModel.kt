package id.telkomsel.merchant.ui.merchant.voucher.daftarVoucher

import android.annotation.SuppressLint
import android.app.Activity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.telkomsel.merchant.base.BaseViewModel
import id.telkomsel.merchant.model.ModelVoucher
import id.telkomsel.merchant.model.response.ModelResponseVoucher
import id.telkomsel.merchant.utils.Constant
import id.telkomsel.merchant.utils.DataSave
import id.telkomsel.merchant.utils.RetrofitUtils
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

    fun initAdapter() {
        rcRequest.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        adapter = AdapterVoucher(listData, statusVoucher)
        rcRequest.adapter = adapter
    }

    fun checkUsername(){
        val merchantId = savedData.getDataMerchant()?.id
        if (merchantId != null){
            getDaftarVoucher(merchantId)
        }
        else{
            status.value = "Error, terjadi kesalahan database"
        }
    }

    private fun getDaftarVoucher(merchantId: Int) {
        isShowLoading.value = true

        RetrofitUtils.getDaftarVoucherByMerchant(startPage, merchantId, statusVoucher,
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

                        if (result.data.isNullOrEmpty()){
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
}