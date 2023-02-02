package com.example.cryptoexchangeapp

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import com.example.cryptoexchangeapp.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val email = intent.getStringExtra("Email")
        val displayName = intent.getStringExtra("Name")

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


}

/*
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
