package com.papicef24.internetbrowser

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class BrowserViewModel : ViewModel() {

    private val homeUrl = "https://www.google.com"
    private var currentUrl = homeUrl
    private var currentYear: String? = null

    private val _currentUrl = MutableStateFlow(homeUrl)
    val currentUrl: StateFlow<String> = _currentUrl.asStateFlow()

    private val _statusMessage = MutableStateFlow("Ready")
    val statusMessage: StateFlow<String> = _statusMessage.asStateFlow()

    private val _urlToLoad = MutableStateFlow("")
    val urlToLoad: StateFlow<String> = _urlToLoad.asStateFlow()

    fun navigate(url: String) {
        var finalUrl = url.trim()

        if (finalUrl.isEmpty()) {
            _statusMessage.value = "Please enter a URL"
            return
        }

        // Add protocol if missing
        if (!finalUrl.startsWith("http://") && !finalUrl.startsWith("https://")) {
            finalUrl = if (!finalUrl.contains(".") || finalUrl.contains(" ")) {
                "https://www.google.com/search?q=${finalUrl.replace(" ", "+")}"
            } else {
                "https://$finalUrl"
            }
        }

        currentUrl = finalUrl
        currentYear = null
        _currentUrl.value = finalUrl
        _statusMessage.value = "Loading $finalUrl..."
        _urlToLoad.value = finalUrl
    }

    fun loadHome() {
        currentUrl = homeUrl
        currentYear = null
        _currentUrl.value = homeUrl
        _statusMessage.value = "Loading Google..."
        _urlToLoad.value = homeUrl
    }

    fun loadArchiveVersion(year: String?) {
        if (year == null) {
            currentYear = null
            _statusMessage.value = "Browsing current version of $currentUrl"
            _urlToLoad.value = currentUrl
        } else {
            currentYear = year
            val cleanUrl = currentUrl
                .replace("https://", "")
                .replace("http://", "")
                .replace("www.", "")

            val archiveUrl = "https://web.archive.org/web/${year}0101000000/$cleanUrl"
            _statusMessage.value = "Loading $year version of $cleanUrl..."
            _urlToLoad.value = archiveUrl
        }
    }
}
