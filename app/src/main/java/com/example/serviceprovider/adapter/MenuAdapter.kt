package com.example.serviceprovider.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.example.serviceprovider.DetailsActivity2
import com.example.serviceprovider.databinding.MenuItemBinding

class MenuAdapter(
    private val menuServicesName:MutableList<String>,
    private val menuServicePrice:MutableList<String>,
    private val menuServiceImages:MutableList<Int>,
    private val requireContext: Context
): RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    private val ItemClickListener : View.OnClickListener ?= null

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
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    ItemClickListener.onItemClick(position)
                }
                // setOnClickListner to open details
                val intent = Intent(requireContext, DetailsActivity2::class.java)
                intent .putExtra("ServiceName",menuServicesName.get(position))
                intent .putExtra("ServiceImage",menuServiceImages.get(position))
                requireContext.startActivity(intent)
            }
        }
        fun bind(position: Int) {
            binding.apply {
                menuServiceName.text = menuServicesName[position]
                menuPrice.text = menuServicePrice[position]
                menuImage.setImageResource(menuServiceImages[position])

            }
        }

    }
    private fun View.OnClickListener?.onItemClick(position: Int) {}
}








