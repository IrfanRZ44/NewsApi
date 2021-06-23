package id.telkomsel.merchant.ui.merchant.listMerchant

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatTextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseViewModel
import id.telkomsel.merchant.model.ModelMerchant
import id.telkomsel.merchant.model.response.ModelResponseDaftarMerchant
import id.telkomsel.merchant.model.response.ModelResponseDataKategori
import id.telkomsel.merchant.ui.merchant.detailMerchant.DetailMerchantAdminFragment
import id.telkomsel.merchant.utils.Constant
import id.telkomsel.merchant.utils.DataSave
import id.telkomsel.merchant.utils.RetrofitUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("StaticFieldLeak")
class DaftarMerchantViewModel(
    private val navController: NavController,
    private val activity: Activity?,
    private val rcRequest: RecyclerView,
    private val statusRequest: String,
    private val savedData: DataSave
) : BaseViewModel() {
    val listRequest = ArrayList<ModelMerchant>()
    lateinit var adapter: AdapterListMerchant
    var isSearching = false
    var startPage = 0

    fun initAdapter() {
        rcRequest.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        adapter = AdapterListMerchant(
            listRequest,
            navController, statusRequest == Constant.statusRequest,
            { kategori_id: Int, textKategori: AppCompatTextView -> getDataKategori(kategori_id, textKategori) },
            { item: ModelMerchant -> onClickItem(item) }
        )
        rcRequest.adapter = adapter
    }

    fun getDataKategori(kategori_id: Int, textKategori: AppCompatTextView){
        isShowLoading.value = true

        RetrofitUtils.getDataKategori(kategori_id,
            object : Callback<ModelResponseDataKategori> {
                override fun onResponse(
                    call: Call<ModelResponseDataKategori>,
                    response: Response<ModelResponseDataKategori>
                ) {
                    isShowLoading.value = false
                    val result = response.body()

                    if (result?.message == Constant.reffSuccess){
                        textKategori.text = result.data?.nama
                    }
                    else{
                        textKategori.text = "-"
                    }
                }

                override fun onFailure(
                    call: Call<ModelResponseDataKategori>,
                    t: Throwable
                ) {
                    isShowLoading.value = false
                    textKategori.text = "-"
                }
            })
    }

    fun getDataTeknisi(search: String?, cluster: String) {
        isShowLoading.value = true
        adapter.notifyDataSetChanged()

        RetrofitUtils.getDaftarMerchantByAdmin(cluster, "cluster", startPage, statusRequest, search,
            object : Callback<ModelResponseDaftarMerchant> {
                override fun onResponse(
                    call: Call<ModelResponseDaftarMerchant>,
                    response: Response<ModelResponseDaftarMerchant>
                ) {
                    isShowLoading.value = false
                    isSearching = false
                    val result = response.body()

                    if (result?.message == Constant.reffSuccess){
                        for (i in result.dataMerchant.indices){
                            if (result.dataMerchant[i].id != savedData.getDataMerchant()?.id){
                                listRequest.add(result.dataMerchant[i])
                                adapter.notifyDataSetChanged()
                            }
                        }

                        startPage += 25
                        if (listRequest.size == 0){
                            if (search.isNullOrEmpty()){
                                if(statusRequest == Constant.statusRequest){
                                    message.value = Constant.noMerchantRequest
                                }
                                else{
                                    message.value = Constant.noMerchant
                                }
                            }
                            else{
                                message.value = "Maaf, belum ada data merchant dengan nama $search"
                            }
                        }
                        else{
                            if (startPage > 0 && result.dataMerchant.isEmpty()){
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
                    call: Call<ModelResponseDaftarMerchant>,
                    t: Throwable
                ) {
                    isSearching = false
                    isShowLoading.value = false
                    message.value = t.message
                }
            })
    }

    private fun onClickItem(item: ModelMerchant){
        val bundle = Bundle()
        val fragmentTujuan = DetailMerchantAdminFragment()
        bundle.putParcelable(Constant.reffMerchant, item)
        fragmentTujuan.arguments = bundle
        navController.navigate(R.id.detailMerchantAdminFragment, bundle)
    }
}