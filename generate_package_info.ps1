$header = @"
/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
"@

$rootDir = Get-Location
$excludedDirs = @("jscience-old-v1", ".git", "target", "node_modules", "gen", ".gemini", "javadoc")

# Function to get relative java package name from path
function Get-PackageName {
    param (
        [string]$Path
    )
    # Assume source root is \src\main\java\
    if ($Path -match "src\\main\\java\\(.*)") {
        return $matches[1].Replace("\", ".")
    }
    return $null
}

# Find all directories containing .java files
$directories = Get-ChildItem -Path $rootDir -Recurse -Directory | Where-Object {
    $dirPath = $_.FullName
    $exclude = $false
    foreach ($dir in $excludedDirs) {
        if ($dirPath -match [regex]::Escape($dir)) {
            $exclude = $true
            break
        }
    }
    if ($exclude) { return $false }
    
    # Check if contains java files
    if ((Get-ChildItem -Path $dirPath -Filter "*.java").Count -gt 0) {
        return $true
    }
    return $false
}

$count = 0
foreach ($dir in $directories) {
    $packageInfoPath = Join-Path $dir.FullName "package-info.java"
    
    if (-not (Test-Path $packageInfoPath)) {
        $packageName = Get-PackageName -Path $dir.FullName
        
        if ($packageName) {
            $description = "Provides classes and interfaces for $packageName."
            
            $content = @"
$header

/**
 * $description
 */
package $packageName;
"@
            Set-Content -Path $packageInfoPath -Value $content -Encoding UTF8
            Write-Host "Created: $packageInfoPath"
            $count++
        }
    }
}

Write-Host "Total package-info.java files created: $count"
