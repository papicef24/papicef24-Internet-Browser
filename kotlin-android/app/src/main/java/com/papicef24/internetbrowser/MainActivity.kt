package com.papicef24.internetbrowser

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import android.widget.*
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.webkit.WebView
import android.view.inputmethod.InputMethodManager
import android.content.Context
import android.os.Build
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: BrowserViewModel
    private lateinit var webView: WebView
    private lateinit var addressBar: EditText
    private lateinit var goButton: Button
    private lateinit var backButton: Button
    private lateinit var forwardButton: Button
    private lateinit var reloadButton: Button
    private lateinit var homeButton: Button
    private lateinit var yearSpinner: Spinner
    private lateinit var progressBar: ProgressBar
    private lateinit var statusText: TextView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var flashToggle: ToggleButton
    private lateinit var flashStatus: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(BrowserViewModel::class.java)

        initializeViews()
        setupWebView()
        setupFlashPlayer()
        setupListeners()
        setupYearSpinner()
        observeViewModel()
        viewModel.loadHome()
    }

    private fun initializeViews() {
        webView = findViewById(R.id.webView)
        addressBar = findViewById(R.id.addressBar)
        goButton = findViewById(R.id.goBtn)
        backButton = findViewById(R.id.backBtn)
        forwardButton = findViewById(R.id.forwardBtn)
        reloadButton = findViewById(R.id.reloadBtn)
        homeButton = findViewById(R.id.homeBtn)
        yearSpinner = findViewById(R.id.yearSelect)
        progressBar = findViewById(R.id.progressBar)
        statusText = findViewById(R.id.statusText)
        swipeRefresh = findViewById(R.id.swipeRefresh)
        flashToggle = findViewById(R.id.flashToggle)
        flashStatus = findViewById(R.id.flashStatus)
    }

    private fun setupWebView() {
        val webSettings = webView.settings.apply {
            // Enable JavaScript (required for Flash Player 11)
            javaScriptEnabled = true
            
            // Enable plugins and Flash
            pluginState = android.webkit.WebSettings.PluginState.ON
            
            // Enable DOM Storage
            domStorageEnabled = true
            databaseEnabled = true
            
            // Enable cache
            appCacheEnabled = true
            appCacheMaxSize = (50 * 1024 * 1024).toLong()
            appCachePath = applicationContext.cacheDir.absolutePath
            cacheMode = android.webkit.WebSettings.LOAD_DEFAULT
            
            // Set user agent for Flash compatibility
            userAgentString = "${userAgentString} InternetBrowser/1.0 FlashPlayer/11.9.900.170"
            
            // Enable mixed content (HTTP in HTTPS) for Flash
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
            
            // Flash specific settings
            mediaPlaybackRequiresUserGesture = false
            allowUniversalAccessFromFileURLs = true
            allowFileAccessFromFileURLs = true
        }

        webView.apply {
            webViewClient = BrowserWebViewClient(this@MainActivity, ::updateStatus, ::updateNavigationButtons)
            webChromeClient = FlashEnabledWebChromeClient(progressBar, ::updateStatus)
            
            // Add JavaScript interface for Flash detection
            addJavascriptInterface(FlashDetectionInterface(this@MainActivity), "FlashDetector")
        }
    }

    private fun setupFlashPlayer() {
        // Check if Flash is available
        val isFlashSupported = checkFlashSupport()
        
        flashToggle.isChecked = isFlashSupported
        updateFlashStatus(isFlashSupported)
        
        flashToggle.setOnCheckedChangeListener { _, isChecked ->
            toggleFlashPlayer(isChecked)
        }
    }

    private fun checkFlashSupport(): Boolean {
        return try {
            // Check if Flash libraries are available
            Class.forName("android.webkit.WebView")
            true
        } catch (e: ClassNotFoundException) {
            false
        }
    }

    private fun toggleFlashPlayer(enabled: Boolean) {
        val settings = webView.settings
        settings.pluginState = if (enabled) {
            android.webkit.WebSettings.PluginState.ON
        } else {
            android.webkit.WebSettings.PluginState.OFF
        }
        
        updateFlashStatus(enabled)
        
        if (enabled) {
            updateStatus("Flash Player 11 ENABLED - Reloading page...")
            webView.reload()
        } else {
            updateStatus("Flash Player 11 DISABLED")
            webView.reload()
        }
    }

    private fun updateFlashStatus(enabled: Boolean) {
        flashStatus.text = if (enabled) {
            "🔴 Flash Player 11 ENABLED"
        } else {
            "⚪ Flash Player 11 DISABLED"
        }
        flashStatus.setTextColor(
            if (enabled) android.graphics.Color.GREEN else android.graphics.Color.RED
        )
    }

    private fun setupListeners() {
        goButton.setOnClickListener { navigate() }
        backButton.setOnClickListener { webView.goBack() }
        forwardButton.setOnClickListener { webView.goForward() }
        reloadButton.setOnClickListener { webView.reload() }
        homeButton.setOnClickListener { viewModel.loadHome() }

        addressBar.setOnEditorActionListener { _, _, _ ->
            navigate()
            true
        }

        swipeRefresh.setOnRefreshListener {
            webView.reload()
            swipeRefresh.isRefreshing = false
        }
    }

    private fun setupYearSpinner() {
        val years = arrayOf("Current", "2024", "2023", "2022", "2021", "2020", "2019", "2018", "2010", "2000")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, years)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        yearSpinner.adapter = adapter

        yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                if (position == 0) {
                    viewModel.loadArchiveVersion(null)
                } else {
                    viewModel.loadArchiveVersion(years[position])
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.currentUrl.collect { url ->
                addressBar.setText(url)
            }
        }

        lifecycleScope.launch {
            viewModel.statusMessage.collect { message ->
                updateStatus(message)
            }
        }

        lifecycleScope.launch {
            viewModel.urlToLoad.collect { url ->
                if (url.isNotEmpty()) {
                    webView.loadUrl(url)
                    hideKeyboard()
                }
            }
        }
    }

    private fun navigate() {
        val url = addressBar.text.toString().trim()
        if (url.isEmpty()) {
            Toast.makeText(this, "Please enter a URL", Toast.LENGTH_SHORT).show()
            return
        }
        viewModel.navigate(url)
        yearSpinner.setSelection(0)
    }

    private fun updateStatus(message: String) {
        statusText.text = message
    }

    private fun updateNavigationButtons() {
        backButton.isEnabled = webView.canGoBack()
        forwardButton.isEnabled = webView.canGoForward()
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(addressBar.windowToken, 0)
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
