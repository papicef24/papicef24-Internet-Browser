/**
 * Internet Browser Application
 * Features:
 * - Google homepage as default
 * - Plugin architecture with Flash Player 11 support
 * - Browse websites by year using Wayback Machine
 * - Full navigation controls
 */

class InternetBrowser {
    constructor() {
        this.history = [];
        this.historyIndex = -1;
        this.currentUrl = 'https://www.google.com';
        this.currentYear = null;
        this.homeUrl = 'https://www.google.com';
        this.isLoading = false;

        this.initializeElements();
        this.attachEventListeners();
        this.loadHome();
    }

    /**
     * Initialize DOM elements
     */
    initializeElements() {
        this.elements = {
            addressBar: document.getElementById('addressBar'),
            contentFrame: document.getElementById('contentFrame'),
            goBtn: document.getElementById('goBtn'),
            backBtn: document.getElementById('backBtn'),
            forwardBtn: document.getElementById('forwardBtn'),
            reloadBtn: document.getElementById('reloadBtn'),
            homeBtn: document.getElementById('homeBtn'),
            yearSelect: document.getElementById('yearSelect'),
            statusText: document.getElementById('statusText'),
            pluginStatus: document.getElementById('pluginStatus'),
            pluginList: document.getElementById('pluginList'),
            pluginPanel: document.querySelector('.plugin-panel'),
            closePluginPanel: document.getElementById('closePluginPanel')
        };
    }

