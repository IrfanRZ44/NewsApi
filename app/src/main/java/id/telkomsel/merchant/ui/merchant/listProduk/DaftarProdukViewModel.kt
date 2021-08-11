package id.telkomsel.merchant.ui.merchant.listProduk

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.santalu.autoviewpager.AutoViewPager
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseViewModel
import id.telkomsel.merchant.listener.ListenerFotoIklan
import id.telkomsel.merchant.model.ModelFotoIklan
import id.telkomsel.merchant.model.ModelKategori
import id.telkomsel.merchant.model.ModelProduk
import id.telkomsel.merchant.model.response.ModelResponse
import id.telkomsel.merchant.model.response.ModelResponseDaftarFotoIklan
import id.telkomsel.merchant.model.response.ModelResponseDaftarKategori
import id.telkomsel.merchant.model.response.ModelResponseDaftarProduk
import id.telkomsel.merchant.ui.merchant.detailProduk.DetailProdukAdminFragment
import id.telkomsel.merchant.utils.Constant
import id.telkomsel.merchant.utils.DataSave
import id.telkomsel.merchant.utils.RetrofitUtils
import id.telkomsel.merchant.utils.adapter.dismissKeyboard
import id.telkomsel.merchant.utils.adapter.getDate
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("StaticFieldLeak")
class DaftarProdukViewModel(
    private val navController: NavController,
    private val activity: Activity?,
    private val context: Context?,
    private val rcKategori: RecyclerView,
    private val rcProduk: RecyclerView,
    private val statusRequest: String,
    private val stok: Int,
    private val isKadaluarsa: Boolean,
    private val savedData: DataSave,
    private val viewPager: AutoViewPager,
    private val dotsIndicator: DotsIndicator,
    private val listener: ListenerFotoIklan
) : BaseViewModel() {
    private val listKategori = ArrayList<ModelKategori>()
    val listProduk = ArrayList<ModelProduk>()
    lateinit var groupAdapter: GroupAdapter<ViewHolder>
    private lateinit var adapterKategori: AdapterKategori
    lateinit var adapterProduk: AdapterProduk
    var isSearching = false
    var startPage = 0
    private lateinit var rcAllKategori: RecyclerView
    lateinit var textMessage: AppCompatTextView
    private lateinit var btnBatal: AppCompatButton
    private var idSubKategori = 0
    lateinit var btmSheet : BottomSheetDialog
    var textSearch = ""
    private val listGambar = ArrayList<ModelFotoIklan>()
    private lateinit var adapterFotoIklan: AdapterFotoIklan

    fun initHeader(cardHeader: RelativeLayout){
        val level = savedData.getDataMerchant()?.level
        if (!level.isNullOrEmpty() && level == Constant.levelChannel){
            cardHeader.visibility = View.VISIBLE
            initAdapterFoto(null)
        }
        else{
            cardHeader.visibility = View.GONE
        }
    }

    private fun initAdapterFoto(gambar: List<ModelFotoIklan>?) {
        val ctx = context

        listGambar.clear()
        listGambar.add(ModelFotoIklan(0, ""))
        listGambar.add(ModelFotoIklan(0, ""))
        listGambar.add(ModelFotoIklan(0, ""))
        listGambar.add(ModelFotoIklan(0, ""))
        listGambar.add(ModelFotoIklan(0, ""))
        listGambar.add(ModelFotoIklan(0, ""))
        listGambar.add(ModelFotoIklan(0, ""))
        listGambar.add(ModelFotoIklan(0, ""))
        listGambar.add(ModelFotoIklan(0, ""))
        listGambar.add(ModelFotoIklan(0, ""))
        listGambar.add(ModelFotoIklan(0, ""))
        listGambar.add(ModelFotoIklan(0, ""))
        listGambar.add(ModelFotoIklan(0, ""))
        listGambar.add(ModelFotoIklan(0, ""))

        if (gambar != null){
            for (i in gambar.indices){
                if (i < 5){
                    listGambar[i] = gambar[i]
                }
            }
        }

        if (ctx != null){
            adapterFotoIklan = AdapterFotoIklan(
                ctx, listGambar, listener
            )
            viewPager.offscreenPageLimit = 0
            viewPager.adapter = adapterFotoIklan
            dotsIndicator.setViewPager(viewPager)
        }
    }
    fun initAdapterKategori() {
        rcKategori.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        adapterKategori = AdapterKategori(
            listKategori
        ) { item: ModelKategori -> onClickItemKategori(item) }
        rcKategori.adapter = adapterKategori
    }

    private fun initAdapterAllKategori() {
        groupAdapter = GroupAdapter<ViewHolder>().apply {
            spanCount = 3
        }

        rcAllKategori.setHasFixedSize(true)
        rcAllKategori.apply {
            layoutManager = GridLayoutManager(btmSheet.context, groupAdapter.spanCount).apply {
                spanSizeLookup = groupAdapter.spanSizeLookup
            }
            adapter = groupAdapter
        }
    }

    fun initAdapterProduk() {
        val layoutManager = GridLayoutManager(activity, 2)
        rcProduk.layoutManager = layoutManager
        adapterProduk = AdapterProduk(
            listProduk
        ) { item: ModelProduk -> onClickItemProduk(item) }
        rcProduk.adapter = adapterProduk
    }

    @SuppressLint("InflateParams")
    fun showDialogFilter(root: View, layoutInflater: LayoutInflater){
        btmSheet = BottomSheetDialog(root.context)
        val bottomView = layoutInflater.inflate(R.layout.behavior_filter_kategori,null)


        btmSheet.setContentView(bottomView)
        btmSheet.setCanceledOnTouchOutside(false)
        btmSheet.setCancelable(false)
        btmSheet.behavior.isDraggable = false
        btmSheet.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        rcAllKategori = bottomView.findViewById(R.id.rcAllKategori)
        textMessage = bottomView.findViewById(R.id.textStatus)
        btnBatal = bottomView.findViewById(R.id.btnBatal)

        getDataAllKategori()

        btnBatal.setOnClickListener {
            btmSheet.dismiss()
            activity?.let { it1 -> dismissKeyboard(it1) }
        }
    }

    fun checkCluster() {
        val cluster = savedData.getDataMerchant()?.cluster
        val merchantId = savedData.getDataMerchant()?.id
        val regional = savedData.getDataMerchant()?.regional
        val level = savedData.getDataMerchant()?.level

        if (level == Constant.levelChannel && !regional.isNullOrEmpty()){
            getDaftarProdukByAdmin(
                regional,
                Constant.levelChannel,
                if (idSubKategori == 0) "" else idSubKategori.toString(),
                ""
            )
        }
        else if ((level == Constant.levelCSO || level == Constant.levelSBP) && !cluster.isNullOrEmpty()){
            getDaftarProdukByAdmin(
                cluster,
                level,
                if (idSubKategori == 0) "" else idSubKategori.toString(),
                ""
            )
        }
        else if (level == Constant.levelMerchant && merchantId != null){
            getDaftarProdukByAdmin(
                "",
                Constant.levelMerchant,
                if (idSubKategori == 0) "" else idSubKategori.toString(),
                merchantId.toString()
            )
        }
        else{
            message.value = "Error, gagal mendapatkan data produk"
        }
    }

    private fun getDataAllKategori() {
        initAdapterAllKategori()

        RetrofitUtils.getDaftarSubKategoriFilterKategori(
            object : Callback<ModelResponseDaftarKategori> {
                override fun onResponse(
                    call: Call<ModelResponseDaftarKategori>,
                    response: Response<ModelResponseDaftarKategori>
                ) {
                    val result = response.body()

                    if (result?.message == Constant.reffSuccess) {
                        var currentIdKategori = 0
                        val tempList = ArrayList<ModelKategori>()

                        for (i in result.data.indices){
                            if (currentIdKategori != result.data[i].kategori_id){
                                if (tempList.size > 0){

                                    ExpandableGroup(AdapterHeaderKategori(result.data[i-1].nama_kategori), true).apply {
                                        for (j in tempList.indices){
                                            add(Section(MutableList(1){
                                                AdapterAllKategori(tempList[j]) { item: ModelKategori ->
                                                        onClickItemAllKategori(
                                                            item
                                                        )
                                                    }
                                                }
                                            ))
                                        }
                                        groupAdapter.add(this)
                                    }
                                }
                                tempList.clear()
                                currentIdKategori = result.data[i].kategori_id
                            }
                            tempList.add(result.data[i])
                        }
                    } else {
                        textMessage.text = result?.message
                    }
                }

                override fun onFailure(
                    call: Call<ModelResponseDaftarKategori>,
                    t: Throwable
                ) {
                    textMessage.text = t.message
                }
            })
    }

    fun getDataKategori() {
        listKategori.clear()
        isShowLoading.value = true
        listKategori.add(
            ModelKategori(
                0, 0, "Semua",
                true, getDate(Constant.dateFormat1), getDate(Constant.dateFormat1)
            )
        )
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

                    if (result?.message == Constant.reffSuccess) {
                        listKategori.addAll(result.data)
                        adapterKategori.notifyDataSetChanged()

                        if (listKategori.size == 0) {
                            rcKategori.visibility = View.GONE
                        } else {
                            rcKategori.visibility = View.VISIBLE
                        }
                    } else {
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

    private fun getDaftarProdukByAdmin(
        cluster: String,
        level: String,
        sub_kategori_id: String?,
        merchantId: String?,
    ) {
        isShowLoading.value = true

        RetrofitUtils.getDaftarProdukByMerchant(merchantId, cluster,
            level,
            startPage,
            statusRequest,
            textSearch,
            sub_kategori_id, stok, isKadaluarsa,
            object : Callback<ModelResponseDaftarProduk> {
                override fun onResponse(
                    call: Call<ModelResponseDaftarProduk>,
                    response: Response<ModelResponseDaftarProduk>
                ) {
                    isShowLoading.value = false
                    isSearching = false
                    val result = response.body()

                    if (result?.message == Constant.reffSuccess) {
                        listProduk.addAll(result.data)
                        adapterProduk.notifyDataSetChanged()

                        startPage += 25
                        if (listProduk.size == 0) {
                            if (textSearch.isEmpty()) {
                                if (statusRequest == Constant.statusRequest) {
                                    message.value = Constant.noProdukRequest
                                } else {
                                    message.value = Constant.noProduk
                                }
                            } else {
                                message.value = "Maaf, belum ada data produk dengan nama $textSearch"
                            }
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
                    isSearching = false
                    isShowLoading.value = false
                    message.value = t.message
                }
            })
    }

    fun createFotoIklan(url_foto: MultipartBody.Part){
        isShowLoading.value = true

        RetrofitUtils.createFotoIklan(url_foto, object : Callback<ModelResponse> {
                override fun onResponse(
                    call: Call<ModelResponse>,
                    response: Response<ModelResponse>
                ) {
                    isShowLoading.value = false
                    val result = response.body()

                    if (result?.message == Constant.reffSuccess){
                        getDaftarFotoIklan()

                        message.value = "Berhasil mengupload foto iklan"
                    }
                    else{
                        message.value = result?.message
                    }
                }

                override fun onFailure(
                    call: Call<ModelResponse>,
                    t: Throwable
                ) {
                    isShowLoading.value = false
                    message.value = t.message
                }
            })
    }

    fun updateFotoIklan(id: RequestBody,
                         url_foto: MultipartBody.Part){
        isShowLoading.value = true

        RetrofitUtils.updateFotoIklan(id, url_foto,
            object : Callback<ModelResponse> {
                override fun onResponse(
                    call: Call<ModelResponse>,
                    response: Response<ModelResponse>
                ) {
                    isShowLoading.value = false
                    val result = response.body()

                    if (result?.message == Constant.reffSuccess){
                        getDaftarFotoIklan()

                        message.value = "Berhasil mengupload foto iklan"
                    }
                    else{
                        message.value = result?.message
                    }
                }

                override fun onFailure(
                    call: Call<ModelResponse>,
                    t: Throwable
                ) {
                    isShowLoading.value = false
                    message.value = t.message
                }
            })
    }

    fun getDaftarFotoIklan(){
        isShowLoading.value = true

        RetrofitUtils.getDaftarFotoIklan(object : Callback<ModelResponseDaftarFotoIklan> {
                override fun onResponse(
                    call: Call<ModelResponseDaftarFotoIklan>,
                    response: Response<ModelResponseDaftarFotoIklan>
                ) {
                    isShowLoading.value = false
                    val result = response.body()

                    if (result?.message == Constant.reffSuccess){
                        initAdapterFoto(result.data)
                    }
                    else{
                        initAdapterFoto(null)
                    }
                }

                override fun onFailure(
                    call: Call<ModelResponseDaftarFotoIklan>,
                    t: Throwable
                ) {
                    isShowLoading.value = false
                    initAdapterFoto(null)
                }
            })
    }

    private fun onClickItemKategori(item: ModelKategori){
        idSubKategori = item.id
        startPage = 0
        listProduk.clear()
        adapterProduk.notifyDataSetChanged()
        checkCluster()
    }

    private fun onClickItemAllKategori(item: ModelKategori){
        btmSheet.dismiss()
        idSubKategori = item.id
        startPage = 0
        listProduk.clear()
        adapterProduk.notifyDataSetChanged()
        checkCluster()
    }

    private fun onClickItemProduk(item: ModelProduk){
        val bundle = Bundle()
        val fragmentTujuan = DetailProdukAdminFragment()
        bundle.putParcelable(Constant.reffProduk, item)
        fragmentTujuan.arguments = bundle
        navController.navigate(R.id.detailProdukAdminFragment, bundle)
    }
}