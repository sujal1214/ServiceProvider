package com.example.serviceprovider.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.serviceprovider.databinding.BookingItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartAdapter(
    private val context : Context,
    private val cartItems: MutableList<String>,
    private val cartItemPrices: MutableList<String>,
    private var cartImages: MutableList<String>,
    private var cartDescriptions : MutableList<String>,
    private val cartQuantity : MutableList<Int>,
    private var cartFacility : MutableList<String>
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    //initialize firebase
    private val auth = FirebaseAuth.getInstance()

    init{
        val database = FirebaseDatabase.getInstance()
        val userId = auth.currentUser?.uid?:""
        val cartItemNumber = cartItems.size

        Companion.itemQuantities = IntArray(cartItemNumber){1}
        cartItemsReference = database.reference.child("users").child(userId).child("CartItems")

    }
    companion object{
        private var itemQuantities : IntArray = intArrayOf()
        private lateinit var cartItemsReference : DatabaseReference
    }

    private var itemQuantities = IntArray(cartItems.size){1}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
       val binding= BookingItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = cartItems.size

    //get updated quantity
    fun getUpdatedItemsQuantities() : MutableList<Int>{
        val itemQuantity = mutableListOf<Int>()
        itemQuantity.addAll(cartQuantity)
        return itemQuantity
    }

    inner class CartViewHolder(private val binding: BookingItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply{
                val quantity = itemQuantities[position]
                cardServiceName.text = cartItems[position]
                cardServicePrice.text = cartItemPrices[position]
                //load image using Glide
                val uriString = cartImages[position]
                val uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(cardImage)
                cardServiceQuantity.text = quantity.toString()

                minusButton.setOnClickListener {
                   decreaseQuantity(position)
                }
                plusButton.setOnClickListener {
                    increaseQuantity(position)
                }
                deleteButton.setOnClickListener {
                    val itemPosition = adapterPosition
                    if (itemPosition != RecyclerView.NO_POSITION){
                        deleteItem(itemPosition)
                    }
                }
            }
        }

        private fun decreaseQuantity(position: Int) {
            if (itemQuantities[position] > 1){
                itemQuantities[position]--
                cartQuantity[position] = itemQuantities[position]
                binding.cardServiceQuantity.text = itemQuantities[position].toString()
            }
        }

        private fun increaseQuantity(position: Int) {
            if (itemQuantities[position] < 5){
                itemQuantities[position]++
                cartQuantity[position] = itemQuantities[position]
                binding.cardServiceQuantity.text = itemQuantities[position].toString()
            }
        }

        private fun deleteItem(position: Int) {
           val positionRetrive = position
            getUniqueKeyAtPosition(positionRetrive){uniqueKey ->
                if(uniqueKey != null){
                    removeItem(position, uniqueKey)
                }
            }

        }

        private fun removeItem(position: Int, uniqueKey: String) {
            if (uniqueKey != null){
                cartItemsReference.child(uniqueKey).removeValue().addOnSuccessListener {
                    cartItems.removeAt(position)
                    cartImages.removeAt(position)
                    cartDescriptions.removeAt(position)
                    cartQuantity.removeAt(position)
                    cartItemPrices.removeAt(position)
                    cartFacility.removeAt(position)
                    Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show()
                    //update item quantity
                    itemQuantities = itemQuantities.filterIndexed{index, i -> index != position}.toIntArray()
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, cartItems.size)
                }.addOnFailureListener {
                    Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun getUniqueKeyAtPosition(
            positionRetrive: Int,
            OnComplete: (String?) -> Unit
        ) {
            cartItemsReference.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    var uniqueKey: String? = null

                    snapshot.children.forEachIndexed { index, dataSnapshot ->
                        if (index == positionRetrive) {
                             uniqueKey = dataSnapshot.key
                            return@forEachIndexed
                        }
                    }

                    // IMPORTANT: return result via callback
                    OnComplete(uniqueKey)
                }

                override fun onCancelled(error: DatabaseError) {
                    OnComplete(null) // handle failure case
                }
            })
        }

    }
}