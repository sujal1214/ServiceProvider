package com.example.serviceprovider.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.serviceprovider.databinding.MenuItemBinding

class MenuAdapter(
    private val menuServicesName:MutableList<String>,
    private val menuServicePrice:MutableList<String>,
    private val menuServiceImages:MutableList<Int>
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

    override fun getItemCount(): Int = menuServicesName.size

    inner class MenuViewHolder(
        private val binding: MenuItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                menuServiceName.text = menuServicesName[position]
                menuPrice.text = menuServicePrice[position]
                menuImage.setImageResource(menuServiceImages[position])
            }
        }

    }
}