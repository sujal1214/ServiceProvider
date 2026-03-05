package com.example.serviceprovider

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.serviceprovider.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var googleSignInClient: GoogleSignInClient

    private val RC_SIGN_IN = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        setupGoogleLogin()
        setupUI()
    }

    private fun setupUI() {

        binding.loginbutton.setOnClickListener {
            loginUser()
        }

        binding.button2.setOnClickListener {
            signInWithGoogle()
        }

        binding.donthavebutton.setOnClickListener {
            startActivity(Intent(this, SignActivity::class.java))
        }
    }

    private fun loginUser() {

        val email = binding.editTextTextEmailAddress2.text.toString().trim()
        val password = binding.editTextTextPassword.text.toString().trim()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.editTextTextEmailAddress2.error = "Enter valid email"
            return
        }

        if (password.length < 6) {
            binding.editTextTextPassword.error = "Password must be 6+ characters"
            return
        }

        binding.progressBar.visibility = View.VISIBLE

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                binding.progressBar.visibility = View.GONE

                if (task.isSuccessful) {

                    Toast.makeText(
                        this,
                        "Login Successful",
                        Toast.LENGTH_SHORT
                    ).show()

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()

                } else {

                    Toast.makeText(
                        this,
                        task.exception?.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    // ---------------- GOOGLE LOGIN ----------------

    private fun setupGoogleLogin() {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun signInWithGoogle() {

        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {

                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)

            } catch (e: Exception) {

                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {

        val credential = GoogleAuthProvider.getCredential(idToken, null)

        binding.progressBar.visibility = View.VISIBLE

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {

                    val user = auth.currentUser

                    val uid = user!!.uid

                    val userMap = hashMapOf(
                        "uid" to uid,
                        "name" to user.displayName,
                        "email" to user.email
                    )

                    db.collection("users")
                        .document(uid)
                        .set(userMap)

                    binding.progressBar.visibility = View.GONE

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()

                } else {

                    binding.progressBar.visibility = View.GONE

                    Toast.makeText(
                        this,
                        "Google login failed",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}