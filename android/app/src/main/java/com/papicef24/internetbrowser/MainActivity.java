import android.os.Build;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.view.inputmethod.InputMethodManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private EditText addressBar;
    private Button goButton;
    private Button backButton;
    private Button forwardButton;
    private Button reloadButton;
    private Button homeButton;
    private Spinner yearSpinner;
    private ProgressBar progressBar;
    private TextView statusText;
    private SwipeRefreshLayout swipeRefresh;

    private static final String HOME_URL = "https://www.google.com";
    private String currentUrl = HOME_URL;
    private String currentYear = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupWebView();
        setupListeners();
        setupYearSpinner();
        loadHome();
    }

    private void initializeViews() {
        webView = findViewById(R.id.webView);
        addressBar = findViewById(R.id.addressBar);
        goButton = findViewById(R.id.goBtn);
        backButton = findViewById(R.id.backBtn);
        forwardButton = findViewById(R.id.forwardBtn);
        reloadButton = findViewById(R.id.reloadBtn);
        homeButton = findViewById(R.id.homeBtn);
        yearSpinner = findViewById(R.id.yearSelect);
        progressBar = findViewById(R.id.progressBar);
        statusText = findViewById(R.id.statusText);
        swipeRefresh = findViewById(R.id.swipeRefresh);
    }

    private void setupWebView() {
        WebSettings webSettings = webView.getSettings();
        
        // Enable JavaScript
        webSettings.setJavaScriptEnabled(true);
        
        // Enable DOM Storage
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        
        // Enable mixed content (HTTP in HTTPS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        
        // Set user agent
        webSettings.setUserAgentString(webSettings.getUserAgentString() + " InternetBrowser/1.0");
        
        // Performance settings
        webSettings.setAppCacheMaxSize(50 * 1024 * 1024);
        webSettings.setAppCachePath(getApplicationContext().getCacheDir().getAbsolutePath());
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        
        // Privacy settings
        webSettings.setSaveFormData(false);
        webSettings.setGeolocationEnabled(true);
        
        // Set WebView client
        webView.setWebViewClient(new CustomWebViewClient());
        webView.setWebChromeClient(new CustomWebChromeClient());
    }

    private void setupListeners() {
        // Navigation buttons
        goButton.setOnClickListener(v -> navigate());
        backButton.setOnClickListener(v -> webView.goBack());
        forwardButton.setOnClickListener(v -> webView.goForward());
        reloadButton.setOnClickListener(v -> webView.reload());
        homeButton.setOnClickListener(v -> loadHome());
        
        // Address bar
        addressBar.setOnEditorActionListener((v, actionId, event) -> {
            navigate();
            return true;
        });
        
        // Swipe to refresh
        swipeRefresh.setOnRefreshListener(() -> {
            webView.reload();
            swipeRefresh.setRefreshing(false);
        });
        
        // Update button states
        updateNavigationButtons();
    }

    private void setupYearSpinner() {
        String[] years = {"Current", "2024", "2023", "2022", "2021", "2020", "2019", "2018", "2010", "2000"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(adapter);
        
        yearSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    currentYear = null;
                    loadUrl(currentUrl);
                } else {
                    currentYear = years[position];
                    loadArchiveVersion();
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });
    }

    private void navigate() {
        String url = addressBar.getText().toString().trim();
        
        if (url.isEmpty()) {
            Toast.makeText(this, "Please enter a URL", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Add protocol if missing
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            if (!url.contains(".") || url.contains(" ")) {
                url = "https://www.google.com/search?q=" + url.replace(" ", "+");
            } else {
                url = "https://" + url;
            }
        }
        
        currentUrl = url;
        yearSpinner.setSelection(0);
        currentYear = null;
        loadUrl(url);
    }

    private void loadUrl(String url) {
        updateStatus("Loading " + url + "...");
        webView.loadUrl(url);
        hideKeyboard();
        updateNavigationButtons();
    }

    private void loadHome() {
        addressBar.setText(HOME_URL);
        currentUrl = HOME_URL;
        yearSpinner.setSelection(0);
        currentYear = null;
        loadUrl(HOME_URL);
    }

    private void loadArchiveVersion() {
        if (currentYear != null) {
            String cleanUrl = currentUrl
                .replace("https://", "")
                .replace("http://", "")
                .replace("www.", "");
            
            String archiveUrl = "https://web.archive.org/web/" + currentYear + "0101000000/" + cleanUrl;
            updateStatus("Loading " + currentYear + " version of " + cleanUrl + "...");
            webView.loadUrl(archiveUrl);
        }
    }

    private void updateNavigationButtons() {
        backButton.setEnabled(webView.canGoBack());
        forwardButton.setEnabled(webView.canGoForward());
    }

    private void updateStatus(String message) {
        statusText.setText(message);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(addressBar.getWindowToken(), 0);
        }
    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
            updateStatus("Loading page...");
            addressBar.setText(url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
            updateStatus("Page loaded");
            updateNavigationButtons();
        }

        @Override
        public void onReceivedError(WebView view, android.webkit.WebResourceRequest request, android.webkit.WebResourceError error) {
            super.onReceivedError(view, request, error);
            updateStatus("Error loading page");
            Toast.makeText(MainActivity.this, "Error: " + error.getDescription(), Toast.LENGTH_SHORT).show();
        }
    }

    private class CustomWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            progressBar.setProgress(newProgress);
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
