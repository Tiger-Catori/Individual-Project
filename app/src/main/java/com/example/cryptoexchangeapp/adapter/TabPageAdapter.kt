package com.example.cryptoexchangeapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cryptoexchangeapp.fragment.*

class TabPageAdapter(activity: FragmentActivity, private val tabCount: Int) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = tabCount

    private val fragmentMap = mapOf(
        0 to HomeFragment(),
        1 to TradesFragment(),
        2 to MarketFragment(),
        3 to WalletFragment(),
        4 to NewsFragment()
    )

    /**
     *  This is a method that returns a fragment based on the given position.
     *  The method takes an integer called
     *  @param position, which represents the position of the desired fragment.
     */
    override fun createFragment(position: Int): Fragment {
        return fragmentMap[position] ?: HomeFragment()
    }



}