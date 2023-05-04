package com.example.cryptoexchangeapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.cryptoexchangeapp.HomeActivity
import com.example.cryptoexchangeapp.R
import com.example.cryptoexchangeapp.databinding.FragmentMarketContainerBinding

class MarketContainerFragment : Fragment() {
    lateinit var binding: FragmentMarketContainerBinding
    var mActivity: FragmentActivity? = null
    lateinit var navController:NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMarketContainerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mActivity = activity
        mActivity?.let { fragmentActivity ->
            val navHostFragment = childFragmentManager.findFragmentById(R.id.nav_host_fragment)
                    as NavHostFragment
             navController = navHostFragment.navController
        }

    }

}