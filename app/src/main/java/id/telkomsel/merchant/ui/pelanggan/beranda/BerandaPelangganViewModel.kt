package id.telkomsel.merchant.ui.pelanggan.beranda

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
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
import id.telkomsel.merchant.model.ModelFotoProduk
import id.telkomsel.merchant.model.ModelKategori
import id.telkomsel.merchant.model.ModelProduk
import id.telkomsel.merchant.model.response.ModelResponse
import id.telkomsel.merchant.model.response.ModelResponseDaftarKategori
import id.telkomsel.merchant.model.response.ModelResponseDaftarProduk
import id.telkomsel.merchant.ui.merchant.listProduk.AdapterAllKategori
import id.telkomsel.merchant.ui.merchant.listProduk.AdapterHeaderKategori
import id.telkomsel.merchant.ui.merchant.listProduk.AdapterKategori
import id.telkomsel.merchant.ui.pelanggan.detailProduk.DetailProdukPelangganFragment
import id.telkomsel.merchant.utils.Constant
import id.telkomsel.merchant.utils.DataSave
import id.telkomsel.merchant.utils.RetrofitUtils
import id.telkomsel.merchant.utils.adapter.*
import id.telkomsel.merchant.utils.listener.ListenerFotoProduk
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("StaticFieldLeak")
class BerandaPelangganViewModel(
    private val navController: NavController,
    private val activity: Activity?,
    private val context: Context?,
    private val rcKategori: RecyclerView,
    private val rcProduk: RecyclerView,
    private val savedData: DataSave,
    private val viewPager: AutoViewPager,
    private val dotsIndicator: DotsIndicator
) : BaseViewModel(), ListenerFotoProduk {
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
    var idSubKategori = 0
    var textSearch = ""
    lateinit var btmSheet : BottomSheetDialog
    val poin = MutableLiveData<String>()
    private val listGambar = ArrayList<ModelFotoProduk>()
    private lateinit var adapterFotoProduk: AdapterFotoProduk

    fun initHeader(cardHeader: CardView){
        val currentPoin = savedData.getDataPelanggan()?.poin
        if (!savedData.getDataPelanggan()?.username.isNullOrEmpty() && currentPoin != null){
            cardHeader.visibility = View.VISIBLE
            poin.value = "${convertNumberWithoutRupiah(currentPoin.toDouble())} Poin"
            initAdapterFoto(null)
        }
        else{
            cardHeader.visibility = View.GONE
        }
    }

    private fun initAdapterFoto(gambar: List<ModelFotoProduk>?) {
        val ctx = context

        listGambar.clear()
        listGambar.add(ModelFotoProduk(0, 0, ""))
        listGambar.add(ModelFotoProduk(0, 0, ""))
        listGambar.add(ModelFotoProduk(0, 0, ""))
        listGambar.add(ModelFotoProduk(0, 0, ""))
        listGambar.add(ModelFotoProduk(0, 0, ""))

        if (gambar != null){
            for (i in gambar.indices){
                if (i < 5){
                    listGambar[i] = gambar[i]
                }
            }
        }

        if (ctx != null){
            adapterFotoProduk = AdapterFotoProduk(
                ctx, listGambar, this
            )
            viewPager.offscreenPageLimit = 0
            viewPager.adapter = adapterFotoProduk
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
            listProduk, { item: ModelProduk -> onClickItemProduk(item) },
            { item: ModelProduk, position: Int -> onClickItemProdukFavorit(item, position) }
        )
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

    fun getDaftarProdukByPelanggan(sub_kategori_id: String?) {
        isShowLoading.value = true

        RetrofitUtils.getDaftarProdukByPelanggan(startPage,
            textSearch,
            sub_kategori_id,
            savedData.getDataPelanggan()?.username?:"",
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
                                message.value = Constant.noProduk
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

    override fun clickFotoProduk(rows: ModelFotoProduk) {
        super.clickFotoProduk(rows)

        onClickFoto(rows.url_foto, navController)
    }

    private fun onClickItemKategori(item: ModelKategori){
        idSubKategori = item.id
        startPage = 0
        listProduk.clear()
        adapterProduk.notifyDataSetChanged()
        getDaftarProdukByPelanggan(
            if (idSubKategori == 0) "" else idSubKategori.toString()
        )
    }

    private fun onClickItemAllKategori(item: ModelKategori){
        btmSheet.dismiss()
        idSubKategori = item.id
        startPage = 0
        listProduk.clear()
        adapterProduk.notifyDataSetChanged()
        getDaftarProdukByPelanggan(
            if (idSubKategori == 0) "" else idSubKategori.toString()
        )
    }

    private fun onClickItemProduk(item: ModelProduk){
        val bundle = Bundle()
        val fragmentTujuan = DetailProdukPelangganFragment()
        bundle.putParcelable(Constant.reffProduk, item)
        fragmentTujuan.arguments = bundle
        navController.navigate(R.id.detailProdukPelangganFragment, bundle)
    }

    private fun onClickItemProdukFavorit(item: ModelProduk, position: Int){
        showLog(item.isFavorite.toString())
        val username = savedData.getDataPelanggan()?.username
        if (username.isNullOrEmpty()){
            status.value = "Maaf, Anda harus login terlebih dahulu"
            val navOption = NavOptions.Builder().setPopUpTo(R.id.pelangganFragment, true).build()
            navController.navigate(R.id.pelangganFragment, null, navOption)
            savedData.setDataBoolean(true, Constant.login)
        }
        else{
            if (item.isFavorite){
                deleteProdukFavorit(item, username, position)
            }
            else{
                createProdukFavorit(item, username, position)
            }
        }
    }

    private fun createProdukFavorit(item: ModelProduk, username: String, position: Int){
        isShowLoading.value = true

        RetrofitUtils.createProdukFav(item.id, username,
            object : Callback<ModelResponse> {
                override fun onResponse(
                    call: Call<ModelResponse>,
                    response: Response<ModelResponse>
                ) {
                    isShowLoading.value = false
                    val result = response.body()

                    if (result?.message == Constant.reffSuccess) {
                        listProduk[position].isFavorite = true
                        adapterProduk.notifyItemChanged(position)
                    } else {
                        status.value = result?.message
                    }
                }

                override fun onFailure(
                    call: Call<ModelResponse>,
                    t: Throwable
                ) {
                    isSearching = false
                    isShowLoading.value = false
                    status.value = t.message
                }
            })
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
                        listProduk[position].isFavorite = false
                        adapterProduk.notifyItemChanged(position)
                    } else {
                        status.value = result?.message
                    }
                }

                override fun onFailure(
                    call: Call<ModelResponse>,
                    t: Throwable
                ) {
                    isSearching = false
                    isShowLoading.value = false
                    status.value = t.message
                }
            })
    }
}