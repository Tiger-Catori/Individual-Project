package com.example.cryptoexchangeapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.Window
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.cryptoexchangeapp.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.internal.operators.observable.ObservableElementAt

@SuppressLint("CheckResult")
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

// Firebase Auth
        initFirebase()


// Validation
        formValidation()

// Click
        initViews()
    }


    private fun initFirebase() {
        auth = FirebaseAuth.getInstance()
    }


    private fun formValidation() {
        // Fullname Validation
        val nameStream = RxTextView.textChanges(binding.etFullname)
            .skipInitialValue()
            .map { name -> name.isEmpty() }

        // Email Validation
        val emailStream = RxTextView.textChanges(binding.etEmail)
            .skipInitialValue()
            .map { email -> !Patterns.EMAIL_ADDRESS.matcher(email).matches() }

        // Username Validation
        val usernameStream = RxTextView.textChanges(binding.etUsername)
            .skipInitialValue()
            .map { username -> username.length < 6 }

        // Password Validation
        val passwordStream = RxTextView.textChanges(binding.etPassword)
            .skipInitialValue()
            .map { password -> password.length < 8 }

        // Confirm Password Validation
        val passwordConfirmStream = Observable.combineLatest(
            RxTextView.textChanges(binding.etPassword),
            RxTextView.textChanges(binding.etConfirmPassword)
        ) { password, confirmPassword ->
            password.toString() != confirmPassword.toString()
        }

        // Validation Streams
        val validationStreams = listOf(
            nameStream.doOnNext { showNameExistAlert(it) },
            emailStream.doOnNext { showValidEmailAlert(it) },
            usernameStream.doOnNext { showTextMinimalAlert(it, "Username") },
            passwordStream.doOnNext { showTextMinimalAlert(it, "Password") },
            passwordConfirmStream.doOnNext { showPasswordConfirmAlert(it) }
        )

        // Button Enable True or False.
        Observable.combineLatest(validationStreams) {
            !it.any { invalid -> invalid as Boolean }
        }.subscribe { isValid ->
            binding.btnRegister.isEnabled = isValid
            binding.btnRegister.backgroundTintList = if (isValid) {
                ContextCompat.getColorStateList(this, R.color.primary_color)
            } else {
                ContextCompat.getColorStateList(this, android.R.color.darker_gray)
            }
        }
    }



    private fun initViews() {
        binding.btnRegister.setOnClickListener{
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            registerUser(email, password)
        }

        binding.tvHaveAccount.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

// Error Alerts
    private fun showNameExistAlert(isNotValid: Boolean) {
        binding.etFullname.error = if (isNotValid) getString(R.string.error_name_empty) else null
    }


    /**
     * This method takes in
     * @param isNotValid
     * @param text
     * The purpose of the method is to show an error message
     * for either the username or password text field if the
     * input text is not valid, and remove the error message if the input text is valid.
     */
    private fun showTextMinimalAlert(isNotValid: Boolean, text: String) {
        val errorMessage = when (text) {
            "Username" -> if (isNotValid) "$text must be more than 6 characters" else null
            "Password" -> if (isNotValid) "$text must be more than 8 characters" else null
            else -> null
        }

        when (text) {
            "Username" -> binding.etUsername.error = errorMessage
            "Password" -> binding.etPassword.error = errorMessage
        }
    }

    private fun showValidEmailAlert(isNotValid: Boolean) {
        binding.etEmail.error = if (isNotValid) getString(R.string.error_email_not_valid) else null
    }

    private fun showPasswordConfirmAlert(isNotValid: Boolean) {
        binding.etConfirmPassword.error = if (isNotValid) getString(R.string.error_passwords_not_match) else null
    }

    /**
     * The regiserUser is used to create a new user account with,
     * @param email &
     * @param password
     * It uses the createUserWithEmailAndPassword method from the Firebase Authentication
     * (auth) instance. If the account creation is successful,
     * it starts the LoginActivity and shows a toast message with
     * "Registration Successful". If the account creation is not successful,
     * it shows a default message of "Unknown error occurred".
     */
    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, LoginActivity::class.java))
                    Toast.makeText(this, "Registration Successful",Toast.LENGTH_SHORT).show()
                } else {
                    val errorMessage = task.exception?.message ?: "Unknown error occurred"
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }
}