package id.telkomsel.merchandise.ui.sales.listStore.daftarStore

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.telkomsel.merchandise.R
import id.telkomsel.merchandise.base.BaseViewModel
import id.telkomsel.merchandise.model.ModelStore
import id.telkomsel.merchandise.utils.Constant
import id.telkomsel.merchandise.utils.DataSave

@SuppressLint("StaticFieldLeak")
class DaftarStoreViewModel(
    private val navController: NavController,
    private val activity: Activity?,
    private val context: Context?,
    private val rcStore: RecyclerView,
    private val statusRequest: String,
    private val stok: Int,
    private val isKadaluarsa: Boolean,
    private val savedData: DataSave
) : BaseViewModel() {
    val listStore = ArrayList<ModelStore>()
    lateinit var adapterStore: AdapterStore
    var isSearching = false
    var startPage = 0
    lateinit var textMessage: AppCompatTextView
    private lateinit var btnBatal: AppCompatButton
    var textSearch = ""

    fun initAdapterStore() {
        val layoutManager = GridLayoutManager(activity, 2)
        rcStore.layoutManager = layoutManager
        adapterStore = AdapterStore(
            listStore
        ) { item: ModelStore -> onClickItemStore(item) }
        rcStore.adapter = adapterStore
    }

    fun checkCluster() {
        val cluster = savedData.getDataSales()?.cluster
        val salesId = savedData.getDataSales()?.id
        val regional = savedData.getDataSales()?.regional
        val level = savedData.getDataSales()?.level

//        if (level == Constant.levelChannel && !regional.isNullOrEmpty()){
//            getDaftarStoreByAdmin(
//                regional,
//                Constant.levelChannel,
//                if (idSubKategori == 0) "" else idSubKategori.toString(),
//                ""
//            )
//        }
//        else if (level == Constant.levelDLS && !cluster.isNullOrEmpty()){
//            getDaftarStoreByAdmin(
//                cluster,
//                level,
//                if (idSubKategori == 0) "" else idSubKategori.toString(),
//                ""
//            )
//        }
//        else if (level == Constant.levelSales && salesId != null){
//            getDaftarStoreByAdmin(
//                "",
//                Constant.levelSales,
//                if (idSubKategori == 0) "" else idSubKategori.toString(),
//                salesId.toString()
//            )
//        }
//        else{
//            message.value = "Error, gagal mendapatkan data store"
//        }
    }
    private fun getDaftarStoreByAdmin(
        cluster: String,
        level: String,
        sub_kategori_id: String?,
        salesId: String?,
    ) {
        isShowLoading.value = true

//        RetrofitUtils.getDaftarStoreBySales(salesId, cluster,
//            level,
//            startPage,
//            statusRequest,
//            textSearch,
//            sub_kategori_id, stok, isKadaluarsa,
//            object : Callback<ModelResponseDaftarStore> {
//                override fun onResponse(
//                    call: Call<ModelResponseDaftarStore>,
//                    response: Response<ModelResponseDaftarStore>
//                ) {
//                    isShowLoading.value = false
//                    isSearching = false
//                    val result = response.body()
//
//                    if (result?.message == Constant.reffSuccess) {
//                        listStore.addAll(result.data)
//                        adapterStore.notifyDataSetChanged()
//
//                        startPage += 25
//                        if (listStore.size == 0) {
//                            if (textSearch.isEmpty()) {
//                                if (statusRequest == Constant.statusRequest) {
//                                    message.value = Constant.noStoreRequest
//                                } else {
//                                    message.value = Constant.noStore
//                                }
//                            } else {
//                                message.value = "Maaf, belum ada data store dengan nama $textSearch"
//                            }
//                        } else {
//                            if (startPage > 0 && result.data.isEmpty()) {
//                                status.value = "Maaf, sudah tidak ada lagi data"
//                            } else {
//                                message.value = ""
//                            }
//                        }
//                    } else {
//                        message.value = result?.message
//                    }
//                }
//
//                override fun onFailure(
//                    call: Call<ModelResponseDaftarStore>,
//                    t: Throwable
//                ) {
//                    isSearching = false
//                    isShowLoading.value = false
//                    message.value = t.message
//                }
//            })
    }

    private fun onClickItemStore(item: ModelStore){
//        val bundle = Bundle()
//        val fragmentTujuan = DetailStoreAdminFragment()
//        bundle.putParcelable(Constant.reffStore, item)
//        fragmentTujuan.arguments = bundle
//        navController.navigate(R.id.detailStoreAdminFragment, bundle)
    }
}