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
                    val sortedData = when (position) {
                        0 -> dataItem.sortedBy { it.quotes[0].percentChange24h }.reversed().take(10)
                        else -> dataItem.sortedByDescending { it.quotes[0].percentChange24h }.take(10)
                    }

                    binding.topGainLoseRecyclerView.adapter = MarketAdapter(requireContext(), sortedData, "home")
                }
            }
        }
    }




}