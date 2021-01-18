package com.exomatik.ballighadmin.ui.main

import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.exomatik.ballighadmin.R
import com.exomatik.ballighadmin.base.BaseFragment
import com.exomatik.ballighadmin.ui.general.adapter.SectionsPagerAdapter
import com.exomatik.ballighadmin.ui.main.listLD.ListLDFragment
import com.exomatik.ballighadmin.ui.main.listMB.ListMBFragment
import com.exomatik.ballighadmin.ui.main.listMJ.ListMJFragment
import com.exomatik.ballighadmin.ui.main.pesan.PesanAdminFragment
import com.exomatik.ballighadmin.utils.Constant.ld_valid
import com.exomatik.ballighadmin.utils.Constant.mb_valid
import com.exomatik.ballighadmin.utils.Constant.mj_valid
import com.exomatik.ballighadmin.utils.Constant.referenceChat
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : BaseFragment() {
    override fun getLayoutResource(): Int = R.layout.main_fragment

    override fun myCodeHere() {
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewPager(view_pager)
        tabs.setupWithViewPager(view_pager)
    }

    @Suppress("DEPRECATION")
    private fun setupViewPager(pager: ViewPager) {
        val adapter = SectionsPagerAdapter(childFragmentManager)
        adapter.addFragment(ListLDFragment(), ld_valid)
        adapter.addFragment(ListMJFragment(), mj_valid)
        adapter.addFragment(ListMBFragment(), mb_valid)
        adapter.addFragment(PesanAdminFragment(), referenceChat)
        pager.adapter = adapter
        tabs.getTabAt(0)?.icon = resources.getDrawable(R.drawable.ic_lembaga_dakwah_white)
        tabs.getTabAt(1)?.icon = resources.getDrawable(R.drawable.ic_masjid_gray)
        tabs.getTabAt(2)?.icon = resources.getDrawable(R.drawable.ic_muballigh_akun_gray)
        tabs.getTabAt(3)?.icon = resources.getDrawable(R.drawable.ic_chat_gray)
        pager.currentItem = this.arguments?.getInt("position")?:0

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when {
                    tab.position == 0 -> {
                        tabs.getTabAt(0)?.icon = resources.getDrawable(R.drawable.ic_lembaga_dakwah_white)
                        tabs.getTabAt(1)?.icon = resources.getDrawable(R.drawable.ic_masjid_gray)
                        tabs.getTabAt(2)?.icon = resources.getDrawable(R.drawable.ic_muballigh_akun_gray)
                        tabs.getTabAt(3)?.icon = resources.getDrawable(R.drawable.ic_chat_gray)
                    }
                    tab.position == 1 -> {
                        tabs.getTabAt(0)?.icon = resources.getDrawable(R.drawable.ic_lembaga_dakwah_gray)
                        tabs.getTabAt(1)?.icon = resources.getDrawable(R.drawable.ic_masjid_white)
                        tabs.getTabAt(2)?.icon = resources.getDrawable(R.drawable.ic_muballigh_akun_gray)
                        tabs.getTabAt(3)?.icon = resources.getDrawable(R.drawable.ic_chat_gray)
                    }
                    tab.position == 2 -> {
                        tabs.getTabAt(0)?.icon = resources.getDrawable(R.drawable.ic_lembaga_dakwah_gray)
                        tabs.getTabAt(1)?.icon = resources.getDrawable(R.drawable.ic_masjid_gray)
                        tabs.getTabAt(2)?.icon = resources.getDrawable(R.drawable.ic_muballigh_akun_white)
                        tabs.getTabAt(3)?.icon = resources.getDrawable(R.drawable.ic_chat_gray)
                    }
                    else -> {
                        tabs.getTabAt(0)?.icon = resources.getDrawable(R.drawable.ic_lembaga_dakwah_gray)
                        tabs.getTabAt(1)?.icon = resources.getDrawable(R.drawable.ic_masjid_gray)
                        tabs.getTabAt(2)?.icon = resources.getDrawable(R.drawable.ic_muballigh_akun_gray)
                        tabs.getTabAt(3)?.icon = resources.getDrawable(R.drawable.ic_chat_white)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }


}