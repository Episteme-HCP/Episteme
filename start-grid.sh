#!/bin/bash
# Start JScience Grid with Docker Compose
echo "Starting JScience Grid..."
echo ""
echo "This will start:"
echo "  - 1x JScience Server (port 50051)"
echo "  - 5x JScience Workers"
echo "  - Prometheus (port 9090)"
echo "  - Grafana (port 3000)"
echo ""

docker-compose up -d --build

echo ""
echo "Grid is starting! Check status with: docker-compose ps"
echo ""
echo "Grafana Dashboard: http://localhost:3000 (admin/jscience)"
echo "Prometheus:        http://localhost:9090"
echo "gRPC Server:       localhost:50051"
