package com.deccovers.retrofittest.ui

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.deccovers.retrofittest.R
import com.deccovers.retrofittest.databinding.ActivityContentBinding
import com.deccovers.retrofittest.ui.carpark.CarparkFragment
import com.deccovers.retrofittest.ui.weather.WeatherFragment
import com.deccovers.retrofittest.ui.webview.WebviewFragment
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "ContentActivity"

@AndroidEntryPoint
class ContentActivity : AppCompatActivity() {

    private var _binding: ActivityContentBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityContentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val receivedString = intent.getStringExtra("Username")
        Log.d(TAG, "receivedString: $receivedString")
        val outputString = "Welcome back $receivedString"
        binding.tvUsername.text = outputString

        val imageBytes = Base64.decode(readFromAsset(), Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        binding.ivOneServiceLogo.setImageBitmap(decodedImage)

        val sdf = SimpleDateFormat("EEEE dd MMM yyyy, HH:mm a", Locale.ENGLISH)
        val currentDateTime = Date(System.currentTimeMillis())
        binding.tvCurrentDateTime.text = sdf.format(currentDateTime)

        binding.tvTab1.setOnClickListener {
            replaceFragment(CarparkFragment())
        }

        binding.tvTab2.setOnClickListener {
            replaceFragment(WeatherFragment())
        }

        binding.tvTab3.setOnClickListener {
            replaceFragment(WebviewFragment())
        }

    }

    private fun readFromAsset(): String {
        val fileName = "Oneservice_img.base64"
        val bufferReader = application.assets.open(fileName).bufferedReader()
        val data = bufferReader.use {
            it.readText()
        }
        Log.d("Read", data)
        
        val base64Data = data.split(",")[1]
        Log.d(TAG, "readFromAsset: base64Data = $base64Data")
        return base64Data
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fcvContents, fragment)
        fragmentTransaction.commit()
    }

    fun removeFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.remove(fragment)
        fragmentTransaction.commit()
    }
}