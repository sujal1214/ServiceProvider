package com.example.serviceprovider.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.serviceprovider.LoginActivity
import com.example.serviceprovider.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private lateinit var nameEdit: EditText
    private lateinit var addressEdit: EditText
    private lateinit var emailEdit: EditText
    private lateinit var phoneEdit: EditText

    private lateinit var editButton: Button
    private lateinit var saveButton: Button
    private lateinit var logoutButton: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        nameEdit = view.findViewById(R.id.nameEdit)
        addressEdit = view.findViewById(R.id.addressEdit)
        emailEdit = view.findViewById(R.id.emailEdit)
        phoneEdit = view.findViewById(R.id.phoneEdit)

        editButton = view.findViewById(R.id.editButton)
        saveButton = view.findViewById(R.id.saveButton)
        logoutButton = view.findViewById(R.id.logoutButton)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        loadProfile()

        editButton.setOnClickListener {
            enableEditing(true)
            Toast.makeText(requireContext(),"Edit mode enabled",Toast.LENGTH_SHORT).show()
        }

        saveButton.setOnClickListener {
            saveProfile()
        }

        logoutButton.setOnClickListener {
            logoutUser()
        }

        return view
    }

    private fun enableEditing(enable: Boolean) {
        nameEdit.isEnabled = enable
        addressEdit.isEnabled = enable
        phoneEdit.isEnabled = enable
    }

    private fun loadProfile() {

        val user = auth.currentUser ?: return

        emailEdit.setText(user.email)

        db.collection("users")
            .document(user.uid)
            .get()
            .addOnSuccessListener {

                if (it.exists()) {
                    nameEdit.setText(it.getString("name"))
                    addressEdit.setText(it.getString("address"))
                    phoneEdit.setText(it.getString("phone"))
                }
            }
    }

    private fun saveProfile() {

        val user = auth.currentUser ?: return

        val userMap = hashMapOf(
            "name" to nameEdit.text.toString(),
            "address" to addressEdit.text.toString(),
            "phone" to phoneEdit.text.toString(),
            "email" to emailEdit.text.toString()
        )

        db.collection("users")
            .document(user.uid)
            .set(userMap)
            .addOnSuccessListener {

                Toast.makeText(
                    requireContext(),
                    "Profile Updated",
                    Toast.LENGTH_SHORT
                ).show()

                enableEditing(false)
            }
    }

    private fun logoutUser() {

        FirebaseAuth.getInstance().signOut()

        startActivity(Intent(requireContext(), LoginActivity::class.java))

        requireActivity().finish()
    }
}