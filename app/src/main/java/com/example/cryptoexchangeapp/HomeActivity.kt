package com.example.cryptoexchangeapp

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.graphics.Typeface.create
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.cryptoexchangeapp.databinding.ActivityHomeBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.Tab
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpTabBar()

        auth = FirebaseAuth.getInstance()

        val email = intent.getStringExtra("Email")
        val displayName = intent.getStringExtra("Name")
        val toolbar = findViewById<MaterialToolbar>(R.id.top_app_bar)
        val customFont = ResourcesCompat.getFont(this, R.font.montserrat)
        // toolbar.typeface = customFont

        // findViewById<TextView>(R.id.textView).text = email + "\n" + displayName


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
            /*
            Intent(this, LoginActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
                Toast.makeText(this, "Logout Successful", Toast.LENGTH_SHORT).show()
            }

            findViewById<Button>(R.id.btn_logout).setOnClickListener {
                auth.signOut()

            }
            */


        }

    }

    private fun setUpTabBar() {
        val adapter = TabPageAdapter(this, tabLayout.tabCount)
        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })

        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: Tab?) {

            }

            override fun onTabReselected(tab: Tab?) {

            }
        })
    }


}

/*

fun EditText.getTextString(): String {
        this.text.toString()
    }

fun changeToolbarFont(toolbar: Toolbar, context: Activity) {
    for (i in 0 until toolbar.getChildCount()) {
        val view: View = toolbar.getChildAt(i)
        if (view is TextView) {
            val tv: TextView = view as TextView
            if (tv.getText().equals(toolbar.getTitle())) {
                applyFont(tv, context)
                break
            }
        }
    }
}

fun applyFont(tv: TextView, context: Activity) {
    tv.setTypeface(Typeface.createFromAsset(context.getAssets(), "../res/@font/montserrat"))

}

changeToolbarFont(findViewById(R.id.app_bar), this);
*/
