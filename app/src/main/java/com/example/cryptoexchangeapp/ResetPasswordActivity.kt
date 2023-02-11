package com.example.cryptoexchangeapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.cryptoexchangeapp.databinding.ActivityResetPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.rxbinding2.widget.RxTextView

@SuppressLint("CheckResult")
class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResetPasswordBinding
    private lateinit var auth: FirebaseAuth


    /**
     * This initializes a Reset Password Activity where the user can reset their password
     * by entering their email address. The following tasks are performed in the method:
     * 1. Initialize the ActivityResetPasswordBinding by inflating the layout.
     * 2. Get an instance of FirebaseAuth
     * 3. Validate the email address using the RxTextView library and a regular expression pattern to check if the email format is valid. If it's not valid, show an error message.
     * 4. Handle the reset password button click event and send a password reset email to the user's email address.
     * 5. Handle the back to login button click event to navigate back to the login screen.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
// Inflate
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initFirebase()
        formValidation()
        resetPassword()
        initViews()

    }

    private fun initFirebase() {
        auth = FirebaseAuth.getInstance()
    }

    private fun formValidation() {
        //  Email Validation with RxTextView
        val emailStream = RxTextView.textChanges(binding.etEmail)
            .skipInitialValue()
            .map { email ->
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }
        emailStream.subscribe{
            showValidEmailAlert(it)
        }
    }

    private fun resetPassword() {
        //  Reset Password
        binding.btnResetPw.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this) { reset ->
                    if (reset.isSuccessful) {
                        Intent(this, LoginActivity::class.java).also {
                            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(it)
                            Toast.makeText(this, "Check email for password reset!", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, reset.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun initViews() {
        //  Click
        binding.tvBackLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }


    /**
     * This is used to show an error message and change the appearance
     * of the "Reset Password" button based on the validity of the email
     * entered in the email edit text (binding.etEmail).
     * If the email is not valid (as indicated by the
     * @param isNotValid parameter), the error message "Email not valid!"
     * is displayed next to the email edit text, the "Reset Password" button
     * (binding.btnResetPw) is disabled, and its background color is changed to a
     * darker gray color. If the email is valid, the error
     * message is removed, the "Reset Password" button is enabled, and its
     * background color is changed to the primary color of the app.
     * In this case red.
     */
    private fun showValidEmailAlert(isNotValid: Boolean) {
        binding.etEmail.error = if (isNotValid) "Email not valid!" else null
        binding.btnResetPw.isEnabled = !isNotValid
        binding.btnResetPw.backgroundTintList = ContextCompat.getColorStateList(
            this,
            if (isNotValid) android.R.color.darker_gray else R.color.primary_color
        )
    }

}