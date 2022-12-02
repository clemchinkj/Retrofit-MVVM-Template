package com.deccovers.retrofittest.ui.weather

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.deccovers.retrofittest.data.weather.model.MyAreaMetadataEntry
import com.deccovers.retrofittest.databinding.FragmentWeatherBinding
import com.deccovers.retrofittest.ui.ContentActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val TAG = "WeatherFragment"

private const val DEFAULT_PARTLY_CLOUDY = "Partly Cloudy (Day)"
private const val DEFAULT_THUNDERY_SHOWERS = "Thundery Showers"
private const val DEFAULT_SHOWERS = "Showers"
private const val DEFAULT_MODERATERAIN = "Moderate Rain"

@AndroidEntryPoint
class WeatherFragment : Fragment() {

    private val weatherViewModel: WeatherViewModel by viewModels()
    private lateinit var areaMetadataListAdapter: AreaMetadataListAdapter

    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this

        binding.weatherViewModel = weatherViewModel

        areaMetadataListAdapter = AreaMetadataListAdapter()

        binding.apply {
            rvArea.adapter = areaMetadataListAdapter
        }

        binding.ivClosePage2.setOnClickListener{
            (activity as ContentActivity?)?.removeFragment(this)
            Log.d(TAG, "ivClosePage2 clicked")
        }
        binding.tvPage2.setOnClickListener{
            (activity as ContentActivity?)?.removeFragment(this)
            Log.d(TAG, "tvPage2 clicked")
        }

        viewLifecycleOwner.lifecycleScope.launch {
            weatherViewModel.getWeatherResponse()
            Log.d(TAG, "weatherViewModel.getWeatherResponse()")

            weatherViewModel.weatherRetrieval.collect { event ->
                when(event) {
                    is WeatherViewModel.WeatherEvent.Success -> {
                        // For Area Metadata
                        binding.progressBarAreaMetadata.isVisible = false
                        val dateTimeString =
                            "Last retrieval: ${event.weatherResponse.items[0].timestamp}"
                        binding.tvWeatherLastRetrieval.text = dateTimeString

                        val areaListSize = event.weatherResponse.area_metadata.size
                        Log.d(TAG, "areaListSize: $areaListSize")

                        val areaMetadataListEntryList = mutableListOf<MyAreaMetadataEntry>()
                        for (i in 0 until areaListSize) {
                            val myAreaMetadataEntry = MyAreaMetadataEntry(
                                event.weatherResponse.area_metadata[i].label_location,
                                event.weatherResponse.area_metadata[i].name
                            )
                            areaMetadataListEntryList.add(myAreaMetadataEntry)
                        }

                        Log.d(TAG, "areaMetadataListEntryList: $areaMetadataListEntryList")
                        areaMetadataListAdapter.submitList(areaMetadataListEntryList)

                        // For Forecast
                        val listOfPartlyCloudyAreas = mutableListOf<String>()
                        val listOfThunderyShowerAreas = mutableListOf<String>()
                        val listOfShowerAreas = mutableListOf<String>()
                        val listOfModerateRainAreas = mutableListOf<String>()

                        val forecastAreasSize = event.weatherResponse.items[0].forecasts.size
                        Log.d(TAG, "forecastAreasSize: $forecastAreasSize")
                        for (i in 0 until forecastAreasSize) {
                            when (event.weatherResponse.items[0].forecasts[i].forecast) {
                                "Partly Cloudy (Day)" -> {
                                    listOfPartlyCloudyAreas.add(event.weatherResponse.items[0].forecasts[i].area)
                                }
                                "Thundery Showers" -> {
                                    listOfThunderyShowerAreas.add(event.weatherResponse.items[0].forecasts[i].area)
                                }
                                "Showers" -> {
                                    listOfShowerAreas.add(event.weatherResponse.items[0].forecasts[i].area)
                                }
                                else -> {  // Moderate rain
                                    listOfModerateRainAreas.add(event.weatherResponse.items[0].forecasts[i].area)
                                }
                            }
                        }

                        // Build radio button texts
                        var stringPartlyCloudy = DEFAULT_PARTLY_CLOUDY
                        for (i in 0 until listOfPartlyCloudyAreas.size) {
                            stringPartlyCloudy += "\n\t- ${listOfPartlyCloudyAreas[i]}"
                        }

                        var stringThunderyShowers = DEFAULT_THUNDERY_SHOWERS
                        for (i in 0 until listOfThunderyShowerAreas.size) {
                            stringThunderyShowers += "\n\t- ${listOfThunderyShowerAreas[i]}"
                        }

                        var stringShowers = DEFAULT_SHOWERS
                        for (i in 0 until listOfShowerAreas.size) {
                            stringShowers += "\n\t- ${listOfShowerAreas[i]}"
                        }

                        var stringModerateRain = DEFAULT_MODERATERAIN
                        for (i in 0 until listOfModerateRainAreas.size) {
                            stringModerateRain += "\n\t- ${listOfModerateRainAreas[i]}"
                        }

                        binding.rbPartlyCloudy.text = stringPartlyCloudy

                        binding.rbPartlyCloudy.setOnClickListener {
                            binding.rbPartlyCloudy.text = stringPartlyCloudy
                            binding.rbThunderyShowers.text = DEFAULT_THUNDERY_SHOWERS
                            binding.rbShowers.text = DEFAULT_SHOWERS
                            binding.rbModerateRain.text = DEFAULT_MODERATERAIN
                        }

                        binding.rbThunderyShowers.setOnClickListener {
                            binding.rbPartlyCloudy.text = DEFAULT_PARTLY_CLOUDY
                            binding.rbThunderyShowers.text = stringThunderyShowers
                            binding.rbShowers.text = DEFAULT_SHOWERS
                            binding.rbModerateRain.text = DEFAULT_MODERATERAIN
                        }

                        binding.rbShowers.setOnClickListener {
                            binding.rbPartlyCloudy.text = DEFAULT_PARTLY_CLOUDY
                            binding.rbThunderyShowers.text = DEFAULT_THUNDERY_SHOWERS
                            binding.rbShowers.text = stringShowers
                            binding.rbModerateRain.text = DEFAULT_MODERATERAIN
                        }

                        binding.rbModerateRain.setOnClickListener {
                            binding.rbPartlyCloudy.text = DEFAULT_PARTLY_CLOUDY
                            binding.rbThunderyShowers.text = DEFAULT_THUNDERY_SHOWERS
                            binding.rbShowers.text = DEFAULT_SHOWERS
                            binding.rbModerateRain.text = stringModerateRain
                        }

                    }

                    is WeatherViewModel.WeatherEvent.Failure -> {
                        binding.progressBarAreaMetadata.isVisible = false
                    }

                    is WeatherViewModel.WeatherEvent.Loading -> {
                        binding.progressBarAreaMetadata.isVisible = true
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