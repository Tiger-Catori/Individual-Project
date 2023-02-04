package com.example.cryptoexchangeapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabPageAdapter(activity: FragmentActivity, private val tabCount: Int) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = tabCount

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> TradesFragment()
            2 -> MarketFragment()
            3 -> WalletFragment()
            4 -> NewsFragment()
            else -> HomeFragment()
        }
    }


}