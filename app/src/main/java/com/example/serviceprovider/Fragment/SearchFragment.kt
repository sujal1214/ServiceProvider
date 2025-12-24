package com.example.serviceprovider.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.serviceprovider.R
import com.example.serviceprovider.adapter.MenuAdapter
import com.example.serviceprovider.databinding.FragmentSearchBinding


class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter : MenuAdapter
    private val originalMenuServicesName = listOf("Plumber","Carpenter","Electrician","Painter","Mechanic","Gardener")
    private val originalMenuServicePrice = listOf("100","200","300","400","500","600")
    private val originalMenuServiceImages = listOf(R.drawable.plumber,R.drawable.carpenter,R.drawable.electrician,R.drawable.painter,R.drawable.mechanic,R.drawable.gardner)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    private val filteredMenuServiceName = mutableListOf<String>()
    private val filteredMenuServicePrice = mutableListOf<String>()
    private val filteredMenuServiceImages = mutableListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSearchBinding.inflate(inflater,container,false)
        adapter = MenuAdapter(filteredMenuServiceName,filteredMenuServicePrice,filteredMenuServiceImages)
        binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.menuRecyclerView.adapter = adapter

        //setup for  search view
        setupSearchView()

        //show all menuServices
       showAllMenu()

        return binding.root

    }

    private fun showAllMenu() {
        filteredMenuServiceName.clear()
        filteredMenuServicePrice.clear()
        filteredMenuServiceImages.clear()

        filteredMenuServiceName.addAll(originalMenuServicesName)
        filteredMenuServicePrice.addAll(originalMenuServicePrice)
        filteredMenuServiceImages.addAll(originalMenuServiceImages)
        adapter.notifyDataSetChanged()
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String): Boolean {
                filterMenuItems(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                filterMenuItems(query)
                return true
            }

        })
    }
    private fun filterMenuItems(query: String) {
        filteredMenuServiceName.clear()
        filteredMenuServicePrice.clear()
        filteredMenuServiceImages.clear()

        originalMenuServicesName.forEachIndexed { index, serviceName ->
            if (serviceName.contains(query, ignoreCase = true)) {
                filteredMenuServiceName.add(serviceName)
                filteredMenuServicePrice.add(originalMenuServicePrice[index])
                filteredMenuServiceImages.add(originalMenuServiceImages[index])
            }
        }
        adapter.notifyDataSetChanged()
    }

    companion object {

    }
}