#!/bin/bash
# Episteme HPC - Benchmark Dependencies Installer (Linux)
set -e

echo "=== Episteme HPC Benchmark Dependencies Installer (Linux) ==="

if [ "$EUID" -ne 0 ]; then 
    echo "Please run as root (use sudo)"
    exit 1
fi

# Packages for benchmarks (Core computation + Native wrappers)
PACKAGES=(
    "libopenblas-dev"
    "liblapack-dev"
    "libfftw3-dev"
    "libatlas-base-dev"
    "gfortran"
    "pkg-config"
    "cpufrequtils" # For performance tuning
)

echo "Installing packages: ${PACKAGES[@]}"
apt-get update -qq
apt-get install -y "${PACKAGES[@]}"

# Performance tuning: Set CPU to 'performance' mode for stable benchmarks
echo "Setting CPU governor to performance..."
for i in /sys/devices/system/cpu/cpu*/cpufreq/scaling_governor; do
    [ -f $i ] && echo "performance" > $i || true
done

echo "✓ Benchmark dependencies installed."
