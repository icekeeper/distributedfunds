const proxy = require('http-proxy-middleware');

module.exports = {
    port: 3000,
    open: false,
    server: {
        baseDir: "src",
        middleware: [proxy('/api', {target: 'http://localhost:8090/'})],
        routes: {
            "/node_modules": "node_modules"
        }
    }
};