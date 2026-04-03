package com.example.serviceprovider.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.serviceprovider.adapter.MenuAdapter
import com.example.serviceprovider.databinding.FragmentSearchBinding
import com.example.serviceprovider.model.MenuItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter : MenuAdapter
    
    private val originalMenuItems = mutableListOf<MenuItem>()
    private val filteredMenuItems = mutableListOf<MenuItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        
        // Initialize adapter with the filtered list
        adapter = MenuAdapter(filteredMenuItems, requireContext())
        binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.menuRecyclerView.adapter = adapter

        // Retrieve menu items from Firebase
        retrieveMenuItems()
        
        setupSearchView()

        return binding.root
    }

    private fun retrieveMenuItems() {
        val database = FirebaseDatabase.getInstance()
        val menuRef: DatabaseReference = database.reference.child("menu")
        
        menuRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                originalMenuItems.clear()
                for (menuSnapshot in snapshot.children) {
                    val menuItem = menuSnapshot.getValue(MenuItem::class.java)
                    menuItem?.let { originalMenuItems.add(it) }
                }
                showAllMenu()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun showAllMenu() {
        filteredMenuItems.clear()
        filteredMenuItems.addAll(originalMenuItems)
        adapter.notifyDataSetChanged()
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
        filteredMenuItems.clear()
        originalMenuItems.forEach { item ->
            if (item.serviceName?.contains(query, ignoreCase = true) == true) {
                filteredMenuItems.add(item)
            }
        }
        adapter.notifyDataSetChanged()
    }
}
