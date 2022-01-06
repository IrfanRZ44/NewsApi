package id.telkomsel.merchandise.ui.sales.listStore.daftarStore

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.telkomsel.merchandise.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import id.telkomsel.merchandise.base.BaseViewModel
import id.telkomsel.merchandise.model.ModelStore
import id.telkomsel.merchandise.model.response.ModelResponseDaftarStore
import id.telkomsel.merchandise.ui.sales.detailStore.DetailStoreAdminFragment
import id.telkomsel.merchandise.utils.Constant
import id.telkomsel.merchandise.utils.DataSave
import id.telkomsel.merchandise.utils.RetrofitUtils

@SuppressLint("StaticFieldLeak")
class DaftarStoreViewModel(
    private val navController: NavController,
    private val activity: Activity?,
    private val rcStore: RecyclerView,
    private val statusRequest: String,
    private val savedData: DataSave
) : BaseViewModel() {
    val listStore = ArrayList<ModelStore>()
    lateinit var adapterStore: AdapterStore
    var isSearching = false
    var startPage = 0
    var textSearch = ""

    fun initAdapterStore() {
        rcStore.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        adapterStore = AdapterStore(
            listStore
        ) { item: ModelStore -> onClickItemStore(item) }
        rcStore.adapter = adapterStore
    }

    fun checkCluster() {
        val cluster = savedData.getDataSales()?.cluster
        val regional = savedData.getDataSales()?.regional
        val level = savedData.getDataSales()?.level

        if (level == Constant.levelChannel && !regional.isNullOrEmpty()){
            getDaftarStoreBySales(
                regional, level
            )
        }
        else if ((level == Constant.levelDLS || level == Constant.levelSales) && !cluster.isNullOrEmpty()){
            getDaftarStoreBySales(
                cluster, level
            )
        }
        else{
            message.value = "Error, terjadi kesalahan database"
        }
    }
    private fun getDaftarStoreBySales(
        cluster: String,
        level: String,
    ) {
        isShowLoading.value = true

        RetrofitUtils.getDaftarStoreBySales(cluster,
            level,
            startPage,
            statusRequest,
            textSearch,
            object : Callback<ModelResponseDaftarStore> {
                override fun onResponse(
                    call: Call<ModelResponseDaftarStore>,
                    response: Response<ModelResponseDaftarStore>
                ) {
                    isShowLoading.value = false
                    isSearching = false
                    val result = response.body()

                    if (result?.message == Constant.reffSuccess) {
                        listStore.addAll(result.dataStore)
                        adapterStore.notifyDataSetChanged()

                        startPage += 25
                        if (listStore.size == 0) {
                            if (textSearch.isEmpty()) {
                                if (statusRequest == Constant.statusRequest) {
//                                    message.value = Constant.noStoreRequest
                                    message.value = "$cluster $level"
                                } else {
                                    message.value = Constant.noStore
                                }
                            } else {
                                message.value = "Maaf, belum ada data store dengan nama $textSearch"
                            }
                        } else {
                            if (startPage > 0 && result.dataStore.isEmpty()) {
                                status.value = "Maaf, sudah tidak ada lagi data"
                            } else {
                                message.value = ""
                            }
                        }
                    } else {
                        message.value = result?.message
                    }
                }

                override fun onFailure(
                    call: Call<ModelResponseDaftarStore>,
                    t: Throwable
                ) {
                    isSearching = false
                    isShowLoading.value = false
                    message.value = t.message
                }
            })
    }

    private fun onClickItemStore(item: ModelStore){
        val bundle = Bundle()
        val fragmentTujuan = DetailStoreAdminFragment()
        bundle.putParcelable(Constant.reffStore, item)
        fragmentTujuan.arguments = bundle
        navController.navigate(R.id.detailStoreAdminFragment, bundle)
    }
}