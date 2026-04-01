package com.example.serviceprovider

import android.net.Uri
import android.os.Bundle
import android.renderscript.ScriptGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.serviceprovider.databinding.ActivityDetails2Binding

class DetailsActivity2 : AppCompatActivity() {
    private lateinit var binding : ActivityDetails2Binding
    private var serviceName : String?=null
    private var servicePrice : String?=null
    private var serviceDescription : String?=null
    private var serviceImage : String?=null
    private var serviceFacility : String?=null
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
    }
}
