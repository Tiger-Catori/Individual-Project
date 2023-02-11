package com.example.cryptoexchangeapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.cryptoexchangeapp.adapter.TopMarketAdapter
import com.example.cryptoexchangeapp.adapter.TradesAdapter
import com.example.cryptoexchangeapp.apis.ApiInterface
import com.example.cryptoexchangeapp.apis.ApiUtilities
import com.example.cryptoexchangeapp.databinding.FragmentHomeBinding
import com.example.cryptoexchangeapp.models.CryptoCurrency
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var list : List<CryptoCurrency>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        val recyclerView = binding.topCurrencyRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = context?.let { TopMarketAdapter(it, list) }
        recyclerView.adapter = adapter

        getTopCurrencyList()
        setTabLayout()

        return binding.root
    }


    /**
     * The setTabLayout method sets up a tab layout and a view pager to display the content in tabs.
     */
    private fun setTabLayout() {
        val adapter = TradesAdapter(this)
        binding.contentViewPager.adapter = adapter

        binding.contentViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setIndicatorVisibility(position)
            }
        })

        TabLayoutMediator(binding.tabLayout, binding.contentViewPager) {
                tab, position ->
            var title = when (position) {
                0 -> "Top Gains"
                1 -> "Top Losses"
                else -> ""
            }
            tab.text = title
        }.attach()
    }

    private fun setIndicatorVisibility(position: Int) {
        if (position == 0) {
            binding.topGainIndicator.visibility = VISIBLE
            binding.topLoseIndicator.visibility = GONE
        } else {
            binding.topGainIndicator.visibility = GONE
            binding.topLoseIndicator.visibility = VISIBLE
        }
    }



    /**
     * The getTopCurrencyList method gets a list of top performing currency data from the API.
     * The method uses the lifecycleScope.launch coroutine builder to make the API call
     * on the IO dispatcher. This ensures that the API call is performed on a
     * background thread to avoid blocking the main thread.
     * Once the API call is complete, the method checks if the response is successful.
     * If the response is successful, it updates the adapter for the binding.topCurrencyRecyclerView
     * RecyclerView with a new TopMarketAdapter and passes the list of crypto currency
     * data to the adapter. If the response is not successful, the method logs an error message.
     */
    private fun getTopCurrencyList() {
        lifecycleScope.launch(Dispatchers.IO) {
            val res = ApiUtilities.getInstance().create(ApiInterface::class.java).getMarketData()

            if (res.isSuccessful) {
                withContext(Dispatchers.Main) {
                    binding.topCurrencyRecyclerView.adapter = TopMarketAdapter(requireContext(), res.body()!!.data.cryptoCurrencyList)
                }

                Log.d("SHUBH", "getTopCurrencyList: ${res.body()!!.data.cryptoCurrencyList}")
            } else {
                Log.e("SHUBH", "getTopCurrencyList: ${res.errorBody()}")
            }
        }
    }
}