package com.example.serviceprovider

import android.net.Uri
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.serviceprovider.databinding.ActivityDetails2Binding
import com.example.serviceprovider.model.BookingItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DetailsActivity2 : AppCompatActivity() {
    private lateinit var binding : ActivityDetails2Binding
    private var serviceName : String?=null
    private var servicePrice : String?=null
    private var serviceDescription : String?=null
    private var serviceImage : String?=null
    private var serviceFacility : String?=null

    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetails2Binding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //initialize firebase auth
        auth = FirebaseAuth.getInstance()
        serviceName  = intent.getStringExtra("MenuItemName")
        serviceDescription = intent.getStringExtra("MenuItemDescription")
        servicePrice  = intent.getStringExtra("MenuItemPrice")
        serviceFacility = intent.getStringExtra("MenuItemFacility")
        serviceImage = intent.getStringExtra("MenuItemImage")

        with(binding){
            detailServiceName.text = serviceName
            detailDescription.text = serviceDescription
            detailFacility.text = serviceFacility
            Glide.with(this@DetailsActivity2).load(Uri.parse(serviceImage)).into(detailServiceImage)
        }

        binding.imageButton2.setOnClickListener {
            finish()
        }
        binding.addItemButton.setOnClickListener {
            addItemToCart()
        }
    }

    fun addItemToCart() {
        val database = FirebaseDatabase.getInstance().reference
        val userId = auth.currentUser?.uid?:""

        //Create a bookingItem object
        val bookingItem = BookingItems(
            serviceName.toString(),
            servicePrice.toString(),
            serviceDescription.toString(),
            serviceImage.toString(),
            1
        )
        //save data to item to firebase
        database.child("users").child(userId).child("BookingItems").push().setValue(bookingItem).addOnSuccessListener {
            Toast.makeText(this, "Items added into booking successfully", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Item not added", Toast.LENGTH_SHORT).show()
        }
    }
}
