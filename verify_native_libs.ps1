# Episteme HPC - Native Libraries Verification Script
# Checks environment variables, local paths, and path visibility

Write-Host "=== Episteme Native Library Verification ===" -ForegroundColor Cyan
Write-Host ""

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$libsDir = Join-Path $scriptDir "libs"

$libraries = @(
    @{ Name = "MPJ Express"; EnvVar = "MPJ_HOME"; LocalDir = "MPJ"; CheckFile = "lib\mpj.jar"; Type = "java" },
    @{ Name = "HDF5";        EnvVar = "HDF5_DIR"; LocalDir = "HDF5"; CheckFile = "bin\hdf5.dll"; Type = "native" },
    @{ Name = "FFTW3";       EnvVar = "FFTW3_DIR"; LocalDir = "FFTW3"; CheckFile = "libfftw3-3.dll"; Type = "native" },
    @{ Name = "OpenBLAS";    EnvVar = "OpenBLAS_DIR"; LocalDir = "OpenBLAS"; CheckFile = "bin\libopenblas.dll"; Type = "native" },
    @{ Name = "Bullet3";     EnvVar = "BULLET_HOME"; LocalDir = "Bullet3DLL"; CheckFile = "libbulletc.dll"; Type = "native" },
    @{ Name = "TarsosDSP";   EnvVar = $null; LocalDir = "Tarsos\Tarsos-master"; CheckFile = "lib\TarsosDSP-2.4.jar"; Type = "java" },
    @{ Name = "oneDNN";      EnvVar = "DNNL_DIR"; LocalDir = "oneDNN\oneDNN-main"; CheckFile = "CMakeLists.txt"; Type = "source" }
)

$currentPath = $env:PATH
$userPath = [Environment]::GetEnvironmentVariable("PATH", "User")
Write-Host "Script directory: $scriptDir" -ForegroundColor Gray
Write-Host "Libs directory:   $libsDir" -ForegroundColor Gray
Write-Host ""

$foundCount = 0
$totalCount = $libraries.Count

foreach ($lib in $libraries) {
    Write-Host "Checking $($lib.Name)..." -NoNewline
    
    $status = "MISSING"
    $details = ""
    $foundPath = $null
    
    # 1. Check Environment Variable
    if ($lib.EnvVar) {
        $envVal = [Environment]::GetEnvironmentVariable($lib.EnvVar, "User")
        if (-not $envVal) { $envVal = [Environment]::GetEnvironmentVariable($lib.EnvVar, "Machine") }
        
        if ($envVal) {
            if (Test-Path $envVal) {
                $details += "EnvVar $($lib.EnvVar)=$envVal. "
                $checkPath = Join-Path $envVal $lib.CheckFile
                if (Test-Path $checkPath) {
                    $status = "OK (Env)"
                    $foundPath = $checkPath
                }
            } else {
                $details += "EnvVar set but path invalid ($envVal). "
            }
        }
    }
    
    # 2. Check local libs/ directory
    if ($status -eq "MISSING") {
        $localPath = Join-Path $libsDir $lib.LocalDir
        if (Test-Path $localPath) {
            $checkPath = Join-Path $localPath $lib.CheckFile
            if (Test-Path $checkPath) {
                $status = "OK (Local)"
                $foundPath = $checkPath
                $details += "Found at $checkPath. "
            } else {
                $details += "Dir exists ($localPath) but check file missing. "
            }
        }
    }
    
    # 3. Check in system PATH (for DLLs/exes)
    if ($status -eq "MISSING" -and $lib.Type -eq "native") {
        $fileName = [System.IO.Path]::GetFileName($lib.CheckFile)
        if (Get-Command $fileName -ErrorAction SilentlyContinue) {
            $status = "OK (PATH)"
        }
    }
    
    if ($status -like "OK*") {
        $foundCount++
        Write-Host " [$status]" -ForegroundColor Green
    } else {
        Write-Host " [MISSING]" -ForegroundColor Red
    }
    Write-Host "  Type: $($lib.Type)" -ForegroundColor DarkGray
    if ($details) { Write-Host "  > $details" -ForegroundColor DarkGray }
}

Write-Host ""
Write-Host "=== Summary: $foundCount / $totalCount libraries found ===" -ForegroundColor $(if ($foundCount -eq $totalCount) { "Green" } else { "Yellow" })
Write-Host ""
Write-Host "Notes:" -ForegroundColor Gray
Write-Host "  - 'native' libraries must be in PATH or pointed to by env vars for FFM/JNI to find them." -ForegroundColor Gray
Write-Host "  - 'java' libraries are resolved via Maven or classpath." -ForegroundColor Gray
Write-Host "  - 'source' libraries must be compiled first (see their README)." -ForegroundColor Gray
