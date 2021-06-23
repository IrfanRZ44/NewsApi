package id.telkomsel.merchant.ui.pelanggan.beranda

import android.annotation.SuppressLint
import android.content.Context
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.telkomsel.merchant.base.BaseViewModel
import id.telkomsel.merchant.model.ModelBarang
import id.telkomsel.merchant.utils.Constant.noData

@SuppressLint("StaticFieldLeak")
class BerandaViewModel(
    private val rcData: RecyclerView,
    private val context: Context?,
    private val navController: NavController
) : BaseViewModel() {
    val listData = ArrayList<ModelBarang>()
    val listDataSearch = ArrayList<ModelBarang>()
    val listNama = ArrayList<ModelBarang>()
    var adapter: AdapterDataBeranda? = null

    fun initAdapter() {
        adapter = AdapterDataBeranda(
            listData,
            { dataData: ModelBarang -> onClickItem(dataData) },
            navController
        )
        rcData.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rcData.adapter = adapter
        rcData.isNestedScrollingEnabled = false
    }

    fun cekList() {
        isShowLoading.value = false

        if (listData.size == 0) status.value = noData
        else status.value = ""
    }

    private fun onClickItem(data: ModelBarang) {
        message.value = data.nama
    }

    fun createBarang(dataBarang: ModelBarang){
//        RetrofitUtils.createBarang(dataBarang,
//            object : Callback<ModelResponseBarang> {
//                override fun onResponse(
//                    call: Call<ModelResponseBarang>,
//                    response: Response<ModelResponseBarang>
//                ) {
//                    isShowLoading.value = false
//                    val result = response.body()
//
//                    if (result?.message == Constant.reffSuccess){
//                        message.value = "Berhasil membuat data"
//                    }
//                    else{
//                        message.value = result?.message
//                    }
//                }
//
//                override fun onFailure(
//                    call: Call<ModelResponseBarang>,
//                    t: Throwable
//                ) {
//                    isShowLoading.value = false
//                    message.value = t.message
//                }
//            })
    }

    fun getBarang(){
        listData.clear()
        listDataSearch.clear()
        adapter?.notifyDataSetChanged()
    }
}