package com.example.serviceprovider.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.serviceprovider.databinding.HireAgainItemBinding

class HireAgainAdapter(
    private val hireAgainService: List<String>,
    private val hireAgainServicePrice: List<String>,
    private val hireAgainServiceImages: List<String>,
    private val context: Context
) : RecyclerView.Adapter<HireAgainAdapter.HireAgainViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HireAgainViewHolder {
        val binding = HireAgainItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HireAgainViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: HireAgainViewHolder,
        position: Int
    ) {
        holder.bind(hireAgainService[position], hireAgainServicePrice[position], hireAgainServiceImages[position])
    }

    override fun getItemCount(): Int = hireAgainService.size

    inner class HireAgainViewHolder(private val binding: HireAgainItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(serviceName: String, servicePrice: String, serviceImage: String) {
            binding.hireAgainServiceName.text = serviceName
            binding.hireAgainServicePrice.text = servicePrice
            val uri = Uri.parse(serviceImage)
            Glide.with(context).load(uri).into(binding.hireAgainServiceImage)
        }
    }
}
