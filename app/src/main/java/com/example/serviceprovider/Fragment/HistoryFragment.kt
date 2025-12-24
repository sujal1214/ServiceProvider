package com.example.serviceprovider.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.serviceprovider.R
import com.example.serviceprovider.adapter.HireAgainAdapter
import com.example.serviceprovider.databinding.FragmentHistoryBinding


class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var hireAgainAdapter: HireAgainAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        setupRecyclerView()
        return binding.root
    }
    private fun setupRecyclerView() {
        val hireAgainServiceName = arrayListOf("Plumber","Carpenter","Electrician","Painter","Mechanic","Gardener")
        val hireAgainServicePrice = arrayListOf("$100","$200","$300","$400","$500","$600")
        val hireAgainServiceImages = arrayListOf(R.drawable.plumber,R.drawable.carpenter,R.drawable.electrician,R.drawable.painter,R.drawable.mechanic,R.drawable.gardner)
        hireAgainAdapter = HireAgainAdapter(hireAgainServiceName,hireAgainServicePrice,hireAgainServiceImages)
        binding.HireAgainRecyclerView.adapter = hireAgainAdapter
        binding.HireAgainRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
    companion object {

    }
}