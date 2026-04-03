package com.example.serviceprovider

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.serviceprovider.adapter.RecentBuyAdapter
import com.example.serviceprovider.databinding.ActivityRecentOrderItemsBinding
import com.example.serviceprovider.model.OrderDetails

class recentOrderItems : AppCompatActivity() {

    private val binding: ActivityRecentOrderItemsBinding by lazy {
        ActivityRecentOrderItemsBinding.inflate(layoutInflater)
    }
    private var allServiceNames: ArrayList<String> = arrayListOf()
    private var allServicePrices: ArrayList<String> = arrayListOf()
    private var allServiceImages: ArrayList<String> = arrayListOf()
    private var allServiceQuantities: ArrayList<Int> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        // Retrieve the list of order details from the intent
        val recentOrderItems = intent.getSerializableExtra("RecentBuyOrderItem") as? ArrayList<OrderDetails>
        
        recentOrderItems?.let { orderDetails ->
            if (orderDetails.isNotEmpty()) {
                // The first item in the list is the most recent order (due to reversal in HistoryFragment)
                val recentOrderItem = orderDetails[0]
                
                // Extract all services that were part of this specific order
                allServiceNames = recentOrderItem.serviceNames as? ArrayList<String> ?: arrayListOf()
                allServicePrices = recentOrderItem.servicePrices as? ArrayList<String> ?: arrayListOf()
                allServiceImages = recentOrderItem.serviceImages as? ArrayList<String> ?: arrayListOf()
                allServiceQuantities = recentOrderItem.serviceQuantities as? ArrayList<Int> ?: arrayListOf()
            }
        }

        setAdapter()
    }

    private fun setAdapter() {
        val rv = binding.recyclerViewRecentBuy
        rv.layoutManager = LinearLayoutManager(this)
        val adapter = RecentBuyAdapter(
            this,
            allServiceNames,
            allServiceImages,
            allServicePrices,
            allServiceQuantities
        )
        rv.adapter = adapter
    }
}
