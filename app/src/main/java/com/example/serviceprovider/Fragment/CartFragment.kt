package com.example.serviceprovider.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.serviceprovider.PayOutActivity
import com.example.serviceprovider.adapter.CartAdapter
import com.example.serviceprovider.databinding.FragmentBookingsBinding
import com.example.serviceprovider.model.BookingItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.jvm.java


class BookingsFragment : Fragment() {
    private lateinit var binding: FragmentBookingsBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private lateinit var serviceNames : MutableList<String>
    private lateinit var servicePrices : MutableList<String>
    private lateinit var serviceDescription : MutableList<String>
    private lateinit var serviceImagesUri : MutableList<String>
    private lateinit var quantity : MutableList<Int>
    private lateinit var serviceFacilities : MutableList<String>
    private lateinit var cartAdapter: CartAdapter

    private lateinit var userId : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookingsBinding.inflate(inflater, container, false)

        //initialize Firebase auth
        auth = FirebaseAuth.getInstance()
        retrieveCartsItems()

        binding.proceedButton.setOnClickListener {
            //get ordered items details before proceeding to check out
            getOrderItemsDetails()
        }



        return binding.root
    }

    fun getOrderItemsDetails() {
        val orderIdReference : DatabaseReference = database.reference.child("users").child(userId).child("BookingItems")

        val serviceName = mutableListOf<String>()
        val servicePrice =  mutableListOf<String>()
        val serviceImage =  mutableListOf<String>()
        val serviceDescription =  mutableListOf<String>()
        val serviceFacility =  mutableListOf<String>()

        //get Items quantities
        val serviceQuantities = cartAdapter.getUpdatedItemsQuantities()

        orderIdReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(serviceSnapshot in snapshot.children){
                    //get the cartItems to respective List
                    val orderItems = serviceSnapshot.getValue(BookingItems::class.java)
                    //add items details into list
                    orderItems?.serviceName?.let{serviceName.add(it)}
                    orderItems?.servicePrice?.let{servicePrice.add(it)}
                    orderItems?.serviceDescription?.let{serviceDescription.add(it)}
                    orderItems?.serviceImage?.let{serviceImage.add(it)}
                    orderItems?.serviceFacility?.let{serviceFacility.add(it)}
                }
                orderNow(serviceName, servicePrice, serviceDescription, serviceImage, serviceFacility, serviceQuantities)
            }

            private fun orderNow(
                serviceName: MutableList<String>,
                servicePrice: MutableList<String>,
                serviceDescription: MutableList<String>,
                serviceImage: MutableList<String>,
                serviceFacility: MutableList<String>,
                serviceQuantities: MutableList<Int>
            ) {
                if(isAdded && context!=null){
                    val intent = Intent(requireContext(), PayOutActivity::class.java)
                    intent.putExtra("ServiceItemName", serviceName as ArrayList<String>)
                    intent.putExtra("ServiceItemPrice", servicePrice as ArrayList<String>)
                    intent.putExtra("ServiceItemImage", serviceImage as ArrayList<String>)
                    intent.putExtra("ServiceItemDescription", serviceDescription as ArrayList<String>)
                    intent.putExtra("ServiceItemFacility", serviceFacility as ArrayList<String>)
                    intent.putExtra("ServiceItemQuantity", serviceQuantities as ArrayList<Int>)
                    startActivity(intent)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Order making failed, please try again", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun retrieveCartsItems() {
        //database reference to firebase
        database = FirebaseDatabase.getInstance()
        userId = auth.currentUser?.uid?:""
        val serviceReference : DatabaseReference = database.reference.child("users").child(userId).child("BookingItems")

        //list to store cart items
        serviceNames = mutableListOf()
        servicePrices = mutableListOf()
        serviceDescription = mutableListOf()
        serviceImagesUri = mutableListOf()
        serviceFacilities = mutableListOf()
        quantity = mutableListOf()

        //fetch data from the database
        serviceReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot : DataSnapshot) {
                for(serviceSnapshot in snapshot.children){
                    //get the cartItems object from the child node
                    val cartItems = serviceSnapshot.getValue(BookingItems::class.java)

                    //add cart items details to the list
                    cartItems?.serviceName?.let { serviceNames.add(it) }
                    cartItems?.servicePrice?.let { servicePrices.add(it) }
                    cartItems?.serviceDescription?.let { serviceDescription.add(it) }
                    cartItems?.serviceImage?.let { serviceImagesUri.add(it) }
                    cartItems?.serviceQuantity?.let { quantity.add(it) }
                    cartItems?.serviceFacility?.let { serviceFacilities.add(it) }
                }

                setAdapter()
            }

            private fun setAdapter() {
                cartAdapter = CartAdapter(requireContext(), serviceNames, servicePrices, serviceDescription, serviceImagesUri, quantity, serviceFacilities)
                binding.cardRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                binding.cardRecyclerView.adapter = cartAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context , "Data not Fetched", Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {

    }
}