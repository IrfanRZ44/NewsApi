package id.telkomsel.merchant.ui.admin

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import id.telkomsel.merchant.R
import id.telkomsel.merchant.utils.Constant
import com.google.android.material.tabs.TabLayout
import id.telkomsel.merchant.base.BaseFragmentBind
import id.telkomsel.merchant.databinding.FragmentAdminBinding
import id.telkomsel.merchant.ui.admin.accountAdmin.AccountAdminFragment
import id.telkomsel.merchant.ui.main.blank1.Blank1Fragment
import id.telkomsel.merchant.utils.adapter.SectionsPagerAdapter

class AdminFragment : BaseFragmentBind<FragmentAdminBinding>() {
    private lateinit var viewModel: AdminViewModel
    override fun getLayoutResource(): Int = R.layout.fragment_admin

    override fun myCodeHere() {
        supportActionBar?.show()
        init()
    }

    private fun init() {
        bind.lifecycleOwner = this
        viewModel = AdminViewModel()
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
        adapter.addFragment(Blank1Fragment(), "Merchant")
        adapter.addFragment(Blank1Fragment(), "Produk")
        adapter.addFragment(AccountAdminFragment(), Constant.akun)

        pager.adapter = adapter

        bind.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                bind.tabs.getTabAt(0)?.icon = resources.getDrawable(R.drawable.ic_multipeople_gray)
                bind.tabs.getTabAt(1)?.icon = resources.getDrawable(R.drawable.ic_trolley_gray)
                bind.tabs.getTabAt(2)?.icon = resources.getDrawable(R.drawable.ic_profile_gray)
                supportActionBar?.title = Constant.request
                supportActionBar?.setDisplayHomeAsUpEnabled(false)

                when (tab.position) {
                    0 -> {
                        supportActionBar?.title = Constant.appName
                        bind.tabs.getTabAt(0)?.icon?.setColorFilter(resources.getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN)
                        bind.tabs.getTabAt(1)?.icon?.setColorFilter(resources.getColor(R.color.gray1), PorterDuff.Mode.SRC_IN)
                        bind.tabs.getTabAt(2)?.icon?.setColorFilter(resources.getColor(R.color.gray1), PorterDuff.Mode.SRC_IN)
                    }
                    1-> {
                        supportActionBar?.title = Constant.akun
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
}