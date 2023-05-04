package com.example.cryptoexchangeapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cryptoexchangeapp.HomeActivity
import com.example.cryptoexchangeapp.R
import com.example.cryptoexchangeapp.databinding.FragmentHomeBinding
import com.example.cryptoexchangeapp.databinding.FragmentWalletBinding

/**
 * A simple [Fragment] subclass.
 * Use the [WalletFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WalletFragment : Fragment() {
    lateinit var binding:FragmentWalletBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWalletBinding.inflate(layoutInflater, container, false)
        binding.imgMenu.setOnClickListener {
            (requireActivity() as HomeActivity).drawerClick()
        }

        return binding.root
    }

}