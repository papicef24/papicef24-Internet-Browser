FROM node:18-alpine

WORKDIR /app

# Copy all browser files
COPY index.html .
COPY styles.css .
COPY app.js .
COPY plugin-manager.js .
COPY package.json .
COPY server.js .

# Install dependencies
RUN npm install

# Expose port 3000
EXPOSE 3000

# Start the server
CMD ["node", "server.js"]
