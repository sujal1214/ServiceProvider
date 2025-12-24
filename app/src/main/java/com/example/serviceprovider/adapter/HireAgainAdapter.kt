package com.example.serviceprovider.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.serviceprovider.databinding.HireAgainItemBinding

class HireAgainAdapter(private val hireAgainService:ArrayList<String>,
                       private val hireAgainServicePrice:ArrayList<String>,
                       private val hireAgainServiceImages:ArrayList<Int>) : RecyclerView.Adapter<HireAgainAdapter.HireAgainViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HireAgainViewHolder {
        val binding = HireAgainItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return HireAgainViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: HireAgainViewHolder,
        position: Int
    ) {
        holder.bind(hireAgainService[position],hireAgainServicePrice[position],hireAgainServiceImages[position])
    }

    override fun getItemCount(): Int = hireAgainService.size
    class HireAgainViewHolder (private val binding: HireAgainItemBinding) : RecyclerView.ViewHolder(binding.root ){
        fun bind(serviceName: String, servicePrice: String, serviceImage: Int) {
            binding.hireAgainServiceName.text = serviceName
            binding.hireAgainServicePrice.text = servicePrice
            binding.hireAgainServiceImage.setImageResource(serviceImage)
        }
    }
}


