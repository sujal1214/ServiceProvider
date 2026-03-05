package com.example.serviceprovider

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.serviceprovider.databinding.SessionsBinding
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: SessionsBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = SessionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        checkSession()
    }

    private fun checkSession() {

        val user = auth.currentUser

        if (user != null) {

            // User already logged in
            startActivity(Intent(this, MainActivity::class.java))
            finish()

        } else {

            // User not logged in
            startActivity(Intent(this, SignActivity::class.java))
            finish()
        }
    }
}