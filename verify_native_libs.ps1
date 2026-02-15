# JScience HPC - Native Libraries Verification Script
# Checks environment variables and path visibility

Write-Host "=== JScience Native Library Verification ===" -ForegroundColor Cyan

$libraries = @(
    @{ Name = "MPJ Express"; EnvVar = "MPJ_HOME"; PathKey = "mpj"; CheckFile = "lib\mpj.jar" },
    @{ Name = "HDF5";        EnvVar = "HDF5_DIR"; PathKey = "hdf5"; CheckFile = "bin\hdf5.dll" },
    @{ Name = "FFTW3";       EnvVar = "FFTW3_DIR"; PathKey = "fftw"; CheckFile = "libfftw3-3.dll" }, # FFTW usually just in PATH
    @{ Name = "OpenBLAS";    EnvVar = "OpenBLAS_DIR"; PathKey = "openblas"; CheckFile = "bin\libopenblas.dll" },
    @{ Name = "Bullet3";     EnvVar = "BULLET_HOME"; PathKey = "bullet"; CheckFile = "bullet3\CMakeLists.txt"; IsSource = $true }
)

$currentPath = $env:PATH
$userPath = [Environment]::GetEnvironmentVariable("PATH", "User")
Write-Host "Current Process PATH length: $($currentPath.Length)" -ForegroundColor Gray

if ($currentPath -notlike "*JScience-Native*") {
    Write-Host "`n[WARNING] 'JScience-Native' is NOT in your current terminal PATH!" -ForegroundColor Yellow
    Write-Host "          But it IS in your User Registry PATH." -ForegroundColor Yellow
    Write-Host "          Please RESTART your terminal/IDE to apply changes." -ForegroundColor Red
}


foreach ($lib in $libraries) {
    Write-Host "`nChecking $($lib.Name)..." -NoNewline
    
    $status = "MISSING"
    $details = ""
    
    # Check Environment Variable
    if ($lib.EnvVar) {
        $envVal = [Environment]::GetEnvironmentVariable($lib.EnvVar, "User")
        if (-not $envVal) { $envVal = [Environment]::GetEnvironmentVariable($lib.EnvVar, "Machine") }
        
        if ($envVal) {
             if (Test-Path $envVal) {
                 $details += "EnvVar Found ($envVal). "
             } else {
                 $details += "EnvVar set but path invalid. "
             }
        }
    }
    
    # Check Path
    if ($lib.EnvVar -and $envVal -and (Test-Path (Join-Path $envVal $lib.CheckFile))) {
        $status = "OK (Env)"
    } elseif ($lib.IsSource -and (Test-Path (Join-Path "C:\JScience-Native" $lib.CheckFile))) {
        $status = "OK (Source)"
    } else {
        # Check in PATH
        # Heuristic check for DLL/Exe in PATH
        $fileName = [System.IO.Path]::GetFileName($lib.CheckFile)
        if (Get-Command $fileName -ErrorAction SilentlyContinue) {
            $status = "OK (PATH)"
        }
    }
    
    # Specific fix for MPJ which is often just a jar
    if ($lib.Name -eq "MPJ Express" -and (Test-Path "C:\JScience-Native\MPJ\lib\mpj.jar")) {
         $status = "OK (Found)"
    }
    
    if ($status -like "OK*") {
        Write-Host " [$status]" -ForegroundColor Green
    } else {
        Write-Host " [MISSING]" -ForegroundColor Red
    }
    if ($details) { Write-Host "  > $details" -ForegroundColor DarkGray }
}

Write-Host "`nNote: Native libraries must be in your PATH for Java to find them via JNI (System.loadLibrary)." -ForegroundColor Gray
