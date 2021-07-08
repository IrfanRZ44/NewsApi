package id.telkomsel.merchant.ui.merchant.listProduk

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
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
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
import id.telkomsel.merchant.utils.adapter.dismissKeyboard
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
    private val stok: Int,
    private val isKadaluarsa: Boolean,
    private val savedData: DataSave
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

    fun checkCluster(search: String?) {
        val cluster = savedData.getDataMerchant()?.cluster
        val merchantId = savedData.getDataMerchant()?.id
        val regional = savedData.getDataMerchant()?.regional
        val level = savedData.getDataMerchant()?.level

        if (level == Constant.levelChannel && !regional.isNullOrEmpty()){
            getDaftarProdukByAdmin(
                regional,
                Constant.levelChannel,
                search,
                if (idSubKategori == 0) "" else idSubKategori.toString(),
                ""
            )
        }
        else if ((level == Constant.levelCSO || level == Constant.levelSBP) && !cluster.isNullOrEmpty()){
            getDaftarProdukByAdmin(
                cluster,
                level,
                search,
                if (idSubKategori == 0) "" else idSubKategori.toString(),
                ""
            )
        }
        else if (level == Constant.levelMerchant && merchantId != null){
            getDaftarProdukByAdmin(
                "",
                Constant.levelMerchant,
                search,
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
        search: String?,
        sub_kategori_id: String?,
        merchantId: String?,
    ) {
        isShowLoading.value = true

        RetrofitUtils.getDaftarProdukByMerchant(merchantId, cluster,
            level,
            startPage,
            statusRequest,
            search,
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
                            if (search.isNullOrEmpty()) {
                                if (statusRequest == Constant.statusRequest) {
                                    message.value = Constant.noProdukRequest
                                } else {
                                    message.value = Constant.noProduk
                                }
                            } else {
                                message.value = "Maaf, belum ada data produk dengan nama $search"
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

    private fun onClickItemKategori(item: ModelKategori){
        idSubKategori = item.id
        startPage = 0
        listProduk.clear()
        adapterProduk.notifyDataSetChanged()
        checkCluster("")
    }

    private fun onClickItemAllKategori(item: ModelKategori){
        btmSheet.dismiss()
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