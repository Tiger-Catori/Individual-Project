package com.example.cryptoexchangeapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cryptoexchangeapp.HomeActivity
import com.example.cryptoexchangeapp.R
import com.example.cryptoexchangeapp.databinding.FragmentHomeBinding
import com.example.cryptoexchangeapp.databinding.FragmentTradesBinding

/**
 * A simple [Fragment] subclass.
 * Use the [TradesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TradesFragment : Fragment() {
    lateinit var binding:FragmentTradesBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTradesBinding.inflate(layoutInflater, container, false)
        binding.imgMenu.setOnClickListener {
            (requireActivity() as HomeActivity).drawerClick()
        }
        return binding.root
    }

}