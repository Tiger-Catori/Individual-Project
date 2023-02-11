package com.example.cryptoexchangeapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.cryptoexchangeapp.adapter.MarketAdapter
import com.example.cryptoexchangeapp.apis.ApiInterface
import com.example.cryptoexchangeapp.apis.ApiUtilities
import com.example.cryptoexchangeapp.models.CryptoCurrency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.cryptoexchangeapp.databinding.FragmentTradesBinding
import java.util.*
import kotlin.collections.ArrayList

class TopLossGainFragment : Fragment() {

    private lateinit var binding : FragmentTradesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTradesBinding.inflate(layoutInflater)

        getMarketData()

        return binding.root
    }

    /**
     * The getMarketData method retrieves market data from an API
     * and displays it in the form of a list.
     */
    private fun getMarketData() {
        val position = requireArguments().getInt("position")
        lifecycleScope.launch(Dispatchers.IO) {
            val res = ApiUtilities.getInstance().create(ApiInterface::class.java).getMarketData()

            if (res.body() != null) {
                withContext(Dispatchers.Main) {
                    val dataItem = res.body()!!.data.cryptoCurrencyList

                    Collections.sort(dataItem) {
                            o1,o2 -> o2.quotes[0].percentChange24h.toInt()
                        .compareTo(o1.quotes[0].percentChange24h.toInt())
                    }

                    val list = ArrayList<CryptoCurrency>()

                    if (position == 0) {
                        list.clear()
                        for (i in 0..9)
                            list.add(dataItem[i])
                        binding.topGainLoseRecyclerView.adapter = MarketAdapter(requireContext(), list)

                    } else {
                        list.clear()
                        for (i in 0..9)
                            list.add(dataItem[dataItem.size-1-i])
                        binding.topGainLoseRecyclerView.adapter = MarketAdapter(requireContext(), list)
                    }

                }
            }
        }
    }




}