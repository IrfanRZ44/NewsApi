package id.telkomsel.merchandise.ui.sales

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import id.telkomsel.merchandise.R
import id.telkomsel.merchandise.utils.Constant
import id.telkomsel.merchandise.databinding.FragmentSalesBinding
import com.google.android.material.tabs.TabLayout
import id.telkomsel.merchandise.base.BaseFragmentBind
import id.telkomsel.merchandise.ui.sales.accountSales.AccountSalesFragment
import id.telkomsel.merchandise.ui.sales.listSales.TabSalesFragment
import id.telkomsel.merchandise.ui.sales.listStore.TabStoreFragment
import id.telkomsel.merchandise.utils.adapter.SectionsPagerAdapter

class SalesFragment : BaseFragmentBind<FragmentSalesBinding>() {
    private lateinit var viewModel: SalesViewModel
    override fun getLayoutResource(): Int = R.layout.fragment_sales

    override fun myCodeHere() {
        supportActionBar?.show()
        supportActionBar?.title = Constant.appName
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        init()
    }

    private fun init() {
        bind.lifecycleOwner = this
        viewModel = SalesViewModel()
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
        if (savedData.getDataSales()?.level != Constant.levelSales){
            viewPagerAdmin(pager)
        }
        else{
            viewPagerSales(pager)
        }

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Suppress("DEPRECATION")
    private fun viewPagerAdmin(pager: ViewPager){
        val adapter = SectionsPagerAdapter(childFragmentManager)
        adapter.addFragment(TabSalesFragment(), Constant.appName)
        adapter.addFragment(TabStoreFragment(), "Store")
        adapter.addFragment(AccountSalesFragment(), Constant.akun)
        supportActionBar?.title = Constant.appName

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
                        supportActionBar?.title = "Store"
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
    private fun viewPagerSales(pager: ViewPager){
        val adapter = SectionsPagerAdapter(childFragmentManager)
        adapter.addFragment(TabStoreFragment(), "Store")
        adapter.addFragment(AccountSalesFragment(), Constant.akun)
        supportActionBar?.title = "Store"

        pager.adapter = adapter

        bind.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                bind.tabs.getTabAt(0)?.icon = resources.getDrawable(R.drawable.ic_trolley_gray)
                bind.tabs.getTabAt(1)?.icon = resources.getDrawable(R.drawable.ic_profile_gray)

                when (tab.position) {
                    0-> {
                        supportActionBar?.title = "Store"
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