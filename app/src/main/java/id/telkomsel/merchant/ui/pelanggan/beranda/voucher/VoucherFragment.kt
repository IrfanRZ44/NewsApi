package id.telkomsel.merchant.ui.pelanggan.beranda.voucher

import android.annotation.SuppressLint
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseFragmentBind
import id.telkomsel.merchant.databinding.FragmentVoucherBinding
import id.telkomsel.merchant.ui.pelanggan.beranda.voucher.daftarVoucher.DaftarVoucherFragment
import id.telkomsel.merchant.utils.Constant
import id.telkomsel.merchant.utils.adapter.SectionsPagerAdapter

class VoucherFragment : BaseFragmentBind<FragmentVoucherBinding>() {
    private lateinit var viewModel: VoucherViewModel
    override fun getLayoutResource(): Int = R.layout.fragment_voucher

    override fun myCodeHere() {
        supportActionBar?.title = Constant.voucher
        supportActionBar?.show()

        init()
    }

    private fun init() {
        bind.lifecycleOwner = this
        viewModel = VoucherViewModel()
        bind.viewModel = viewModel
    }

    @Suppress("DEPRECATION")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewPager()
        bind.tabs.setupWithViewPager(bind.viewPager)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Suppress("DEPRECATION")
    private fun setupViewPager() {
        val adapter = SectionsPagerAdapter(childFragmentManager)
        adapter.addFragment(DaftarVoucherFragment(Constant.active), "Aktif")
        adapter.addFragment(DaftarVoucherFragment(Constant.expired), "Kadaluarsa")

        bind.viewPager.adapter = adapter
    }
}