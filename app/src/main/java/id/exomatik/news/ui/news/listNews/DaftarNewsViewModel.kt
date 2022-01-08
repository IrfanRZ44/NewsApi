package id.exomatik.news.ui.news.listNews

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import id.exomatik.news.R
import id.exomatik.news.base.BaseViewModel
import id.exomatik.news.model.ModelCountry
import id.exomatik.news.model.ModelKategori
import id.exomatik.news.model.ModelNews
import id.exomatik.news.model.response.ModelResponseDaftarNews
import id.exomatik.news.ui.news.detailNews.DetailNewsFragment
import id.exomatik.news.utils.Constant
import id.exomatik.news.utils.RetrofitUtils
import id.exomatik.news.utils.adapter.dismissKeyboard
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("StaticFieldLeak")
class DaftarNewsViewModel(
    private val navController: NavController,
    private val activity: Activity?,
    private val rcKategori: RecyclerView,
    private val rcNews: RecyclerView,
) : BaseViewModel() {
    private val listKategori = ArrayList<ModelKategori>()
    private val listCountry = ArrayList<ModelCountry>()
    val listNews = ArrayList<ModelNews>()
    private lateinit var adapterKategori: AdapterKategoriNews
    lateinit var adapterNews: AdapterNews
    private lateinit var adapterCountry: AdapterCountry
    var startPage = 1
    var idKategori = ""
    var textSearch = ""
    private var textFilter = false
    private var textCountry = "id"
    lateinit var btmSheet : BottomSheetDialog
    private lateinit var textMessage: AppCompatTextView
    private lateinit var btnBatal: AppCompatButton
    private lateinit var rcCountry: RecyclerView

    fun initAdapterKategoriNews() {
        rcKategori.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        listKategori.add(ModelKategori("", true, Constant.allKategori))
        listKategori.add(ModelKategori(Constant.kategori1, false, Constant.namaKategori1))
        listKategori.add(ModelKategori(Constant.kategori2, false, Constant.namaKategori2))
        listKategori.add(ModelKategori(Constant.kategori3, false, Constant.namaKategori3))
        listKategori.add(ModelKategori(Constant.kategori4, false, Constant.namaKategori4))
        listKategori.add(ModelKategori(Constant.kategori5, false, Constant.namaKategori5))
        listKategori.add(ModelKategori(Constant.kategori6, false, Constant.namaKategori6))

        adapterKategori = AdapterKategoriNews(
            listKategori
        ) { item: ModelKategori -> onClickItemKategori(item) }
        rcKategori.adapter = adapterKategori
    }

    fun initAdapterNews() {
        rcNews.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        adapterNews = AdapterNews(
            listNews
        ) { item: ModelNews -> onClickItemSource(item) }
        rcNews.adapter = adapterNews
    }

    private fun initAdapterCountry() {
        rcCountry.layoutManager = GridLayoutManager(activity, 3)

        listCountry.add(ModelCountry("id", true, "Indonesia"))
        listCountry.add(ModelCountry("ar", false, "Argentina"))
        listCountry.add(ModelCountry("au", false, "Australia"))
        listCountry.add(ModelCountry("in", false, "India"))
        listCountry.add(ModelCountry("gb", false, "United Kingdom"))
        listCountry.add(ModelCountry("us", false, "United States"))
        listCountry.add(ModelCountry("sg", false, "Singapore"))
        listCountry.add(ModelCountry("fr", false, "France"))
        listCountry.add(ModelCountry("de", false, "Germany"))
        listCountry.add(ModelCountry("jp", false, "Japan"))

        adapterCountry = AdapterCountry(
            listCountry
        ) { item: ModelCountry -> onClickItemCountry(item) }
        rcCountry.adapter = adapterCountry
    }

    @SuppressLint("InflateParams")
    fun showDialogFilter(root: View, layoutInflater: LayoutInflater){
        btmSheet = BottomSheetDialog(root.context)
        val bottomView = layoutInflater.inflate(R.layout.behavior_filter_search, null)

        btmSheet.setContentView(bottomView)
        btmSheet.setCanceledOnTouchOutside(false)
        btmSheet.setCancelable(false)
        btmSheet.behavior.isDraggable = false
        btmSheet.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        rcCountry = bottomView.findViewById(R.id.rcCountry)
        textMessage = bottomView.findViewById(R.id.textStatus)
        btnBatal = bottomView.findViewById(R.id.btnBatal)

        initAdapterCountry()

        btnBatal.setOnClickListener {
            btmSheet.dismiss()
            activity?.let { it1 -> dismissKeyboard(it1) }
        }
    }

    fun getDaftarNews() {
        isShowLoading.value = true

        val category = if (idKategori.isNotEmpty() && textSearch.isEmpty()){
            "&category=$idKategori"
        }
        else{
            ""
        }

        val search = if (textSearch.isNotEmpty() && textFilter){
            "qInTitle=$textSearch"
        }
        else if (textSearch.isNotEmpty() && !textFilter){
            "qInTitle=$textSearch"
        }
        else{
            ""
        }

        val firstPoin = if (textSearch.isNotEmpty()){
           "everything?"
        }
        else{
           "top-headlines?country=$textCountry"
        }

        RetrofitUtils.getDaftarNews("${firstPoin}${category}${search}&apiKey=${Constant.reffApiKey}&page=$startPage",
            object : Callback<ModelResponseDaftarNews> {
                override fun onResponse(
                    call: Call<ModelResponseDaftarNews>,
                    response: Response<ModelResponseDaftarNews>
                ) {
                    isShowLoading.value = false
                    val result = response.body()

                    if (result?.status == Constant.reffSuccess) {
                        val artikel = result.articles
                        if (!artikel.isNullOrEmpty() && !artikel.isNullOrEmpty()) {
                            listNews.addAll(result.articles)
                            adapterNews.notifyDataSetChanged()

                            startPage += 1
                            message.value = ""
                        } else {
                            if (textSearch.isEmpty()) {
                                if (startPage > 1) {
                                    status.value = "Maaf, sudah tidak ada lagi data"
                                } else {
                                    message.value =
                                        "Maaf, tidak ditemukan daftar berita dengan kategori $idKategori"
                                }
                            } else {
                                message.value =
                                    "Maaf, belum ada data berita dengan judul $textSearch"
                            }
                        }
                    } else {
                        if (textSearch.isEmpty()) {
                            if (startPage > 1) {
                                status.value = "Maaf, sudah tidak ada lagi data"
                            } else {
                                message.value =
                                    "Maaf, tidak ditemukan daftar berita dengan kategori $idKategori"
                            }
                        } else {
                            message.value = "Maaf, belum ada data berita dengan judul $textSearch"
                        }
                    }
                }

                override fun onFailure(
                    call: Call<ModelResponseDaftarNews>,
                    t: Throwable
                ) {
                    isShowLoading.value = false
                    message.value = t.message + " adasd"
                }
            })
    }

    private fun onClickItemKategori(item: ModelKategori){
        idKategori = item.id
        startPage = 1
        listNews.clear()
        adapterNews.notifyDataSetChanged()
        getDaftarNews()
    }

    private fun onClickItemSource(item: ModelNews){
        val bundle = Bundle()
        val fragmentTujuan = DetailNewsFragment()
        bundle.putParcelable(Constant.reffNews, item)
        fragmentTujuan.arguments = bundle
        navController.navigate(R.id.detailNewsFragment, bundle)
    }

    private fun onClickItemCountry(item: ModelCountry){
        textCountry = item.id
        startPage = 1
        btmSheet.dismiss()
        listNews.clear()
        adapterCountry.notifyDataSetChanged()
        getDaftarNews()
    }
}