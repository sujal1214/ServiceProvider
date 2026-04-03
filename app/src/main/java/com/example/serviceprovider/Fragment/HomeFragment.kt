package com.example.serviceprovider.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.serviceprovider.MenuBottomSheetFragment
import com.example.serviceprovider.R
import com.example.serviceprovider.adapter.MenuAdapter
import com.example.serviceprovider.databinding.FragmentHomeBinding
import com.example.serviceprovider.model.MenuItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var database : FirebaseDatabase
    private lateinit var menuItems : MutableList<MenuItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.viewAllMenu.setOnClickListener{
            val bottomSheetDialog = MenuBottomSheetFragment()
            bottomSheetDialog.show(parentFragmentManager,"Test")
        }
        //retrieve PopularMenuItem
        reteriveAndDisplayPopularItems()
        return binding.root
    }

    private fun reteriveAndDisplayPopularItems() {
        //get reference to the database
        database = FirebaseDatabase.getInstance()
        val serviceRef : DatabaseReference = database.reference.child("menu")
        menuItems = mutableListOf()

        //reterive menu items from fatabase
        serviceRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (serviceSnapshot in snapshot.children) {
                    val menuItem = serviceSnapshot.getValue(MenuItem::class.java)
                    menuItem?.let { menuItems.add(it) }
                }
                // display a random popular items
                randomPopularItems()

                // Optional: Log or update UI
                Log.d("DATA", "Items fetched: ${menuItems.size}")

            }

            private fun randomPopularItems() {
                // create a shuffled list of menu items
                val index = menuItems.indices.toList().shuffled()
                val numItemToShow = 6
                val subsetMenuItems = index.take(numItemToShow).map{menuItems[it]}

                setPopularItemsAdapter(subsetMenuItems)
            }

            private fun setPopularItemsAdapter(subsetMenuItems: List<MenuItem>) {
                val adapter = MenuAdapter(subsetMenuItems, requireContext())
                binding.PopularRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                binding.PopularRecyclerView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ERROR", "Database error: ${error.message}")
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.banner1, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner2, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner3, ScaleTypes.FIT))

        val imageSlider = binding.imageSlider
        imageSlider.setImageList(imageList)
        imageSlider.setImageList(imageList, ScaleTypes.FIT)

        imageSlider.setItemClickListener(object : ItemClickListener{
            override fun doubleClick(position: Int) {
                TODO("Not yet implemented")
            }
            override fun onItemSelected(position: Int) {
                val itemPosition = imageList[position]
                val itemMessage = "Selected Image $position"
                Toast.makeText(requireContext(), itemMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}