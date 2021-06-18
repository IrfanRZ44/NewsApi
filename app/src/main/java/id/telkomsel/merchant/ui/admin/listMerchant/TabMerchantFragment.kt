package id.telkomsel.merchant.ui.admin.listMerchant

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import id.telkomsel.merchant.R
import id.telkomsel.merchant.utils.Constant
import com.google.android.material.tabs.TabLayout
import id.telkomsel.merchant.databinding.FragmentTabMerchantBinding
import id.telkomsel.merchant.base.BaseFragmentBind
import id.telkomsel.merchant.utils.adapter.SectionsPagerAdapter

class TabMerchantFragment : BaseFragmentBind<FragmentTabMerchantBinding>() {
    private lateinit var viewModel: TabMerchantViewModel
    override fun getLayoutResource(): Int = R.layout.fragment_tab_merchant
    private val requestDaftarMerchant = DaftarMerchantFragment(Constant.statusRequest)
    private val activeDaftarMerchant = DaftarMerchantFragment(Constant.statusActive)
    private val declinedDaftarMerchant = DaftarMerchantFragment(Constant.statusDeclined)

    override fun myCodeHere() {
        supportActionBar?.show()
        supportActionBar?.title = Constant.request
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        init()
    }

    private fun init() {
        bind.lifecycleOwner = this
        viewModel = TabMerchantViewModel()
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
        supportActionBar?.title = Constant.appName
        val adapter = SectionsPagerAdapter(childFragmentManager)
        adapter.addFragment(requestDaftarMerchant, Constant.statusDiproses)
        adapter.addFragment(activeDaftarMerchant, Constant.statusDisetujui)
        adapter.addFragment(declinedDaftarMerchant, Constant.statusDitolak)

        pager.adapter = adapter

        bind.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {
                        bind.tabs.getTabAt(0)?.icon?.setColorFilter(resources.getColor(R.color.white), PorterDuff.Mode.SRC_IN)
                        bind.tabs.getTabAt(1)?.icon?.setColorFilter(resources.getColor(R.color.gray1), PorterDuff.Mode.SRC_IN)
                        bind.tabs.getTabAt(2)?.icon?.setColorFilter(resources.getColor(R.color.gray1), PorterDuff.Mode.SRC_IN)
                    }
                    1-> {
                        bind.tabs.getTabAt(0)?.icon?.setColorFilter(resources.getColor(R.color.gray1), PorterDuff.Mode.SRC_IN)
                        bind.tabs.getTabAt(1)?.icon?.setColorFilter(resources.getColor(R.color.white), PorterDuff.Mode.SRC_IN)
                        bind.tabs.getTabAt(2)?.icon?.setColorFilter(resources.getColor(R.color.gray1), PorterDuff.Mode.SRC_IN)
                    }
                    else -> {
                        bind.tabs.getTabAt(0)?.icon?.setColorFilter(resources.getColor(R.color.gray1), PorterDuff.Mode.SRC_IN)
                        bind.tabs.getTabAt(1)?.icon?.setColorFilter(resources.getColor(R.color.gray1), PorterDuff.Mode.SRC_IN)
                        bind.tabs.getTabAt(2)?.icon?.setColorFilter(resources.getColor(R.color.white), PorterDuff.Mode.SRC_IN)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }
}