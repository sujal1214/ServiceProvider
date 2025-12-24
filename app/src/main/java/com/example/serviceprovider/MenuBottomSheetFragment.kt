package com.example.serviceprovider

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.serviceprovider.adapter.MenuAdapter
import com.example.serviceprovider.databinding.FragmentMenuBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MenuBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentMenuBottomSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentMenuBottomSheetBinding.inflate(inflater, container, false)

        binding.buttonBack.setOnClickListener {
            dismiss()
        }

        val menuServiceName = listOf("Plumber","Carpenter","Electrician","Painter","Mechanic","Gardener")
        val menuPrice = listOf("100","200","300","400","500","600")
        val menuImage = listOf(R.drawable.plumber,R.drawable.carpenter,R.drawable.electrician,R.drawable.painter,R.drawable.mechanic,R.drawable.gardner)
        val adapter = MenuAdapter(
            ArrayList(menuServiceName), ArrayList(menuPrice),
            ArrayList(menuImage)
        )
        binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.menuRecyclerView.adapter = adapter
        return binding.root
    }

    companion object {

    }
}