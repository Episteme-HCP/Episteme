$Header = @"
/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

$RootDir = Get-Location
$Files = Get-ChildItem -Path $RootDir -Recurse -Filter "*.java"

$ignoreDirs = @(".git", ".vscode", "target", "node_modules")

foreach ($File in $Files) {
    # Skip ignored directories
    $skip = $false
    foreach ($dir in $ignoreDirs) {
        if ($File.FullName -match [regex]::Escape([System.IO.Path]::DirectorySeparatorChar + $dir + [System.IO.Path]::DirectorySeparatorChar)) {
            $skip = $true
            break
        }
    }
    if ($skip) { continue }

    $Content = Get-Content -Path $File.FullName -Raw -Encoding UTF8
    if ($null -eq $Content) { continue }

    # Normalize line endings to LF for processing consistency
    $Content = $Content -replace "\r\n", "`n"
    $HeaderNorm = $Header -replace "\r\n", "`n"

    # Check if file already starts with the correct header
    if ($Content.StartsWith($HeaderNorm)) {
        continue
    }

    # Regex to capture existing header (block comment at start)
    # Allows whitespace before
    # Matches /* ... */ lazily, before package declaration or imports or classes
    $regex = "^\s*/\*.*?\*/\s*"
    
    # Check if there is an existing header to replace
    # We assume a header is a block comment at the very beginning appearing before 'package'
    if ($Content -match "$regex(package|import|public|class|interface|enum)") {
         # Identify if it looks like a license header (contains Copyright or License)
         # We want to be careful not to kill class javadoc if it's the first thing
         $foundHeader = $matches[0]
         
         # Strategy: Replace the first comment block ONLY if it's not a Javadoc (/**) OR if it contains "Copyright"/"License"
         # But Real.java uses /* so it is NOT Javadoc.
         
         # Let's clean replace: 
         # Find the start of code (package, import, or class)
         $codeStart = $Content | Select-String -Pattern "(package|import|public|abstract|class|interface|enum)" -List | Select-Object -First 1
         
         if ($codeStart) {
            $idx = $Content.IndexOf($codeStart.Matches[0].Value)
            if ($idx -ge 0) {
                # content after header
                $body = $Content.Substring($idx)
                # check if there is a header before
                $prefix = $Content.Substring(0, $idx)
                
                if ($prefix -match "/\*") {
                    # Replace prefix with new header
                    # But keep Javadoc if it was separate? 
                    # If the prefix contains MULTIPLE comments, we might be destroying class javadoc if it's before package (rare in Java, usually after package).
                    # Standard Java: Header -> Package -> Imports -> Class Javadoc -> Class
                    
                    # So, effectively, we want to replace everything before 'package' with the new Header + 2 newlines.
                    # Wait, if there are imports but no package (default package), we look for imports.
                }
            }
         }
    }
    
    # Simplify regex approach:
    # 1. Remove any leading comment block that looks like a simplified license or old header.
    # 2. Prepend new header.
    
    # Correct Regex to match a C-style comment at the start of the string
    # (?s) enables dot-matches-newline
    $newContent = $Content
    
    if ($Content -match "^(?s)\s*/\*.*?\*/\s*") {
        # Detected a comment block at start.
        # Verify if it's a license header (heuristics)
        $block = $matches[0]
        if ($block -match "Copyright" -or $block -match "License" -or $block -match "JScience") {
            # It is a header, remove it
            $newContent = $Content.Substring($block.Length)
        }
    }
    
    # Ensure package statement starts cleanly
    $newContent = $newContent.TrimStart()
    
    # Combine
    $finalContent = $HeaderNorm + "`n`n" + $newContent
    
    # Write back
    Set-Content -Path $File.FullName -Value $finalContent -Encoding UTF8 -NoNewline
    Write-Host "Updated header: $($File.Name)" -ForegroundColor Green
}
