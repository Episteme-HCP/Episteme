/** @type {import('next').NextConfig} */
const nextConfig = {
    reactStrictMode: true,
    async rewrites() {
        return [
            {
                source: '/api/grpc/:path*',
                destination: 'http://localhost:8080/api/:path*'
            },
            {
                source: '/metrics',
                destination: 'http://localhost:9090/metrics'
            }
        ]
    }
}

module.exports = nextConfig
