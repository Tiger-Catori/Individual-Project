package com.example.cryptoexchangeapp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoexchangeapp.R
import com.example.cryptoexchangeapp.adapter.MarketAdapter
import com.example.cryptoexchangeapp.adapter.TopMarketAdapter
import com.example.cryptoexchangeapp.apis.ApiInterface
import com.example.cryptoexchangeapp.apis.ApiUtilities
import com.example.cryptoexchangeapp.databinding.FragmentMarketBinding
import com.example.cryptoexchangeapp.models.CryptoCurrency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 * Use the [MarketFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MarketFragment : Fragment() {

    private lateinit var binding: FragmentMarketBinding

    private val list = mutableListOf<CryptoCurrency>()
    private lateinit var adapter: MarketAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // inflate the layout for this fragment
        binding = FragmentMarketBinding.inflate(layoutInflater)

        adapter = MarketAdapter(requireContext(), list)
        binding.currencyRecyclerView.adapter = adapter

        lifecycleScope.launch {
            val res = withContext(Dispatchers.IO) {
                ApiUtilities.getInstance().create(ApiInterface::class.java).getMarketData()
            }

            if (res.body() != null) {
                withContext(Dispatchers.Main) {
                    list.clear()
                    list.addAll(res.body()!!.data.cryptoCurrencyList)
                    adapter.notifyDataSetChanged()
                }
            }
        }

         searchCoins()
//
//        val recyclerView = binding.currencyRecyclerView
//        recyclerView.layoutManager = LinearLayoutManager(context)
//        val adapter = context?.let { MarketAdapter(it, list) }
//        recyclerView.adapter = adapter

        val recyclerView = binding.currencyRecyclerView
        // Set the layout manager for the RecyclerView
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager

        return binding.root
    }

    private fun searchCoins() {
        binding.etSearch.addTextChangedListener(searchTextWatcher)
    }

    private val searchTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            val searchText = p0.toString().toLowerCase(Locale.getDefault())
            updateRecyclerView(searchText)
        }
    }

    private fun updateRecyclerView(searchText: String) {
        val filteredData = list.filter {
            val coinName = it.name.toLowerCase(Locale.getDefault())
            val coinSymbol = it.symbol.toLowerCase(Locale.getDefault())
            coinName.contains(searchText) || coinSymbol.contains(searchText)
        }

        adapter.updateData(if (filteredData.isNotEmpty()) filteredData else list)
    }


}


