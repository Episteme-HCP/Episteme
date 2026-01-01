$rootDir = Get-Location
$files = Get-ChildItem -Path $rootDir -Recurse -Filter "package-info.java"

foreach ($file in $files) {
    $content = Get-Content -Path $file.FullName
    
    $packageLines = $content | Where-Object { $_ -match "^package\s+" }
    
    if ($packageLines.Count -gt 1) {
        Write-Host "Fixing corrupted file (multiple packages): $($file.FullName)"
        
        $realPackageDecl = $null
        $descText = ""
        
        foreach ($line in $packageLines) {
            if ($line -match "^package\s+org\.jscience[\w\.]*.*;") {
                $realPackageDecl = $line
            } else {
                # This is likely the corrupted description
                # "package defines ..." -> "Defines ..."
                $rawDesc = $line -replace "^package\s+", ""
                # Capitalize first letter
                if ($rawDesc.Length -gt 0) {
                     $descText = $rawDesc.Substring(0,1).ToUpper() + $rawDesc.Substring(1)
                }
                # Remove trailing junk from failed regex captures or partial lines if any
                 $descText = $descText -replace "\*/.*", "" 
            }
        }
        
        if ($realPackageDecl) {
             # Reconstruct file
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

/**
 * $descText
 */
$realPackageDecl
"@
            [System.IO.File]::WriteAllText($file.FullName, $header, [System.Text.UTF8Encoding]::new($false))
        }
    }
}
Write-Host "Repair complete."
