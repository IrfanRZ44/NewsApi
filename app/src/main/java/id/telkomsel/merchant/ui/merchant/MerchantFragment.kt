package id.telkomsel.merchant.ui.merchant

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import id.telkomsel.merchant.R
import id.telkomsel.merchant.utils.Constant
import id.telkomsel.merchant.databinding.FragmentMerchantBinding
import com.google.android.material.tabs.TabLayout
import id.telkomsel.merchant.base.BaseFragmentBind
import id.telkomsel.merchant.ui.merchant.accountAdmin.AccountAdminFragment
import id.telkomsel.merchant.ui.merchant.listMerchant.TabMerchantFragment
import id.telkomsel.merchant.ui.merchant.listProduk.TabProdukFragment
import id.telkomsel.merchant.ui.pelanggan.blank1.Blank1Fragment
import id.telkomsel.merchant.utils.adapter.SectionsPagerAdapter

class MerchantFragment : BaseFragmentBind<FragmentMerchantBinding>() {
    private lateinit var viewModel: MerchantViewModel
    override fun getLayoutResource(): Int = R.layout.fragment_merchant

    override fun myCodeHere() {
        supportActionBar?.show()
        supportActionBar?.title = Constant.request
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        init()
    }

    private fun init() {
        bind.lifecycleOwner = this
        viewModel = MerchantViewModel()
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
        if (savedData.getDataMerchant()?.level != Constant.levelMerchant){
            viewPagerAdmin(pager)
        }
        else{
            viewPagerMerchant(pager)
        }

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Suppress("DEPRECATION")
    private fun viewPagerAdmin(pager: ViewPager){
        val adapter = SectionsPagerAdapter(childFragmentManager)
        adapter.addFragment(TabMerchantFragment(), Constant.appName)
        adapter.addFragment(TabProdukFragment(), "Produk")
        adapter.addFragment(AccountAdminFragment(), Constant.akun)

        pager.adapter = adapter

        bind.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                bind.tabs.getTabAt(0)?.icon = resources.getDrawable(R.drawable.ic_multipeople_gray)
                bind.tabs.getTabAt(1)?.icon = resources.getDrawable(R.drawable.ic_trolley_gray)
                bind.tabs.getTabAt(2)?.icon = resources.getDrawable(R.drawable.ic_profile_gray)

                when (tab.position) {
                    0 -> {
                        supportActionBar?.title = Constant.appName
                        bind.tabs.getTabAt(0)?.icon?.setColorFilter(resources.getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN)
                        bind.tabs.getTabAt(1)?.icon?.setColorFilter(resources.getColor(R.color.gray1), PorterDuff.Mode.SRC_IN)
                        bind.tabs.getTabAt(2)?.icon?.setColorFilter(resources.getColor(R.color.gray1), PorterDuff.Mode.SRC_IN)
                    }
                    1-> {
                        supportActionBar?.title = "Produk"
                        bind.tabs.getTabAt(0)?.icon?.setColorFilter(resources.getColor(R.color.gray1), PorterDuff.Mode.SRC_IN)
                        bind.tabs.getTabAt(1)?.icon?.setColorFilter(resources.getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN)
                        bind.tabs.getTabAt(2)?.icon?.setColorFilter(resources.getColor(R.color.gray1), PorterDuff.Mode.SRC_IN)
                    }
                    else -> {
                        supportActionBar?.title = Constant.akun
                        bind.tabs.getTabAt(0)?.icon?.setColorFilter(resources.getColor(R.color.gray1), PorterDuff.Mode.SRC_IN)
                        bind.tabs.getTabAt(1)?.icon?.setColorFilter(resources.getColor(R.color.gray1), PorterDuff.Mode.SRC_IN)
                        bind.tabs.getTabAt(2)?.icon?.setColorFilter(resources.getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Suppress("DEPRECATION")
    private fun viewPagerMerchant(pager: ViewPager){
        val adapter = SectionsPagerAdapter(childFragmentManager)
        adapter.addFragment(Blank1Fragment(), "Produk")
        adapter.addFragment(AccountAdminFragment(), Constant.akun)

        pager.adapter = adapter

        bind.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                bind.tabs.getTabAt(0)?.icon = resources.getDrawable(R.drawable.ic_trolley_gray)
                bind.tabs.getTabAt(1)?.icon = resources.getDrawable(R.drawable.ic_profile_gray)

                when (tab.position) {
                    0-> {
                        supportActionBar?.title = Constant.akun
                        bind.tabs.getTabAt(0)?.icon?.setColorFilter(resources.getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN)
                        bind.tabs.getTabAt(1)?.icon?.setColorFilter(resources.getColor(R.color.gray1), PorterDuff.Mode.SRC_IN)
                    }
                    else -> {
                        supportActionBar?.title = Constant.akun
                        bind.tabs.getTabAt(0)?.icon?.setColorFilter(resources.getColor(R.color.gray1), PorterDuff.Mode.SRC_IN)
                        bind.tabs.getTabAt(1)?.icon?.setColorFilter(resources.getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }
}