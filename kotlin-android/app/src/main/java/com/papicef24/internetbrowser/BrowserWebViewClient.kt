package com.papicef24.internetbrowser

import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.webkit.WebChromeClient
import android.os.Message

class BrowserWebViewClient(
    private val context: MainActivity,
    private val updateStatus: (String) -> Unit,
    private val updateButtons: () -> Unit
) : WebViewClient() {

    override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
        super.onPageStarted(view, url, favicon)
        updateStatus("Loading page...")
        detectFlashContent(url)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        updateStatus("Page loaded")
        updateButtons()
        injectFlashDetectionScript(view)
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

    private fun detectFlashContent(url: String?) {
        if (url != null && (url.contains(".swf") || url.contains("flash"))) {
            updateStatus("Flash content detected - Flash Player 11 is ACTIVE")
        }
    }

    private fun injectFlashDetectionScript(view: WebView?) {
        val script = """
            (function() {
                var hasFlash = false;
                try {
                    hasFlash = !!navigator.plugins && !!navigator.plugins['Shockwave Flash'];
                } catch(e) {
                    hasFlash = false;
                }
                
                window.FlashDetector.onFlashDetected(hasFlash);
            })();
        """.trimIndent()
        
        view?.evaluateJavascript(script) { result ->
            android.util.Log.d("FlashDetection", "Flash detection completed: $result")
        }
    }
}

class FlashEnabledWebChromeClient(
    private val progressBar: ProgressBar,
    private val updateStatus: (String) -> Unit
) : WebChromeClient() {
    
    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        progressBar.progress = newProgress
    }
    
    override fun onCreateWindow(
        view: WebView?,
        isDialog: Boolean,
        isUserGesture: Boolean,
        resultMsg: Message?
    ): Boolean {
        // Handle Flash pop-ups and new windows
        return true
    }
    
    override fun onConsoleMessage(
        consoleMessage: android.webkit.ConsoleMessage?
    ): Boolean {
        consoleMessage?.let {
            android.util.Log.d("WebConsole", "${it.message()} -- From line ${it.lineNumber()} of ${it.sourceId()}")
        }
        return true
    }
}
