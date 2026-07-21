# Internet Browser - Web Edition

A modern web-based internet browser built with HTML, CSS, and JavaScript featuring plugin architecture support and Wayback Machine integration for browsing websites by year.

## Features

### 🌐 Core Browser Features
- **Google Homepage**: Default landing page (https://www.google.com)
- **Address Bar**: Navigate to any URL or search the web
- **Navigation Controls**: Back, Forward, Reload, and Home buttons
- **History Management**: Track visited pages with navigation history
- **Status Bar**: Real-time feedback on page loading and browser status

### 🔌 Plugin Architecture

The browser includes a powerful plugin system supporting:

#### Built-in Plugins
1. **Flash Player 11** (Legacy Support)
   - Version: 11.9.900.170
   - MIME Types: `application/x-shockwave-flash`, `application/futuresplash`
   - Status: Disabled (deprecated, for archival support only)
   - Detects Flash content and recommends HTML5 alternatives

2. **PDF Viewer**
   - Built-in PDF viewing capability
   - MIME Types: `application/pdf`
   - Status: Enabled

3. **Video Player**
   - HTML5 video playback
   - MIME Types: `video/mp4`, `video/webm`, `video/ogg`
   - Status: Enabled

4. **Wayback Machine Integration**
   - Access archived versions of websites by year
   - Status: Enabled
   - Features: Browse website snapshots from different years

5. **Cookie Manager**
   - Manage website cookies and local storage
   - Status: Enabled

### 📅 Browse Websites by Year

The browser integrates with [Wayback Machine (web.archive.org)](https://web.archive.org) to access historical versions of websites:

1. Navigate to any website
2. Use the "Browse by Year" dropdown to select a year
3. The browser will load that year's archived version using Wayback Machine
4. Available years: Current, 2024, 2023, 2022, 2021, 2020, 2019, 2018, 2010, 2000

#### Example Usage
- Navigate to: `wikipedia.org`
- Select Year: `2010`
- View: Wikipedia from 2010 via Wayback Machine

### 🖥️ Plugin Management

Click the "Plugins" indicator in the toolbar to:
- View all installed plugins
- Check plugin version and author
- See supported MIME types
- Check enabled/disabled status
- View plugin descriptions

## File Structure

```
├── index.html           # Main HTML structure
├── styles.css           # Browser styling and UI
├── plugin-manager.js    # Plugin system implementation
├── app.js               # Main browser application logic
└── README.md            # This file
```

## How It Works

### Plugin System Architecture

The plugin system is built on a hook-based architecture:

```javascript
// Plugins register hooks for lifecycle events
pluginManager.registerPlugin({
    id: 'plugin-id',
    name: 'Plugin Name',
    version: '1.0.0',
    hooks: {
        'page:load': function(data) { /* ... */ },
        'year:change': function(data) { /* ... */ }
    }
});

// Browser executes hooks at appropriate times
pluginManager.executeHook('page:load', { url: 'https://example.com' });
```

### Available Hooks

- `page:load` - Triggered when a page loads
- `page:navigate` - Triggered when navigating to a URL
- `year:change` - Triggered when browsing by year
- `plugin:detect` - Triggered for plugin detection

## Usage

### Basic Navigation
```
1. Enter URL in address bar: "example.com"
2. Click Go or press Enter
3. Use Back/Forward buttons to navigate history
4. Click Home to return to Google
```

### Search
```
1. Enter search query: "web development"
2. Click Go or press Enter
3. Redirects to Google search results
```

### Browse by Year
```
1. Navigate to a website
2. Select year from "Browse by Year" dropdown
3. Browser loads archived version via Wayback Machine
4. Select "Current" to return to present-day version
```

## Technical Details

### Browser Compatibility
- Chrome/Edge: ✅ Full support
- Firefox: ✅ Full support
- Safari: ✅ Full support
- Mobile browsers: ⚠️ Partial support (responsive design included)

### Sandbox Security
The iframe uses sandbox attributes for security:
```html
<iframe sandbox="allow-same-origin allow-scripts allow-forms allow-popups allow-presentation"></iframe>
```

### Flash Player Legacy Support
While Flash Player has been deprecated and removed from modern browsers, this browser's plugin architecture includes support for:
- Flash Player 11 detection
- Proper MIME type handling
- Recommendations for HTML5 alternatives
- Archival browsing of Flash-based content via Wayback Machine

## Extending the Browser

### Creating Custom Plugins

```javascript
// Register a custom plugin
pluginManager.registerPlugin({
    id: 'my-plugin',
    name: 'My Custom Plugin',
    version: '1.0.0',
    author: 'Developer Name',
    description: 'Plugin description',
    enabled: true,
    mimeTypes: ['application/x-custom'],
    hooks: {
        'page:load': function(data) {
            console.log('Page loaded:', data.url);
            return { success: true };
        }
    }
});
```

## Future Enhancements

- [ ] Bookmarks and favorites
- [ ] Download manager
- [ ] Tab support
- [ ] Search bar suggestions
- [ ] Cache management
- [ ] User agent customization
- [ ] Developer tools integration
- [ ] Theme customization
- [ ] Extension marketplace
- [ ] Sync across devices

## License

MIT License - Feel free to use and modify this browser.

## Credits

- Wayback Machine: [archive.org](https://archive.org)
- PDF.js: [mozilla.github.io/pdf.js](https://mozilla.github.io/pdf.js)
- Plugin architecture inspired by modern browser extension systems

## Support

For issues or feature requests, please open an issue on the GitHub repository.