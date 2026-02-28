# Episteme HPC - Benchmark Dependencies Installer (Windows)
# Focuses on BLAS/LAPACK and performance counters

param(
    [string]$InstallDir = (Join-Path $PSScriptRoot "libs")
)

Write-Host "=== Episteme HPC Benchmark Dependencies Installer (Windows) ===" -ForegroundColor Cyan

if (!(Test-Path $InstallDir)) {
    New-Item -ItemType Directory -Path $InstallDir | Out-Null
}

# 1. Download OpenBLAS Optimized for Windows Benchmarking
$openblasUrl = "https://github.com/xianyi/OpenBLAS/releases/download/v0.3.26/OpenBLAS-0.3.26-x64.zip"
$destZip = "$env:TEMP\OpenBLAS-Bench.zip"

Write-Host "Downloading OpenBLAS for benchmarks..." -ForegroundColor Yellow
Invoke-WebRequest -Uri $openblasUrl -OutFile $destZip -UseBasicParsing
Expand-Archive -Path $destZip -DestinationPath "$InstallDir\OpenBLAS" -Force

# 2. Add to User Path
$currentPath = [Environment]::GetEnvironmentVariable("Path", "User")
$binPath = "$InstallDir\OpenBLAS\bin"
if ($currentPath -notlike "*$binPath*") {
    [Environment]::SetEnvironmentVariable("Path", "$currentPath;$binPath", "User")
    Write-Host "✓ Added $binPath to Path" -ForegroundColor Green
}

Write-Host "✓ Benchmark dependencies download complete." -ForegroundColor Green
Write-Host "Note: For JMH benchmarks, ensure your power plan is set to 'High Performance'." -ForegroundColor Yellow
