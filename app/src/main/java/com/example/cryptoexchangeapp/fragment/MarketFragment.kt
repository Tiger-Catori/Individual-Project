package com.example.cryptoexchangeapp.fragment

import android.os.Bundle
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
        adapter = MarketAdapter(requireContext(), list)
        binding.currencyRecyclerView.adapter = adapter
        // currencyRecyclerView = findViewById( R.id.currencyRecyclerView );
        // currencyRecyclerView.setAdapter(  );

        lifecycleScope.launch(Dispatchers.IO) {
            val res = ApiUtilities.getInstance().create(ApiInterface::class.java).getMarketData()

            if (res.body() != null) {
                withContext(Dispatchers.Main) {
                    list = res.body()!!.data.cryptoCurrencyList

                    adapter.updateData(list)
                    //binding.spinKitView.visibility = GONE
                }
            }
        }

        return binding.root
    }





}