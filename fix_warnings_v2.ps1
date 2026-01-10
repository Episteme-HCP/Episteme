$files = Get-ChildItem -Path "C:\Silvere\Encours\Developpement\JScience\jscience-server\target\generated-sources" -Recurse -Filter "*.java"
foreach ($file in $files) {
    $content = Get-Content $file.FullName -Raw
    
    # 1. Ensure @SuppressWarnings("all") is present at class level
    if ($content -match "public final class" -and -not ($content -match "@SuppressWarnings\(`"all`"\)")) {
        $content = $content -replace "public final class", "@SuppressWarnings(`"all`")`npublic final class"
    }

    # 2. Remove specific @SuppressWarnings("unchecked") that cause "Unnecessary" warnings because "all" covers it?
    # Actually, simpler: replace ANY @SuppressWarnings(...) with nothing, EXCEPT the one we added? 
    # Or just replace standard ones.
    # The error was "Unnecessary @SuppressWarnings("unchecked")". 
    # Let's simple remove all existing @SuppressWarnings lines before adding our global one.
    
    # But doing that with regex on a file content string is tricky if it spans lines.
    # Minimal viable fix: Replace specific problematic patterns seen in logs.
    
    $content = $content -replace '@SuppressWarnings\("unchecked"\)', ''
    $content = $content -replace '@SuppressWarnings\("rawtypes"\)', ''
    
    # 3. Handle "Redundant superinterface"
    # This is a compiler warning about the class signature. @SuppressWarnings("all") should cover it.
    # If it doesn't, we might need @SuppressWarnings("all", "deprecation", "unchecked", "rawtypes") just in case "all" isn't enough for some compilers?
    # Usually "all" is enough.
    
    Set-Content $file.FullName $content
    Write-Host "Processed $($file.Name)"
}
