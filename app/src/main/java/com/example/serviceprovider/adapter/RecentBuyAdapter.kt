package com.example.serviceprovider.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.serviceprovider.databinding.RecentBuyItemBinding

class RecentBuyAdapter(
    private var context: Context,
    private var serviceNameList: ArrayList<String>,
    private var serviceImageList: ArrayList<String>,
    private var servicePriceList: ArrayList<String>,
    private var serviceQuantityList: ArrayList<Int>
): RecyclerView.Adapter<RecentBuyAdapter.RecentViewHolder>() {
    override fun onCreateViewHolder(
        p0: ViewGroup,
        p1: Int
    ): RecentViewHolder {
        val binding = RecentBuyItemBinding.inflate(LayoutInflater.from(context), p0, false)
        return RecentViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: RecentViewHolder,
        position: Int
    ) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = serviceNameList.size
    inner class RecentViewHolder(private val binding: RecentBuyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(position: Int) {
                binding.apply{
                    serviceName.text = serviceNameList[position]
                    foodPrice.text = servicePriceList[position]
                    serviceQuantity.text = serviceQuantityList[position].toString()
                    val uriString = serviceImageList[position]
                    val uri = Uri.parse(uriString)
                    Glide.with(context).load(uri).into(serviceImage)
                }
            }
    }
}