# MathML Documentation Fixer
$dir = "c:\Silvere\Encours\Developpement\JScience\jscience-core\src\main\java\org\w3c\dom\mathml"
$files = Get-ChildItem -Path $dir -Filter "*.java"

foreach ($file in $files) {
    $content = Get-Content $file.FullName
    $changed = $false
    
    # 1. Class level doc
    if ($content -match "DOCUMENT ME!") {
        $className = $file.BaseName
        $content = $content -replace "\s*\*\s*DOCUMENT ME!", " * This interface represents the MathML $className element."
        $changed = $true
    }
    
    # 2. Method level doc
    if ($content -match "DOCUMENT ME!") {
        $content = $content -replace "@return DOCUMENT ME!", "@return the requested attribute or element"
        $content = $content -replace "@param (\w+) DOCUMENT ME!", "@param `$1 the value to set"
        $content = $content -replace "@throws DOMException DOCUMENT ME!", "@throws DOMException if an error occurs"
        $changed = $true
    }
    
    if ($changed) {
        $content | Set-Content $file.FullName
        Write-Host "Updated $($file.Name)"
    }
}
