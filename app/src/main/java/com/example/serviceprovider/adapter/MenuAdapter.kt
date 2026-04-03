package com.example.serviceprovider.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.serviceprovider.DetailsActivity2
import com.example.serviceprovider.databinding.MenuItemBinding
import com.example.serviceprovider.model.MenuItem

class MenuAdapter(
    private val menuItems: List<MenuItem>,
    private val requireContext: Context
): RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MenuViewHolder {
        val binding = MenuItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MenuViewHolder,
        position: Int
    ) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = menuItems.size

    inner class MenuViewHolder(
        private val binding: MenuItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    openDetailsActivity(position)
                }
            }
        }

        //set data into recyclerview items name, price, image
        fun bind(position: Int) {
            val menuItem = menuItems[position]
            binding.apply {
                menuServiceName.text = menuItem.serviceName
                menuPrice.text = menuItem.servicePrice
                val uri = Uri.parse(menuItem.serviceImage)
                Glide.with(requireContext).load(uri).into(menuImage)
            }
        }

    }

    private fun openDetailsActivity(position: Int) {
        val menuItem = menuItems[position]

        //an Intent to open details activity and pass data
        val  intent = Intent(requireContext, DetailsActivity2::class.java).apply{
            putExtra("MenuItemName", menuItem.serviceName)
            putExtra("MenuItemImage", menuItem.serviceImage)
            putExtra("MenuItemDescription", menuItem.serviceDescription)
            putExtra("MenuItemFacility", menuItem.serviceFacility)
            putExtra("MenuItemPrice", menuItem.servicePrice)
        }
        //start the details activity
        requireContext.startActivity(intent)
    }

    private fun View.OnClickListener?.onItemClick(position: Int) {}
}








