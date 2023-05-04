package com.example.cryptoexchangeapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.cryptoexchangeapp.adapter.TabPageAdapter
import com.example.cryptoexchangeapp.databinding.ActivityHomeBinding
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initFirebase()
        initViews()
        setUpTabBar()
    }

    private fun initFirebase() {
        auth = FirebaseAuth.getInstance()
    }


    private fun initViews() {
        with(binding) {
            topAppBar.setNavigationOnClickListener {
                drawerLayout.openDrawer(navigationView)
            }

            navigationView.setNavigationItemSelectedListener { menuItem ->
                menuItem.isChecked = true
                drawerLayout.closeDrawer(navigationView)
                true
            }

            btnLogout.setOnClickListener {
                auth.signOut()
                startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
            }
        }
    }


    private fun setUpTabBar() {
        val adapter = TabPageAdapter(this, binding.tabLayout.tabCount)
        binding.viewPager.adapter = adapter

        // Synchronize ViewPager2 page changes with TabLayout
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tabLayout.getTabAt(position)?.select()
            }
        })

        // Synchronize TabLayout tab selection with ViewPager2
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

public fun drawerClick(){
    if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
        binding.drawerLayout.closeDrawer(GravityCompat.START)
    }else{
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }

}
}




