package id.telkomsel.merchant.ui.merchant.listProduk

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseViewModel
import id.telkomsel.merchant.model.ModelKategori
import id.telkomsel.merchant.model.ModelProduk
import id.telkomsel.merchant.model.response.ModelResponseDaftarKategori
import id.telkomsel.merchant.model.response.ModelResponseDaftarProduk
import id.telkomsel.merchant.ui.merchant.detailProduk.DetailProdukAdminFragment
import id.telkomsel.merchant.utils.Constant
import id.telkomsel.merchant.utils.DataSave
import id.telkomsel.merchant.utils.RetrofitUtils
import id.telkomsel.merchant.utils.adapter.getDate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("StaticFieldLeak")
class DaftarProdukViewModel(
    private val navController: NavController,
    private val activity: Activity?,
    private val rcKategori: RecyclerView,
    private val rcProduk: RecyclerView,
    private val statusRequest: String,
    private val savedData: DataSave
) : BaseViewModel() {
    val listKategori = ArrayList<ModelKategori>()
    val listProduk = ArrayList<ModelProduk>()
    lateinit var adapterKategori: AdapterListKategori
    lateinit var adapterProduk: AdapterListProduk
    var isSearching = false
    var startPage = 0
    private var idSubKategori = 0

    fun initAdapterKategori() {
        rcKategori.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        adapterKategori = AdapterListKategori(listKategori
        ) { item: ModelKategori -> onClickItemKategori(item) }
        rcKategori.adapter = adapterKategori
    }

    fun initAdapterProduk() {
        val layoutManager = GridLayoutManager(activity, 2)
        rcProduk.layoutManager = layoutManager
        adapterProduk = AdapterListProduk(listProduk
        ) { item: ModelProduk -> onClickItemProduk(item) }
        rcProduk.adapter = adapterProduk
    }

    fun checkCluster(search: String?) {
        val cluster = savedData.getDataMerchant()?.cluster
        val regional = savedData.getDataMerchant()?.regional
        val level = savedData.getDataMerchant()?.level

        if (level == Constant.levelChannel && !regional.isNullOrEmpty()){
            getDaftarProdukByAdmin(regional, "regional", search, if (idSubKategori == 0) "" else idSubKategori.toString())
        }
        else if ((level == Constant.levelCSO || level == Constant.levelSBP) && !cluster.isNullOrEmpty()){
            getDaftarProdukByAdmin(cluster, cluster, search, if (idSubKategori == 0) "" else idSubKategori.toString())
        }
        else{
            message.value = "Error, gagal mendapatkan data cluster Anda"
        }
    }

    fun getDataKategori() {
        listKategori.clear()
        isShowLoading.value = true
        listKategori.add(ModelKategori(0, 0, "Semua",
            true, getDate(Constant.dateFormat1), getDate(Constant.dateFormat1)))
        adapterKategori.notifyDataSetChanged()

        RetrofitUtils.getTopSubKategori(
            object : Callback<ModelResponseDaftarKategori> {
                override fun onResponse(
                    call: Call<ModelResponseDaftarKategori>,
                    response: Response<ModelResponseDaftarKategori>
                ) {
                    isShowLoading.value = false
                    isSearching = false
                    val result = response.body()

                    if (result?.message == Constant.reffSuccess){
                        listKategori.addAll(result.data)
                        adapterKategori.notifyDataSetChanged()

                        if (listKategori.size == 0){
                            rcKategori.visibility = View.GONE
                        }
                        else{
                            rcKategori.visibility = View.VISIBLE
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

    private fun getDaftarProdukByAdmin(cluster: String, userRequest: String, search: String?, sub_kategori_id: String?) {
        isShowLoading.value = true

        RetrofitUtils.getDaftarProdukByAdmin(cluster, userRequest, startPage, statusRequest, search, sub_kategori_id,
            object : Callback<ModelResponseDaftarProduk> {
                override fun onResponse(
                    call: Call<ModelResponseDaftarProduk>,
                    response: Response<ModelResponseDaftarProduk>
                ) {
                    isShowLoading.value = false
                    isSearching = false
                    val result = response.body()

                    if (result?.message == Constant.reffSuccess){
                        listProduk.addAll(result.data)
                        adapterProduk.notifyDataSetChanged()

                        startPage += 25
                        if (listProduk.size == 0){
                            if (search.isNullOrEmpty()){
                                if(statusRequest == Constant.statusRequest){
                                    message.value = Constant.noProdukRequest
                                }
                                else{
                                    message.value = Constant.noProduk
                                }
                            }
                            else{
                                message.value = "Maaf, belum ada data produk dengan nama $search"
                            }
                        }
                        else{
                            if (startPage > 0 && result.data.isEmpty()){
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
                    call: Call<ModelResponseDaftarProduk>,
                    t: Throwable
                ) {
                    isSearching = false
                    isShowLoading.value = false
                    message.value = t.message
                }
            })
    }

    private fun onClickItemKategori(item: ModelKategori){
        idSubKategori = item.id
        startPage = 0
        listProduk.clear()
        adapterProduk.notifyDataSetChanged()
        checkCluster("")
    }

    private fun onClickItemProduk(item: ModelProduk){
        val bundle = Bundle()
        val fragmentTujuan = DetailProdukAdminFragment()
        bundle.putParcelable(Constant.reffProduk, item)
        fragmentTujuan.arguments = bundle
        navController.navigate(R.id.detailProdukAdminFragment, bundle)
    }
}