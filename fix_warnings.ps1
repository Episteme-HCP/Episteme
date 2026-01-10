$files = Get-ChildItem -Path "C:\Silvere\Encours\Developpement\JScience\jscience-server\target\generated-sources" -Recurse -Filter "*.java"
foreach ($file in $files) {
    $content = Get-Content $file.FullName -Raw
    if ($content -match "public final class") {
        $newContent = $content -replace "public final class", "@SuppressWarnings(`"all`")`npublic final class"
        Set-Content $file.FullName $newContent
        Write-Host "Updated $($file.Name)"
    }
}
