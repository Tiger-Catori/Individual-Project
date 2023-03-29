package com.example.cryptoexchangeapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.hardware.biometrics.BiometricManager
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.cryptoexchangeapp.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins

@SuppressLint("CheckResult")
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleLogin: GoogleSignInClient
    // private lateinit var biometricAuthenticator: BiometricAuthenticator


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)


// Firebase Auth
        initFirebase()

// Validation
        formValidation()

// Views
        initViews()

        setContentView(binding.root)
    }

    private fun initFirebase() {
        auth = FirebaseAuth.getInstance()
    }


    private fun formValidation() {
        // Username Validation
        val usernameStream = RxTextView.textChanges(binding.etEmail)
            .skipInitialValue()
            .map { username ->
                username.isNotBlank()
            }

        // Password Validation
        val passwordStream = RxTextView.textChanges(binding.etPassword)
            .skipInitialValue()
            .map { password ->
                password.isNotBlank()
            }

        // Button Enable True or False
        val validFormStream = Observable.combineLatest(
            usernameStream, passwordStream
        ) { usernameValid, passwordValid ->
            usernameValid && passwordValid
        }

        validFormStream.subscribe { isValid ->
            updateLoginButton(isValid)
        }

        // Show minimal alerts for invalid input fields
        usernameStream.subscribe { isValid ->
            showTextMinimalAlert(!isValid, "Email/Username")
        }

        passwordStream.subscribe { isValid ->
            showTextMinimalAlert(!isValid, "Password")
        }
    }

    private fun updateLoginButton(isValid: Boolean) {
        binding.btnLogin.apply {
            isEnabled = isValid
            backgroundTintList = ContextCompat.getColorStateList(
                this@LoginActivity,
                if (isValid) R.color.primary_color else android.R.color.darker_gray
            )
        }
    }


    private fun initViews() {
        setupLoginButton()
        setupGoogleLoginButton()
        setupRegisterButton()
        setupForgotPasswordButton()
    }

    // Login Button
    private fun setupLoginButton() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            loginUser(email, password)
        }
    }

    // Login With Google Button
    private fun setupGoogleLoginButton() {
        binding.btnLoginGoogle.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.webclient_id))
                .requestEmail()
                .build()
            googleLogin = GoogleSignIn.getClient(this, gso)
            loginGoogle()
        }
    }

    // No account Register Button
    private fun setupRegisterButton() {
        binding.tvHaventAccount.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    // Forgotten Password Button
    private fun setupForgotPasswordButton() {
        binding.tvForgotPw.setOnClickListener {
            startActivity(Intent(this, ResetPasswordActivity::class.java))
        }
    }


    private fun loginGoogle() {
          val signInIntent = googleLogin.signInIntent
          launcher.launch(signInIntent)
      }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
          result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                        manageResults(task)
                    }
      }


    /**
     *  This manages the result of a task. For Google sign in.
     *  This methods takes in
     *  @param task which represents the task to be executed.
     */
    private fun manageResults(task: Task<GoogleSignInAccount>?) {
        if (task?.isSuccessful == true) {
            task.result?.let { account ->
                updateUserInterface(account)
            }
        } else {
            task?.exception?.message?.let { message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }


    /**
     * This is a method that updates the user interface
     * after a successful sign-in with Google.
     * The method takes in a
     * @param GoogleSignInAccount object, which represents the account of the user
     * that has signed in using Google.
     * The method first creates a Credential object using the GoogleAuthProvider
     * and the idToken property of the account object.
     * The Credential object is then used to sign in the user
     * with the signInWithCredential method from the auth object,
     * which is part of the Firebase Authentication API.
     */
    private fun updateUserInterface(account: GoogleSignInAccount) {
        val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credentials).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                startActivity(Intent(this, HomeActivity::class.java))
            } else {
                task.exception?.message?.let { message ->
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



    /**
     * The method showTextMinimalAlert is a function that displays an error message for a text input field.
     * In this case for username and password.
     * The function takes two arguments
     * @param isNotValid A boolean value that indicates whether the input text is invalid or not.
     * @param text A string value that specifies which text input field the error message belongs to
     */
    private fun showTextMinimalAlert(isNotValid: Boolean, fieldName: String) {
        val requiredCharacters = if (fieldName == "Email/Username") 6 else 8
        val errorMessage = "$fieldName must be more than $requiredCharacters characters"
        when (fieldName) {
            "Email/Username" -> binding.etEmail.error = if (isNotValid) errorMessage else null
            "Password" -> binding.etPassword.error = if (isNotValid) errorMessage else null
        }
    }



    /**
     * The loginUser method implements user login functionality.
     * It uses the 'signInWithEmailAndPassword' method from the 'auth'
     * object to attempt to sign in the user with the provided
     * @param email and
     * @param password .
     * The signInWithEmailAndPassword method is part of the Firebase Authentication API.
     *The result of the sign-in attempt is passed to an OnCompleteListener which checks if the
     * sign-in was successful by checking the isSuccessful property of the login object.
     * If the sign-in was successful, the method starts a new task with the HomeActivity
     * and clears the current task by setting the flags property of the Intent object.
     * It also displays a toast message with the text "Login Successful".
     * If the sign-in was not successful, the method displays a toast message with
     * the error message stored in the message property of the exception object of the login object.
     */

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { loginTask ->
            if (loginTask.isSuccessful) {
                val intent = Intent(this, HomeActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
            } else {
                loginTask.exception?.message?.let { message ->
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}

