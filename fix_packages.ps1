$files = Get-ChildItem -Path "C:\Silvere\Encours\Developpement\JScience\jscience-io\src\main\java\org\jscience\io\mathml" -Filter *.java
foreach ($file in $files) {
    $content = Get-Content $file.FullName -Raw
    $newContent = $content -replace "package org.jscience.ml.mathml;", "package org.jscience.io.mathml;" -replace "import org.jscience.ml.mathml", "import org.jscience.io.mathml"
    if ($content -ne $newContent) {
        Set-Content -Path $file.FullName -Value $newContent
        Write-Host "Updated $($file.Name)"
    }
}
