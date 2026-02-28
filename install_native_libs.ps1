# Episteme HPC - Native Libraries Auto-Installer
# PowerShell script to download and install required native libraries
# Usage: .\install_native_libs.ps1 [[-Libraries] <String[]>] [-InstallDir <String>]
# Example: .\install_native_libs.ps1 -Libraries HDF5, OpenBLAS
# Example: .\install_native_libs.ps1 (Installs All)

param(
    [Parameter(Position=0)]
    [ValidateSet("All", "MPJ", "HDF5", "FFTW", "Bullet", "OpenBLAS")]
    [string[]]$Libraries = @("All"),
    
    [string]$InstallDir = (Join-Path $PSScriptRoot "libs")
)

$ErrorActionPreference = "Stop"
$ScriptDir = $PSScriptRoot

Write-Host "=== Episteme HPC Native Libraries Installer ===" -ForegroundColor Cyan
Write-Host "Installation Directory: $InstallDir" -ForegroundColor Yellow
Write-Host "Selected Libraries: $($Libraries -join ', ')" -ForegroundColor Yellow
Write-Host "Script Directory: $ScriptDir" -ForegroundColor White
Write-Host ""

# Create installation directory
if (!(Test-Path $InstallDir)) {
    New-Item -ItemType Directory -Path $InstallDir -Force | Out-Null
    Write-Host "[OK] Created installation directory" -ForegroundColor Green
}

# Function to download or use local archive
function Install-Library {
    param(
        [string]$Name,
        [string]$Url,
        [string]$FileNamePattern, 
        [string]$SubDir,
        [scriptblock]$PostInstallAction = $null
    )
    
    Write-Host "`n--- Installing $Name ---" -ForegroundColor Cyan
    
    # 1. Search for local archive (in script dir or current dir)
    $localArchive = $null
    
    # Try exact match first if FileNamePattern is a specific file, else glob
    $patterns = @($FileNamePattern)
    if ($FileNamePattern -notlike "*") { $patterns += "$FileNamePattern" }
    
    foreach ($pat in $patterns) {
        $candidates = @(Get-ChildItem -Path $ScriptDir -Filter $pat -ErrorAction SilentlyContinue)
        $candidates += @(Get-ChildItem -Path (Get-Location) -Filter $pat -ErrorAction SilentlyContinue)
        
        if ($candidates.Count -gt 0) {
            $localArchive = $candidates[0].FullName
            break
        }
    }

    $extractPath = Join-Path $InstallDir $SubDir
    $archiveToExtract = $null
    $downloaded = $false

    try {
        if ($localArchive) {
            Write-Host "[INFO] Found local archive: $localArchive" -ForegroundColor Green
            $archiveToExtract = $localArchive
        } else {
             # Download
             if ([string]::IsNullOrWhiteSpace($Url)) {
                 Write-Host "[WARN] No download URL available for $Name and no local archive found." -ForegroundColor Yellow
                 Write-Host "       Please download '$FileNamePattern' manually and place it in this folder." -ForegroundColor Yellow
                 return $null
             }
             
             # Use the pattern as filename if it's a specific name, else use a default
             $dlFileName = if ($FileNamePattern -notlike "*") { $FileNamePattern } else { "$Name.zip" }
             $downloadPath = Join-Path $env:TEMP $dlFileName
             
             Write-Host "Downloading from $Url..." -ForegroundColor Yellow
             try {
                Invoke-WebRequest -Uri $Url -OutFile $downloadPath -UseBasicParsing
                Write-Host "[OK] Downloaded" -ForegroundColor Green
                $archiveToExtract = $downloadPath
                $downloaded = $true
             } catch {
                 Write-Host "[ERROR] Download failed: $($_.Exception.Message)" -ForegroundColor Red
                 Write-Host "       Please download '$dlFileName' manually and place it in this folder." -ForegroundColor Yellow
                 return $null
             }
        }
        
        # Extract
        if (-not (Test-Path $extractPath)) {
            New-Item -ItemType Directory -Path $extractPath -Force | Out-Null
        }
        
        Write-Host "Extracting to $extractPath..." -ForegroundColor Yellow
        
        $ext = [System.IO.Path]::GetExtension($archiveToExtract).ToLower()
        if ($ext -eq ".zip") {
            Expand-Archive -Path $archiveToExtract -DestinationPath $extractPath -Force
        } elseif ($ext -eq ".gz" -or $ext -eq ".tgz") {
            # Requires tar (available in Windows 10+)
            # Note: tar usually extracts to a subdirectory using the internal structure
            # We assume user wants it in $extractPath or its subfolder
            tar -xzf $archiveToExtract -C $InstallDir 
            
            # Heuristic: If tar created a folder like "mpj-v0_44", move its content or rename it
            # This is tricky without knowing exact internal structure
            if ($Name -eq "MPJ Express") {
                 $extractedFolder = Join-Path $InstallDir "mpj-v0_44"
                 if (Test-Path $extractedFolder) {
                    if (Test-Path $extractPath) { Remove-Item $extractPath -Recurse -Force }
                    Rename-Item -Path $extractedFolder -NewName $SubDir -Force
                 }
            }
        } elseif ($ext -eq ".msi") {
             Write-Host "[INFO] Installing MSI..." -ForegroundColor Yellow
             Start-Process -FilePath "msiexec.exe" -ArgumentList "/i `"$archiveToExtract`" /quiet /norestart TARGETDIR=`"$extractPath`"" -Wait -NoNewWindow
        }
        
        Write-Host "[OK] Extracted/Installed" -ForegroundColor Green
        
        # Cleanup download
        if ($downloaded) {
            Remove-Item $archiveToExtract -Force
        }
        
        # Run post-install logic
        if ($PostInstallAction) {
            & $PostInstallAction -InstallPath $extractPath
        }

        return $extractPath

    } catch {
        Write-Host "[ERROR] Failed to install $Name : $_" -ForegroundColor Red
        return $null
    }
}

