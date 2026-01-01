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

$count = 0
foreach ($file in $files) {
    $content = Get-Content -Path $file.FullName -Raw
    
    # Regex to identify the start of the package declaration
    # We replace everything before 'package' with the new header
    # Check if file has a package declaration
    if ($content -match "(?s)(.*?)((?m)^\s*package\s+.*)") {
        $packageDecl = $matches[2]
        # Only replace if the existing header is different (simple string check might fail due to whitespace, 
        # so we just overwrite identifying that we are putting the header before package)
        
        $newContent = $header + "`n`n" + $packageDecl
        
        # Write back if changed (simplified check: simplistic overwriting is safer than complex diffing for headers)
        Set-Content -Path $file.FullName -Value $newContent -Encoding UTF8
        $count++
        Write-Host "Updated: $($file.FullName)"
    } else {
        # Default package or module-info or something else
        if ($file.Name -eq "module-info.java") {
             # Replace everything before 'module'
             if ($content -match "(?s)(.*?)(\bopen\s+module|\bmodule\s+.*)") {
                  $moduleDecl = $matches[2]
                  $newContent = $header + "`n`n" + $moduleDecl
                  Set-Content -Path $file.FullName -Value $newContent -Encoding UTF8
                  $count++
                  Write-Host "Updated module-info: $($file.FullName)"
             }
        }
    }
}

Write-Host "Total files updated: $count"
