package id.telkomsel.merchant.ui.merchant.produk

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.telkomsel.merchant.base.BaseViewModel
import id.telkomsel.merchant.model.ModelKategori
import id.telkomsel.merchant.model.ModelProduk
import id.telkomsel.merchant.model.response.ModelResponseDaftarKategori
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
    private val savedData: DataSave
) : BaseViewModel() {
    val listKategori = ArrayList<ModelKategori>()
    val listProduk = ArrayList<ModelProduk>()
    lateinit var adapter: AdapterListKategori
    var isSearching = false

    fun initAdapter() {
        rcKategori.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        adapter = AdapterListKategori(listKategori)
        rcKategori.adapter = adapter
    }

    fun getDataKategori() {
        listKategori.clear()
        listKategori.add(ModelKategori(0, 0, "Semua",
            true, getDate(Constant.dateFormat1), getDate(Constant.dateFormat1)))
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
                        listKategori.addAll(result.data)
                        adapter.notifyDataSetChanged()

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

    private fun onClickItem(item: ModelKategori){
//        val bundle = Bundle()
//        val fragmentTujuan = DetailMerchantAdminFragment()
//        bundle.putParcelable(Constant.reffMerchant, item)
//        fragmentTujuan.arguments = bundle
//        navController.navigate(R.id.detailMerchantAdminFragment, bundle)
    }
}