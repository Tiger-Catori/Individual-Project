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
     * The setTabLayout method sets up a tab layout and a view pager to display the content in tabs.
     */
    private fun updateTabView(tabView:Int) {
        if(lastSelected!=tabView){
            when(tabView){
                0->{
                    lastSelected=0
                    setTopGainsList()

                    binding.btnTopGains.setBackgroundResource(R.drawable.tab_bg_selected)
                    binding.btnLosses.setBackgroundResource(R.drawable.tab_bg_unselected)
                }
                1->{
                    setTopLosesList()
                    lastSelected=1
                    binding.btnTopGains.setBackgroundResource(R.drawable.tab_bg_unselected)
                    binding.btnLosses.setBackgroundResource(R.drawable.tab_bg_selected)
                }
            }
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
    private fun setTopGainsList() {
                val tempList:ArrayList<CryptoCurrency> = arrayListOf()
                    list.forEachIndexed { index, cryptoCurrency ->
                        if(cryptoCurrency.quotes.size>0){
                            if (cryptoCurrency.quotes[0].percentChange24h!=null && cryptoCurrency.quotes[0].percentChange24h>0){
                                tempList.add(cryptoCurrency)
                            }
                        }
                    }
        binding.progressBar.visibility=View.GONE
               tempList.sortBy{ it.quotes[0].percentChange24h}
                    mAdapter?.updateList(tempList)
                }

private fun setTopLosesList() {
    val tempList:ArrayList<CryptoCurrency> = arrayListOf()
    list.forEachIndexed { index, cryptoCurrency ->
        if(cryptoCurrency.quotes.size>0){
            if (cryptoCurrency.quotes[0].percentChange24h!=null && cryptoCurrency.quotes[0].percentChange24h<0){
                tempList.add(cryptoCurrency)
            }
        }
    }
    binding.progressBar.visibility=View.GONE
    tempList.sortByDescending{ it.quotes[0].percentChange24h}
    mAdapter?.updateList(tempList)
}


    fun fetchData(){
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

    override fun onItemClickListner(item: CryptoCurrency) {
        val coin= DataHelper.listOfCoins().find { it.symbol==item.symbol }
        coin?.let { coinsID ->
            // Create a NavDirections object with the necessary arguments
            val coinITem=item
            coinITem.coinUUID=coinsID.uuid
           DetailsActivity.dataItem=coinITem
            val intent=Intent(requireActivity(),DetailsActivity::class.java)
            startActivity(intent)
        }

    }

}