package com.deccovers.retrofittest.ui.webview

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.webkit.WebViewClient
import com.deccovers.retrofittest.databinding.FragmentWebviewBinding
import com.deccovers.retrofittest.ui.ContentActivity

private const val TAG = "WebviewFragment"

class WebviewFragment : Fragment() {

    private var _binding: FragmentWebviewBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        _binding = FragmentWebviewBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this

        binding.btnWebview.setOnClickListener {
            // Go to url in editText
            val urlString = binding.etUrlInput.text.toString()

            hideKeyboard(requireActivity())

            binding.webView.webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    binding.progressBarWebView.visibility = View.VISIBLE
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    binding.progressBarWebView.visibility = View.GONE

                }
            }

            binding.webView.settings.javaScriptEnabled = true

            val settings = binding.webView.settings
            settings.domStorageEnabled = true

            binding.webView.loadUrl(urlString)
        }

        binding.ivClosePage3.setOnClickListener{
            (activity as ContentActivity?)?.removeFragment(this)
            Log.d(TAG, "ivClosePage3 clicked")
        }
        binding.tvPage3.setOnClickListener{
            (activity as ContentActivity?)?.removeFragment(this)
            Log.d(TAG, "tvPage3 clicked")
        }
        return binding.root
    }

    private fun hideKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusedView = activity.currentFocus
        currentFocusedView.let {
            inputMethodManager.hideSoftInputFromWindow(
                currentFocusedView?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}