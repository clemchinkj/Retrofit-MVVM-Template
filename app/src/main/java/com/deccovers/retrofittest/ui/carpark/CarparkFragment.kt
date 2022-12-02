package com.deccovers.retrofittest.ui.carpark

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.deccovers.retrofittest.data.carpark.model.MyCarparkEntry
import com.deccovers.retrofittest.databinding.FragmentCarparkBinding
import com.deccovers.retrofittest.ui.ContentActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val TAG = "CarparkFragment"

@AndroidEntryPoint
class CarparkFragment : Fragment() {

    private val carparkViewModel: CarparkViewModel by viewModels()
    private lateinit var carparkListAdapter: CarparkListAdapter

    private var _binding: FragmentCarparkBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        _binding = FragmentCarparkBinding.inflate(inflater, container, false)

        binding.carparkViewModel = carparkViewModel

        carparkListAdapter = CarparkListAdapter()

        binding.apply {
            rvCarpark.adapter = carparkListAdapter
        }

        binding.ivClosePage1.setOnClickListener{
            (activity as ContentActivity?)?.removeFragment(this)
            Log.d(TAG, "ivClosePage1 clicked")
        }
        binding.tvPage1.setOnClickListener{
            (activity as ContentActivity?)?.removeFragment(this)
            Log.d(TAG, "tvPage1 clicked")
        }

        viewLifecycleOwner.lifecycleScope.launch {
            carparkViewModel.getCarparkResponse()
            Log.d(TAG, "carparkViewModel.getCarparkResponse()")

            carparkViewModel.carparkRetrieval.collect { event ->
                when(event) {
                    is CarparkViewModel.CarparkEvent.Success -> {
                        binding.progressBarCarpark.isVisible = false
                        val dateTimeString = "Last retrieval: ${event.carparkResponse.items[0].timestamp}"
                        binding.tvCarparkLastRetrieval.text = dateTimeString
                        val carparkListSize = event.carparkResponse.items[0].carpark_data.size
                        Log.d(TAG, "carparkListSize: $carparkListSize")

                        val carparkEntryList = mutableListOf<MyCarparkEntry>()
                        carparkEntryList.add(MyCarparkEntry(
                            "Car Park #",
                            "Total Lots",
                            "Lot Type",
                            "Lots Available",
                            "Update Date/Time"
                        ))
                        for(i in 0 until carparkListSize) {
                            val myCarparkEntry = MyCarparkEntry(
                                event.carparkResponse.items[0].carpark_data[i].carpark_number,
                                event.carparkResponse.items[0].carpark_data[i].carpark_info[0].total_lots,
                                event.carparkResponse.items[0].carpark_data[i].carpark_info[0].lot_type,
                                event.carparkResponse.items[0].carpark_data[i].carpark_info[0].lots_available,
                                event.carparkResponse.items[0].carpark_data[i].update_datetime
                            )
                            Log.d(TAG, "myCarparkEntry position $i: $myCarparkEntry")
                            carparkEntryList.add(myCarparkEntry)
                        }
                        Log.d(TAG, "carparkEntryList: $carparkEntryList")
                        carparkListAdapter.submitList(carparkEntryList)
                    }

                    is CarparkViewModel.CarparkEvent.Failure -> {
                        binding.progressBarCarpark.isVisible = false
                    }

                    is CarparkViewModel.CarparkEvent.Loading -> {
                        binding.progressBarCarpark.isVisible = true
                    }

                    else -> Unit
                }

            }
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}