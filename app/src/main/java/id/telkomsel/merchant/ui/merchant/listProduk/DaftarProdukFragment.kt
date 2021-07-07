package id.telkomsel.merchant.ui.merchant.listProduk

import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseFragmentBind
import id.telkomsel.merchant.databinding.FragmentDaftarProdukBinding
import id.telkomsel.merchant.utils.Constant

class DaftarProdukFragment(private val statusRequest: String,
                           private val stok: Int,
                           private val isKadaluarsa: Boolean
) : BaseFragmentBind<FragmentDaftarProdukBinding>() {
    override fun getLayoutResource(): Int = R.layout.fragment_daftar_produk
    lateinit var viewModel: DaftarProdukViewModel


    override fun myCodeHere() {
        supportActionBar?.show()
        supportActionBar?.title = "Produk"
        init()
    }

    fun init(){
        bind.lifecycleOwner = this
        viewModel = DaftarProdukViewModel(
            findNavController(), activity, bind.rcKategori, bind.rcProduk,
            statusRequest, stok, isKadaluarsa, savedData
        )
        bind.viewModel = viewModel
        viewModel.initAdapterKategori()
        viewModel.initAdapterProduk()
        viewModel.showDialogFilter(bind.root, layoutInflater)

        viewModel.getDataKategori()
        viewModel.checkCluster("")

        if (savedData.getDataMerchant()?.level == Constant.levelCSO || savedData.getDataMerchant()?.level == Constant.levelSBP){
            bind.rfaLayout.visibility = View.VISIBLE
            floatingAction()
        }
        else{
            bind.rfaLayout.visibility = View.GONE
        }

        bind.swipeRefresh.setOnRefreshListener {
            viewModel.startPage = 0
            viewModel.listProduk.clear()
            viewModel.adapterProduk.notifyDataSetChanged()
            bind.swipeRefresh.isRefreshing = false
            viewModel.checkCluster("")
        }

        bind.rcProduk.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE && viewModel.isShowLoading.value == false) {
                    viewModel.isShowLoading.value = true
                    viewModel.checkCluster("")
                }
            }
        })
    }

    private fun floatingAction() {
        val rfaContent = RapidFloatingActionContentLabelList(context)
        val item = listOf(
            RFACLabelItem<Int>()
                .setLabel("Tambah Produk")
                .setResId(R.drawable.ic_add_white)
                .setIconNormalColor(0xff52af44.toInt())
                .setIconPressedColor(0xff3E8534.toInt())
                .setWrapper(0)

        )

        rfaContent.setItems(item).setIconShadowColor(0xff888888.toInt())

        val rfabHelper = RapidFloatingActionHelper(
            context,
            bind.rfaLayout,
            bind.rfaBtn,
            rfaContent
        ).build()

        rfaContent.setOnRapidFloatingActionContentLabelListListener(object :
            RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener<Any> {
            override fun onRFACItemLabelClick(position: Int, item: RFACLabelItem<Any>?) {
                when(position) {
                    0 -> {
                        findNavController().navigate(R.id.addProdukFragment)
                    }
                }
                rfabHelper.toggleContent()
            }

            override fun onRFACItemIconClick(position: Int, item: RFACLabelItem<Any>?) {
                when(position) {
                    0 -> {
                        findNavController().navigate(R.id.addProdukFragment)
                    }
                }
                rfabHelper.toggleContent()
            }
        })
    }
}