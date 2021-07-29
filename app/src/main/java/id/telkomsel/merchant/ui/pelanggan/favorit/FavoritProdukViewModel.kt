package id.telkomsel.merchant.ui.pelanggan.favorit

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseViewModel
import id.telkomsel.merchant.model.ModelProduk
import id.telkomsel.merchant.model.response.ModelResponse
import id.telkomsel.merchant.model.response.ModelResponseDaftarProduk
import id.telkomsel.merchant.ui.pelanggan.detailProduk.DetailProdukPelangganFragment
import id.telkomsel.merchant.utils.Constant
import id.telkomsel.merchant.utils.DataSave
import id.telkomsel.merchant.utils.RetrofitUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("StaticFieldLeak")
class FavoritProdukViewModel(
    private val navController: NavController,
    private val activity: Activity?,
    private val rcProduk: RecyclerView,
    private val savedData: DataSave
) : BaseViewModel() {
    val listProduk = ArrayList<ModelProduk>()
    lateinit var adapterProduk: AdapterProdukFavorit
    var startPage = 0

    fun initAdapterProduk() {
        val layoutManager = GridLayoutManager(activity, 2)
        rcProduk.layoutManager = layoutManager
        adapterProduk = AdapterProdukFavorit(
            listProduk, { item: ModelProduk -> onClickItemProduk(item) },
            { item: ModelProduk, position: Int -> onClickItemProdukFavorit(item, position) }
        )
        rcProduk.adapter = adapterProduk
    }

    fun getDaftarProdukFavorit(username: String) {
        isShowLoading.value = true

        RetrofitUtils.getDaftarProdukFavorit(startPage,
            username,
            object : Callback<ModelResponseDaftarProduk> {
                override fun onResponse(
                    call: Call<ModelResponseDaftarProduk>,
                    response: Response<ModelResponseDaftarProduk>
                ) {
                    isShowLoading.value = false
                    val result = response.body()

                    if (result?.message == Constant.reffSuccess) {
                        listProduk.addAll(result.data)
                        adapterProduk.notifyDataSetChanged()

                        startPage += 25
                        if (listProduk.size == 0) {
                            message.value = Constant.noProdukFavorit
                        } else {
                            if (startPage > 0 && result.data.isEmpty()) {
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
                    call: Call<ModelResponseDaftarProduk>,
                    t: Throwable
                ) {
                    isShowLoading.value = false
                    message.value = t.message
                }
            })
    }

    private fun onClickItemProduk(item: ModelProduk){
        val bundle = Bundle()
        val fragmentTujuan = DetailProdukPelangganFragment()
        bundle.putParcelable(Constant.reffProduk, item)
        fragmentTujuan.arguments = bundle
        navController.navigate(R.id.detailProdukPelangganFragment, bundle)
    }

    private fun onClickItemProdukFavorit(item: ModelProduk, position: Int){
        val username = savedData.getDataPelanggan()?.username
        if (username.isNullOrEmpty()){
            status.value = "Maaf, Anda harus login terlebih dahulu"
            val navOption = NavOptions.Builder().setPopUpTo(R.id.pelangganFragment, true).build()
            navController.navigate(R.id.pelangganFragment, null, navOption)
            savedData.setDataBoolean(true, Constant.login)
        }
        else{
            deleteProdukFavorit(item, username, position)
        }
    }

    private fun deleteProdukFavorit(item: ModelProduk, username: String, position: Int){
        isShowLoading.value = true

        RetrofitUtils.deleteProdukFav(item.id, username,
            object : Callback<ModelResponse> {
                override fun onResponse(
                    call: Call<ModelResponse>,
                    response: Response<ModelResponse>
                ) {
                    isShowLoading.value = false
                    val result = response.body()

                    if (result?.message == Constant.reffSuccess) {
                        listProduk.removeAt(position)
                        adapterProduk.notifyItemRemoved(position)
                    } else {
                        status.value = result?.message
                    }
                }

                override fun onFailure(
                    call: Call<ModelResponse>,
                    t: Throwable
                ) {
                    isShowLoading.value = false
                    status.value = t.message
                }
            })
    }
}