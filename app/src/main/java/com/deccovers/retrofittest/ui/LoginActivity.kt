package com.deccovers.retrofittest.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.deccovers.retrofittest.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigInteger
import java.security.MessageDigest

private const val TAG = "LoginActivity"

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            if (binding.etUsernameInput.text.isNotEmpty() && binding.etPasswordInput.text.isNotEmpty()) {
                val combinedString = binding.etUsernameInput.text.toString() + binding.etPasswordInput.text.toString()
                Log.d(TAG, "combinedString: $combinedString")

                val md5HashedString = md5(combinedString)
                Log.d(TAG, "md5HashedString: $md5HashedString")

                val onlyDigitsString = md5HashedString.filter(Char:: isDigit)
                Log.d(TAG, "onlyDigitsString: $onlyDigitsString")

                val charArray: CharArray = onlyDigitsString.toCharArray()
                var sum = 0
                for (i in charArray) {
                    sum += i.digitToInt()
                    Log.d(TAG, "Number: ${i.digitToInt()}, sum = $sum")
                }

                if (sum%2 == 0) {
                    // Go to dashboard
                    val intent = Intent(this@LoginActivity, ContentActivity::class.java)
                    intent.putExtra("Username", binding.etUsernameInput.text.toString())
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Login Invalid", Toast.LENGTH_SHORT).show()
                }



            } else {
                Toast.makeText(this, "Enter fields first", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun md5(input:String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

}