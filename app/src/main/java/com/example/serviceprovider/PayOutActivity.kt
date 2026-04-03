package com.example.serviceprovider

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.serviceprovider.databinding.ActivityPayOutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PayOutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPayOutBinding

    private lateinit var auth : FirebaseAuth
    private lateinit var name : String
    private lateinit var address : String
    private lateinit var phone : String
    private lateinit var totalAmount : String
    private lateinit var serviceItemName : ArrayList<String>
    private lateinit var serviceItemPrice : ArrayList<String>
    private lateinit var serviceItemImage : ArrayList<String>
    private lateinit var serviceItemDescription : ArrayList<String>
    private lateinit var serviceItemFacility : ArrayList<String>
    private lateinit var serviceItemQuantity : ArrayList<Int>
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userId : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPayOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Initializing FFirebase and User Details
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference()

        //set user data
        setUserData()

        //get user data from firebase
        val intent = intent
        serviceItemName = intent.getStringArrayListExtra("ServiceItemName") as ArrayList<String>
        serviceItemPrice = intent.getStringArrayListExtra("ServiceItemPrice") as ArrayList<String>
        serviceItemImage = intent.getStringArrayListExtra("ServiceItemImage") as ArrayList<String>
        serviceItemDescription = intent.getStringArrayListExtra("ServiceItemDescription") as ArrayList<String>
        serviceItemFacility = intent.getStringArrayListExtra("ServiceItemFacility") as ArrayList<String>
        serviceItemQuantity = intent.getIntegerArrayListExtra("ServiceItemQuantity") as ArrayList<Int>

        totalAmount = calculateTotalAmount().toString() + "₹"
        //binding.totalAmount.isEnabled = false
        binding.totalAmount.setText(totalAmount)

        binding.backButtonPayout.setOnClickListener {
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.PlaceMyOrder.setOnClickListener {
            val bottomSheetDialog = CongratsBottomSheet()
            bottomSheetDialog.show(supportFragmentManager, "Test")
        }
    }

    private fun calculateTotalAmount(): Int {
        var totalAmount = 0
        for (i in 0 until  serviceItemPrice.size){
            var price = serviceItemPrice[i]
            val lastChar = price.last()
            val priceIntValue = if(lastChar == '₹'){
                price.dropLast(1).toInt()
            }else{
                price.toInt()
            }
            var quantity = serviceItemQuantity[i]
            totalAmount += priceIntValue * quantity
        }
        return totalAmount
    }

    private fun setUserData() {
        val user = auth.currentUser
        if(user!=null){
            val userId = user.uid
            val userReference = databaseReference.child("users").child(userId)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val names = snapshot.child("name").getValue(String::class.java)?:""
                        val addresses = snapshot.child("address").getValue(String::class.java)?:""
                        val phones = snapshot.child("phone").getValue(String::class.java)?:""
                        binding.apply{
                            name.setText(names)
                            address.setText(addresses)
                            phone.setText(phones)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }
}
