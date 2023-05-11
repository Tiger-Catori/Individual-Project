package com.example.cryptoexchangeapp.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.cryptoexchangeapp.DetailsActivity
import com.example.cryptoexchangeapp.HomeActivity
import com.example.cryptoexchangeapp.R
import com.example.cryptoexchangeapp.adapter.TopMarketAdapter
import com.example.cryptoexchangeapp.adapter.TradesAdapter
import com.example.cryptoexchangeapp.apis.ApiInterface
import com.example.cryptoexchangeapp.apis.ApiUtilities
import com.example.cryptoexchangeapp.databinding.FragmentHomeBinding
import com.example.cryptoexchangeapp.models.CryptoCurrency
import com.example.cryptoexchangeapp.utils.DataHelper
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class HomeFragment : Fragment(),TopMarketAdapter.ItemClickListner {

    private lateinit var binding: FragmentHomeBinding
    private var list : ArrayList<CryptoCurrency> = arrayListOf()
    var mAdapter:TopMarketAdapter?=null
    var lastSelected=-1
    var isFetching=false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        binding.progressBar.visibility=View.VISIBLE
        val recyclerView = binding.topCurrencyRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        mAdapter = context?.let { TopMarketAdapter(it, list,this) }
        recyclerView.adapter = mAdapter

//        getTopCurrencyList()
//        setTabLayout()
        binding.imgMenu.setOnClickListener {
            (requireActivity() as HomeActivity).drawerClick()
        }
        fetchData()

        binding.btnTopGains.setOnClickListener {
            updateTabView(0)
        }
        binding.btnLosses.setOnClickListener {
            updateTabView(1)
        }
        return binding.root
    }

    /**
     * Updates the tab view by changing the content and appearance of the selected tab.
     * If the currently selected tab is different from the provided tabView, this method
     * updates the lastSelected variable, sets the appropriate content for the new tab,
     * and updates the tab buttons' background resources.
     *
     * @param tabView The index of the tab to be selected (0 for Top Gains, 1 for Top Loses).
     */
    private fun updateTabView(tabView: Int) {
        if (lastSelected != tabView) {
            lastSelected = tabView

            when (tabView) {
                0 -> setTopGainsList()
                1 -> setTopLosesList()
            }

            updateTabButtons()
        }
    }

    /**
     * Updates the background resources of the tab buttons based on the selected tab.
     * If the Top Gains tab (index 0) is selected, the Top Gains button will have the
     * selected background, and the Top Loses button will have the unselected background.
     * If the Top Loses tab (index 1) is selected, the Top Loses button will have the
     * selected background, and the Top Gains button will have the unselected background.
     */
    private fun updateTabButtons() {
        binding.btnTopGains.setBackgroundResource(
            if (lastSelected == 0) R.drawable.tab_bg_selected else R.drawable.tab_bg_unselected
        )
        binding.btnLosses.setBackgroundResource(
            if (lastSelected == 1) R.drawable.tab_bg_selected else R.drawable.tab_bg_unselected
        )
    }






    /**
     * setTopGainsList() function is used to filter and sort a list of crypto
     * currencies based on their 24-hour percent change, and then update the adapter
     * for the top currency RecyclerView with the resulting list of
     * currencies with positive 24-hour percent changes.
     */
    private fun setTopGainsList() {
        val tempList = list.filter {
            it.quotes.isNotEmpty() && it.quotes[0].percentChange24h > 0.0
        }.sortedByDescending { it.quotes[0].percentChange24h }
            .takeWhile { it.quotes[0].percentChange24h > 0.0 }
            .toMutableList()

        binding.progressBar.visibility = View.GONE
        mAdapter?.updateList(tempList)
    }



    /**
     * setTopLosesList() function is responsible
     * for filtering and sorting a list of crypto currencies based
     * on their 24-hour percent change and then updating the adapter
     * for the top currency RecyclerView with the resulting list
     * */
    private fun setTopLosesList() {
        val tempList = list.filter {
            it.quotes.isNotEmpty() && it.quotes[0].percentChange24h < 0.0
        }.toMutableList()

        binding.progressBar.visibility = View.GONE
        tempList.sortBy { it.quotes[0].percentChange24h }
        mAdapter?.updateList(tempList)
    }



    /**
     * Makes an API call to fetch market data for crypto currencies.
     * */
    private fun fetchData(){
        isFetching=true
        binding.progressBar.visibility=View.VISIBLE
        lifecycleScope.launch {
            val res = withContext(Dispatchers.IO) {
                ApiUtilities.getInstance().create(ApiInterface::class.java).getMarketData()
            }
            if (res.body() != null) {
                isFetching=false

                withContext(Dispatchers.Main) {
                    list.clear()
                    list.addAll(res.body()!!.data.cryptoCurrencyList)
                    updateTabView(0)
                    binding.progressBar.visibility=View.GONE
                }
            }else{
                isFetching=false
                binding.progressBar.visibility=View.GONE
            }
        }
    }

    /**
     * Handles the click events for items in the top currency RecyclerView.
     * */
    override fun onItemClickListener(cryptoCurrency: CryptoCurrency) {
        val coin= DataHelper.listOfCoins().find { it.symbol==cryptoCurrency.symbol }
        coin?.let { coinsID ->
            // Create a NavDirections object with the necessary arguments
            val item = cryptoCurrency
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