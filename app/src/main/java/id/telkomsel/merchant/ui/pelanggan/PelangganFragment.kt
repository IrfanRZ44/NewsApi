package id.telkomsel.merchant.ui.pelanggan

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import id.telkomsel.merchant.R
import id.telkomsel.merchant.utils.Constant
import com.google.android.material.tabs.TabLayout
import id.telkomsel.merchant.base.BaseFragmentBind
import id.telkomsel.merchant.databinding.FragmentPelangganBinding
import id.telkomsel.merchant.ui.pelanggan.account.AccountPelangganFragment
import id.telkomsel.merchant.ui.pelanggan.beranda.BerandaPelangganFragment
import id.telkomsel.merchant.ui.pelanggan.loginPelanggan.LoginPelangganFragment
import id.telkomsel.merchant.utils.adapter.SectionsPagerAdapter

class PelangganFragment : BaseFragmentBind<FragmentPelangganBinding>() {
    private lateinit var viewModel: PelangganViewModel
    override fun getLayoutResource(): Int = R.layout.fragment_pelanggan

    override fun myCodeHere() {
        supportActionBar?.title = Constant.beranda
        supportActionBar?.show()

        init()
    }

    private fun init() {
        bind.lifecycleOwner = this
        viewModel = PelangganViewModel()
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
        adapter.addFragment(BerandaPelangganFragment(), "Beranda")

        if (savedData.getDataPelanggan()?.username.isNullOrEmpty()){
            adapter.addFragment(LoginPelangganFragment(), Constant.akun)
        }
        else{
            adapter.addFragment(AccountPelangganFragment(), Constant.akun)
        }

        pager.adapter = adapter

        bind.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                bind.tabs.getTabAt(0)?.icon = resources.getDrawable(R.drawable.ic_trolley_gray)
                bind.tabs.getTabAt(1)?.icon = resources.getDrawable(R.drawable.ic_profile_gray)
                supportActionBar?.setDisplayHomeAsUpEnabled(false)

                when (tab.position) {
                    0-> {
                        supportActionBar?.setDisplayHomeAsUpEnabled(false)
                        supportActionBar?.title = Constant.beranda
                        supportActionBar?.show()
                        bind.tabs.getTabAt(0)?.icon?.setColorFilter(resources.getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN)
                        bind.tabs.getTabAt(1)?.icon?.setColorFilter(resources.getColor(R.color.gray1), PorterDuff.Mode.SRC_IN)
                    }
                    else -> {
                        supportActionBar?.show()
                        if (savedData.getDataPelanggan()?.username.isNullOrEmpty()){
                            supportActionBar?.title = "Login"
                        }
                        else{
                            supportActionBar?.title = Constant.akun
                        }
                        supportActionBar?.setDisplayHomeAsUpEnabled(false)
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