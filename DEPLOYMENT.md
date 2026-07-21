# Deploying Internet Browser on Wasmer.io

## Overview

This guide explains how to deploy your **Internet Browser with Plugin Architecture** on Wasmer.io, a serverless platform for WebAssembly.

## Prerequisites

1. **Wasmer CLI** installed
   ```bash
   curl https://get.wasmer.io -sSfL | sh
   ```

2. **GitHub Account** (already have one)

3. **Wasmer Account** (create at https://wasmer.io)

4. **Docker** (optional, for local testing)

## Installation Steps

### Step 1: Install Wasmer CLI

```bash
# macOS/Linux
curl https://get.wasmer.io -sSfL | sh

# Windows (PowerShell)
IEX(New-Object Net.WebClient).DownloadString('https://get.wasmer.io')
```

### Step 2: Login to Wasmer

```bash
wasmer login
# Follow the prompts to authenticate
```

### Step 3: Initialize Wasmer Package

Already done! The `wasmer.toml` file is configured.

### Step 4: Publish to Wasmer

```bash
wasmer publish
```

This will:
- Package your application
- Upload it to Wasmer Registry
- Make it available at `wasmer.io/papicef24/internet-browser`

### Step 5: Deploy on Wasmer Edge

```bash
# Deploy using Wasmer Edge Cloud
wasmer deploy
```

## Alternative: Docker Deployment

If you prefer using Docker:

### Build Docker Image

```bash
docker build -t papicef24/internet-browser:latest .
```

### Run Locally

```bash
docker run -p 3000:3000 papicef24/internet-browser:latest
```

Then open: `http://localhost:3000`

### Push to Docker Hub

```bash
# Login to Docker Hub
docker login

# Tag image
docker tag papicef24/internet-browser:latest papicef24/internet-browser:v1.0.0

# Push
docker push papicef24/internet-browser:latest
```

## Files Explained

### wasmer.toml
- **Purpose**: Wasmer package configuration
- **Contains**: Package metadata, module definitions, commands
- **Used by**: Wasmer CLI for packaging and deployment

### Dockerfile
- **Purpose**: Container configuration
- **Uses**: Node.js 18 Alpine (minimal image)
- **Exposes**: Port 3000
- **Runs**: Express server with HTTP endpoint

### server.js
- **Purpose**: Express HTTP server
- **Routes**:
  - `GET /` - Serves index.html
  - `GET /api/plugins` - Plugin information
  - `GET /api/status` - Server status
  - `GET /health` - Health check

### package.json
- **Purpose**: Node.js dependencies
- **Dependencies**: Express.js
- **Scripts**: start command for running server

## Environment Variables

```bash
# Port (default 3000)
export PORT=8080

# Node environment
export NODE_ENV=production
```

## Accessing Your Browser

### Local Development
```
http://localhost:3000
```

### Wasmer Edge
```
https://<your-namespace>.wasmer.app/internet-browser
```

### Docker
```
http://localhost:3000
```

## Features Available After Deployment

✅ **All Features Enabled:**
- Google Homepage
- Address Bar Navigation
- Back/Forward History
- Search Functionality
- **Flash Player 11** (Enabled)
- PDF Viewer
- Video Player
- **Wayback Machine Integration** (Browse by Year)
- Cookie Manager
- Plugin Management Panel

## Troubleshooting

### Port Already in Use
```bash
# Use different port
PORT=3001 npm start
```

### CORS Issues
```javascript
// Already handled in server.js
// Express serves static files with proper headers
```

### Network Issues
- Ensure firewall allows port 3000
- Check Docker daemon is running
- Verify Wasmer authentication with `wasmer whoami`

## Performance Optimization

### For Wasmer Edge:
1. Files are cached globally
2. Low latency delivery
3. Automatic HTTPS
4. CDN-backed distribution

### For Docker:
1. Alpine Linux keeps image size small (~150MB)
2. Multi-stage builds can optimize further
3. Layer caching for faster rebuilds

## Continuous Deployment

### GitHub Actions (Optional)

Create `.github/workflows/deploy.yml`:

```yaml
name: Deploy to Wasmer

on:
  push:
    branches: [main]

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - uses: wasmerio/setup-wasmer@v1
      - run: wasmer publish
        env:
          WASMER_TOKEN: ${{ secrets.WASMER_TOKEN }}
```

## Security Considerations

✅ **Already Implemented:**
- iFrame sandbox attributes
- Secure MIME type handling
- Plugin validation
- Static file serving

⚠️ **Additional Recommendations:**
- Use HTTPS only in production
- Implement rate limiting for API endpoints
- Add authentication if needed
- Regular security updates

## Support & Resources

- **Wasmer Docs**: https://docs.wasmer.io
- **Wasmer Community**: https://slack.wasmer.io
- **Repository**: https://github.com/papicef24/papicef24-Internet-Browser
- **Issues**: https://github.com/papicef24/papicef24-Internet-Browser/issues

## Next Steps

1. Install Wasmer CLI
2. Login with `wasmer login`
3. Run `wasmer publish` from repository root
4. Your browser will be available globally!

---

**Happy deploying! 🚀**
