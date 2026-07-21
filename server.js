const express = require('express');
const path = require('path');
const app = express();
const PORT = process.env.PORT || 3000;

// Middleware
app.use(express.static(path.join(__dirname, '.')));

// Routes
app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, 'index.html'));
});

// API endpoints for plugin management
app.get('/api/plugins', (req, res) => {
    res.json({
        message: 'Plugin API endpoint',
        description: 'Use the browser interface to manage plugins'
    });
});

app.get('/api/status', (req, res) => {
    res.json({
        status: 'running',
        version: '1.0.0',
        name: 'Internet Browser',
        timestamp: new Date().toISOString()
    });
});

// Health check endpoint
app.get('/health', (req, res) => {
    res.json({ status: 'healthy' });
});

// Error handling
app.use((err, req, res, next) => {
    console.error(err.stack);
    res.status(500).json({ error: 'Something went wrong!' });
});

// Start server
app.listen(PORT, () => {
    console.log(`\n🌐 Internet Browser is running!`);
    console.log(`📍 Server: http://localhost:${PORT}`);
    console.log(`🔌 Plugins: Flash Player 11, PDF Viewer, Video Player, Wayback Machine, Cookie Manager`);
    console.log(`📅 Browse by Year: Enabled (Wayback Machine Integration)`);
    console.log(`\n✨ Open your browser and navigate to http://localhost:${PORT}\n`);
});
