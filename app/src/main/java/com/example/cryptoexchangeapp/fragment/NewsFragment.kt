package com.example.cryptoexchangeapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cryptoexchangeapp.HomeActivity
import com.example.cryptoexchangeapp.R
import com.example.cryptoexchangeapp.databinding.FragmentNewsBinding
import com.example.cryptoexchangeapp.databinding.FragmentWalletBinding

/**
 * A simple [Fragment] subclass.
 * Use the [NewsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewsFragment : Fragment() {
    lateinit var binding:FragmentNewsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        binding.imgMenu.setOnClickListener {
            (requireActivity() as HomeActivity).drawerClick()
        }
        return binding.root
    }

}