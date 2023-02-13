package com.example.cryptoexchangeapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.cryptoexchangeapp.adapter.TabPageAdapter
import com.example.cryptoexchangeapp.databinding.ActivityHomeBinding
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
//import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)

        // setContentView(R.layout.activity_home)


        initFirebase()

        setUpTabBar()

        initViews()

        setContentView(binding.root)
        // recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
//        val recyclerView = findViewById<RecyclerView>(R.id.currencyRecyclerView)
//
//        // Set the layout manager for the RecyclerView
//        val layoutManager = LinearLayoutManager(this)
//        recyclerView.layoutManager = layoutManager

    }

    private fun initFirebase() {
        auth = FirebaseAuth.getInstance()
    }


    private fun initViews() {
        binding.topAppBar.setNavigationOnClickListener {
            binding.drawerLayout.openDrawer(binding.navigationView)
        }


        binding.navigationView.setNavigationItemSelectedListener {
                menuItem -> menuItem.isChecked = true
            binding.drawerLayout.closeDrawer(binding.navigationView)
            true
        }

        binding.btnLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))

        }
    }


    private fun setUpTabBar()
    {
        val adapter = TabPageAdapter(this, binding.tabLayout.tabCount)
        binding.viewPager.adapter = adapter

        binding.viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback()
        {
            override fun onPageSelected(position: Int) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener
        {
            override fun onTabSelected(tab: TabLayout.Tab)
            {
                binding.viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

}




