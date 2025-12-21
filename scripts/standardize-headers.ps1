# JScience Header Standardization Script
# Updates all Java files with uniform MIT license headers

$standardHeader = @"
/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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

$authorBlock = @"
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
"@

param(
    [string]$Module = "jscience-core",
    [string]$Package = "",
    [switch]$DryRun = $false,
    [switch]$Report = $false
)

$basePath = "C:\Silvere\Encours\Developpement\JScience"
$sourcePath = "$basePath\$Module\src\main\java"
if ($Package) {
    $sourcePath = "$sourcePath\$($Package.Replace('.', '\'))"
}

$files = Get-ChildItem -Path $sourcePath -Filter "*.java" -Recurse -ErrorAction SilentlyContinue

$stats = @{
    Total = $files.Count
    NeedUpdate = 0
    Updated = 0
    AlreadyCorrect = 0
}

foreach ($file in $files) {
    $content = Get-Content $file.FullName -Raw
    
    # Check if already has standard header
    $hasStandardHeader = $content.StartsWith("/*`n * JScience - Java(TM) Tools")
    $hasAuthors = $content -match "@author Silvere Martin-Michiellot" -and $content -match "@author Gemini AI"
    $hasSince = $content -match "@since 1\.\d"
    
    if ($hasStandardHeader -and $hasAuthors -and $hasSince) {
        $stats.AlreadyCorrect++
        if ($Report) { Write-Host "OK: $($file.Name)" -ForegroundColor Green }
    } else {
        $stats.NeedUpdate++
        if ($Report) { 
            Write-Host "NEEDS UPDATE: $($file.FullName)" -ForegroundColor Yellow 
            if (-not $hasStandardHeader) { Write-Host "  - Missing standard header" }
            if (-not $hasAuthors) { Write-Host "  - Missing author tags" }
            if (-not $hasSince) { Write-Host "  - Missing @since tag" }
        }
        
        if (-not $DryRun) {
            # Remove old header (everything before 'package')
            $newContent = $content -replace "^/\*[\s\S]*?\*/\s*", ""
            $newContent = $standardHeader + "`n" + $newContent
            
            # Add @since 1.0 after class Javadoc if missing
            if (-not $hasSince -and $newContent -match "(\s*\*/\s*\r?\npublic)") {
                $newContent = $newContent -replace "(\s*\*/\s*\r?\npublic)", " * @since 1.0`n`$1"
            }
            
            Set-Content -Path $file.FullName -Value $newContent -NoNewline
            $stats.Updated++
        }
    }
}

Write-Host "`n=== Header Standardization Report ===" -ForegroundColor Cyan
Write-Host "Module: $Module"
Write-Host "Total files: $($stats.Total)"
Write-Host "Already correct: $($stats.AlreadyCorrect)" -ForegroundColor Green
Write-Host "Need update: $($stats.NeedUpdate)" -ForegroundColor Yellow
if (-not $DryRun) {
    Write-Host "Updated: $($stats.Updated)" -ForegroundColor Blue
}
