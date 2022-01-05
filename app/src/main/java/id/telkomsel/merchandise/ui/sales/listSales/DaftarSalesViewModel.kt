package id.telkomsel.merchandise.ui.sales.listSales

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.telkomsel.merchandise.R
import id.telkomsel.merchandise.base.BaseViewModel
import id.telkomsel.merchandise.model.ModelSales
import id.telkomsel.merchandise.model.response.ModelResponseDaftarSales
import id.telkomsel.merchandise.ui.sales.detailSales.DetailSalesAdminFragment
import id.telkomsel.merchandise.utils.Constant
import id.telkomsel.merchandise.utils.DataSave
import id.telkomsel.merchandise.utils.RetrofitUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("StaticFieldLeak")
class DaftarSalesViewModel(
    private val navController: NavController,
    private val activity: Activity?,
    private val rcRequest: RecyclerView,
    private val statusRequest: String,
    private val savedData: DataSave
) : BaseViewModel() {
    val listRequest = ArrayList<ModelSales>()
    lateinit var adapter: AdapterSales
    var isSearching = false
    var startPage = 0

    fun initAdapter() {
        rcRequest.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        adapter = AdapterSales(
            listRequest,
            navController, statusRequest == Constant.statusRequest
        ) { item: ModelSales -> onClickItem(item) }
        rcRequest.adapter = adapter
    }

    fun getDataTeknisi(search: String?, cluster: String, userRequest: String) {
        isShowLoading.value = true
        adapter.notifyDataSetChanged()

        RetrofitUtils.getDaftarSalesByAdmin(cluster, userRequest, startPage, statusRequest, search,
            object : Callback<ModelResponseDaftarSales> {
                override fun onResponse(
                    call: Call<ModelResponseDaftarSales>,
                    response: Response<ModelResponseDaftarSales>
                ) {
                    isShowLoading.value = false
                    isSearching = false
                    val result = response.body()

                    if (result?.message == Constant.reffSuccess){
                        for (i in result.dataSales.indices){
                            if (result.dataSales[i].id != savedData.getDataSales()?.id){
                                listRequest.add(result.dataSales[i])
                                adapter.notifyDataSetChanged()
                            }
                        }

                        startPage += 25
                        if (listRequest.size == 0){
                            if (search.isNullOrEmpty()){
                                if(statusRequest == Constant.statusRequest){
                                    message.value = Constant.noSalesRequest
                                }
                                else{
                                    message.value = Constant.noSales
                                }
                            }
                            else{
                                message.value = "Maaf, belum ada data merchandise dengan nama $search"
                            }
                        }
                        else{
                            if (startPage > 0 && result.dataSales.isEmpty()){
                                status.value = "Maaf, sudah tidak ada lagi data"
                            }
                            else{
                                message.value = ""
                            }
                        }
                    }
                    else{
                        message.value = result?.message
                    }
                }

                override fun onFailure(
                    call: Call<ModelResponseDaftarSales>,
                    t: Throwable
                ) {
                    isSearching = false
                    isShowLoading.value = false
                    message.value = t.message
                }
            })
    }

    private fun onClickItem(item: ModelSales){
        val bundle = Bundle()
        val fragmentTujuan = DetailSalesAdminFragment()
        bundle.putParcelable(Constant.reffSales, item)
        fragmentTujuan.arguments = bundle
        navController.navigate(R.id.detailSalesAdminFragment, bundle)
    }
}