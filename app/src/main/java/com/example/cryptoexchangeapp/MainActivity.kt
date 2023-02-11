package com.example.cryptoexchangeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.cryptoexchangeapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initFirebase()
        initViews()
    }

    private fun initFirebase() {
        auth = FirebaseAuth.getInstance()
    }

    private fun initViews() {
        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }


    /**
     * The onStart() method is a lifecycle method in the
     * Android framework that is called when the
     * activity becomes visible to the user.
     * In this implementation the method checks if there is a
     * current user in the FirebaseAuth instance (auth). If there is a current user,
     * the method launches the MainActivity and sets flags such that the MainActivity
     * is the only task on the activity stack. This is done to ensure that users are not able
     * to navigate back to the login screen if they have already logged in.
     */
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val mainIntent = Intent(this, MainActivity::class.java)
            mainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(mainIntent)
        }
    }

}