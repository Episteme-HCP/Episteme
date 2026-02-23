$files = Get-ChildItem -Path "jscience-client\src\main\java\org\jscience\client\client" -Filter *.java -Recurse
foreach ($f in $files) {
    $content = Get-Content $f.FullName -Raw
    if ($content -match "extends Application" -and $content -notmatch "getIcons\(\)\.add") {
        $replaced = [regex]::Replace($content, "(public void start\((?:javafx\.stage\.)?Stage (\w+)\)\s*(?:throws Exception\s*)?\{)", "`${1}`n        try { `${2}.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream(`"/org/jscience/core/ui/icon.png`"))); } catch (Exception e) {}")
        if ($replaced -cne $content) {
            Set-Content -Path $f.FullName -Value $replaced -NoNewline
            Write-Host "Updated $($f.Name)"
        }
    }
}
