package com.example.serviceprovider

import android.os.Bundle
import android.renderscript.ScriptGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.serviceprovider.databinding.ActivityDetails2Binding

class DetailsActivity2 : AppCompatActivity() {
    private lateinit var binding : ActivityDetails2Binding

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

        val ServiceName = intent.getStringExtra("ServiceName")
        val ServiceImage = intent.getIntExtra("ServiceImage",0)
        binding.detailServiceName.text = ServiceName
        binding.DetailServiceImage.setImageResource(ServiceImage)

        binding.imageButton2.setOnClickListener {
            finish()
        }
    }
}
