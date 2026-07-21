/**
 * Plugin Manager
 * Handles plugin architecture for the browser
 */

class PluginManager {
    constructor() {
        this.plugins = new Map();
        this.hooks = new Map();
        this.loadedPlugins = [];
        this.initializePlugins();
    }

    /**
     * Initialize built-in and custom plugins
     */
    initializePlugins() {
        // Flash Player 11 Plugin (Legacy Support)
        this.registerPlugin({
            id: 'flash-player-11',
            name: 'Flash Player 11',
            version: '11.9.900.170',
            author: 'Adobe Systems',
            description: 'Legacy Flash player plugin for archival support',
            enabled: true,
            mimeTypes: ['application/x-shockwave-flash', 'application/futuresplash'],
            hooks: {
                'page:load': this.onPageLoad.bind(this),
                'plugin:detect': this.detectFlash.bind(this)
            }
        });

        // PDF Viewer Plugin
        this.registerPlugin({
            id: 'pdf-viewer',
            name: 'PDF Viewer',
            version: '1.0.0',
            author: 'Browser Team',
            description: 'Built-in PDF viewer plugin',
            enabled: true,
            mimeTypes: ['application/pdf'],
            hooks: {
                'page:load': this.handlePDF.bind(this)
            }
        });

        // Video Player Plugin
        this.registerPlugin({
            id: 'video-player',
            name: 'Video Player',
            version: '1.0.0',
            author: 'Browser Team',
            description: 'HTML5 Video player plugin',
            enabled: true,
            mimeTypes: ['video/mp4', 'video/webm', 'video/ogg'],
            hooks: {
                'page:load': this.handleVideo.bind(this)
            }
        });

        // Archive.org Integration Plugin
        this.registerPlugin({
            id: 'wayback-machine',
            name: 'Wayback Machine Integration',
            version: '1.0.0',
            author: 'Browser Team',
            description: 'Access archived versions of websites by year',
            enabled: true,
            hooks: {
                'year:change': this.handleYearChange.bind(this)
            }
        });

        // Cookie Manager Plugin
        this.registerPlugin({
            id: 'cookie-manager',
            name: 'Cookie Manager',
            version: '1.0.0',
            author: 'Browser Team',
            description: 'Manage website cookies and storage',
            enabled: true,
            hooks: {
                'page:navigate': this.manageCookies.bind(this)
            }
        });
    }

    /**
     * Register a plugin
     */
    registerPlugin(pluginConfig) {
        const plugin = {
            id: pluginConfig.id,
            name: pluginConfig.name,
            version: pluginConfig.version,
            author: pluginConfig.author,
            description: pluginConfig.description,
            enabled: pluginConfig.enabled !== false,
            mimeTypes: pluginConfig.mimeTypes || [],
            hooks: pluginConfig.hooks || {}
        };

        this.plugins.set(plugin.id, plugin);
        this.loadedPlugins.push(plugin);
        console.log(`Plugin registered: ${plugin.name} v${plugin.version}`);
    }

    /**
     * Get all plugins
     */
    getPlugins() {
        return Array.from(this.plugins.values());
    }

    /**
     * Get enabled plugins
     */
    getEnabledPlugins() {
        return this.getPlugins().filter(p => p.enabled);
    }

    /**
     * Enable/disable a plugin
     */
    setPluginEnabled(pluginId, enabled) {
        const plugin = this.plugins.get(pluginId);
        if (plugin) {
            plugin.enabled = enabled;
            console.log(`Plugin ${plugin.name} ${enabled ? 'enabled' : 'disabled'}`);
            return true;
        }
        return false;
    }

    /**
     * Execute plugin hooks
     */
    executeHook(hookName, data) {
        const results = [];
        this.plugins.forEach(plugin => {
            if (plugin.enabled && plugin.hooks[hookName]) {
                try {
                    const result = plugin.hooks[hookName](data);
                    results.push({
                        plugin: plugin.name,
                        result: result
                    });
                } catch (error) {
                    console.error(`Hook error in ${plugin.name}:`, error);
                }
            }
        });
        return results;
    }

    /**
     * Detect if Flash content is present
     */
    detectFlash(data) {
        return {
            detected: false,
            message: 'Flash Player is deprecated and no longer supported in modern browsers',
            recommendation: 'Use HTML5 alternatives for video and animation content'
        };
    }

    /**
     * Handle PDF files
     */
    handlePDF(data) {
        if (data.url && data.url.endsWith('.pdf')) {
            return {
                handled: true,
                viewer: 'Built-in PDF Viewer',
                url: `https://mozilla.github.io/pdf.js/web/viewer.html?file=${encodeURIComponent(data.url)}`
            };
        }
        return { handled: false };
    }

    /**
     * Handle video files
     */
    handleVideo(data) {
        const videoExtensions = ['.mp4', '.webm', '.ogg'];
        if (data.url && videoExtensions.some(ext => data.url.toLowerCase().endsWith(ext))) {
            return {
                handled: true,
                player: 'HTML5 Video Player'
            };
        }
        return { handled: false };
    }

    /**
     * Handle year change for Wayback Machine
     */
    handleYearChange(data) {
        if (data.year && data.url) {
            const archiveUrl = `https://web.archive.org/web/${data.year}0101000000*/${data.url}`;
            return {
                handled: true,
                archiveUrl: archiveUrl,
                message: `Loading archive from ${data.year}`
            };
        }
        return { handled: false };
    }

    /**
     * Manage cookies
     */
    manageCookies(data) {
        return {
            cookiesEnabled: true,
            message: 'Cookie management enabled'
        };
    }

    /**
     * Handle page load
     */
    onPageLoad(data) {
        return {
            loaded: true,
            timestamp: new Date().toISOString()
        };
    }

    /**
     * Get plugin status summary
     */
    getStatus() {
        const total = this.plugins.size;
        const enabled = this.getEnabledPlugins().length;
        return {
            total: total,
            enabled: enabled,
            disabled: total - enabled
        };
    }
}

// Create global instance
const pluginManager = new PluginManager();
