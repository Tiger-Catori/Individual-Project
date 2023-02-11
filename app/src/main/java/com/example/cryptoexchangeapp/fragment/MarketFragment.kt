package com.example.cryptoexchangeapp.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.cryptoexchangeapp.R
import com.example.cryptoexchangeapp.adapter.MarketAdapter
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

    private lateinit var list: List<CryptoCurrency>
    private lateinit var adapter: MarketAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // inflate the layout for this fragment
        binding = FragmentMarketBinding.inflate(layoutInflater)

        list = listOf()
        adapter = MarketAdapter(requireContext(), list, "home")
        binding.currencyRecyclerView.adapter = adapter

        lifecycleScope.launch(Dispatchers.IO) {
            val res = ApiUtilities.getInstance().create(ApiInterface::class.java).getMarketData()

            if (res.body() != null) {
                withContext(Dispatchers.Main) {
                    list = res.body()!!.data.cryptoCurrencyList
                    adapter.updateData(list)
                }
            } else {

            }
        }

        searchCoins()

        return binding.root
    }

    private fun searchCoins() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                val searchText = p0.toString().toLowerCase(Locale.getDefault())
                updateRecyclerView(searchText)
            }
        })
    }


    private fun updateRecyclerView(searchText: String) {
        val data = ArrayList<CryptoCurrency>()
        for (item in list) {
            val coinName = item.name.toLowerCase(Locale.getDefault())
            val coinSymbol = item.symbol.toLowerCase(Locale.getDefault())
            if (coinName.contains(searchText) || coinSymbol.contains(searchText)) {
                data.add(item)
            }
        }

        if (data.isNotEmpty()) {
            adapter.updateData(data)
        } else {
            adapter.updateData(list)
        }
    }



}