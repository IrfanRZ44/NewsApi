package id.telkomsel.merchant.ui.pelanggan.beranda.voucher.daftarVoucher

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
        adapter = AdapterVoucher(listData)
        rcRequest.adapter = adapter
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

                    if (result?.message == Constant.reffSuccess){
                        for (i in result.data.indices){
                            listData.add(result.data[i])
                            adapter.notifyDataSetChanged()
                        }

                        startPage += 25
                    }
                    else{
                        if (startPage == 0){
                            message.value =
                                "Maaf, Anda belum memiliki voucher"
                        }
                        else{
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