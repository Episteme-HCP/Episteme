# JScience HPC - Native Libraries Auto-Installer
# PowerShell script to download and install all required native libraries

param(
    [string]$InstallDir = "C:\JScience-Native",
    [switch]$SkipMPI,
    [switch]$SkipHDF5,
    [switch]$SkipFFTW,
    [switch]$SkipBullet,
    [switch]$SkipOpenBLAS
)

$ErrorActionPreference = "Stop"

Write-Host "=== JScience HPC Native Libraries Installer ===" -ForegroundColor Cyan
Write-Host "Installation Directory: $InstallDir" -ForegroundColor Yellow
Write-Host ""

# Create installation directory
if (!(Test-Path $InstallDir)) {
    New-Item -ItemType Directory -Path $InstallDir | Out-Null
    Write-Host "✓ Created installation directory" -ForegroundColor Green
}

# Function to download and extract
function Install-Library {
    param(
        [string]$Name,
        [string]$Url,
        [string]$FileName,
        [string]$SubDir
    )
    
    Write-Host "`n--- Installing $Name ---" -ForegroundColor Cyan
    
    $downloadPath = Join-Path $env:TEMP $FileName
    $extractPath = Join-Path $InstallDir $SubDir
    
    try {
        # Download
        Write-Host "Downloading from $Url..." -ForegroundColor Yellow
        Invoke-WebRequest -Uri $Url -OutFile $downloadPath -UseBasicParsing
        Write-Host "✓ Downloaded" -ForegroundColor Green
        
        # Extract
        Write-Host "Extracting to $extractPath..." -ForegroundColor Yellow
        if ($FileName.EndsWith(".zip")) {
            Expand-Archive -Path $downloadPath -DestinationPath $extractPath -Force
        } elseif ($FileName.EndsWith(".tar.gz") -or $FileName.EndsWith(".tgz")) {
            # Requires tar (available in Windows 10+)
            tar -xzf $downloadPath -C $extractPath
        }
        Write-Host "✓ Extracted" -ForegroundColor Green
        
        # Cleanup
        Remove-Item $downloadPath -Force
        
        return $extractPath
    } catch {
        Write-Host "✗ Failed to install $Name : $_" -ForegroundColor Red
        return $null
    }
}

# 1. Install MPJ Express
if (!$SkipMPI) {
    $mpjPath = Install-Library `
        -Name "MPJ Express" `
        -Url "https://sourceforge.net/projects/mpjexpress/files/releases/mpj-v0_44.tar.gz/download" `
        -FileName "mpj-v0_44.tar.gz" `
        -SubDir "MPJ"
    
    if ($mpjPath) {
        [System.Environment]::SetEnvironmentVariable('MPJ_HOME', $mpjPath, 'Machine')
        Write-Host "✓ Set MPJ_HOME=$mpjPath" -ForegroundColor Green
    }
}

# 2. Install HDF5
if (!$SkipHDF5) {
    Write-Host "`n--- Installing HDF5 ---" -ForegroundColor Cyan
    Write-Host "Note: HDF5 Windows binaries require manual download from:" -ForegroundColor Yellow
    Write-Host "https://www.hdfgroup.org/downloads/hdf5/" -ForegroundColor Yellow
    Write-Host "Please download and extract to: $InstallDir\HDF5" -ForegroundColor Yellow
    
    # Alternative: Try to download pre-built binaries
    $hdf5Url = "https://support.hdfgroup.org/ftp/HDF5/releases/hdf5-1.14/hdf5-1.14.3/bin/windows/hdf5-1.14.3-2-win-vs2022_intel.zip"
    $hdf5Path = Install-Library `
        -Name "HDF5" `
        -Url $hdf5Url `
        -FileName "hdf5-1.14.3.zip" `
        -SubDir "HDF5"
    
    if ($hdf5Path) {
        [System.Environment]::SetEnvironmentVariable('HDF5_DIR', $hdf5Path, 'Machine')
        $env:PATH += ";$hdf5Path\bin"
        [System.Environment]::SetEnvironmentVariable('PATH', $env:PATH, 'Machine')
        Write-Host "✓ Added HDF5 to PATH" -ForegroundColor Green
    }
}

