package com.papicef24.internetbrowser

import android.content.Context
import android.webkit.JavascriptInterface
import android.widget.Toast

class FlashDetectionInterface(private val context: Context) {
    
    @JavascriptInterface
    fun onFlashDetected(hasFlash: Boolean) {
        val message = if (hasFlash) {
            "✅ Flash Player 11 Detected"
        } else {
            "⚠️ Flash content requires Flash Player 11"
        }
        
        android.util.Log.d("FlashDetection", message)
    }
    
    @JavascriptInterface
    fun logFlashEvent(event: String) {
        android.util.Log.d("FlashEvent", event)
    }
    
    @JavascriptInterface
    fun notifyFlashVersion(version: String) {
        android.util.Log.d("FlashVersion", "Flash Version: $version")
    }
}
