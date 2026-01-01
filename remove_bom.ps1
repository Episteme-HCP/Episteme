$rootDir = Get-Location
$excludedDirs = @("jscience-old-v1", ".git", "target", "node_modules", "gen", ".gemini", "javadoc")

$utf8NoBom = New-Object System.Text.UTF8Encoding $false

# Recursively get all .java files
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

$count = 0
foreach ($file in $files) {
    $bytes = [System.IO.File]::ReadAllBytes($file.FullName)
    
    # Check for UTF-8 BOM (EF BB BF)
    if ($bytes.Length -ge 3 -and $bytes[0] -eq 0xEF -and $bytes[1] -eq 0xBB -and $bytes[2] -eq 0xBF) {
        # Remove BOM by writing bytes starting from index 3
        # But safer to just read as string and write back with NoBOM encoding
        $content = [System.IO.File]::ReadAllText($file.FullName)
        [System.IO.File]::WriteAllText($file.FullName, $content, $utf8NoBom)
        $count++
        Write-Host "Removed BOM: $($file.FullName)"
    }
}

Write-Host "Total files fixed: $count"
