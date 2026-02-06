package com.example.serviceprovider.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.serviceprovider.CongratsBottomSheet
import com.example.serviceprovider.PayOutActivity
import com.example.serviceprovider.R
import com.example.serviceprovider.adapter.CartAdapter
import com.example.serviceprovider.databinding.FragmentBookingsBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.ArrayList


class BookingsFragment : Fragment() {
    private lateinit var binding: FragmentBookingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookingsBinding.inflate(inflater, container, false)


        val cardServiceName = listOf("Plumber","Carpenter","Electrician","Painter","Mechanic","Gardener")
        val cardServicePrice = listOf("100","200","300","400","500","600")
        val cardImage = listOf(R.drawable.plumber,R.drawable.carpenter,R.drawable.electrician,R.drawable.painter,R.drawable.mechanic,R.drawable.gardner)
        val adapter = CartAdapter(ArrayList(cardServiceName), ArrayList(cardServicePrice),
            ArrayList(cardImage))
        binding.cardRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.cardRecyclerView.adapter = adapter

        binding.proceedButton.setOnClickListener {
            val intent = Intent(requireContext(), PayOutActivity::class.java)
            startActivity(intent)
        }



        return binding.root
    }

    companion object {

    }
}