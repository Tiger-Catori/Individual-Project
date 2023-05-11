package com.example.cryptoexchangeapp.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import androidx.navigation.fragment.findNavController
import com.example.cryptoexchangeapp.DetailsActivity
import com.example.cryptoexchangeapp.HomeActivity
import com.example.cryptoexchangeapp.utils.DataHelper
// import com.example.cryptoexchangeapp.MarketFragmentDirections
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 * Use the [MarketFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MarketFragment : Fragment(), MarketAdapter.OnItemClickListener {

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
        adapter = MarketAdapter(requireContext(), list, this::onItemClick)
        binding.imgMenu.setOnClickListener {
            (requireActivity() as HomeActivity).drawerClick()
        }
        binding.currencyRecyclerView.adapter = adapter
        binding.progressBar.visibility=View.VISIBLE
        lifecycleScope.launch {
            val res = withContext(Dispatchers.IO) {
                ApiUtilities.getInstance().create(ApiInterface::class.java).getMarketData()
            }
            if (res.body() != null) {
                binding.progressBar.visibility=View.GONE
                withContext(Dispatchers.Main) {
                    list.clear()
                    list.addAll(res.body()!!.data.cryptoCurrencyList)
                    adapter.notifyDataSetChanged()
                }
            }else{
                binding.progressBar.visibility=View.GONE
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

        adapter.updateData(filteredData.ifEmpty { list })
    }


    override fun onItemClick(cryptoCurrency: CryptoCurrency) {
        val coin= DataHelper.listOfCoins().find { it.symbol==cryptoCurrency.symbol }
        coin?.let { coinsID ->
            // Create a NavDirections object with the necessary arguments
            val item=cryptoCurrency
            item.coinUUID=coinsID.uuid
            DetailsActivity.dataItem=item
            val intent=Intent(requireActivity(), DetailsActivity::class.java)
            startActivity(intent)
          /*  val action = MarketFragmentDirections.actionMarketFragmentToDetailsFragment(item)
            // Navigate to the DetailsFragment using the NavController
            findNavController().navigate(action)*/
        }

    }


}


