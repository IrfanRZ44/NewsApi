package id.telkomsel.merchant.ui.merchant.voucher

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseFragmentBind
import id.telkomsel.merchant.databinding.FragmentTabVoucherBinding
import id.telkomsel.merchant.ui.merchant.voucher.daftarVoucher.DaftarVoucherFragment
import id.telkomsel.merchant.utils.Constant
import id.telkomsel.merchant.utils.adapter.SectionsPagerAdapter

class TabVoucherFragment : BaseFragmentBind<FragmentTabVoucherBinding>() {
    private lateinit var viewModel: TabVoucherViewModel
    override fun getLayoutResource(): Int = R.layout.fragment_tab_voucher

    override fun myCodeHere() {
        supportActionBar?.show()
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.title = Constant.voucher
        setHasOptionsMenu(false)

        init()
        floatingAction()
    }

    private fun init() {
        bind.lifecycleOwner = this
        viewModel = TabVoucherViewModel()
        bind.viewModel = viewModel
    }

    @Suppress("DEPRECATION")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewPager(bind.viewPager)
        bind.tabs.setupWithViewPager(bind.viewPager)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Suppress("DEPRECATION")
    private fun setupViewPager(pager: ViewPager) {
        val adapter = SectionsPagerAdapter(childFragmentManager)

        adapter.addFragment(DaftarVoucherFragment(Constant.active), "Terjual")
        adapter.addFragment(DaftarVoucherFragment(Constant.expired), "Terpakai")
        adapter.addFragment(DaftarVoucherFragment(Constant.notused), "Kadaluarsa")

        pager.adapter = adapter
    }

    private fun floatingAction() {
        val rfaContent = RapidFloatingActionContentLabelList(context)
        val item = listOf(
            RFACLabelItem<Int>()
                .setLabel("Tambah Produk")
                .setResId(R.drawable.ic_add_white)
                .setIconNormalColor(0xffd84315.toInt())
                .setIconPressedColor(0xffbf360c.toInt())
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