    /**
     * Attach event listeners
     */
    attachEventListeners() {
        // Navigation buttons
        this.elements.goBtn.addEventListener('click', () => this.navigate());
        this.elements.backBtn.addEventListener('click', () => this.goBack());
        this.elements.forwardBtn.addEventListener('click', () => this.goForward());
        this.elements.reloadBtn.addEventListener('click', () => this.reload());
        this.elements.homeBtn.addEventListener('click', () => this.loadHome());

        // Address bar
        this.elements.addressBar.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') this.navigate();
        });

        this.elements.addressBar.addEventListener('focus', (e) => {
            e.target.select();
        });

        // Year selection
        this.elements.yearSelect.addEventListener('change', (e) => {
            this.handleYearChange(e.target.value);
        });

        // Plugin status
        this.elements.pluginStatus.addEventListener('click', () => {
            this.togglePluginPanel();
        });

        this.elements.closePluginPanel.addEventListener('click', () => {
            this.togglePluginPanel();
        });

        // Frame load events
        this.elements.contentFrame.addEventListener('load', () => {
            this.onFrameLoaded();
        });

        this.elements.contentFrame.addEventListener('loadstart', () => {
            this.setLoading(true);
        });
    }

    /**
     * Navigate to URL
     */
    navigate(url = null) {
        url = url || this.elements.addressBar.value;

        if (!url.trim()) {
            this.updateStatus('Please enter a URL');
            return;
        }

        // Add protocol if missing
        if (!url.startsWith('http://') && !url.startsWith('https://')) {
            // Check if it's a search query
            if (!url.includes('.') || url.includes(' ')) {
                url = `https://www.google.com/search?q=${encodeURIComponent(url)}`;
            } else {
                url = 'https://' + url;
            }
        }

        this.currentUrl = url;
        this.elements.addressBar.value = url;
        this.loadUrl(url);

        // Add to history
        if (this.historyIndex < this.history.length - 1) {
            this.history = this.history.slice(0, this.historyIndex + 1);
        }
        this.history.push(url);
        this.historyIndex = this.history.length - 1;
        this.updateNavigationButtons();

        // Execute plugin hooks
        pluginManager.executeHook('page:navigate', { url: url });
    }

    /**
     * Load URL in iframe
     */
    loadUrl(url) {
        try {
            this.setLoading(true);
            this.updateStatus(`Loading ${url}...`);
            this.elements.contentFrame.src = url;
            pluginManager.executeHook('page:load', { url: url });
        } catch (error) {
            this.updateStatus(`Error loading URL: ${error.message}`);
            console.error('Navigation error:', error);
        }
    }

    /**
     * Load home page
     */
    loadHome() {
        this.elements.yearSelect.value = '';
        this.currentYear = null;
        this.navigate(this.homeUrl);
    }

    /**
     * Go back in history
     */
    goBack() {
        if (this.historyIndex > 0) {
            this.historyIndex--;
            this.currentUrl = this.history[this.historyIndex];
            this.elements.addressBar.value = this.currentUrl;
            this.loadUrl(this.currentUrl);
            this.updateNavigationButtons();
        }
    }

    /**
     * Go forward in history
     */
    goForward() {
        if (this.historyIndex < this.history.length - 1) {
            this.historyIndex++;
            this.currentUrl = this.history[this.historyIndex];
            this.elements.addressBar.value = this.currentUrl;
            this.loadUrl(this.currentUrl);
            this.updateNavigationButtons();
        }
    }

    /**
     * Reload current page
     */
    reload() {
        this.updateStatus('Reloading...');
        this.elements.contentFrame.src = this.elements.contentFrame.src;
    }

    /**
     * Handle year selection for Wayback Machine
     */
    handleYearChange(year) {
        if (!year) {
            // Load current version
            this.currentYear = null;
            this.loadUrl(this.currentUrl);
            this.updateStatus(`Browsing current version of ${this.currentUrl}`);
            return;
        }

        this.currentYear = year;
        const cleanUrl = this.currentUrl
            .replace(/^https?:\/\//, '')
            .replace(/^www\./, '');

        // Use Wayback Machine
        const archiveUrl = `https://web.archive.org/web/${year}0101000000/${cleanUrl}`;
        
        this.updateStatus(`Loading ${year} version of ${cleanUrl}...`);
        this.loadUrl(archiveUrl);

        // Execute plugin hook
        pluginManager.executeHook('year:change', {
            year: year,
            url: this.currentUrl,
            archiveUrl: archiveUrl
        });
    }

    /**
     * Update navigation buttons state
     */
    updateNavigationButtons() {
        this.elements.backBtn.disabled = this.historyIndex <= 0;
        this.elements.forwardBtn.disabled = this.historyIndex >= this.history.length - 1;
    }

    /**
     * Handle frame load completion
     */
    onFrameLoaded() {
        this.setLoading(false);
        this.updateStatus('Page loaded successfully');
    }

    /**
     * Set loading state
     */
    setLoading(loading) {
        this.isLoading = loading;
        this.elements.reloadBtn.disabled = loading;
        this.elements.goBtn.disabled = loading;
    }

    /**
     * Update status bar
     */
    updateStatus(message) {
        this.elements.statusText.textContent = message;
    }

    /**
     * Toggle plugin panel visibility
     */
    togglePluginPanel() {
        this.elements.pluginPanel.classList.toggle('active');
        if (this.elements.pluginPanel.classList.contains('active')) {
            this.updatePluginList();
        }
    }

    /**
     * Update plugin list display
     */
    updatePluginList() {
        const plugins = pluginManager.getPlugins();
        const status = pluginManager.getStatus();

        let html = '';
        plugins.forEach(plugin => {
            html += `
                <div class="plugin-item">
                    <div class="plugin-item-name">${plugin.name}</div>
                    <div class="plugin-item-version">v${plugin.version} by ${plugin.author}</div>
                    <div class="plugin-item-version" style="margin-top: 4px; color: #666;">${plugin.description}</div>
                    <div class="plugin-item-version" style="margin-top: 4px;">
                        Status: <strong>${plugin.enabled ? '✓ Enabled' : '✗ Disabled'}</strong>
                    </div>
                    ${plugin.mimeTypes.length > 0 ? `
                        <div class="plugin-item-version" style="margin-top: 4px;">
                            MIME Types: ${plugin.mimeTypes.join(', ')}
                        </div>
                    ` : ''}
                </div>
            `;
        });

        this.elements.pluginList.innerHTML = html;

        // Update plugin status indicator
        this.elements.pluginStatus.textContent = `Plugins: ${status.enabled}/${status.total} enabled`;
    }
}

// Initialize browser when DOM is ready
document.addEventListener('DOMContentLoaded', () => {
    const browser = new InternetBrowser();
    
    // Update plugin list on startup
    browser.updatePluginList();
    
    console.log('Internet Browser initialized');
    console.log('Plugins loaded:', pluginManager.getStatus());
});