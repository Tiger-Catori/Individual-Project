package com.example.cryptoexchangeapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        initFirebase()
        formValidation()
        resetPassword()
        initViews()

        setContentView(binding.root)
    }

    private fun initFirebase() {
        auth = FirebaseAuth.getInstance()
    }

    private fun formValidation() {
        // Email validation using RxTextView
        val emailStream = RxTextView.textChanges(binding.etEmail)
            .skipInitialValue()
            .map { email -> !isValidEmail(email) }

        emailStream.subscribe { isInvalid ->
            displayInvalidEmailAlert(isInvalid)
        }
    }

    /**
        This function checks if the given CharSequence is a
        valid email address using Android's built-in email address pattern.
     */
    private fun isValidEmail(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


    /**
     * Function to reset the password of the user in the app
     * using firebase authentication
     */
    private fun resetPassword() {
        binding.btnResetPw.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            if (email.isNotEmpty()) {
                sendResetPasswordEmail(email)
            } else {
                showToast("Please enter your email")
            }
        }
    }

    private fun sendResetPasswordEmail(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener(this) { resetTask ->
                if (resetTask.isSuccessful) {
                    navigateToLoginActivity()
                    showToast("Password reset email sent, please check your inbox.")
                } else {
                    showToast("Error: ${resetTask.exception?.localizedMessage}")
                }
            }
    }

    private fun navigateToLoginActivity() {
        Intent(this, LoginActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(it)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun initViews() {
        // Click listener
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
    private fun displayInvalidEmailAlert(isInvalid: Boolean) {
        binding.etEmail.error = if (isInvalid) "Email not valid!" else null
        updateResetPasswordButtonState(!isInvalid)
    }

    private fun updateResetPasswordButtonState(isEnabled: Boolean) {
        binding.btnResetPw.apply {
            backgroundTintList = ContextCompat.getColorStateList(
                this@ResetPasswordActivity,
                if (!isEnabled) android.R.color.darker_gray else R.color.primary_color
            )
        }
    }


}