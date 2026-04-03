package com.example.serviceprovider.Fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.serviceprovider.adapter.HireAgainAdapter
import com.example.serviceprovider.databinding.FragmentHistoryBinding
import com.example.serviceprovider.model.OrderDetails
import com.example.serviceprovider.recentOrderItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var hireAgainAdapter: HireAgainAdapter
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var userId: String
    private var listOfOrderItem: MutableList<OrderDetails> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        // Retrieve and display the user order history
        retrieveBuyHistory()
        binding.recentbuyitem.setOnClickListener {
            seeItemsRecentBuy()
        }
        return binding.root
    }

    private fun retrieveBuyHistory() {
        binding.recentbuyitem.visibility = View.INVISIBLE
        userId = auth.currentUser?.uid ?: ""

        val buyItemReference: DatabaseReference =
            database.reference.child("users").child(userId).child("BuyHistory")
        val shortingQuery = buyItemReference.orderByChild("currentTime")

        shortingQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listOfOrderItem.clear()
                for (buySnapshot in snapshot.children) {
                    val buyHistoryItem = buySnapshot.getValue(OrderDetails::class.java)
                    buyHistoryItem?.let {
                        listOfOrderItem.add(it)
                    }
                }
                listOfOrderItem.reverse()
                if (listOfOrderItem.isNotEmpty()) {
                    setDataInRecentBuyItem()
                    setPreviousBuyItemsRecyclerView()
                }
            }

            private fun setDataInRecentBuyItem() {
                binding.recentbuyitem.visibility = View.VISIBLE
                val recentOrderItem = listOfOrderItem.firstOrNull()
                recentOrderItem?.let {
                    with(binding) {
                        hireAgainServiceName.text = it.serviceNames?.firstOrNull() ?: ""
                        hireAgainServicePrice.text = it.servicePrices?.firstOrNull() ?: ""
                        val image = it.serviceImages?.firstOrNull() ?: ""
                        val uri = Uri.parse(image)
                        Glide.with(requireContext()).load(uri).into(hireAgainServiceImage)

                        val isOrderAccepted = it.orderAccepted
                        if (isOrderAccepted) {
                            orderStatus.setCardBackgroundColor(android.graphics.Color.GREEN)
                        } else {
                            orderStatus.setCardBackgroundColor(android.graphics.Color.RED)
                        }
                    }
                }
            }

            private fun setPreviousBuyItemsRecyclerView() {
                val buyAgainServiceName = mutableListOf<String>()
                val buyAgainServicePrice = mutableListOf<String>()
                val buyAgainServiceImage = mutableListOf<String>()

                for (i in 1 until listOfOrderItem.size) {
                    listOfOrderItem[i].serviceNames?.firstOrNull()
                        ?.let { buyAgainServiceName.add(it) }
                    listOfOrderItem[i].servicePrices?.firstOrNull()
                        ?.let { buyAgainServicePrice.add(it) }
                    listOfOrderItem[i].serviceImages?.firstOrNull()
                        ?.let { buyAgainServiceImage.add(it) }
                }

                val rv = binding.HireAgainRecyclerView
                rv.layoutManager = LinearLayoutManager(requireContext())
                hireAgainAdapter = HireAgainAdapter(
                    buyAgainServiceName,
                    buyAgainServicePrice,
                    buyAgainServiceImage,
                    requireContext()
                )
                rv.adapter = hireAgainAdapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


    private fun seeItemsRecentBuy() {
        listOfOrderItem.firstOrNull()?.let { recentBuy ->
            val intent = Intent(requireContext(), recentOrderItems::class.java)
            intent.putExtra("RecentBuyOrderItem", listOfOrderItem as java.io.Serializable)
            startActivity(intent)
        }


    }
}
