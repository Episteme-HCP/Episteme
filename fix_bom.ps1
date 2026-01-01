$rootDir = Get-Location
$excludedDirs = @("jscience-old-v1", ".git", "target", "node_modules", "gen", ".gemini", "javadoc")

# Get all Java files recursively, excluding specific directories
$files = Get-ChildItem -Path $rootDir -Recurse -Filter "*.java" | Where-Object {
    $path = $_.FullName
    $exclude = $false
    foreach ($dir in $excludedDirs) {
        if ($path -match [regex]::Escape($dir)) {
            $exclude = $true
            break
        }
    }
    -not $exclude
}

$utf8NoBom = New-Object System.Text.UTF8Encoding($false)
$count = 0

foreach ($file in $files) {
    try {
        # Read absolute path
        $content = [System.IO.File]::ReadAllText($file.FullName)
        
        # Write back without BOM
        [System.IO.File]::WriteAllText($file.FullName, $content, $utf8NoBom)
        
        $count++
        if ($count % 100 -eq 0) {
            Write-Host "Processed $count files..."
        }
    } catch {
        Write-Host "Error processing: $($file.FullName) - $_"
    }
}

Write-Host "Finished removing BOM from $count files."
