package com.example.cryptoexchangeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cryptoexchangeapp.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
// import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setUpTabBar()

        auth = FirebaseAuth.getInstance()



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

//    private fun setUpTabBar() {
//        val adapter = TabPageAdapter(this, tabLayout.tabCount)
//        viewPager.adapter = adapter
//
//        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
//            override fun onPageSelected(position: Int) {
//                tabLayout.selectTab(tabLayout.getTabAt(position))
//            }
//        })
//
//        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab) {
//                viewPager.currentItem = tab.position
//            }
//
//            override fun onTabUnselected(tab: Tab?) {
//
//            }
//
//            override fun onTabReselected(tab: Tab?) {
//
//            }
//        })
//    }


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
