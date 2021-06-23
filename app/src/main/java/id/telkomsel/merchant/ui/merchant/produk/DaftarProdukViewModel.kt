package id.telkomsel.merchant.ui.merchant.produk

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.telkomsel.merchant.base.BaseViewModel
import id.telkomsel.merchant.model.ModelKategori
import id.telkomsel.merchant.model.response.ModelResponseDaftarKategori
import id.telkomsel.merchant.utils.Constant
import id.telkomsel.merchant.utils.DataSave
import id.telkomsel.merchant.utils.RetrofitUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("StaticFieldLeak")
class DaftarProdukViewModel(
    private val navController: NavController,
    private val activity: Activity?,
    private val rcRequest: RecyclerView,
    private val savedData: DataSave
) : BaseViewModel() {
    val listRequest = ArrayList<ModelKategori>()
    lateinit var adapter: AdapterListKategori
    var isSearching = false

    fun initAdapter() {
        rcRequest.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        adapter = AdapterListKategori(
            listRequest
        )
        rcRequest.adapter = adapter
    }

    fun getDataKategori() {
        isShowLoading.value = true
        adapter.notifyDataSetChanged()

        RetrofitUtils.getDaftarKategori(
            object : Callback<ModelResponseDaftarKategori> {
                override fun onResponse(
                    call: Call<ModelResponseDaftarKategori>,
                    response: Response<ModelResponseDaftarKategori>
                ) {
                    isShowLoading.value = false
                    isSearching = false
                    val result = response.body()

                    if (result?.message == Constant.reffSuccess){
                        listRequest.addAll(result.data)
                        adapter.notifyDataSetChanged()

                        if (listRequest.size == 0){
                            rcRequest.visibility = View.GONE
                        }
                        else{
                            rcRequest.visibility = View.VISIBLE
                        }
                    }
                    else{
                        message.value = result?.message
                    }
                }

                override fun onFailure(
                    call: Call<ModelResponseDaftarKategori>,
                    t: Throwable
                ) {
                    isSearching = false
                    isShowLoading.value = false
                    message.value = t.message
                }
            })
    }

    private fun onClickItem(item: ModelKategori){
//        val bundle = Bundle()
//        val fragmentTujuan = DetailMerchantAdminFragment()
//        bundle.putParcelable(Constant.reffMerchant, item)
//        fragmentTujuan.arguments = bundle
//        navController.navigate(R.id.detailMerchantAdminFragment, bundle)
    }
}