# 3. Install FFTW3
if (!$SkipFFTW) {
    $fftwPath = Install-Library `
        -Name "FFTW3" `
        -Url "http://www.fftw.org/fftw-3.3.10-dll64.zip" `
        -FileName "fftw-3.3.10-dll64.zip" `
        -SubDir "FFTW3"
    
    if ($fftwPath) {
        $env:PATH += ";$fftwPath"
        [System.Environment]::SetEnvironmentVariable('PATH', $env:PATH, 'Machine')
        Write-Host "✓ Added FFTW3 to PATH" -ForegroundColor Green
        
        # Generate import libraries
        Write-Host "Generating import libraries..." -ForegroundColor Yellow
        Push-Location $fftwPath
        try {
            lib /def:libfftw3-3.def /out:libfftw3-3.lib /machine:x64 2>&1 | Out-Null
            lib /def:libfftw3f-3.def /out:libfftw3f-3.lib /machine:x64 2>&1 | Out-Null
            lib /def:libfftw3l-3.def /out:libfftw3l-3.lib /machine:x64 2>&1 | Out-Null
            Write-Host "✓ Generated import libraries" -ForegroundColor Green
        } catch {
            Write-Host "⚠ Could not generate import libraries (lib.exe not found)" -ForegroundColor Yellow
        }
        Pop-Location
    }
}

# 4. Install Bullet Physics
if (!$SkipBullet) {
    Write-Host "`n--- Installing Bullet Physics ---" -ForegroundColor Cyan
    Write-Host "Bullet Physics requires building from source." -ForegroundColor Yellow
    Write-Host "Instructions:" -ForegroundColor Yellow
    Write-Host "1. Install CMake: https://cmake.org/download/" -ForegroundColor White
    Write-Host "2. Clone Bullet3: git clone https://github.com/bulletphysics/bullet3.git" -ForegroundColor White
    Write-Host "3. Build with CMake (see NATIVE_LIBS_INSTALLATION.md)" -ForegroundColor White
    
    # Check if git is available
    if (Get-Command git -ErrorAction SilentlyContinue) {
        $bulletPath = Join-Path $InstallDir "bullet3"
        if (!(Test-Path $bulletPath)) {
            Write-Host "Cloning Bullet3 repository..." -ForegroundColor Yellow
            git clone https://github.com/bulletphysics/bullet3.git $bulletPath
            Write-Host "✓ Cloned Bullet3" -ForegroundColor Green
            Write-Host "⚠ You must build Bullet3 manually with CMake" -ForegroundColor Yellow
        } else {
            Write-Host "✓ Bullet3 already cloned at $bulletPath" -ForegroundColor Green
        }
    }
}

# 5. Install OpenBLAS
if (!$SkipOpenBLAS) {
    Write-Host "`n--- Installing OpenBLAS ---" -ForegroundColor Cyan
    $openblasUrl = "https://github.com/xianyi/OpenBLAS/releases/download/v0.3.25/OpenBLAS-0.3.25-x64.zip"
    $openblasPath = Install-Library `
        -Name "OpenBLAS" `
        -Url $openblasUrl `
        -FileName "OpenBLAS-0.3.25-x64.zip" `
        -SubDir "OpenBLAS"
    
    if ($openblasPath) {
        $env:PATH += ";$openblasPath\bin"
        [System.Environment]::SetEnvironmentVariable('PATH', $env:PATH, 'Machine')
        Write-Host "✓ Added OpenBLAS to PATH" -ForegroundColor Green
    }
}

# 6. Install CFITSIO
Write-Host "`n--- Installing CFITSIO ---" -ForegroundColor Cyan
Write-Host "CFITSIO Windows binaries are not readily available." -ForegroundColor Yellow
Write-Host "Options:" -ForegroundColor Yellow
Write-Host "1. Build from source: https://heasarc.gsfc.nasa.gov/fitsio/" -ForegroundColor White
Write-Host "2. Use vcpkg: vcpkg install cfitsio:x64-windows" -ForegroundColor White

# Summary
Write-Host "`n=== Installation Summary ===" -ForegroundColor Cyan
Write-Host "Installation directory: $InstallDir" -ForegroundColor White

$installedLibs = @()
if (Test-Path (Join-Path $InstallDir "MPJ")) { $installedLibs += "MPJ Express" }
if (Test-Path (Join-Path $InstallDir "HDF5")) { $installedLibs += "HDF5" }
if (Test-Path (Join-Path $InstallDir "FFTW3")) { $installedLibs += "FFTW3" }
if (Test-Path (Join-Path $InstallDir "bullet3")) { $installedLibs += "Bullet3 (source)" }
if (Test-Path (Join-Path $InstallDir "OpenBLAS")) { $installedLibs += "OpenBLAS" }

Write-Host "`nInstalled libraries:" -ForegroundColor Green
$installedLibs | ForEach-Object { Write-Host "  ✓ $_" -ForegroundColor Green }

Write-Host "`nNext steps:" -ForegroundColor Yellow
Write-Host "1. Restart your terminal to apply PATH changes" -ForegroundColor White
Write-Host "2. Run verify_native_libs.ps1 to verify installation" -ForegroundColor White
Write-Host "3. Build Bullet Physics if needed (see NATIVE_LIBS_INSTALLATION.md)" -ForegroundColor White
Write-Host "4. Add MPJ jar to your Maven dependencies" -ForegroundColor White

Write-Host "`n✓ Installation complete!" -ForegroundColor Green
