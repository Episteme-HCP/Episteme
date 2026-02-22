$flags = "--add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED"

$files = Get-ChildItem -Path . -Include *.bat,*.sh -File -Recurse | Where-Object { $_.DirectoryName -match "JScience$" -or $_.DirectoryName -match "launchers$" }

foreach ($f in $files) {
    if ($f.Name -match "^(run-master-control|build_|install_|generate_)") { continue }
    
    $content = Get-Content $f.FullName -Raw
    if ($content -notmatch "jdk.incubator.vector") {
        $newContent = $content -replace "java ", "java $flags "
        if ($newContent -cne $content) {
            Set-Content -Path $f.FullName -Value $newContent -NoNewline
            Write-Host "Updated $($f.FullName)"
        }
    }
}
