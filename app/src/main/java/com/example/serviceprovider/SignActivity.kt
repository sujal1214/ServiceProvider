package com.example.serviceprovider

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.serviceprovider.databinding.ActivitySignBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.signupButton.setOnClickListener {
            registerUser()
        }

        binding.alreadyhavebutton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.googleButton.setOnClickListener {
            Toast.makeText(this,"Google Login Coming Next Step",Toast.LENGTH_SHORT).show()
        }
    }

    private fun registerUser() {

        val name = binding.nameEdit.text.toString().trim()
        val email = binding.emailEdit.text.toString().trim()
        val password = binding.passwordEdit.text.toString().trim()

        if(name.isEmpty()){
            binding.nameEdit.error = "Enter name"
            return
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.emailEdit.error = "Enter valid email"
            return
        }

        if(password.length < 6){
            binding.passwordEdit.error = "Password must be 6+ characters"
            return
        }

        binding.progressBar.visibility = View.VISIBLE

        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->

                if(task.isSuccessful){

                    val uid = auth.currentUser!!.uid

                    val userMap = HashMap<String,Any>()
                    userMap["uid"] = uid
                    userMap["name"] = name
                    userMap["email"] = email

                    db.collection("users")
                        .document(uid)
                        .set(userMap)
                        .addOnSuccessListener {

                            binding.progressBar.visibility = View.GONE

                            Toast.makeText(
                                this,
                                "Account Created",
                                Toast.LENGTH_SHORT
                            ).show()

                            startActivity(Intent(this,MainActivity::class.java))
                            finish()

                        }

                }else{

                    binding.progressBar.visibility = View.GONE

                    Toast.makeText(
                        this,
                        task.exception?.message,
                        Toast.LENGTH_LONG
                    ).show()

                }

            }
    }
}