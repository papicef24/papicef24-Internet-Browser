package com.papicef24.internetbrowser

import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.webkit.WebChromeClient

class BrowserWebViewClient(
    private val context: MainActivity,
    private val updateStatus: (String) -> Unit,
    private val updateButtons: () -> Unit
) : WebViewClient() {

    override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
        super.onPageStarted(view, url, favicon)
        updateStatus("Loading page...")
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        updateStatus("Page loaded")
        updateButtons()
    }

    override fun onReceivedError(
        view: WebView?,
        request: android.webkit.WebResourceRequest?,
        error: android.webkit.WebResourceError?
    ) {
        super.onReceivedError(view, request, error)
        updateStatus("Error loading page")
        android.widget.Toast.makeText(
            context,
            "Error: ${error?.description}",
            android.widget.Toast.LENGTH_SHORT
        ).show()
    }
}

class BrowserWebChromeClient(private val progressBar: ProgressBar) : WebChromeClient() {
    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        progressBar.progress = newProgress
    }
}
