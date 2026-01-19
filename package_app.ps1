$ErrorActionPreference = "Stop"

# Define paths
$projectRoot = "c:\Silvere\Encours\Developpement\JScience"
$targetDir = "$projectRoot\launchers\packaged"
$libDir = "$targetDir\lib"

# 1. Build the project
Write-Host "Building project..."
mvn clean install "-DskipTests" -f "$projectRoot\pom.xml"
if ($LASTEXITCODE -ne 0) { throw "Build failed" }

# 2. Prepare directories
if (Test-Path $targetDir) { Remove-Item -Recurse -Force $targetDir }
New-Item -ItemType Directory -Path $libDir -Force | Out-Null

# 3. Copy dependencies
Write-Host "Copying dependencies..."
mvn dependency:copy-dependencies "-DoutputDirectory=$libDir" "-DincludeScope=runtime" "-DskipTests" -f "$projectRoot\pom.xml"

# 4. Copy project jars
Write-Host "Copying project jars..."
$modules = @("jscience-core", "jscience-natural", "jscience-social", "jscience-benchmarks", "jscience-featured-apps", "jscience-jni", "jscience-server", "jscience-worker", "jscience-client")
foreach ($mod in $modules) {
    Copy-Item "$projectRoot\$mod\target\*.jar" $libDir -Force
}

# 5. Create Launch Script
$mainClass = "org.jscience.ui.JScienceMasterControl"
$scriptContent = "@echo off
setlocal
set CP=lib\*
java -cp %CP% $mainClass
endlocal"

Set-Content -Path "$targetDir\run.bat" -Value $scriptContent

Write-Host "Packaging complete at $targetDir"
