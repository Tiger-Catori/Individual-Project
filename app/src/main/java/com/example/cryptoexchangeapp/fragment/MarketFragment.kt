package com.example.cryptoexchangeapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cryptoexchangeapp.adapter.MarketAdapter
import com.example.cryptoexchangeapp.databinding.FragmentMarketBinding
import com.example.cryptoexchangeapp.models.CryptoCurrency

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
        adapter = MarketAdapter(requireContext(), list as ArrayList<CryptoCurrency>)
        binding.currencyRecyclerView.adapter = adapter

        lifecycle

        return binding.root
    }





}