# Add-Paths Function
function Add-UserPath {
    param([string]$NewPath)
    $currentPath = [System.Environment]::GetEnvironmentVariable('PATH', 'User')
    if ($currentPath -split ';' -notcontains $NewPath) {
        $cleanPath = $currentPath.TrimEnd(';') + ";$NewPath"
        [System.Environment]::SetEnvironmentVariable('PATH', $cleanPath, 'User')
        Write-Host "[OK] Added to User PATH: $NewPath" -ForegroundColor Green
    } else {
        Write-Host "[OK] Already in User PATH: $NewPath" -ForegroundColor Green
    }
}


# --- Execution Loop ---

# 1. MPJ Express
if ($Libraries -contains "All" -or $Libraries -contains "MPJ") {
    $mpjPath = Install-Library `
        -Name "MPJ Express" `
        -Url "https://sourceforge.net/projects/mpjexpress/files/releases/mpj-v0_44.tar.gz/download?use_mirror=autoselect" `
        -FileNamePattern "mpj-*.tar.gz" `
        -SubDir "MPJ"

    if ($mpjPath) {
        [System.Environment]::SetEnvironmentVariable('MPJ_HOME', $mpjPath, 'User')
        Write-Host "[OK] Set MPJ_HOME=$mpjPath" -ForegroundColor Green
        # Add MPJ bin to PATH check? Usually needed.
        Add-UserPath (Join-Path $mpjPath "bin")
    }
}

# 2. HDF5
if ($Libraries -contains "All" -or $Libraries -contains "HDF5") {
    # HDF5 is tricky. URL often dead or behind login.
    # We check for MSI first because it's usually the binary installer
    $hdf5Path = Install-Library `
        -Name "HDF5" `
        -Url "" `
        -FileNamePattern "hdf5-*.msi" `
        -SubDir "HDF5"

    if (-not $hdf5Path) {
        # Fallback to ZIP
        $hdf5Path = Install-Library `
            -Name "HDF5" `
            -Url "" `
            -FileNamePattern "hdf5-*.zip" `
            -SubDir "HDF5"
    }

    if ($hdf5Path) {
        [System.Environment]::SetEnvironmentVariable('HDF5_DIR', $hdf5Path, 'User')
        Add-UserPath (Join-Path $hdf5Path "bin")
    }
}

# 3. FFTW3
if ($Libraries -contains "All" -or $Libraries -contains "FFTW") {
    $fftwPath = Install-Library `
        -Name "FFTW3" `
        -Url "http://www.fftw.org/fftw-3.3.5-dll64.zip" `
        -FileNamePattern "fftw-*.zip" `
        -SubDir "FFTW3" `
        -PostInstallAction {
            param($InstallPath)
            Write-Host "Generating import libraries..." -ForegroundColor Yellow
            Push-Location $InstallPath
            if (Get-Command lib -ErrorAction SilentlyContinue) {
                try {
                    lib /def:libfftw3-3.def /out:libfftw3-3.lib /machine:x64 2>&1 | Out-Null
                    lib /def:libfftw3f-3.def /out:libfftw3f-3.lib /machine:x64 2>&1 | Out-Null
                    lib /def:libfftw3l-3.def /out:libfftw3l-3.lib /machine:x64 2>&1 | Out-Null
                    Write-Host "[OK] Generated import libraries" -ForegroundColor Green
                } catch { Write-Host "[WARN] Lib generation failed: $_" -ForegroundColor Yellow }
            } else {
                 Write-Host "[WARN] 'lib.exe' not found (VS tools missing?). Skipping lib generation." -ForegroundColor Yellow
            }
            Pop-Location
        }

    if ($fftwPath) {
        Add-UserPath $fftwPath
    }
}

# 4. Bullet Physics
if ($Libraries -contains "All" -or $Libraries -contains "Bullet") {
    if (Get-Command git -ErrorAction SilentlyContinue) {
        $bulletPath = Join-Path $InstallDir "bullet3"
        if (!(Test-Path $bulletPath)) {
            Write-Host "`n--- Installing Bullet Physics ---" -ForegroundColor Cyan
            Write-Host "Cloning Bullet3..." -ForegroundColor Yellow
            git clone https://github.com/bulletphysics/bullet3.git $bulletPath
            Write-Host "[OK] Cloned Bullet3" -ForegroundColor Green
        } else {
            Write-Host "`n--- Bullet Physics ---" -ForegroundColor Cyan
            Write-Host "[OK] Already cloned" -ForegroundColor Green
        }
    } else {
        Write-Host "`n[WARN] git not found. Skipping Bullet3." -ForegroundColor Yellow
    }
}

# 5. OpenBLAS
if ($Libraries -contains "All" -or $Libraries -contains "OpenBLAS") {
    $openblasPath = Install-Library `
        -Name "OpenBLAS" `
        -Url "https://github.com/xianyi/OpenBLAS/releases/download/v0.3.25/OpenBLAS-0.3.25-x64.zip" `
        -FileNamePattern "OpenBLAS-*.zip" `
        -SubDir "OpenBLAS"

    if ($openblasPath) {
        Add-UserPath (Join-Path $openblasPath "bin")
    }
}

Write-Host "`n[OK] Installation/Configuration complete!" -ForegroundColor Green
Write-Host "Please restart your terminal to apply environment variable changes." -ForegroundColor White
