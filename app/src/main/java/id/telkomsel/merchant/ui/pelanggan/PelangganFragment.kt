package id.telkomsel.merchant.ui.pelanggan

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseFragmentBind
import id.telkomsel.merchant.databinding.FragmentPelangganBinding
import id.telkomsel.merchant.ui.pelanggan.account.AccountPelangganFragment
import id.telkomsel.merchant.ui.pelanggan.beranda.BerandaPelangganFragment
import id.telkomsel.merchant.ui.pelanggan.favorit.FavoritProdukFragment
import id.telkomsel.merchant.ui.pelanggan.loginPelanggan.LoginPelangganFragment
import id.telkomsel.merchant.utils.Constant
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
        setupViewPager()
        bind.tabs.setupWithViewPager(bind.viewPager)

        if (savedData.getKeyBoolean(Constant.login)){
            bind.viewPager.currentItem = 1
            savedData.setDataBoolean(false, Constant.login)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Suppress("DEPRECATION")
    private fun setupViewPager() {
        val adapter = SectionsPagerAdapter(childFragmentManager)
        adapter.addFragment(BerandaPelangganFragment(), "Beranda")

        if (savedData.getDataPelanggan()?.username.isNullOrEmpty()){
            adapter.addFragment(LoginPelangganFragment(), Constant.akun)
        }
        else{
            adapter.addFragment(FavoritProdukFragment(), Constant.favorit)
            adapter.addFragment(AccountPelangganFragment(), Constant.akun)
        }

        bind.viewPager.adapter = adapter

        bind.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                bind.tabs.getTabAt(0)?.icon = resources.getDrawable(R.drawable.ic_trolley_gray)
                supportActionBar?.show()
                supportActionBar?.setDisplayHomeAsUpEnabled(false)

                if (savedData.getDataPelanggan()?.username.isNullOrEmpty()){
                    bind.tabs.getTabAt(1)?.icon = resources.getDrawable(R.drawable.ic_profile_gray)
                    setTabNoAccount(tab)
                }
                else{
                    bind.tabs.getTabAt(1)?.icon = resources.getDrawable(R.drawable.ic_favorite_gray)
                    bind.tabs.getTabAt(2)?.icon = resources.getDrawable(R.drawable.ic_profile_gray)
                    setTabLogin(tab)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    @Suppress("DEPRECATION")
    private fun setTabNoAccount(tab: TabLayout.Tab){
        when (tab.position) {
            0 -> {
                supportActionBar?.title = Constant.beranda
                bind.tabs.getTabAt(0)?.icon?.setColorFilter(
                    resources.getColor(R.color.colorPrimary),
                    PorterDuff.Mode.SRC_IN
                )
                bind.tabs.getTabAt(1)?.icon?.setColorFilter(
                    resources.getColor(R.color.gray1),
                    PorterDuff.Mode.SRC_IN
                )
            }
            else -> {
                supportActionBar?.title = "Login"
                bind.tabs.getTabAt(0)?.icon?.setColorFilter(
                    resources.getColor(R.color.gray1),
                    PorterDuff.Mode.SRC_IN
                )
                bind.tabs.getTabAt(1)?.icon?.setColorFilter(
                    resources.getColor(R.color.colorPrimary),
                    PorterDuff.Mode.SRC_IN
                )
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun setTabLogin(tab: TabLayout.Tab){
        when (tab.position) {
            0 -> {
                supportActionBar?.title = Constant.beranda

                bind.tabs.getTabAt(0)?.icon?.setColorFilter(
                    resources.getColor(R.color.colorPrimary),
                    PorterDuff.Mode.SRC_IN
                )
                bind.tabs.getTabAt(1)?.icon?.setColorFilter(
                    resources.getColor(R.color.gray1),
                    PorterDuff.Mode.SRC_IN
                )
                bind.tabs.getTabAt(2)?.icon?.setColorFilter(
                    resources.getColor(R.color.gray1),
                    PorterDuff.Mode.SRC_IN
                )
            }
            1 -> {
                supportActionBar?.title = Constant.produkFavorit

                bind.tabs.getTabAt(0)?.icon?.setColorFilter(
                    resources.getColor(R.color.gray1),
                    PorterDuff.Mode.SRC_IN
                )
                bind.tabs.getTabAt(1)?.icon?.setColorFilter(
                    resources.getColor(R.color.colorPrimary),
                    PorterDuff.Mode.SRC_IN
                )
                bind.tabs.getTabAt(2)?.icon?.setColorFilter(
                    resources.getColor(R.color.gray1),
                    PorterDuff.Mode.SRC_IN
                )
            }
            else -> {
                supportActionBar?.title = "Akun"
                bind.tabs.getTabAt(0)?.icon?.setColorFilter(
                    resources.getColor(R.color.gray1),
                    PorterDuff.Mode.SRC_IN
                )
                bind.tabs.getTabAt(1)?.icon?.setColorFilter(
                    resources.getColor(R.color.gray1),
                    PorterDuff.Mode.SRC_IN
                )
                bind.tabs.getTabAt(2)?.icon?.setColorFilter(
                    resources.getColor(R.color.colorPrimary),
                    PorterDuff.Mode.SRC_IN
                )
            }
        }
    }
}